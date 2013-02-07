package fr.vitec.main;

import java.util.HashMap;
import java.util.Map;

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
import fr.vitec.fmk.image.ImageUtil;
import fr.vitec.fmk.resource.SWTResourceManager;
import fr.vitec.main.message.Messages;
import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.FilmType;

public class ViewPartDetails extends ViewPart {

	public static final String ID = "fr.vitec.main.viewDetails"; //$NON-NLS-1$
	public static Map<FilmType, Image> images = new HashMap<FilmType, Image>();
	
	
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
	private Text lblTitle;
	private Text lblYear;
	private Text lblGenre;
	private Text lblCountry;
	private Text lblRuntime;

	private FilmType film;

	private Label lblImage;
	
	public ViewPartDetails() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		Section sctnNewSection = toolkit.createSection(parent, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnNewSection = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 3);
		gd_sctnNewSection.widthHint = 659;
		gd_sctnNewSection.heightHint = 415;
		sctnNewSection.setLayoutData(gd_sctnNewSection);
		sctnNewSection.setSize(227, 80);
		toolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText(Messages.ViewPartDetails_1);
		sctnNewSection.setDescription(""); //$NON-NLS-1$


		Composite composite = toolkit.createComposite(sctnNewSection, SWT.WRAP);
		sctnNewSection.setClient(composite);
		toolkit.paintBordersFor(composite);

		lblTitle = new Text(composite, SWT.NONE);
		lblTitle.setBounds(183, 12, 390, 33);
		lblTitle.setFont(SWTResourceManager.getFont("Tahoma", 16, SWT.BOLD)); //$NON-NLS-1$

		lblImage = toolkit.createLabel(composite, "", SWT.NONE);
		lblImage.setImage(SWTResourceManager.getImage("D:\\dev\\workspace_test\\fr.vitec.main\\src\\product_lg.gif")); //$NON-NLS-1$
		lblImage.setBounds(10, 10, 167, 202);

		lblYear = new Text(composite, SWT.NONE);
		lblYear.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblYear.setBounds(262, 65, 309, 21);

		lblGenre = new Text(composite, SWT.NONE);
		lblGenre.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblGenre.setBounds(262, 131, 309, 21);

		lblCountry = new Text(composite, SWT.NONE);
		lblCountry.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblCountry.setBounds(262, 98, 309, 21);

		lblRuntime = new Text(composite, SWT.NONE);
		lblRuntime.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblRuntime.setBounds(262, 164, 309, 21);

		Label lblRalisation = toolkit.createLabel(composite, Messages.ViewPartDetails_11, SWT.NONE);
		lblRalisation.setBounds(193, 200, 63, 13);

		txtDirector = toolkit.createText(composite, "New Text", SWT.V_SCROLL); //$NON-NLS-1$
		txtDirector.setText(""); //$NON-NLS-1$
		txtDirector.setBounds(262, 197, 309, 21);

		Label lblActeurs = toolkit.createLabel(composite, Messages.ViewPartDetails_14, SWT.NONE);
		lblActeurs.setBounds(193, 233, 49, 13);

		txtActor = toolkit.createText(composite, "New Text", SWT.V_SCROLL | SWT.MULTI); //$NON-NLS-1$
		txtActor.setText(""); //$NON-NLS-1$
		txtActor.setBounds(262, 230, 309, 21);

		Label lblRsum = toolkit.createLabel(composite, Messages.ViewPartDetails_17, SWT.NONE);
		lblRsum.setBounds(193, 264, 49, 13);

		txtSummary = toolkit.createText(composite, "", SWT.V_SCROLL); //$NON-NLS-1$
		txtSummary.setText(""); //$NON-NLS-1$
		txtSummary.setBounds(264, 261, 309, 120);

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);

		bindingManager = new BindingManager();
		
		Label label = new Label(composite, SWT.NONE);
		label.setBounds(193, 65, 49, 13);
		toolkit.adapt(label, true, true);
		label.setText(Messages.ViewPartDetails_7);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(193, 96, 69, 15);
		toolkit.adapt(lblNewLabel, true, true);
		lblNewLabel.setText(Messages.ViewPartDetails_9);
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(193, 131, 37, 13);
		toolkit.adapt(lblNewLabel_1, true, true);
		lblNewLabel_1.setText(Messages.ViewPartDetails_8);
		
		Label lblDure = new Label(composite, SWT.NONE);
		lblDure.setBounds(193, 164, 63, 13);
		toolkit.adapt(lblDure, true, true);
		lblDure.setText(Messages.ViewPartDetails_10);
																new Label(parent, SWT.NONE);
														
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
		Image image = images.get(film);
		if(image == null){
			String imageEncodedStr = film.getImageEncoded();
			image = ImageUtil.getSwtImageFromEncodedString(imageEncodedStr);
			images.put(film, image);
		}
		lblImage.setImage(image);
	}
}
