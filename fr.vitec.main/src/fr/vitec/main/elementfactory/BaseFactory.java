package fr.vitec.main.elementfactory;


import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import fr.vitec.fmk.rcp.RcpUtils;
import fr.vitec.main.ViewPartMaster;
import fr.vitec.main.handler.OpenHandler;

public class BaseFactory implements IElementFactory {
	private static final String TAG_PATH = "path"; //$NON-NLS-1$
	public static final String ID = "fr.vitec.main.elementfactory.BaseFactory";

	@Override
	public IAdaptable createElement(IMemento memento) {
		String fileName = memento.getString(TAG_PATH);
		if (fileName != null) {
		    new OpenHandler().open(fileName);
			return RcpUtils.getView(ViewPartMaster.ID);
		}
		return null;
	}

	public static void saveState(IMemento memento, String fileName) {
		if (fileName != null) {
			memento.putString(TAG_PATH, fileName);
		}
	}

}
