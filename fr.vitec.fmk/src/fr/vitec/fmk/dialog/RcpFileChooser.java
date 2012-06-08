package fr.vitec.fmk.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class RcpFileChooser {
	public static String XML_EXTENTION =  "*.xml";
	public static String[][] tabExt = {
								{XML_EXTENTION, "Xml File"}
							};
	private Shell parent;
	public RcpFileChooser() {
		this.parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	
	public String openFile(String extention){
		return openFile(extention, null);
	}
	
	public String openFile(String extention, String filtersName){
		    
		FileDialog dialog = new FileDialog(parent, SWT.OPEN);    
		dialog.setFilterExtensions(new String[] {extention});
		
		if(filtersName == null){
			dialog.setFilterNames(new String[] {getFilterName(extention)});
		} else {
			dialog.setFilterNames(new String[] {filtersName});
		}
		return dialog.open();    
	}

	private String getFilterName(String extention) {
		for (Object[] ext : tabExt) {
			if(extention.equals((String)ext[0])){
				return (String)ext[1];
			}
		}
		return "";
	}
	
	public String openDir(){
		return openDir(null);
	}
	
	public String openDir(String path){
		DirectoryDialog dd = new DirectoryDialog(parent);
		dd.setFilterPath(path);
		return dd.open();
	}
}
