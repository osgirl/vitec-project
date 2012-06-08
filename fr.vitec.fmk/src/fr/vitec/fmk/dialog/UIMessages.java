package fr.vitec.fmk.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
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
}
