package com.testing.ui;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ColorDescriptor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.json.JSONObject;

import com.project.dto.ResponseDto;
import com.project.utils.RestCallUtility;

import org.eclipse.swt.widgets.Display;

import org.eclipse.wb.swt.ResourceManager;

public class TestingView3 extends ViewPart {

	public static final String ID = "com.testing.ui.TestingView3"; //$NON-NLS-1$
	private StyledText urlInputTextBox;
	private LocalResourceManager localResourceManager;
	private Text requestText;
	private Text responsetext;
	private TableViewer tableViewer;

	public TestingView3() {
		createResourceManager();
		setTitleImage(ResourceManager.getPluginImage("ApiTestingTool",
				"icons/icons8-rocket-with-escape-velosity-isolated-on-a-white-background-tal-revivo-tritone-16.png"));
	}

	private void createResourceManager() {
		localResourceManager = new LocalResourceManager(JFaceResources.getResources());
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Black", 9, SWT.BOLD)));
		container.setLayout(new FormLayout());

		Combo combo = new Combo(container, SWT.READ_ONLY);
		combo.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 9, SWT.BOLD)));
		combo.setItems(new String[] { "POST", "GET", "PUT", "PATCH", "DELETE" });
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(0, 40);
		fd_combo.top = new FormAttachment(0, 12);
		fd_combo.left = new FormAttachment(0, 10);
		combo.setLayoutData(fd_combo);
		combo.setText("POST");

		urlInputTextBox = new StyledText(container, SWT.BORDER);
		urlInputTextBox.setMarginColor(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 255, 0))));
		urlInputTextBox
				.setSelectionBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));
		urlInputTextBox.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(255, 127, 80))));
		urlInputTextBox
				.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Semibold", 9, SWT.NORMAL)));
		fd_combo.right = new FormAttachment(urlInputTextBox, -6);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(0, 132);
		fd_text.bottom = new FormAttachment(0, 42);
		fd_text.top = new FormAttachment(0, 12);
		urlInputTextBox.setLayoutData(fd_text);

		Button processUrlButton = new Button(container, SWT.NONE);
		processUrlButton.setImage(ResourceManager.getPluginImage("ApiTestingTool", "icons/icons8-send-16.png"));
		fd_text.right = new FormAttachment(processUrlButton, -6);
		processUrlButton.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 9, SWT.BOLD)));
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(combo, 0, SWT.BOTTOM);
		fd_btnNewButton.top = new FormAttachment(0, 11);
		fd_btnNewButton.right = new FormAttachment(100, -10);
		processUrlButton.setLayoutData(fd_btnNewButton);
		processUrlButton.setText("Process URL");

		Label label = new Label(container, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(100, -10);
		fd_label.top = new FormAttachment(0, 84);
		label.setLayoutData(fd_label);

		SashForm sashForm = new SashForm(container, SWT.SMOOTH);
		FormData fd_sashForm = new FormData();
		fd_sashForm.top = new FormAttachment(urlInputTextBox, 4);
		fd_sashForm.left = new FormAttachment(0, 10);
		fd_sashForm.bottom = new FormAttachment(100, -47);
		fd_sashForm.right = new FormAttachment(100, -10);
		sashForm.setLayoutData(fd_sashForm);

		TabFolder tabRequestFolder = new TabFolder(sashForm, SWT.NONE);
		tabRequestFolder.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		tabRequestFolder
				.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Semibold", 9, SWT.BOLD)));
		tabRequestFolder.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DISABLED_FOREGROUND));

		TabItem tbtmRequestBody = new TabItem(tabRequestFolder, SWT.NONE);
		tbtmRequestBody.setText("Request Body");

		requestText = new Text(tabRequestFolder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		requestText.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		requestText.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Semibold", 9, SWT.NORMAL)));
		requestText.setBackground(localResourceManager.create(ColorDescriptor.createFrom(new RGB(0, 0, 0))));
		tbtmRequestBody.setControl(requestText);

		TabItem tbtmHeaders = new TabItem(tabRequestFolder, SWT.NONE);
		tbtmHeaders.setText("Headers");

		tableViewer = new TableViewer(tabRequestFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tbtmHeaders.setControl(tableViewer.getTable());

		Button btnAddRow = new Button(container, SWT.NONE);
		btnAddRow.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 9, SWT.BOLD)));
		FormData fd_btnAddRow = new FormData();
		fd_btnAddRow.width = 90;
		fd_btnAddRow.top = new FormAttachment(sashForm, 7);

		TabFolder tabResponseFolder = new TabFolder(sashForm, SWT.NONE);
		tabResponseFolder
				.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Semibold", 9, SWT.BOLD)));

		TabItem tbtmResponse = new TabItem(tabResponseFolder, SWT.NONE);
		tbtmResponse.setText("Response : ");

		// This whole label is extra remove after
		// Image img =
		// ResourceManager.getImage("icons/icons8-rocket-sbts2018-outline-16.png");
		final ImageRegistry imageRegistry = new ImageRegistry();
		Image image = imageRegistry.get("YOUR_IMAGE_KEY");
		if (image == null) {
			// Image not in registry, create and add it
			ImageDescriptor descriptor = ImageDescriptor
					.createFromURL(TestingView3.class.getResource("/icons/icons8-send-new-mail-button-100.png"));
			image = descriptor.createImage();
			imageRegistry.put("YOUR_IMAGE_KEY", image);
		}
		Label labelImage = new Label(tabResponseFolder, SWT.CENTER);
		labelImage.setImage(image);
		tbtmResponse.setControl(labelImage);
		// till here

		responsetext = new Text(tabResponseFolder, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL);
		responsetext
				.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI Semibold", 9, SWT.NORMAL)));
		// this is extra
		responsetext.setVisible(false);
		// uncomment this
		// tbtmResponse.setControl(responsetext);

		fd_btnAddRow.left = new FormAttachment(combo, 0, SWT.LEFT);
		btnAddRow.setLayoutData(fd_btnAddRow);
		btnAddRow.setText("Add Row");
		btnAddRow.setVisible(false);

		Button btnRemoveRow = new Button(container, SWT.NONE);
		btnRemoveRow.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 9, SWT.BOLD)));
		FormData fd_btnRemoveRow = new FormData();
		fd_btnRemoveRow.bottom = new FormAttachment(btnAddRow, 0, SWT.BOTTOM);
		fd_btnRemoveRow.left = new FormAttachment(btnAddRow, 13);
		btnRemoveRow.setLayoutData(fd_btnRemoveRow);
		btnRemoveRow.setText("Remove Row");
		btnRemoveRow.setVisible(false);

		showTable(tableViewer, btnAddRow, btnRemoveRow);
		sashForm.setWeights(new int[] { 1, 1 });

		Button beautifyBtn = new Button(container, SWT.NONE);
		beautifyBtn.setFont(localResourceManager.create(FontDescriptor.createFrom("Segoe UI", 9, SWT.BOLD)));
		FormData beautifyBtnbtnNewButton = new FormData();
		beautifyBtnbtnNewButton.top = new FormAttachment(sashForm, 7);
		beautifyBtnbtnNewButton.left = new FormAttachment(combo, 0, SWT.LEFT);
		beautifyBtn.setLayoutData(beautifyBtnbtnNewButton);
		beautifyBtn.setText("Beautify");
		beautifyBtn.setVisible(true);
		// Add selection listener to the tab folder
		tabRequestFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TabItem[] selectedTab = tabRequestFolder.getSelection();
				if (selectedTab != null && selectedTab[0].equals(tbtmHeaders)) {
					// "Headers" tab is selected, make the buttons visible
					btnAddRow.setVisible(true);
					btnRemoveRow.setVisible(true);
					beautifyBtn.setVisible(false);
				} else {
					// Another tab is selected, hide the buttons
					btnAddRow.setVisible(false);
					btnRemoveRow.setVisible(false);
					beautifyBtn.setVisible(true);
				}
			}
		});

		beautifyBtn.addListener(SWT.Selection, event -> {
			// URL not empty
			System.out.println("Request Body : " + requestText.getText());
			requestText.setText(formatJson(requestText.getText()));

		});

		processUrlButton.addListener(SWT.Selection, event -> {
			processUrlButton.setEnabled(false);
			System.out.println("In Process Url btn listener.");
			// Start a new thread for the time-consuming operation
			new Thread(() -> {
				try {	
					// Execute UI updates on the UI thread
					Display.getDefault().asyncExec(() -> {
						System.out.println("In asyncExec block.");
						tbtmResponse.setControl(responsetext);
						responsetext.setVisible(true);
						String url = urlInputTextBox.getText().trim();
						System.out.println("URL : " + url);
						if (url.isEmpty() || url == null) {
							responsetext.setText("URL is empty!");
							processUrlButton.setEnabled(true);
						} else {
							String[][] tableData = (String[][]) tableViewer.getInput();
							System.out.println("In REST call block.");
							RestCallUtility rest = new RestCallUtility();
							ResponseDto response = rest.processApiCall(requestText.getText(), combo.getText(), url,
									tableData);
							System.out.println("In Third display block.");
							responsetext.setText(formatJson(response.getResponse()));
							processUrlButton.setEnabled(true);
						}
					});
				} finally {
					// Display.getDefault().asyncExec(() -> processUrlButton.setEnabled(true));
				}
			}).start();
		});
		// createActions();
		// initializeMenu();
	}

	private static String formatJson(String jsonString) {
		try {
			JSONObject json = new JSONObject(jsonString);
			return json.toString(5); // Use 4 spaces for indentation
		} catch (Exception e) {
			return jsonString;
		}
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
		menuManager.setVisible(true);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	private static void showTable(TableViewer tableViewer, Button addButton, Button removeButton) {

		// Create a TableViewer to display the key-value pairs
		tableViewer.getTable().setHeaderVisible(true);

		// Create columns for the key and value
		TableViewerColumn keyColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn = keyColumn.getColumn();
		keyColumn.getColumn().setText("Key");
		keyColumn.getColumn().setWidth(100);
		keyColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((String[]) element)[0];
			}
		});
		keyColumn.setEditingSupport(new KeyValueEditingSupport(tableViewer, 0));

		TableViewerColumn valueColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tableColumn_1 = valueColumn.getColumn();
		valueColumn.getColumn().setText("Value");
		valueColumn.getColumn().setWidth(100);
		valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((String[]) element)[1];
			}
		});
		valueColumn.setEditingSupport(new KeyValueEditingSupport(tableViewer, 1));

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		addButton.addListener(SWT.Selection, event -> {
			String[][] keyValueData1 = addRow(tableViewer);
			tableViewer.setInput(keyValueData1);
			tableViewer.refresh();
		});

		removeButton.addListener(SWT.Selection, event -> {
			String[][] keyValueData1 = removeRow(tableViewer);
			tableViewer.setInput(keyValueData1);
			tableViewer.refresh();
		});
	}

	private static String[][] removeRow(TableViewer tableViewer) {
		String[][] oldData = (String[][]) tableViewer.getInput();
		int size = oldData.length;
		if (size > 0) {
			String[][] newData = new String[size - 1][2];
			System.arraycopy(oldData, 0, newData, 0, size - 1);
			return newData;
		} else {
			return new String[0][2];
		}
	}

	private static String[][] addRow(TableViewer tableViewer) {
		String[][] oldData = (String[][]) tableViewer.getInput();
		int length = 0;
		if (oldData != null) {
			length = oldData.length;
			String[][] newData = new String[length + 1][2];
			System.arraycopy(oldData, 0, newData, 0, length);
			newData[length] = new String[] { "New Key", "New Value" };
			return newData;
		}
		String[][] keyValueData = { { "New Key", "New Value" } };
		return keyValueData;
	}

	private static class KeyValueEditingSupport extends EditingSupport {

		private final TableViewer tableViewer;
		private final TextCellEditor editor;
		private final int columnIndex;

		public KeyValueEditingSupport(TableViewer viewer, int columnIndex) {
			super(viewer);
			this.tableViewer = viewer;
			this.editor = new TextCellEditor(viewer.getTable());
			this.columnIndex = columnIndex;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected Object getValue(Object element) {
			String[] keyValue = (String[]) element;
			return columnIndex == 0 ? keyValue[0] : keyValue[1];
		}

		@Override
		protected void setValue(Object element, Object value) {
			String[] keyValue = (String[]) element;
			if (columnIndex == 0) {
				keyValue[0] = value.toString();
			} else {
				keyValue[1] = value.toString();
			}
			tableViewer.update(element, null);
		}
	}
}
