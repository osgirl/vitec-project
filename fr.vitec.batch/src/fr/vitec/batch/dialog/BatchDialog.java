package fr.vitec.batch.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class BatchDialog extends TitleAreaDialog {

	private ProgressBar progressBar;
	private StyledText styledText;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public BatchDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setTitle("Traitement ...");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(null);
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.heightHint = 189;
		container.setLayoutData(gd_container);
		
		progressBar = new ProgressBar(container, SWT.NONE);
		progressBar.setBounds(0, 254, 423, 18);
		
		styledText = new StyledText(container, SWT.BORDER | SWT.V_SCROLL);
		styledText.setDoubleClickEnabled(false);
		styledText.setBounds(0, 0, 423, 254);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(429, 411);
	}

	public ProgressBar getProgressBar() {
		return this.progressBar;
	}

	public StyledText getStyledText() {
		return styledText;
	}
	
}
