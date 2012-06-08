package fr.vitec.batch.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import pojo.BaseSample;
import fr.vitec.batch.dialog.BatchDialog;
import fr.vitec.batch.extension.FindFilmInfo;
import fr.vitec.batch.process.BatchManager;
import fr.vitec.batch.process.logger.BatchDialogLogger;
import fr.vitec.batch.process.logger.IBatchLogger;
import fr.vitec.batch.process.progressmanager.ProgressBarManager;
import fr.vitec.batch.ui.celleditor.CellModifier;
import fr.vitec.batch.ui.celleditor.PathCellEditor;
import fr.vitec.batch.ui.provider.ViewComboxLabelProvider;
import fr.vitec.batch.ui.provider.ViewTableContentProvider;
import fr.vitec.batch.ui.provider.ViewTableLabelProvider;
import fr.vitec.batch.util.ChoixFilm;
import fr.vitec.fmk.dialog.UIMessages;
import fr.vitec.fmk.exception.VitecException;
import fr.vitec.fmk.resource.SWTResourceManager;
import fr.vitec.model.VitecModel;
import fr.vitec.model.xmlbinding.DirectoryType;


public class BatchView extends ViewPart {

	private static final String EXTENTION_FIND_FILM_ID = "fr.vitec.batch.findFilm";

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "fr.vitec.batch.views.BatchView";

	private TableViewer viewer;
	private ComboViewer comboViewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());


	private List<String> columnsName = new ArrayList<String>();

	private Button btnAutomatique;

	private Button btnDemanderSiConflit;

	private Button btnToujoursDemander;
	
	/**
	 * The constructor.
	 */
	public BatchView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		parent.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		parent.setLayout(new GridLayout(1, false));

		Section sctnAnalyseDesDisques = formToolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
		GridData gd_sctnAnalyseDesDisques = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_sctnAnalyseDesDisques.heightHint = 220;
		gd_sctnAnalyseDesDisques.widthHint = 847;
		sctnAnalyseDesDisques.setLayoutData(gd_sctnAnalyseDesDisques);
		formToolkit.paintBordersFor(sctnAnalyseDesDisques);
		sctnAnalyseDesDisques.setText(Messages.BatchView_4);

		Composite composite_1 = formToolkit.createComposite(sctnAnalyseDesDisques, SWT.WRAP);
		sctnAnalyseDesDisques.setClient(composite_1);
		formToolkit.paintBordersFor(composite_1);
		
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		viewer = new TableViewer(composite_1, style);
		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnChemin = tableViewerColumn.getColumn();
		tblclmnChemin.setWidth(430);
		tblclmnChemin.setText(Messages.BatchView_tblclmnChemin_text);

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnNewColumn = tableViewerColumn_1.getColumn();
		tblclmnNewColumn.setWidth(49);
		tblclmnNewColumn.setText(Messages.BatchView_tblclmnNewColumn_text_2);

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnRcursif = tableViewerColumn_2.getColumn();
		tblclmnRcursif.setWidth(51);
		tblclmnRcursif.setText(Messages.BatchView_tblclmnRcursif_text_1);

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tblclmnChoixChemin = tableViewerColumn_3.getColumn();
		tblclmnChoixChemin.setWidth(88);
		tblclmnChoixChemin.setText(Messages.BatchView_tblclmnChoixChemin_text_1);

		Composite composite = formToolkit.createComposite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 17;
		gd_composite.widthHint = 101;
		composite.setLayoutData(gd_composite);
		formToolkit.paintBordersFor(composite);

		Section sctnParamtrageDeLanalyse = formToolkit.createSection(parent, Section.EXPANDED | Section.TITLE_BAR);
		GridData gd_sctnParamtrageDeLanalyse = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_sctnParamtrageDeLanalyse.widthHint = 847;
		gd_sctnParamtrageDeLanalyse.heightHint = 157;
		sctnParamtrageDeLanalyse.setLayoutData(gd_sctnParamtrageDeLanalyse);
		formToolkit.paintBordersFor(sctnParamtrageDeLanalyse);
		sctnParamtrageDeLanalyse.setText(Messages.BatchView_5);

		Composite composite_2 = formToolkit.createComposite(sctnParamtrageDeLanalyse, SWT.NONE);
		sctnParamtrageDeLanalyse.setClient(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(2, false));

		Label lblSiteWeb = formToolkit.createLabel(composite_2, Messages.BatchView_6, SWT.NONE);
		lblSiteWeb.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		comboViewer = new ComboViewer(composite_2, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 417;
		combo.setLayoutData(gd_combo);
		formToolkit.paintBordersFor(combo);
		
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setLabelProvider(new ViewComboxLabelProvider());
		Object[] findFilmInfosArray = getFindFilmInfos().toArray();
		comboViewer.setInput(findFilmInfosArray);
		
		Group grpChoixDuFilm = new Group(composite_2, SWT.NONE);
		grpChoixDuFilm.setLayout(new GridLayout(3, false));
		GridData gd_grpChoixDuFilm = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_grpChoixDuFilm.heightHint = 29;
		gd_grpChoixDuFilm.widthHint = 356;
		grpChoixDuFilm.setLayoutData(gd_grpChoixDuFilm);
		grpChoixDuFilm.setText(Messages.BatchView_grpChoixDuFilm_text);
		formToolkit.adapt(grpChoixDuFilm);
		formToolkit.paintBordersFor(grpChoixDuFilm);

		btnAutomatique = new Button(grpChoixDuFilm, SWT.RADIO);
		btnAutomatique.setSelection(true);
		formToolkit.adapt(btnAutomatique, true, true);
		btnAutomatique.setText(Messages.BatchView_btnAutomatique_text);

		btnDemanderSiConflit = new Button(grpChoixDuFilm, SWT.RADIO);
		formToolkit.adapt(btnDemanderSiConflit, true, true);
		btnDemanderSiConflit.setText(Messages.BatchView_btnDemanderSiConflit_text);

		btnToujoursDemander = new Button(grpChoixDuFilm, SWT.RADIO);
		formToolkit.adapt(btnToujoursDemander, true, true);
		btnToujoursDemander.setText(Messages.BatchView_btnToujoursDemander_text);

		Button btnLancementDeLanalye = formToolkit.createButton(parent, Messages.BatchView_7, SWT.NONE);
		btnLancementDeLanalye.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = ((Button)e.getSource()).getParent().getShell();
				BatchDialog dialog = new BatchDialog(shell);
				dialog.setBlockOnOpen(false);
				dialog.open();
				
				BatchManager batchManager = new BatchManager(getSiteSelection(), ((VitecModel)viewer.getInput()).getDirectories(), getChoixFilm());
				IBatchLogger batchLogger = new BatchDialogLogger(dialog);
				batchManager.setLogger(batchLogger);
				batchManager.setProgressManager(new ProgressBarManager(dialog.getProgressBar()));
				
				
				batchManager.execute();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		setCellsEditor();
		
		viewer.setContentProvider(new ViewTableContentProvider());
		viewer.setLabelProvider(new ViewTableLabelProvider());
		//viewer.setSorter(new NameSorter());

		//viewer.setInput(VitecModel.getInstance());
		BaseSample model = new BaseSample();
		viewer.setInput(model.getModel());
		
		if(findFilmInfosArray.length > 0){
			comboViewer.setSelection(new StructuredSelection(findFilmInfosArray[0]));
		} else{
			btnLancementDeLanalye.setEnabled(false);
		}
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	protected List<FindFilmInfo> getFindFilmInfos() {
		List<FindFilmInfo> findFilmInfos = new ArrayList<FindFilmInfo>();
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENTION_FIND_FILM_ID);
		for (IConfigurationElement e : config) {
			FindFilmInfo o;
			try {
				o = (FindFilmInfo) e.createExecutableExtension("class");
			} catch (CoreException ex) {
				throw new VitecException(ex);
			}
			if (o instanceof FindFilmInfo) {
				findFilmInfos.add(o);
			}
		}
		return findFilmInfos;
	}

	private void setCellsEditor() {
		final Table table = viewer.getTable();
		viewer.setColumnProperties(getColumnsName());
		
		// Create the cell editors
		CellEditor[] editors = new CellEditor[table.getColumnCount()];

		TextCellEditor pathEditor = new TextCellEditor(table);
		editors[0] = pathEditor;
		((Text)pathEditor.getControl()).addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				DirectoryType dir = getDirectorySelection();
				if(!new File(dir.getPath()).exists()){
					UIMessages.warning("Erreur de saisie", "Le répertoire n'existe pas");
					((Text)(e.getSource())).setFocus();
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		}); 

		editors[1] = new TextCellEditor(table);
		editors[2] = new CheckboxCellEditor(table);
		editors[3] = new PathCellEditor(viewer);
		// Assign the cell editors to the viewer 
		viewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		viewer.setCellModifier(new CellModifier(viewer));

	}

	private String[] getColumnsName() {
		if(columnsName.size()!=viewer.getTable().getColumnCount()){
			for(int i = 0; i<viewer.getTable().getColumnCount(); i++){
				columnsName.add(new Integer(i).toString());
			}
		}
		return columnsName.toArray(new String[columnsName.size()]);
	}

	private DirectoryType getDirectorySelection(){
		return (DirectoryType)((StructuredSelection)viewer.getSelection()).getFirstElement();
	}
	
	private FindFilmInfo getSiteSelection(){
		return (FindFilmInfo)((StructuredSelection)comboViewer.getSelection()).getFirstElement();
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				BatchView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed"); //$NON-NLS-1$
			}
		};
		action1.setText("Action 1"); //$NON-NLS-1$
		action1.setToolTipText("Action 1 tooltip"); //$NON-NLS-1$
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed"); //$NON-NLS-1$
			}
		};
		action2.setText("Action 2"); //$NON-NLS-1$
		action2.setToolTipText("Action 2 tooltip"); //$NON-NLS-1$
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString()); //$NON-NLS-1$
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				//doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
				viewer.getControl().getShell(),
				"Batch View", //$NON-NLS-1$
				message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private ChoixFilm getChoixFilm(){
		if(btnAutomatique.getSelection()){
			return ChoixFilm.NEVER;
		}else if(btnDemanderSiConflit.getSelection()){
			return ChoixFilm.CONFLICT;
		}else if(btnToujoursDemander.getSelection()){
			return ChoixFilm.ALWYAYS;
		}
		return null;
	}
}