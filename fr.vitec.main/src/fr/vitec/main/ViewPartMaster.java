package fr.vitec.main;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.part.ViewPart;

import fr.vitec.fmk.rcp.RcpUtils;
import fr.vitec.main.elementfactory.BaseFactory;
import fr.vitec.main.ui.treemaster.TreeMasterContentProvider;
import fr.vitec.main.ui.treemaster.TreeMasterLabelProvider;
import fr.vitec.main.util.id.Id;
import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.FilmType;

public class ViewPartMaster extends ViewPart implements Observer, IPersistableElement {

	public static final String ID = "fr.vitec.main.viewMaster";
	
	private TreeViewer viewer;

	private VitecModel model;
	

	
	public ViewPartMaster() {
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		TreeMasterContentProvider contentProvider = new TreeMasterContentProvider();
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(new TreeMasterLabelProvider());
		// Expand 
		viewer.setAutoExpandLevel(2);
		getSite().setSelectionProvider(viewer);
		
		ISelectionChangedListener listener = new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				 ISelection selection = event.getSelection();
				if(selection instanceof TreeSelection && ((TreeSelection)selection).getFirstElement() instanceof FilmType){
					if(!RcpUtils.isActivityEnabled(Id.ACTIVITY_DETAIL)){
						RcpUtils.activateActivity(Id.ACTIVITY_DETAIL);
						ViewPartDetails view = (ViewPartDetails) RcpUtils.getView(ViewPartDetails.ID);
						view.setFilm((FilmType)((TreeSelection)selection).getFirstElement());
					}
				}
				
			}
		};
		viewer.addSelectionChangedListener(listener);
	}
	
	public void setInput(VitecModel model){
		this.model = model;
		viewer.setInput(model);
		model.addObserver(this);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof VitecModel){
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					viewer.refresh();
				}
			});
		}
	}

	@Override
	public String getFactoryId() {
		return BaseFactory.ID;
	}

	@Override
	public void saveState(IMemento memento) {
		BaseFactory.saveState(memento, model.getBasePath());
	}
	
	public IPersistableElement getPersistable() {
		return model == null ? null : this;
	}
	
	
	
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
	
	
}
