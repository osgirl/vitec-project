package fr.vitec.fmk.exception;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


public class VitecException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VitecException(Exception e) {
		e.printStackTrace();
		StackTraceElement[] stks = e.getStackTrace();
		String str = "";
		for (StackTraceElement stk : stks) {
			str += stk.toString()+"\n";
		}
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		MessageDialog.openInformation(window.getShell(), e.getClass().getSimpleName(), e.getMessage()+"\n"+str);
	}

	public VitecException(String msg) {
		super(msg);
	}

}
