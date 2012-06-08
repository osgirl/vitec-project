package fr.vitec.batch.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fr.vitec.model.VitecModel;

public class ViewTableContentProvider implements IStructuredContentProvider {
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	public void dispose() {
	}
	public Object[] getElements(Object parent) {
		if(parent instanceof VitecModel){
			VitecModel model = (VitecModel)parent;
			return model.getDirectories().toArray();
		}
		return new String[] {}; 
	}
}
