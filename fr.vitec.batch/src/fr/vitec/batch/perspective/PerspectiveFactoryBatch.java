package fr.vitec.batch.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.vitec.batch.views.BatchView;

public class PerspectiveFactoryBatch implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		IFolderLayout folder = layout.createFolder("Master", IPageLayout.LEFT, 1.0f, editorArea);
		folder.addPlaceholder(BatchView.ID + ":*");
		folder.addView(BatchView.ID);
			
		
		layout.getViewLayout(BatchView.ID).setCloseable(true);
	}

}
