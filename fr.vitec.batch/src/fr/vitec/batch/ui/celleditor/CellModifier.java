package fr.vitec.batch.ui.celleditor;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import fr.vitec.model.xmlbinding.DirectoryType;

public class CellModifier implements ICellModifier {

	private TableViewer tableViewer;

	public CellModifier(TableViewer viewer) {
		this.tableViewer = viewer;
	}
	
	@Override
	public boolean canModify(Object element, String property) {
		@SuppressWarnings("unused")
		int i = 0;
		return true;
	}

	@Override
	public Object getValue(Object element, String property) {
		int columnIndex = Integer.parseInt(property);

		Object result = null;
		DirectoryType directory = (DirectoryType) element;

		switch (columnIndex) {
			case 0 :  
				result = directory.getPath();
				break;
			case 1 : 
				result = directory.getFilter();
				break;
			case 2 : 
				result = directory.isRecursive();					
				break;
			case 3 : 
				break;
			default :
				result = "";
		}
		return result;	
	}

	@Override
	public void modify(Object element, String property, Object value) {
		int columnIndex = Integer.parseInt(property);

		DirectoryType directory = (DirectoryType) (((TableItem) element).getData());

		switch (columnIndex) {
			case 0 :  
				directory.setPath(value.toString());
				break;
			case 1 : 
				directory.setFilter(value.toString());
				break;
			case 2 : 
				directory.setRecursive((Boolean)value);					
				break;
			case 3 : 
			default :
		}
		tableViewer.update(directory, null);
	}

}
