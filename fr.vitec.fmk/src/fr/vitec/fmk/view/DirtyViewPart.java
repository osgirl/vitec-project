package fr.vitec.fmk.view;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.part.ViewPart;

import fr.vitec.fmk.binding.DirtyView;
import fr.vitec.fmk.dialog.UIMessages;

public class DirtyViewPart extends ViewPart implements ISaveablePart2,	DirtyView {

	protected boolean dirty = false;
	
	@Override
	public void createPartControl(Composite parent) {
		
	}

	@Override
	public void setFocus() {
		
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		//System.out.println("doSave this.dirty = false");
		this.dirty = false;
		firePropertyChange(PROP_DIRTY); 
	}

	@Override
	public void doSaveAs() {
		//System.out.println("doSaveAs this.dirty = false");
		this.dirty = false;
	}

	@Override
	public boolean isDirty() {
		//System.out.println("isDirty "+this.dirty);
		return this.dirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		//System.out.println("isSaveAsAllowed true");
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		//System.out.println("isSaveOnCloseNeeded true");
		return true;
	}

	@Override
	public void setDirty(boolean dirty) {
		//System.out.println("setDirty "+dirty);
		this.dirty  = dirty;
		 Display.getDefault().asyncExec( new Runnable() { 
			 								public void run() {
			 									firePropertyChange(PROP_DIRTY);
			 								}
			 							}); 
		 
	}

	@Override
	public int promptToSaveOnClose() {
		final int dialogResult = UIMessages.messageDialogYesNoCancel(this.getViewSite().getShell(), "Sauvegarde","Sauvegarder les changements ?");
		
		if(dialogResult == 0){
			doSave(null);
			return ISaveablePart2.YES;
		}else if(dialogResult == 1){
			return ISaveablePart2.NO;
		}else{
			return ISaveablePart2.CANCEL;
		}
	}


}
