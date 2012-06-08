package fr.vitec.batch.process.logger;

import org.eclipse.swt.widgets.Display;

import fr.vitec.batch.dialog.BatchDialog;

public class BatchDialogLogger implements IBatchLogger {

	private BatchDialog dialog;

	public BatchDialogLogger(BatchDialog dialog) {
		this.dialog = dialog;
	}

	@Override
	public void logInfo(final String msg) {
		addText("\t"+msg);
	}

	@Override
	public void logTitle(String msg) {
		addText("=>"+msg);
		
	}

	protected void addText(final String msg) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				String txt = dialog.getStyledText().getText();
				if(txt.isEmpty()){
					txt = msg;
				}else{
					txt = txt+"\n"+msg;
				}
				dialog.getStyledText().setText(txt);
			}
		});
	}
}
