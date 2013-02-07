package fr.vitec.fmk.rcp;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

import fr.vitec.fmk.exception.VitecException;

public class RcpUtils {
	public static IViewPart getView(String viewId) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    IViewReference[] refs = window.getActivePage().getViewReferences();
	    for (IViewReference viewReference : refs) {
	        if (viewReference.getId().equals(viewId)) {
	            return viewReference.getView(true);
	        }
	    }
	    return null;
	    
	    //CE code semble mieux ! a TESTER !
//	    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage page = window.getActivePage();
//		page.findView(viewId);
	}

	public static boolean isActivityEnabled(String activityId){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchActivitySupport activitySupport = window.getWorkbench().getActivitySupport();
		IActivityManager activityManager = activitySupport.getActivityManager();
		
		if (activityManager.getActivity(activityId).isDefined()) {
			return activityManager.getActivity(activityId).isEnabled();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static void activateActivity(String activityId){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchActivitySupport activitySupport = window.getWorkbench().getActivitySupport();
		IActivityManager activityManager = activitySupport.getActivityManager();
		Set<String> enabledActivities = activityManager.getEnabledActivityIds();
		Set<String> newEnabledActivities = getNewEnabledActivities(enabledActivities);
		
		if (activityManager.getActivity(activityId).isDefined()) {
			newEnabledActivities.add(activityId);
		}
		activitySupport.setEnabledActivityIds(newEnabledActivities);
	}
	
	private static Set<String> getNewEnabledActivities(Set<String> enabledActivities) {
		Set<String> newEnabledActivities = new HashSet<String>();
		for (String enabledActivity : enabledActivities) {
			newEnabledActivities.add(enabledActivity);
		}
		
		return newEnabledActivities;
	}

	public static void deactivateActivity(String activityId){
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchActivitySupport activitySupport = window.getWorkbench().getActivitySupport();
		Set<String> enabledActivities = new HashSet<String>();
		Set<String> newEnabledActivities = getNewEnabledActivities(enabledActivities);
		newEnabledActivities.remove(activityId);
		
		activitySupport.setEnabledActivityIds(enabledActivities);
		// Now I have to reset the perspective to update also the views
		window.getActivePage().resetPerspective();
	}

	public static boolean isViewVisible(String id) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		return page.isPartVisible(RcpUtils.getView(id));
	}
	
	public static void hideView(String id) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.hideView(RcpUtils.getView(id));
	}
	
	public static void showView(String id) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(id);
		} catch (PartInitException e) {
			throw new VitecException(e);
		}
	}

	public static void switchPerspective(final String perspectiveId) {
		final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        IPerspectiveDescriptor activePerspective = workbenchWindow.getActivePage().getPerspective();

        if (perspectiveId == null) {
            return;
        }

        if (activePerspective == null || !activePerspective.getId().equals(perspectiveId)) {
        	try {
				workbenchWindow.getWorkbench().showPerspective(perspectiveId, workbenchWindow);
			} catch (WorkbenchException e) {
				throw new VitecException(e);
			}
        	// Switching of the perspective is delayed using Display.asyncExec
            // because switching the perspective while the workbench is
            // activating parts might cause conflicts.
//            Display.getCurrent().asyncExec(new Runnable() {
//
//                public void run() {
//                    try {
//                        workbenchWindow.getWorkbench().showPerspective(perspectiveId,
//                                workbenchWindow);
//                    } catch (WorkbenchException e) {
//                        throw new VitecException(e);
//                    }
//                }
//
//            });
        }
	}
}
