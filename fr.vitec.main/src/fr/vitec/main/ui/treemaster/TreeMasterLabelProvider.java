package fr.vitec.main.ui.treemaster;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import fr.vitec.model.xmlbinding.DirectoryType;
import fr.vitec.model.xmlbinding.FilmType;

public class TreeMasterLabelProvider extends //LabelProvider{
											StyledCellLabelProvider {
	
	public TreeMasterLabelProvider() {
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		StyledString text = new StyledString();

		if (element instanceof DirectoryType) {
			DirectoryType dir = (DirectoryType) element;
			text.append(dir.getPath());
			cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			text.append(" (" +dir.getFilm().size() + ")", StyledString.COUNTER_STYLER);
		} else {
			FilmType film = (FilmType) element;
			text.append(film.getTitle());
			cell.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
		}
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		super.update(cell);
	}
	
//	@Override
//	public String getText(Object element) {
//		if (element instanceof DirectoryType) {
//			DirectoryType dir = (DirectoryType) element;
//			return dir.getName();
//		}
//		return ((FilmType) element).getTitle();
//	}
//
//	@Override
//	public Image getImage(Object element) {
//		if (element instanceof DirectoryType) {
//			return PlatformUI.getWorkbench().getSharedImages()
//					.getImage(ISharedImages.IMG_OBJ_FOLDER);
//		}
//		return PlatformUI.getWorkbench().getSharedImages()
//		.getImage(ISharedImages.IMG_OBJ_FILE);
//	}


}
