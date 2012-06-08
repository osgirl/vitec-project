package fr.vitec.batch.process.progressmanager;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

public class ProgressBarManager implements IProgressManager{

	private Display display;
	private ProgressBar bar;
	private int cpt;

	public ProgressBarManager(ProgressBar bar) {
		this.bar = bar;
		this.display = bar.getShell().getDisplay();
	}
	
	@Override
	public void init(final int value) {
		if (display.isDisposed()) return;
		cpt = 0;
		display.asyncExec(new Runnable() {
			public void run() {
			if (bar.isDisposed ()) return;
			bar.setMaximum(value);
			}
		});
	}

	@Override
	public void progress() {
		cpt++;
		if (display.isDisposed()) return;
		
		display.asyncExec(new Runnable() {
			public void run() {
			if (bar.isDisposed ()) return;
				bar.setSelection(cpt);
			}
		});
	}

}
