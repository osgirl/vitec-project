package fr.vitec.main;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "fr.vitec.main.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		//PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_PROGRESS_ON_STARTUP, true);
		PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS, false);
		//configurer.setSaveAndRestore(true);
	}

	@Override
	public void fillActionBars(IWorkbenchWindow window,
			IActionBarConfigurer configurer, int flags) {

		IMenuManager menuBar = configurer.getMenuManager();

		MenuManager fileMenu = createFileMenu(window);
		MenuManager windowsMenu = createWindowsMenu(window);
		MenuManager helpMenu = createHelpMenu(window);

		menuBar.add(fileMenu);
		menuBar.add(windowsMenu);
		menuBar.add(helpMenu);	
	}

	private MenuManager createFileMenu(IWorkbenchWindow window) {
		MenuManager menuMgr = new MenuManager("File",
				IWorkbenchActionConstants.M_FILE);
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

		return menuMgr;
	}

	private MenuManager createWindowsMenu(IWorkbenchWindow window) {
		MenuManager menu = new MenuManager("Windows",
				IWorkbenchActionConstants.M_WINDOW);
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		MenuManager showViewMenuMgr = new MenuManager("Show View", "showView"); 
				IContributionItem showViewMenu = 
				ContributionItemFactory.VIEWS_SHORTLIST.create(window);
				showViewMenuMgr.add(showViewMenu);
				menu.add(showViewMenuMgr);
				menu.add(new Separator());
				IAction openPreferencesAction = ActionFactory.PREFERENCES.create
						(window);
				menu.add(openPreferencesAction);        
				menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));

				return menu;
	}

	private MenuManager createHelpMenu(IWorkbenchWindow window) {
		MenuManager menu = new MenuManager("Help",
				IWorkbenchActionConstants.M_HELP);
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));

		return menu;
	}
}
