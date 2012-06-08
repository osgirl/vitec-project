package fr.vitec.main;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import fr.vitec.fmk.binding.BindingManager;
import fr.vitec.fmk.resource.SWTResourceManager;
import fr.vitec.main.message.Messages;
import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.FilmType;

public class ViewPartDetails extends ViewPart {

	public static final String ID = "fr.vitec.main.viewDetails"; //$NON-NLS-1$

	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// we ignore our own selections
			if (sourcepart != ViewPartDetails.this) {
				showSelection(sourcepart, selection);
			}
		}
	};

	private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
	
	BindingManager bindingManager;
	private Action action;

	private Text txtDirector;
	private Text txtActor;
	private Text txtSummary;
	private Text txtTitleDisk;
	private Text txtPath;
	private Label lblTitle;
	private Label lblYear;
	private Label lblGenre;
	private Label lblCountry;
	private Label lblRuntime;

	private FilmType film;
	
	public ViewPartDetails() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		Section sctnNewSection = toolkit.createSection(parent, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnNewSection = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnNewSection.widthHint = 659;
		gd_sctnNewSection.heightHint = 324;
		sctnNewSection.setLayoutData(gd_sctnNewSection);
		sctnNewSection.setSize(227, 80);
		toolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(Messages.ViewPartDetails_1);
		sctnNewSection.setDescription(""); //$NON-NLS-1$


		Composite composite = toolkit.createComposite(sctnNewSection, SWT.WRAP);
		sctnNewSection.setClient(composite);
		toolkit.paintBordersFor(composite);

		lblTitle = toolkit.createLabel(composite, Messages.ViewPartDetails_3, SWT.NONE);
		lblTitle.setBounds(139, 10, 498, 33);
		lblTitle.setFont(SWTResourceManager.getFont("Tahoma", 16, SWT.BOLD)); //$NON-NLS-1$

		Label label = toolkit.createLabel(composite, "", SWT.NONE); //$NON-NLS-1$
		label.setImage(SWTResourceManager.getImage("D:\\dev\\workspace_test\\fr.vitec.main\\src\\product_lg.gif")); //$NON-NLS-1$
		label.setBounds(10, 10, 123, 189);

		lblYear = toolkit.createLabel(composite, Messages.ViewPartDetails_7, SWT.NONE);
		lblYear.setBounds(149, 49, 378, 13);

		lblGenre = toolkit.createLabel(composite, Messages.ViewPartDetails_8, SWT.NONE);
		lblGenre.setBounds(149, 89, 378, 13);

		lblCountry = toolkit.createLabel(composite, Messages.ViewPartDetails_9, SWT.NONE);
		lblCountry.setBounds(149, 68, 378, 13);

		lblRuntime = toolkit.createLabel(composite, Messages.ViewPartDetails_10, SWT.NONE);
		lblRuntime.setBounds(149, 108, 378, 13);

		Label lblRalisation = toolkit.createLabel(composite, Messages.ViewPartDetails_11, SWT.NONE);
		lblRalisation.setBounds(149, 130, 63, 13);

		txtDirector = toolkit.createText(composite, "New Text", SWT.V_SCROLL); //$NON-NLS-1$
		txtDirector.setText(""); //$NON-NLS-1$
		txtDirector.setBounds(218, 127, 309, 21);

		Label lblActeurs = toolkit.createLabel(composite, Messages.ViewPartDetails_14, SWT.NONE);
		lblActeurs.setBounds(149, 153, 49, 13);

		txtActor = toolkit.createText(composite, "New Text", SWT.V_SCROLL | SWT.MULTI); //$NON-NLS-1$
		txtActor.setText(""); //$NON-NLS-1$
		txtActor.setBounds(218, 150, 309, 21);

		Label lblRsum = toolkit.createLabel(composite, Messages.ViewPartDetails_17, SWT.NONE);
		lblRsum.setBounds(149, 176, 49, 13);

		txtSummary = toolkit.createText(composite, "", SWT.V_SCROLL); //$NON-NLS-1$
		txtSummary.setText(""); //$NON-NLS-1$
		txtSummary.setBounds(218, 173, 309, 120);

		Section sctnInformationsTechniques = toolkit.createSection(parent, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnInformationsTechniques = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sctnInformationsTechniques.heightHint = 136;
		gd_sctnInformationsTechniques.widthHint = 658;
		sctnInformationsTechniques.setLayoutData(gd_sctnInformationsTechniques);
		sctnInformationsTechniques.setDescription(""); //$NON-NLS-1$
		toolkit.paintBordersFor(sctnInformationsTechniques);
		sctnInformationsTechniques.setText(Messages.ViewPartDetails_21);

		Composite composite_1 = toolkit.createComposite(sctnInformationsTechniques, SWT.WRAP);
		toolkit.paintBordersFor(composite_1);
		sctnInformationsTechniques.setClient(composite_1);

		Label lblTitreSurLe = toolkit.createLabel(composite_1, Messages.ViewPartDetails_22, SWT.NONE);
		lblTitreSurLe.setBounds(10, 13, 94, 13);

		txtTitleDisk = toolkit.createText(composite_1, "", SWT.NONE); //$NON-NLS-1$
		txtTitleDisk.setText(""); //$NON-NLS-1$
		txtTitleDisk.setBounds(110, 10, 416, 21);

		Label lblChemin = toolkit.createLabel(composite_1, Messages.ViewPartDetails_25, SWT.NONE);
		lblChemin.setBounds(10, 36, 49, 13);

		txtPath = toolkit.createText(composite_1, "", SWT.NONE);//$NON-NLS-1$
		txtPath.setText(""); //$NON-NLS-1$
		txtPath.setBounds(110, 33, 416, 21);

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);

		bindingManager = new BindingManager();
		bindingManager.addControls(this, lblTitle, lblYear, lblGenre, 
				txtActor, lblCountry, lblRuntime, txtDirector, txtSummary, txtTitleDisk, txtPath);
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
		{
			action = new Action() {				@Override
				public ImageDescriptor getImageDescriptor() {
					return new ImageDescriptor() {
						
						@Override
						public ImageData getImageData() {
							Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_SAVE_EDIT);
							return image.getImageData();
						}
					};   
				}
				@Override
				public void run() {
					updateModel();
				}
				
			};
		}
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager tbm = getViewSite().getActionBars().getToolBarManager();
		tbm.add(action);
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager manager = getViewSite().getActionBars().getMenuManager();
	}
	@Override
	public void setFocus() {
		txtDirector.setFocus();

	}

	@Override
	public void dispose() {
		toolkit.dispose();
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		super.dispose();
	}

	public void showSelection(IWorkbenchPart sourcepart, ISelection selection) {
		//setContentDescription(sourcepart.getTitle() + " (" + selection.getClass().getName() + ")");
		
		if (!selection.isEmpty() && selection instanceof TreeSelection) {
			TreeSelection treeSelection = (TreeSelection)selection;
			Object firstElement = treeSelection.getFirstElement();
			if(firstElement instanceof FilmType){
				setFilm((FilmType)firstElement);
			}
		}
	}

	private void bind(FilmType film) {
		bindingManager.setModel(film);
	}
	
	private void updateModel() {
		bindingManager.updateModel();
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			
			@Override
			public void run() {
				VitecModel.getInstance().save();
			}
		}) ;
		
	}

	public void setFilm(FilmType film) {
		this.film = film;
		bind(film);
	}
}
