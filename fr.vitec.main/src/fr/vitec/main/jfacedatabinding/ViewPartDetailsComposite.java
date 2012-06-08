package fr.vitec.main.jfacedatabinding;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ViewPartDetailsComposite extends Composite {

	private DataBindingContext m_bindingContext;
	private fr.vitec.model.xmlbinding.FilmType filmType = new fr.vitec.model.xmlbinding.FilmType();
	private Text titleText;

	public ViewPartDetailsComposite(Composite parent, int style,
			fr.vitec.model.xmlbinding.FilmType newFilmType) {
		this(parent, style);
		setFilmType(newFilmType);
	}

	public ViewPartDetailsComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		new Label(this, SWT.NONE).setText("Title:");

		titleText = new Text(this, SWT.BORDER | SWT.SINGLE);
		titleText
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		if (filmType != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue titleObserveWidget = SWTObservables.observeText(
				titleText, SWT.Modify);
		IObservableValue titleObserveValue = PojoObservables.observeValue(
				filmType, "title");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(titleObserveWidget, titleObserveValue, null,
				null);
		//
		return bindingContext;
	}

	public fr.vitec.model.xmlbinding.FilmType getFilmType() {
		return filmType;
	}

	public void setFilmType(fr.vitec.model.xmlbinding.FilmType newFilmType) {
		setFilmType(newFilmType, true);
	}

	public void setFilmType(
			fr.vitec.model.xmlbinding.FilmType newFilmType, boolean update) {
		filmType = newFilmType;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (filmType != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
