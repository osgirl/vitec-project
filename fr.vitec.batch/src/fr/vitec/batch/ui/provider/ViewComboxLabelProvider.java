package fr.vitec.batch.ui.provider;

import org.eclipse.jface.viewers.LabelProvider;

import fr.vitec.batch.extension.FindFilmInfo;


public class ViewComboxLabelProvider extends LabelProvider{
	@Override
	public String getText(Object element) {
		if (element instanceof FindFilmInfo) {
			FindFilmInfo findFilmInfo = (FindFilmInfo)element;
			return findFilmInfo.getSiteName();
		}
		return super.getText(element);
	}

}
