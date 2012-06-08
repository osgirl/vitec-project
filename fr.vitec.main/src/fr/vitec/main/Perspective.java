package fr.vitec.main;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import fr.vitec.batch.views.BatchView;
import fr.vitec.main.message.Messages;

public class Perspective implements IPerspectiveFactory {

	public static final String ID = "fr.vitec.main.perspective"; //$NON-NLS-1$

	public void createInitialLayout(IPageLayout layout) {
		//layout.setEditorAreaVisible(false);
		//layout.setFixed(true);
		
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		
		//layout.addStandaloneView(ViewPartMaster.ID,  false, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder(Messages.Perspective_0, IPageLayout.LEFT, 0.25f, editorArea);
		folder.addPlaceholder(ViewPartMaster.ID + ":*"); //$NON-NLS-1$
		folder.addView(ViewPartMaster.ID);
		
		
		folder = layout.createFolder(Messages.Perspective_3, IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(ViewPartDetails.ID + ":*"); //$NON-NLS-1$
		folder.addView(ViewPartDetails.ID);

		folder = layout.createFolder("tmp", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(BatchView.ID + ":*"); //$NON-NLS-1$
		folder.addView(BatchView.ID);

		layout.getViewLayout(ViewPartMaster.ID).setCloseable(false);
	}

}
