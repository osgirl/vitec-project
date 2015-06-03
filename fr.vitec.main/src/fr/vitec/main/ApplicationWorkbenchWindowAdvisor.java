package fr.vitec.main;

import java.awt.Dimension;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;


import fr.vitec.fmk.rcp.RcpUtils;
import fr.vitec.main.util.id.Id;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private static final double COEF = 0.85;

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		//configurer.setInitialSize(new Point(400, 300));
		Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize(); 
		configurer.setInitialSize(new Point((int)(dim.getWidth()*COEF), (int)(dim.getHeight()*COEF))); 

		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(false);
		
		configurer.setShowPerspectiveBar(true);
	}

	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		RcpUtils.deactivateAllActivities();
		
	}
}
