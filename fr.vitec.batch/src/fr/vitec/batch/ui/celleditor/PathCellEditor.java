package fr.vitec.batch.ui.celleditor;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Control;

import fr.vitec.fmk.dialog.RcpFileChooser;
import fr.vitec.model.xmlbinding.DirectoryType;

public class PathCellEditor extends DialogCellEditor {
	
	private TableViewer viewer;

	public PathCellEditor(TableViewer viewer) {
		super(viewer.getTable());
		this.viewer = viewer;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		RcpFileChooser rfc = new RcpFileChooser();
		DirectoryType directoryType = (DirectoryType)((StructuredSelection)viewer.getSelection()).getFirstElement();
		String dir = rfc.openDir(directoryType.getPath());
		if(dir != null){
			directoryType.setPath(dir);
			viewer.update(directoryType, null);
		}
		return null;
	}
}
