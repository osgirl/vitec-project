package fr.vitec.batch.ui.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import fr.vitec.fmk.resource.SWTResourceManager;
import fr.vitec.model.xmlbinding.DirectoryType;

public class ViewTableLabelProvider  extends LabelProvider implements ITableLabelProvider {

	private static final int INDEX_COL_RECURSIVE = 2;
//	private static ImageRegistry imageRegistry = new ImageRegistry();
	public static final String CHECKED_IMAGE 	= "icons/checked.gif";
	public static final String UNCHECKED_IMAGE  = "icons/unchecked.gif";

//	/**
//	 * Note: An image registry owns all of the image objects registered with it,
//	 * and automatically disposes of them the SWT Display is disposed.
//	 */ 
//	static {
//		String iconPath = "icons/"; 
//		imageRegistry.put(CHECKED_IMAGE, ImageDescriptor.createFromFile(
//				ViewTableLabelProvider.class, 
//				iconPath + CHECKED_IMAGE + ".gif"
//				)
//				);
//		imageRegistry.put(UNCHECKED_IMAGE, ImageDescriptor.createFromFile(
//				ViewTableLabelProvider.class, 
//				iconPath + UNCHECKED_IMAGE + ".gif"
//				)
//				);	
//	}

	public String getColumnText(Object obj, int index) {
		String result = "";
		if(obj instanceof DirectoryType){
			DirectoryType dir = (DirectoryType)obj;
			switch (index) {
			case 0:  
				result = dir.getPath();
				break;
			case 1 :
				result = dir.getFilter();
				break;
			case INDEX_COL_RECURSIVE :
				result = Boolean.toString(dir.isRecursive());
				break;
			case 3 :
				break;
			default :
				break; 	
			}
		}
		return result;

	}
	public Image getColumnImage(Object obj, int index) {
		return (index == INDEX_COL_RECURSIVE)?getImage(((DirectoryType) obj).isRecursive()):null;	
	}

	private Image getImage(boolean isSelected) {
		String key = isSelected ? CHECKED_IMAGE : UNCHECKED_IMAGE;
		return  SWTResourceManager.getPluginImage(key);
	}


}
