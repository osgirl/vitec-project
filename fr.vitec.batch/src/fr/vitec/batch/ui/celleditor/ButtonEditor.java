package fr.vitec.batch.ui.celleditor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ButtonEditor extends CellEditor {
	private Button button;
	
	public ButtonEditor(Composite table) {
		button = new Button(table, SWT.PUSH);
		button.setText("...");
	}
	
	@Override
	protected Control createControl(Composite parent) {
		return button;
	}

	@Override
	protected Object doGetValue() {
		return button.getText();
	}

	@Override
	protected void doSetFocus() {
		button.setFocus();
	}

	@Override
	protected void doSetValue(Object value) {
		//button.setText(value.toString());
	}
	
}
