package fr.vitec.main;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Fichier
	private IWorkbenchAction exitAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAllAction;

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}


	@Override
	protected void makeActions(IWorkbenchWindow window) {
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
		
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		
		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);
	}

	@Override
	protected void fillMenuBar(final IMenuManager menuBar) {
		
		IWorkbenchWindow window = getActionBarConfigurer().getWindowConfigurer().getWindow();
		
		//Menu Fichier
		final MenuManager fileMenu = new MenuManager("Fichiers", "fr.vitec.main.menu.file");
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(saveAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		//fileMenu.add(ContributionItemFactory.REOPEN_EDITORS.create(window));
		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MRU));
		fileMenu.add(exitAction);

		fileMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				IContributionItem[] contributionItems = fileMenu.getItems();
				for (int i = 0; i < contributionItems.length; i++) {

					contributionItems[i].update();
				}
				fileMenu.update(true);
			}

		});
		menuBar.add(fileMenu);

		//Menu Fenêtres
		MenuManager windows = new MenuManager("Fen\u00Eatres", "fr.vitec.main.menu.windows");
		windows.add(new GroupMarker("windowsStart"));
		//fileMenu.add(ContributionItemFactory.REOPEN_EDITORS.create(window));
		windows.add(new GroupMarker("windowsEnd"));
		menuBar.add(windows);
	}
	
	@Override
	public void fillActionBars(int flags) {
		super.fillActionBars(flags);
		
	}
	
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {

		IToolBarManager saveToolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
	    saveToolbar.add(saveAction);
	    saveToolbar.add(saveAllAction);
	    coolBar.add(new ToolBarContributionItem(saveToolbar, "save"));

	}
}
