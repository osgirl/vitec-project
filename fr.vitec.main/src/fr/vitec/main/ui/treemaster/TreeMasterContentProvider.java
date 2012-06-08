package fr.vitec.main.ui.treemaster;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.DirectoryType;
import fr.vitec.model.xmlbinding.FilmType;



public class TreeMasterContentProvider implements ITreeContentProvider {
	
	VitecModel model;

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.model = (VitecModel) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return model.getDirectories().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		List<FilmType> films = new ArrayList<FilmType>();
		if (parentElement instanceof DirectoryType) {
			DirectoryType dir = (DirectoryType) parentElement;
			return dir.getFilm().toArray();
		}
		return films.toArray();
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof DirectoryType) {
			return true;
		}
		return false;
	}

	public VitecModel getModel() {
		return model;
	}

}

