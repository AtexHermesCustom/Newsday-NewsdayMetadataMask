package com.atex.h11.newsday.metadata.sp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.common.Constants;
import com.atex.h11.newsday.metadata.filter.NumericDocumentFilter;
import com.atex.h11.newsday.util.DateLabelFormatter;
import com.atex.h11.newsday.util.InfoBox;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.xml.xpath.XPathExpressionException;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ScrollPaneConstants;
import javax.swing.JFormattedTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.AbstractDocument;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.tree.DefaultTreeModel;

public class MetadataPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger;
	
	private String objId;
	private String objName;
	private String objLevel;
	private String pub;
	private ConfigModel config = null;
	private HashMap<String, String> metadata = null;
	private boolean panelDisabled = false;
	
	private JLabel lblPub;	
	private JLabel lblTitle;
	private JLabel lblLevel;
	private JTextField txtEmail1;
	private JTextField txtEmail2;
	private JTextField txtEmail3;
	private JTextField txtContributor;
	private JTextField txtStoryGroup;
	private JTextField txtCustomKeyword;
	private JComboBox cmbPub;
	private JComboBox cmbReporter1;
	private JComboBox cmbDesk;
	private JComboBox cmbReporter2;
	private JComboBox cmbReporter3;
	private JComboBox cmbPriority;
	private JComboBox cmbStoryType;
	private JComboBox cmbLabel;
	private JComboBox cmbPrintSection;
	private JComboBox cmbPrintSequence;
	private JComboBox cmbHomepage;
	private JComboBox cmbArrivalStatus;
	private JDatePickerImpl dtpckEmbargoDate;
	private JSpinner spnrEmbargoTime;
	private JLabel lblPage;
	private JCheckBox chkEmbargo;	
	private JCheckBox chkExclusive;
	private JScrollPane scrlCommunities;
	private JScrollPane scrlCategories;
	private JTree trCategories;
	private JTree trCommunities;
	private JScrollPane scrlSelCategories;
	private JScrollPane scrlSelCommunities;
	private JList lstSelCategories;
	private JList lstSelCommunities;
	private JScrollPane scrlPrintExtra;
	private JScrollPane scrlDigitalExtra1;
	private JScrollPane scrlDigitalExtra2;
	private JScrollPane scrlDescription;
	private JTextArea txtrDescription;
	private JTextArea txtrPrintExtra;
	private JTextArea txtrDigitalExtra1;
	private JTextArea txtrDigitalExtra2;
	private JLabel label;
	private JFormattedTextField ftfAssignLength;
	private JFormattedTextField ftfPrintPage;
	private DefaultListModel selCategoriesModel = null;
	private DefaultListModel selCommunitiesModel = null;
	JButton btnAddKeyword;
	private JLabel lblEmbargoTime;
	private JLabel lblVersion;
	private JLabel lblPhoto;
	private JTextField txtPhotoDescription;
	private JLabel lblBudgetHead;
	private JTextField txtBudgetHead;	
	
	private String prevReporter1 = "";
	private String prevReporter2 = "";
	private String prevReporter3 = "";

	// constructor
	public MetadataPanel(ConfigModel config, HashMap<String, String> metadata, Logger l, 
		String objId, String objName, String objLevel, String pub) 
			throws XPathExpressionException {
		
		this.objId = objId;
		this.objName = objName;
		this.objLevel = objLevel;
		this.pub = pub;
		this.config = config;
		this.metadata = metadata;		
		logger = l;
		
		InfoBox.setParentComponent(this);
		
		// initialize panel
		initPanel();
		
		if (objName == null || objLevel == null || objName.isEmpty() || objLevel.isEmpty()) {
			// disable - do not allow entry of metadata
			disableControls();
			panelDisabled = true;
		} else {
			// init components and set values based from metadata hash
			setComponentValues();
			
			// initialize listeners 
			// - must be done after setting component values
			setComponentListeners();
			
			String desk = metadata.get("DESK");
			if (desk == null || desk.isEmpty()) {	// set defaults based on level
				setDefaults(objLevel);
			}
			
			// not needed
			//// check and highlight mandatory fields that are missing 
			//isReady();		
		}		
	}

	protected void initPanel() { 
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(41dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(50dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(63dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(53dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(68dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:30dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:36dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:30dlu"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(12dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(12dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(12dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(68dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("top:max(40dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		lblTitle = new JLabel("Story Package Metadata");
		add(lblTitle, "2, 2, 7, 1");
		
		lblLevel = new JLabel("Level:");
		add(lblLevel, "10, 2, 3, 1");
		
		lblPub = new JLabel("Pub");
		add(lblPub, "14, 2, right, default");
		
		cmbPub = new JComboBox();
		add(cmbPub, "16, 2, 3, 1, fill, default");
		
		JLabel lblReporter = new JLabel("Reporter");
		add(lblReporter, "2, 4, right, default");
		
		cmbReporter1 = new JComboBox();
		cmbReporter1.setEditable(true);
		add(cmbReporter1, "4, 4, 5, 1, fill, default");
		
		txtEmail1 = new JTextField();
		add(txtEmail1, "10, 4, 3, 1, fill, default");
		txtEmail1.setColumns(10);
		
		label = new JLabel("Desk");
		add(label, "14, 4, right, default");
		
		cmbDesk = new JComboBox();
		add(cmbDesk, "16, 4, 3, 1, fill, default");
		
		JLabel lblndReporter = new JLabel("2nd Reporter");
		add(lblndReporter, "2, 6, right, default");
		
		cmbReporter2 = new JComboBox();
		cmbReporter2.setEditable(true);
		add(cmbReporter2, "4, 6, 5, 1, fill, default");
		
		txtEmail2 = new JTextField();
		txtEmail2.setColumns(10);
		add(txtEmail2, "10, 6, 3, 1, fill, default");
		
		JLabel lblAssignedLength = new JLabel("Length");
		add(lblAssignedLength, "14, 6");
		
		JLabel lblPriority = new JLabel("Priority");
		add(lblPriority, "16, 6");
		
		JLabel lblrdReporter = new JLabel("3rd Reporter");
		add(lblrdReporter, "2, 8, right, default");
		
		cmbReporter3 = new JComboBox();
		cmbReporter3.setEditable(true);
		add(cmbReporter3, "4, 8, 5, 1, fill, default");
		
		txtEmail3 = new JTextField();
		txtEmail3.setColumns(10);
		add(txtEmail3, "10, 8, 3, 1, fill, default");
		
		ftfAssignLength = new JFormattedTextField();
		((AbstractDocument) ftfAssignLength.getDocument()).setDocumentFilter(
				new NumericDocumentFilter(ftfAssignLength.getDocument(), false, false));
		add(ftfAssignLength, "14, 8, fill, default");
		
		cmbPriority = new JComboBox();
		add(cmbPriority, "16, 8, 3, 1, fill, default");
		
		JLabel lblContributor = new JLabel("Contributor");
		add(lblContributor, "2, 10, right, default");
		
		txtContributor = new JTextField();
		txtContributor.setColumns(10);
		add(txtContributor, "4, 10, 5, 1, fill, default");
		
		lblBudgetHead = new JLabel("Budget Head");
		add(lblBudgetHead, "10, 10, right, default");
		
		txtBudgetHead = new JTextField();
		txtBudgetHead.setColumns(10);
		add(txtBudgetHead, "12, 10, 7, 1, fill, default");
		
		JLabel lblStoryGroup = new JLabel("Story Group");
		add(lblStoryGroup, "2, 12, right, default");
		
		txtStoryGroup = new JTextField();
		txtStoryGroup.setColumns(10);
		add(txtStoryGroup, "4, 12, 5, 1, fill, default");
		
		scrlDescription = new JScrollPane();
		scrlDescription.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDescription, "12, 12, 7, 3, fill, fill");
		
		txtrDescription = new JTextArea();
		txtrDescription.setLineWrap(true);
		scrlDescription.setViewportView(txtrDescription);
		
		JLabel lblDescription = new JLabel("Description");
		add(lblDescription, "10, 12, right, default");
		
		JLabel lblStoryType = new JLabel("Story Type");
		add(lblStoryType, "2, 14, right, default");
		
		cmbStoryType = new JComboBox();
		add(cmbStoryType, "4, 14, 3, 1, fill, default");
		
		JLabel lblLabel = new JLabel("Label");
		add(lblLabel, "2, 16, right, default");
		
		cmbLabel = new JComboBox();
		add(cmbLabel, "4, 16, 3, 1, fill, default");
		
		scrlDigitalExtra1 = new JScrollPane();
		scrlDigitalExtra1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDigitalExtra1, "12, 16, 7, 3, fill, fill");
		
		txtrDigitalExtra1 = new JTextArea();
		txtrDigitalExtra1.setLineWrap(true);
		scrlDigitalExtra1.setViewportView(txtrDigitalExtra1);
		
		JLabel lblDigitalExtras = new JLabel("Digital Extras");
		add(lblDigitalExtras, "10, 16, right, default");
		
		lblPhoto = new JLabel("Photo");
		add(lblPhoto, "2, 18, right, default");
		
		txtPhotoDescription = new JTextField();
		txtPhotoDescription.setColumns(10);
		add(txtPhotoDescription, "4, 18, 5, 1, fill, default");
		
		JLabel lblPrintExtra = new JLabel("Print Extra");
		add(lblPrintExtra, "2, 20, right, default");
		
		scrlPrintExtra = new JScrollPane();
		scrlPrintExtra.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlPrintExtra, "4, 20, 5, 3, fill, fill");
		
		txtrPrintExtra = new JTextArea();
		txtrPrintExtra.setLineWrap(true);
		scrlPrintExtra.setViewportView(txtrPrintExtra);
		
		scrlDigitalExtra2 = new JScrollPane();
		scrlDigitalExtra2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDigitalExtra2, "12, 20, 7, 3, fill, fill");
		
		txtrDigitalExtra2 = new JTextArea();
		txtrDigitalExtra2.setLineWrap(true);
		scrlDigitalExtra2.setViewportView(txtrDigitalExtra2);
		
		JLabel lblPrint = new JLabel("PRINT");
		add(lblPrint, "2, 24, right, default");
		
		JLabel lblSection = new JLabel("Section");
		add(lblSection, "4, 24, right, default");
		
		cmbPrintSection = new JComboBox();
		add(cmbPrintSection, "6, 24, 3, 1, fill, default");
		
		JLabel lblSequenceOr = new JLabel("Sequence");
		add(lblSequenceOr, "10, 24, right, default");
		
		cmbPrintSequence = new JComboBox();
		add(cmbPrintSequence, "12, 24, 3, 1, fill, default");
		
		lblPage = new JLabel("Page");
		add(lblPage, "16, 24, right, default");
		
		ftfPrintPage = new JFormattedTextField();
		((AbstractDocument) ftfPrintPage.getDocument()).setDocumentFilter(
				new NumericDocumentFilter(ftfPrintPage.getDocument(), false, false));		
		//((AbstractDocument) ftfPrintPage.getDocument()).setDocumentFilter(
		//		new AlphaNumericDocumentFilter());		
		add(ftfPrintPage, "18, 24, fill, default");
		
		JLabel lblWeb = new JLabel("WEB");
		add(lblWeb, "2, 26, right, default");
		
		JLabel lblHomePage = new JLabel("Home page");
		add(lblHomePage, "4, 26, right, default");
		
		cmbHomepage = new JComboBox();
		add(cmbHomepage, "6, 26, 3, 1, fill, default");
		
		JLabel lblArrivalStatus = new JLabel("Arrival Status");
		add(lblArrivalStatus, "10, 26, right, default");
		
		cmbArrivalStatus = new JComboBox();
		add(cmbArrivalStatus, "12, 26, 3, 1, fill, default");

		Date currentDate = new Date();

		// date picker
		UtilDateModel dateModel = new UtilDateModel();	// current date by default
		dateModel.setSelected(true);
		
		Properties datePanelProps = new Properties();
		datePanelProps.put("text.today", "Today");
		datePanelProps.put("text.month", "Month");
		datePanelProps.put("text.year", "Year");								
		
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, datePanelProps);
		
		chkEmbargo = new JCheckBox("Embargo");
		add(chkEmbargo, "4, 28");
		
		dtpckEmbargoDate = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		dtpckEmbargoDate.getJFormattedTextField().setEditable(true);
		add(dtpckEmbargoDate, "6, 28, 3, 1");		
				
		lblEmbargoTime = new JLabel("Embargo Time");
		add(lblEmbargoTime, "10, 28, right, default");

		SpinnerDateModel timeModel = new SpinnerDateModel(currentDate, null, null, Calendar.MINUTE);
		spnrEmbargoTime = new JSpinner();
		spnrEmbargoTime.setModel(timeModel);
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnrEmbargoTime, Constants.TIME_FORMAT);
		spnrEmbargoTime.setEditor(dateEditor);
		add(spnrEmbargoTime, "12, 28, fill, fill");
		
		JLabel lblCategories = new JLabel("Categories");
		add(lblCategories, "2, 30, right, top");
		
		scrlCategories = new JScrollPane();
		add(scrlCategories, "4, 30, 5, 1, fill, fill");
		
		trCategories = new JTree();
		trCategories.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("JTree")));
		trCategories.setToolTipText("Double click an item to add it to the Selected list");
		scrlCategories.setViewportView(trCategories);
		
		JLabel lblCommunities = new JLabel("Communities");
		add(lblCommunities, "10, 30, right, top");
		
		scrlCommunities = new JScrollPane();
		add(scrlCommunities, "12, 30, 7, 1, fill, fill");
		
		trCommunities = new JTree();
		trCommunities.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("JTree")));
		trCommunities.setToolTipText("Double click an item to add it to the Selected list");
		scrlCommunities.setViewportView(trCommunities);
		
		JLabel lblSelectedCategories = new JLabel("<html><p>Selected</p><p>Categories</p></html>");
		lblSelectedCategories.setVerticalAlignment(SwingConstants.TOP);
		add(lblSelectedCategories, "2, 32, right, top");
		
		scrlSelCategories = new JScrollPane();
		add(scrlSelCategories, "4, 32, 5, 1, fill, fill");
		
		lstSelCategories = new JList();
		lstSelCategories.setToolTipText("Double click an item to remove it from the list");
		scrlSelCategories.setViewportView(lstSelCategories);
		
		JLabel lblselectedcommunities = new JLabel("<html><p>Selected</p><p>Communities</p></html>");
		lblselectedcommunities.setVerticalAlignment(SwingConstants.TOP);
		add(lblselectedcommunities, "10, 32, right, top");
		
		scrlSelCommunities = new JScrollPane();
		add(scrlSelCommunities, "12, 32, 7, 1, fill, fill");
		
		lstSelCommunities = new JList();
		lstSelCommunities.setToolTipText("Double click an item to remove it from the list");
		scrlSelCommunities.setViewportView(lstSelCommunities);
		
		JLabel lblcustomkeyword = new JLabel("<html><p>Custom</p><p>Keyword</p></html>");
		lblcustomkeyword.setVerticalAlignment(SwingConstants.TOP);
		add(lblcustomkeyword, "2, 34, right, top");
		
		txtCustomKeyword = new JTextField();
		txtCustomKeyword.setColumns(10);
		add(txtCustomKeyword, "4, 34, 3, 1, fill, default");
		
		btnAddKeyword = new JButton("Add Keyword");
		add(btnAddKeyword, "8, 34");		
		
		chkExclusive = new JCheckBox("Exclusive");
		add(chkExclusive, "12, 34, 2, 1");
		
		lblVersion = new JLabel("Version:");
		add(lblVersion, "14, 34, 5, 1, right, default");		
		
		// hide the Pub combobox - not needed
		// the pub is determined from the level
		lblPub.setVisible(false);
		cmbPub.setVisible(false);
	}
		
	protected void setComponentListeners() {
		// -----------------------------------------------------------------		
		// Init listeners
		cmbReporter1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					prevReporter1 = e.getItem().toString();
				}
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String selReporter = item.toString();
						if (isDuplicateReporter(1, selReporter)) {	
							// check duplicate reporter
							InfoBox.showMessage("WARNING: \"" + selReporter + "\" has already been selected.", 
									"Duplicate reporter", JOptionPane.WARNING_MESSAGE);
							cmbReporter1.setSelectedItem(prevReporter1);
						} else {
							updateEmail(selReporter, txtEmail1);
						}						
					}
				}
			}
		});
		
		cmbReporter2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					prevReporter2 = e.getItem().toString();
				}
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String selReporter = item.toString();
						if (isDuplicateReporter(2, selReporter)) {	
							// check duplicate reporter
							InfoBox.showMessage("WARNING: \"" + selReporter + "\" has already been selected.", 
									"Duplicate reporter", JOptionPane.WARNING_MESSAGE);
							cmbReporter2.setSelectedItem(prevReporter2);
						} else {
							updateEmail(selReporter, txtEmail2);
						}		
					}
				}
			}
		});		
		
		cmbReporter3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					prevReporter3 = e.getItem().toString();
				}
				
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String selReporter = item.toString();
						if (isDuplicateReporter(3, selReporter)) {	
							// check duplicate reporter
							InfoBox.showMessage("WARNING: \"" + selReporter + "\" has already been selected.", 
									"Duplicate reporter", JOptionPane.WARNING_MESSAGE);
							cmbReporter3.setSelectedItem(prevReporter3);
						} else {
							updateEmail(selReporter, txtEmail3);
						}							
					}
				}
			}
		});
		
		cmbDesk.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String desk = item.toString();
						String deskXpath;
						if (desk.equalsIgnoreCase(Constants.ALL)) {
							deskXpath = "desk";		// no condition
						} else {
							deskXpath = "desk[@id='" + desk + "']";		// should match desk id
						}						
						try {							
							// update related components
							config.initComboBox(cmbHomepage, pub, "homepage", deskXpath + "/item");
							config.initComboBox(cmbPrintSection, pub, "printSection", deskXpath + "/item");
							config.initComboBox(cmbPrintSequence, pub, "printSequence", 
									deskXpath + "/printSection[@id='" + cmbPrintSection.getSelectedItem().toString().trim() + "']/item");							
						} catch (Exception ex) {
							InfoBox.ShowException(ex);
						}
					}
				}
			}
		});
		
		cmbPrintSection.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String printSection = item.toString();
						String desk = cmbDesk.getSelectedItem().toString().trim();
						String deskXpath;
						if (desk.equalsIgnoreCase(Constants.ALL)) {
							deskXpath = "desk";		// no condition
						} else {
							deskXpath = "desk[@id='" + desk + "']";		// should match desk id
						}							
						try {							
							// update related components
							config.initComboBox(cmbPrintSequence, pub, "printSequence", 
									deskXpath + "/printSection[@id='" + printSection + "']/item");							
						} catch (Exception ex) {
							InfoBox.ShowException(ex);
						}
					}
				}
			}			
		});
		
		cmbArrivalStatus.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item != null) {
						String arrivalStatus = item.toString().trim();
						if (arrivalStatus.equalsIgnoreCase(Constants.LIVE)) {
							// enable Embargo checkbox
							chkEmbargo.setEnabled(true);
							// enable Embargo-related components if checkbox is ticked
							if (chkEmbargo.isSelected()) {
								setEmbargoComponentsEnabled(true);
							} else {
								setEmbargoComponentsEnabled(false);
							}
						} else {
							// disable and deselect Embargo checkbox
							chkEmbargo.setEnabled(false);
							chkEmbargo.setSelected(false);
							// disable Embargo-related components
							setEmbargoComponentsEnabled(false);							
						}
					}
				}
			}
		});
		
		chkEmbargo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chkEmbargo.isSelected()) {	
					// enable Embargo-related components
					setEmbargoComponentsEnabled(true);
				} else {	
					// disable Embargo-related components
					setEmbargoComponentsEnabled(false);
				}
			}
		});			
		
		trCategories.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) trCategories.getLastSelectedPathComponent();
				if (selNode != null && selNode.isLeaf()) {
					if (e.getClickCount() == 2) {	// double-click
						String selItem = selNode.getUserObject().toString();
						addItemToListModel(selCategoriesModel, selItem);
					}
				}
			}
		});		
		
		trCommunities.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) trCommunities.getLastSelectedPathComponent();
				if (selNode != null && selNode.isLeaf()) {
					if (e.getClickCount() == 2) {	// double-click
						String selItem = selNode.getUserObject().toString();
						addItemToListModel(selCommunitiesModel, selItem);
					}
				}
			}
		});			
		
		selCategoriesModel.addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) {
			    SwingUtilities.invokeLater(new Runnable() {
			    	public void run() {
			    		lstSelCategories.ensureIndexIsVisible(selCategoriesModel.size() - 1);
			    	}
			    });				
			}

			@Override
			public void intervalRemoved(ListDataEvent e) { }

			@Override
			public void contentsChanged(ListDataEvent e) { }
		});
		
		selCommunitiesModel.addListDataListener(new ListDataListener() {
			@Override
			public void intervalAdded(ListDataEvent e) {
			    SwingUtilities.invokeLater(new Runnable() {
			    	public void run() {
			    		lstSelCommunities.ensureIndexIsVisible(selCommunitiesModel.size() - 1);
			    	}
			    });								
			}

			@Override
			public void intervalRemoved(ListDataEvent e) { }

			@Override
			public void contentsChanged(ListDataEvent e) { }
		});		
		
		lstSelCategories.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selIndex = lstSelCategories.getSelectedIndex();
				if (selIndex > -1) {
					if (e.getClickCount() == 2) { 	// double-click
						removeItemFromListModel(selCategoriesModel, selIndex);
					}					
				}
			}
		});
		
		lstSelCommunities.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selIndex = lstSelCommunities.getSelectedIndex();
				if (selIndex > -1) {
					if (e.getClickCount() == 2) { 	// double-click
						removeItemFromListModel(selCommunitiesModel, selIndex);
					}					
				}
			}
		});		
		
		btnAddKeyword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addItemToListModel(selCategoriesModel, txtCustomKeyword.getText().trim());
				txtCustomKeyword.setText("");	// clear
			}
		});				
	}
	
	protected void addItemToListModel(DefaultListModel model, String item) {
		// check if item is already in the list - custom check to be case-insensitive
		boolean duplicate = false;
		Object[] selList = model.toArray();
		
		for (int i = 0; i < selList.length; i++) {
			if (((String) selList[i]).trim().equalsIgnoreCase(item.trim())) {
				duplicate = true;
				break;
			}
		}
		
		if (!duplicate) {
			model.addElement(item);
		} else {
			InfoBox.showMessage("\"" + item + "\" is already in the list.", "Duplicate item", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	protected void removeItemFromListModel(DefaultListModel model, int index) {
		model.remove(index);
	}
	
	protected boolean isDuplicateReporter(int reporterNum, String reporter) {
		boolean retValue = false;
		
		if (reporter != null && !reporter.trim().isEmpty()) {
			switch (reporterNum) {
				case 1:
					if (reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter2)) ||
						reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter3))) {
							retValue = true;
					}
					break;
				case 2:
					if (reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter1)) ||
						reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter3))) {
							retValue = true;
					}
					break;
				case 3:
					if (reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter1)) ||
						reporter.equalsIgnoreCase(getComboBoxSelectedItem(cmbReporter2))) {
							retValue = true;
					}
					break;
			}
		}	
		
		return retValue;
	}
	
	protected void updateEmail(String reporter, JTextField txtEmail) {
		try {
			txtEmail.setText(config.getMetadataXpathValue(pub, "reporter", "item[.='" + reporter.trim() + "']/@email"));		
		} catch (Exception e) {
			// ignore
		}
	}
	
	protected void setEmbargoComponentsEnabled(boolean enabled) {
		for (Component component : getAllComponents(dtpckEmbargoDate)) {
		    component.setEnabled(enabled);
		}	
		spnrEmbargoTime.setEnabled(enabled);
	}
	
	protected void setComponentValues() 
			throws XPathExpressionException {
		// title label
		String title = "<html><p><b>Story Package Metadata</b>";
		if (objName != null && !objName.isEmpty()) {
			title += "<b> for <font color=\"blue\">" + objName + "</font> (id: " + objId + ")</b>";
		}
		title += "</p></html>";
		lblTitle.setText(title);

		// level label
		String level = "";
		if (objLevel != null && !objLevel.isEmpty()) {
			level = "<html><b>Level: " + objLevel + "</b></html>";
		}
		lblLevel.setText(level);		
		
		lblVersion.setText("<html><p><font color=\"gray\">Version: " + 
				MetadataPanel.class.getPackage().getImplementationVersion() + "</font></p></html>");
		
		// pub - unused
		//config.initComboBox(cmbPub, Constants.ALL, "publication");		
		//setComboBoxSelectedItem(cmbPub, metadata.get("PUB"));
		
		// priority
		config.initComboBox(cmbPriority, Constants.ALL, "priority");
		setComboBoxSelectedItem(cmbPriority, metadata.get("PRIORITY"));

		// story type
		config.initComboBox(cmbStoryType, Constants.ALL, "storyType");
		setComboBoxSelectedItem(cmbStoryType, metadata.get("STORY_TYPE"));
		
		// label
		config.initComboBox(cmbLabel, Constants.ALL, "label");
		setComboBoxSelectedItem(cmbLabel, metadata.get("LABEL"));
		
		// arrival status
		config.initComboBox(cmbArrivalStatus, Constants.ALL, "arrivalStatus");
		setComboBoxSelectedItem(cmbArrivalStatus, metadata.get("ARRIVAL_STATUS"));
				
		// reporter fields
		config.initComboBox(cmbReporter1, pub, "reporter");
		setComboBoxSelectedItem(cmbReporter1, metadata.get("REPORTER1"));

		config.initComboBox(cmbReporter2, pub, "reporter");
		setComboBoxSelectedItem(cmbReporter2, metadata.get("REPORTER2"));

		config.initComboBox(cmbReporter3, pub, "reporter");
		setComboBoxSelectedItem(cmbReporter3, metadata.get("REPORTER3"));

		// desk
		config.initComboBox(cmbDesk, pub, "desk");
		setComboBoxSelectedItem(cmbDesk, metadata.get("DESK"));
		
		String deskXpath;
		String currentDesk = cmbDesk.getSelectedItem().toString().trim();
		if (currentDesk.equalsIgnoreCase(Constants.ALL)) {
			deskXpath = "desk";		// no condition
		} else {
			deskXpath = "desk[@id='" + currentDesk + "']";		// should match desk id
		}		

		// web homepage - list depends on current desk
		config.initComboBox(cmbHomepage, pub, "homepage", deskXpath + "/item");
		String homepage = metadata.get("HOMEPAGE");
		setComboBoxSelectedItem(cmbHomepage, homepage);

		// print section - list depends on current desk
		config.initComboBox(cmbPrintSection, pub, "printSection", deskXpath + "/item");
		String printSection = metadata.get("PRINT_SECTION");
		setComboBoxSelectedItem(cmbPrintSection, printSection);
		
		// print sequence - list depends on current desk and print section
		config.initComboBox(cmbPrintSequence, pub, "printSequence", deskXpath + "/printSection[@id='" + printSection + "']/item");
		String printSequence = metadata.get("SEQUENCE");
		setComboBoxSelectedItem(cmbPrintSequence, printSequence);

		// text fields
		txtEmail1.setText(metadata.get("REPORTER1_EMAIL"));
		txtEmail2.setText(metadata.get("REPORTER2_EMAIL"));
		txtEmail3.setText(metadata.get("REPORTER3_EMAIL"));
		ftfAssignLength.setText(metadata.get("ASSIGN_LEN"));
		ftfPrintPage.setText(metadata.get("PRINT_PAGE"));
		txtContributor.setText(metadata.get("CONTRIBUTOR"));
		txtStoryGroup.setText(metadata.get("STORY_GROUP"));
		txtrDescription.setText(metadata.get("DESCRIPTION"));
		txtPhotoDescription.setText(metadata.get("PHOTO_DESCRIPTION"));
		txtBudgetHead.setText(metadata.get("BUDGET_HEAD"));
		txtrPrintExtra.setText(metadata.get("PRINT_EXTRA"));
		txtrDigitalExtra1.setText(metadata.get("DIGITAL_EXTRA1"));
		txtrDigitalExtra2.setText(metadata.get("DIGITAL_EXTRA2"));

		if (cmbArrivalStatus.getSelectedItem().toString().equalsIgnoreCase(Constants.LIVE)) {
			try {
				boolean hasEmbargoDate = false;
				boolean hasEmbargoTime = false;
				
				// embargo date
				String embargoDate = metadata.get("EMBARGO_DATE");
				if (embargoDate != null && !embargoDate.isEmpty()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DB);
					Date d = (Date) dateFormat.parse(embargoDate);
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					dtpckEmbargoDate.getModel().setYear(cal.get(Calendar.YEAR));
					dtpckEmbargoDate.getModel().setMonth(cal.get(Calendar.MONTH));
					dtpckEmbargoDate.getModel().setDay(cal.get(Calendar.DAY_OF_MONTH));
					hasEmbargoDate = true;
				}				
				
				// embargo time			
				String embargoTime = metadata.get("EMBARGO_TIME");
				if (embargoTime != null && !embargoTime.isEmpty()) {
					SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
					Date d = (Date) timeFormat.parse(embargoTime);
					spnrEmbargoTime.setValue(d);
					hasEmbargoTime = true;
				}
				
				if (hasEmbargoDate && hasEmbargoTime) {
					chkEmbargo.setSelected(true);
				} else {
					chkEmbargo.setSelected(false);
					// disable embargo related components
					setEmbargoComponentsEnabled(false);					
				}
			} catch (Exception e) {
				InfoBox.ShowException(e);
			}
		} else {
			// disable and deselect embargo checkbox
			chkEmbargo.setEnabled(false);
			chkEmbargo.setSelected(false);
			// disable embargo related components
			setEmbargoComponentsEnabled(false);
		}		

		// exclusive checkbox
		chkExclusive.setSelected(metadata.get("EXCLUSIVE_FLAG").equalsIgnoreCase(Constants.TRUE));

		// categories list
		config.initTreeWithGroups(trCategories, pub, "category");
		selCategoriesModel = new DefaultListModel();
		lstSelCategories.setModel(selCategoriesModel);
		setModelListItems(selCategoriesModel, metadata.get("CATEGORIES"));		
		
		// communities list
		config.initTree(trCommunities, pub, "community");	
		selCommunitiesModel = new DefaultListModel();
		lstSelCommunities.setModel(selCommunitiesModel);				
		setModelListItems(selCommunitiesModel, metadata.get("COMMUNITIES"));
	}
	
	protected void setDefaults(String objLevel) 
			throws XPathExpressionException {
		// set default desk
		String defaultDesk = config.getMetadataXpathValue(pub, "defaultValue",
				"level[@id='" + objLevel + "']/desk");
		if (defaultDesk != null && !defaultDesk.isEmpty()) {
			setComboBoxSelectedItem(cmbDesk, defaultDesk);
		}
	}
	
	protected void disableControls() {		
		for (Component component : getAllComponents(this)) {
		    component.setEnabled(false);
		}
		
		String title = "<html><p><b><font color=\"red\">This tab will be enabled once the package is created</font></b></html>";
		lblTitle.setText(title);
		lblTitle.setEnabled(true);
	}
	
	protected Component[] getAllComponents(Component container) {
		ArrayList<Component> list = null;

	    try {
	    	list = new ArrayList<Component>(Arrays.asList(((Container) container).getComponents()));
	        for (int index = 0; index < list.size(); index++) {
	        	for (Component currentComponent : getAllComponents(list.get(index))) {
	        		list.add(currentComponent);
	        	}
	        }
	    } catch (ClassCastException e) {
	    	list = new ArrayList<Component>();
	    }

	    return list.toArray(new Component[list.size()]);
    }
	
	public boolean isReady() {
		/*
		 * put any required validations here
		 */
		boolean isReady = true;
		
		return isReady;
	}
	
	public HashMap<String,String> getMetadataValues() 
			throws XPathExpressionException {
		HashMap<String,String> retMetadata = new HashMap<String,String>();
		retMetadata.clear();
		
		if (!panelDisabled) {
			//retMetadata.put("PUB", getComboBoxSelectedItem(cmbPub)); - unused
			retMetadata.put("DESK", getComboBoxSelectedItem(cmbDesk));
			retMetadata.put("REPORTER1", getComboBoxSelectedItem(cmbReporter1));
			retMetadata.put("REPORTER2", getComboBoxSelectedItem(cmbReporter2));
			retMetadata.put("REPORTER3", getComboBoxSelectedItem(cmbReporter3));
			retMetadata.put("REPORTER1_EMAIL", txtEmail1.getText().trim());
			retMetadata.put("REPORTER2_EMAIL", txtEmail2.getText().trim());
			retMetadata.put("REPORTER3_EMAIL", txtEmail3.getText().trim());
			retMetadata.put("ASSIGN_LEN", ftfAssignLength.getText().trim());
			retMetadata.put("CONTRIBUTOR", txtContributor.getText().trim());
			retMetadata.put("STORY_GROUP", txtStoryGroup.getText().trim());
			retMetadata.put("STORY_TYPE", getComboBoxSelectedItem(cmbStoryType));
			retMetadata.put("LABEL", getComboBoxSelectedItem(cmbLabel));
			retMetadata.put("PRIORITY", getComboBoxSelectedItem(cmbPriority));
			retMetadata.put("DESCRIPTION", txtrDescription.getText().trim());
			retMetadata.put("PHOTO_DESCRIPTION", txtPhotoDescription.getText().trim());
			retMetadata.put("BUDGET_HEAD", txtBudgetHead.getText().trim());
			retMetadata.put("PRINT_EXTRA", txtrPrintExtra.getText().trim());
			retMetadata.put("DIGITAL_EXTRA1", txtrDigitalExtra1.getText().trim());
			retMetadata.put("DIGITAL_EXTRA2", txtrDigitalExtra2.getText().trim());
			retMetadata.put("PRINT_SECTION", getComboBoxSelectedItem(cmbPrintSection));
			retMetadata.put("SEQUENCE", getComboBoxSelectedItem(cmbPrintSequence));
			retMetadata.put("PRINT_PAGE", ftfPrintPage.getText().trim());
			retMetadata.put("HOMEPAGE", getComboBoxSelectedItem(cmbHomepage));
			retMetadata.put("ARRIVAL_STATUS", getComboBoxSelectedItem(cmbArrivalStatus));
			retMetadata.put("EXCLUSIVE_FLAG", chkExclusive.isSelected() ? Constants.TRUE : Constants.FALSE);
			
			String embargoDate = "";
			if (dtpckEmbargoDate.getComponent(0).isEnabled()) {
				Date d = (Date) dtpckEmbargoDate.getModel().getValue();
				SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_DB);
				embargoDate = dateFormat.format(d);
			}
			retMetadata.put("EMBARGO_DATE", embargoDate);
			
			String embargoTime = "";
			if (spnrEmbargoTime.isEnabled()) {
				Date d = (Date) spnrEmbargoTime.getValue();
				SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
				embargoTime = timeFormat.format(d);
			}
			retMetadata.put("EMBARGO_TIME", embargoTime);
			
			retMetadata.put("CATEGORIES", getStringFromListModel(selCategoriesModel));
			retMetadata.put("COMMUNITIES", getStringFromListModel(selCommunitiesModel));
		}
			
		return retMetadata;
	}  	
	
	protected void setComboBoxSelectedItem(JComboBox cmbControl, String item) {
		if (cmbControl.getModel().getSize() > 0) {
			cmbControl.setSelectedItem(item);
		}
	}
	
	protected String getComboBoxSelectedItem(JComboBox cmbControl) {
		if (cmbControl.getSelectedIndex() > -1) {
			return cmbControl.getSelectedItem().toString().trim();
		} else {
			return "";
		}
	}
	
	protected void setModelListItems(DefaultListModel model, String stringList) {
		if (stringList.contains(",")) {
			String[] itemList = stringList.split(",");
			for (int i = 0; i < itemList.length; i++) {
				model.addElement(itemList[i].trim());
			}
		} else if (!stringList.isEmpty()) {
			model.addElement(stringList);
		}
	}
	
	protected String getStringFromListModel(DefaultListModel model) {
		String retStringList = "";
		Object[] list = model.toArray();
		
		for (int i = 0; i < list.length; i++) {
			if (!retStringList.isEmpty()) {
				retStringList += ",";
			}
			retStringList += ((String) list[i]).trim(); 
		}		
	
		return retStringList;
	}
}
