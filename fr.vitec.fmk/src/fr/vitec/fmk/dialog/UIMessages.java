package fr.vitec.fmk.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class UIMessages {

	public static void information(String title, String message){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		MessageDialog.openInformation(window.getShell(), title, message);
	}
	
	public static void error(String title, String message){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		MessageDialog.openError(window.getShell(), title, message);
	}
	
	public static void warning(String title, String message){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		MessageDialog.openWarning(window.getShell(), title, message);
	}
	
	public static int messageDialogYesNoCancel(Shell shell, String title, String message){
		MessageDialog dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, new String[]{IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
		final int dialogResult = dialog.open();	
		
		return dialogResult;
		
	}
	
	public static void messageDialogYesNo(Shell shell, String title, String message, PostExecution postExecution){
		MessageDialog dialog = new MessageDialog(shell, title, null, message, MessageDialog.QUESTION, new String[]{IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL}, 0);
		final int dialogResult = dialog.open();	
		
		if(dialogResult == 0){
			postExecution.execute();
		}
		
	}
	
	public interface PostExecution{
		void execute();
	}
}
