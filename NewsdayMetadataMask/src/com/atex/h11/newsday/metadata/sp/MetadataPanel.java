package com.atex.h11.newsday.metadata.sp;

import java.util.Calendar;
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
import com.atex.h11.newsday.metadata.common.DateLabelFormatter;
import com.atex.h11.newsday.metadata.common.NumericDocumentFilter;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

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
import javax.swing.text.AbstractDocument;

public class MetadataPanel extends JPanel {
	private static Logger logger;
	
	private String objName;
	private String objLevel;
	private ConfigModel config = null;
	private HashMap<String, String> metadata = null;
	
	private JLabel lblTitle;
	private JTextField txtEmail1;
	private JTextField txtEmail2;
	private JTextField txtEmail3;
	private JTextField txtContributor;
	private JTextField txtStoryGroup;
	private JTextField txtCustomKeyword;
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
	private JCheckBox chckbxEmbargo;
	private JDatePickerImpl dtpckEmbargo;
	private JSpinner spnEmbargoTime;
	private JLabel lblPage;
	private JCheckBox chckbxExclusive;
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
	private JFormattedTextField ftxtAssignLength;
	private JFormattedTextField ftxtPrintPage;
	
	// constructor
	public MetadataPanel(ConfigModel config, HashMap<String, String> metadata, Logger l, String objName, String objLevel) 
			throws XPathExpressionException {
		
		this.objName = objName;
		this.objLevel = objLevel;
		this.config = config;
		this.metadata = metadata;		
		logger = l;
		
		// initialize panel
		initPanel();
		
		// set component values, read from the metadata hash
		setComponentValues();
		
		// check and highlight mandatory fields that are missing 
		isReady();		
	}

	protected void initPanel() 
			throws XPathExpressionException {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:default"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(41dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(50dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(60dlu;default)"),
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
		String title = "<html><p><b>Story Package Metadata</b>";
		if (objName != null && !objName.isEmpty()) {
			title += "<b> for <font color=\"red\">" + objName + "</font></b>";
		}
		title += "</p></html>";
		lblTitle.setText(title);
		add(lblTitle, "2, 2, 7, 1");
		
		JLabel lblReporter = new JLabel("Reporter");
		add(lblReporter, "2, 4, right, default");
		
		cmbReporter1 = config.initComboBox("reporter");
		add(cmbReporter1, "4, 4, 5, 1, fill, default");
		
		txtEmail1 = new JTextField();
		add(txtEmail1, "10, 4, 3, 1, fill, default");
		txtEmail1.setColumns(10);
		
		label = new JLabel("Desk");
		add(label, "14, 4, right, default");
		
		cmbDesk = config.initComboBox("desk", "item[@pub='ND']");
		add(cmbDesk, "16, 4, 3, 1, fill, default");
		
		JLabel lblndReporter = new JLabel("2nd Reporter");
		add(lblndReporter, "2, 6, right, default");
		
		cmbReporter2 = config.initComboBox("reporter", "item[@pub='ND']");
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
		
		cmbReporter3 = config.initComboBox("reporter", "item[@pub='ND']");
		add(cmbReporter3, "4, 8, 5, 1, fill, default");
		
		txtEmail3 = new JTextField();
		txtEmail3.setColumns(10);
		add(txtEmail3, "10, 8, 3, 1, fill, default");
		
		ftxtAssignLength = new JFormattedTextField();
		((AbstractDocument) ftxtAssignLength.getDocument()).setDocumentFilter(
				new NumericDocumentFilter(ftxtAssignLength.getDocument(), false, false));
		add(ftxtAssignLength, "14, 8, fill, default");
		
		cmbPriority = config.initComboBox("priority");
		add(cmbPriority, "16, 8, 3, 1, fill, default");
		
		JLabel lblContributor = new JLabel("Contributor");
		add(lblContributor, "2, 10, right, default");
		
		txtContributor = new JTextField();
		txtContributor.setColumns(10);
		add(txtContributor, "4, 10, 5, 1, fill, default");
		
		JLabel lblDescription = new JLabel("Description");
		add(lblDescription, "10, 10, right, default");
		
		JLabel lblStoryGroup = new JLabel("Story Group");
		add(lblStoryGroup, "2, 12, right, default");
		
		txtStoryGroup = new JTextField();
		txtStoryGroup.setColumns(10);
		add(txtStoryGroup, "4, 12, 5, 1, fill, default");
		
		scrlDescription = new JScrollPane();
		scrlDescription.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDescription, "12, 10, 7, 3, fill, fill");
		
		txtrDescription = new JTextArea();
		txtrDescription.setLineWrap(true);
		scrlDescription.setViewportView(txtrDescription);
		
		JLabel lblStoryType = new JLabel("Story Type");
		add(lblStoryType, "2, 14, right, default");
		
		cmbStoryType = config.initComboBox("storyType");
		add(cmbStoryType, "4, 14, 3, 1, fill, default");
		
		JLabel lblDigitalExtras = new JLabel("Digital Extras");
		add(lblDigitalExtras, "10, 14, right, default");
		
		JLabel lblLabel = new JLabel("Label");
		add(lblLabel, "2, 16, right, default");
		
		cmbLabel = config.initComboBox("label");
		add(cmbLabel, "4, 16, 3, 1, fill, default");
		
		scrlDigitalExtra1 = new JScrollPane();
		scrlDigitalExtra1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDigitalExtra1, "12, 14, 7, 3, fill, fill");
		
		txtrDigitalExtra1 = new JTextArea();
		txtrDigitalExtra1.setLineWrap(true);
		scrlDigitalExtra1.setViewportView(txtrDigitalExtra1);
		
		JLabel lblPrintExtra = new JLabel("Print Extra");
		add(lblPrintExtra, "2, 18, right, default");
		
		scrlPrintExtra = new JScrollPane();
		scrlPrintExtra.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlPrintExtra, "4, 18, 5, 3, fill, fill");
		
		txtrPrintExtra = new JTextArea();
		txtrPrintExtra.setLineWrap(true);
		scrlPrintExtra.setViewportView(txtrPrintExtra);
		
		scrlDigitalExtra2 = new JScrollPane();
		scrlDigitalExtra2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrlDigitalExtra2, "12, 18, 7, 3, fill, fill");
		
		txtrDigitalExtra2 = new JTextArea();
		txtrDigitalExtra2.setLineWrap(true);
		scrlDigitalExtra2.setViewportView(txtrDigitalExtra2);
		
		JLabel lblPrint = new JLabel("PRINT");
		add(lblPrint, "2, 22, right, default");
		
		JLabel lblSection = new JLabel("Section");
		add(lblSection, "4, 22, right, default");
		
		cmbPrintSection = config.initComboBox("printSection");
		add(cmbPrintSection, "6, 22, 3, 1, fill, default");
		
		JLabel lblSequenceOr = new JLabel("Sequence");
		add(lblSequenceOr, "10, 22, right, default");
		
		cmbPrintSequence = config.initComboBox("printSequence");
		add(cmbPrintSequence, "12, 22, 3, 1, fill, default");
		
		lblPage = new JLabel("Page");
		add(lblPage, "16, 22, right, default");
		
		ftxtPrintPage = new JFormattedTextField();
		((AbstractDocument) ftxtPrintPage.getDocument()).setDocumentFilter(
				new NumericDocumentFilter(ftxtPrintPage.getDocument(), false, false));		
		//((AbstractDocument) ftxtPrintPage.getDocument()).setDocumentFilter(
		//		new AlphaNumericDocumentFilter());		
		add(ftxtPrintPage, "18, 22, fill, default");
		
		JLabel lblWeb = new JLabel("WEB");
		add(lblWeb, "2, 24, right, default");
		
		JLabel lblHomePage = new JLabel("Home page");
		add(lblHomePage, "4, 24, right, default");
		
		cmbHomepage = config.initComboBox("homepage");
		add(cmbHomepage, "6, 24, 3, 1, fill, default");
		
		JLabel lblArrivalStatus = new JLabel("Arrival Status");
		add(lblArrivalStatus, "10, 24, right, default");
		
		cmbArrivalStatus = config.initComboBox("arrivalStatus");
		add(cmbArrivalStatus, "12, 24, 3, 1, fill, default");
		
		chckbxEmbargo = new JCheckBox("Embargo");
		add(chckbxEmbargo, "4, 26");
		
		// date picker
		UtilDateModel dateModel = new UtilDateModel();
		dateModel.setDate(1990, 8, 24);
		dateModel.setSelected(true);
		
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");		
		
		JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
		
		dtpckEmbargo = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		add(dtpckEmbargo, "6, 26, 3, 1");		
		
		SpinnerDateModel timeModel = new SpinnerDateModel();
		timeModel.setCalendarField(Calendar.MINUTE);
		spnEmbargoTime = new JSpinner();
		spnEmbargoTime.setModel(timeModel);
		spnEmbargoTime.setEditor(new JSpinner.DateEditor(spnEmbargoTime, "h:mm a"));		
		add(spnEmbargoTime, "10, 26, fill, default");
		
		JLabel lblCategories = new JLabel("Categories");
		add(lblCategories, "2, 28, right, top");
		
		scrlCategories = new JScrollPane();
		add(scrlCategories, "4, 28, 5, 1, fill, fill");
		
		trCategories = config.initTreeWithGroups("category");
		trCategories.setToolTipText("Double click an item to add it to the Selected list");
		scrlCategories.setViewportView(trCategories);
		
		JLabel lblCommunities = new JLabel("Communities");
		add(lblCommunities, "10, 28, right, top");
		
		scrlCommunities = new JScrollPane();
		add(scrlCommunities, "12, 28, 7, 1, fill, fill");
		
		trCommunities = config.initTree("community");
		trCommunities.setToolTipText("Double click an item to add it to the Selected list");
		scrlCommunities.setViewportView(trCommunities);
		
		JLabel lblSelectedCategories = new JLabel("<html><p>Selected</p><p>Categories</p></html>");
		lblSelectedCategories.setVerticalAlignment(SwingConstants.TOP);
		add(lblSelectedCategories, "2, 30, right, top");
		
		scrlSelCategories = new JScrollPane();
		add(scrlSelCategories, "4, 30, 5, 1, fill, fill");
		
		lstSelCategories = new JList<String>();
		lstSelCategories.setToolTipText("Double click an item to remove it from the list");
		scrlSelCategories.setViewportView(lstSelCategories);
		
		JLabel lblselectedcommunities = new JLabel("<html><p>Selected</p><p>Communities</p></html>");
		lblselectedcommunities.setVerticalAlignment(SwingConstants.TOP);
		add(lblselectedcommunities, "10, 30, right, top");
		
		scrlSelCommunities = new JScrollPane();
		add(scrlSelCommunities, "12, 30, 7, 1, fill, fill");
		
		lstSelCommunities = new JList<String>();
		lstSelCommunities.setToolTipText("Double click an item to remove it from the list");
		scrlSelCommunities.setViewportView(lstSelCommunities);
		
		JLabel lblcustomkeyword = new JLabel("<html><p>Custom</p><p>Keyword</p></html>");
		lblcustomkeyword.setVerticalAlignment(SwingConstants.TOP);
		add(lblcustomkeyword, "2, 32, right, top");
		
		txtCustomKeyword = new JTextField();
		txtCustomKeyword.setColumns(10);
		add(txtCustomKeyword, "4, 32, 3, 1, fill, default");
		
		JButton btnAddKeyword = new JButton("Add Keyword");
		add(btnAddKeyword, "8, 32");		
		
		chckbxExclusive = new JCheckBox("Exclusive");
		add(chckbxExclusive, "12, 32, 2, 1");
	}
	
	protected void setComponentValues() 
			throws XPathExpressionException {

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
		
		retMetadata.put("EXCLUSIVE_FLAG", Constants.TRUE);
		retMetadata.put("EMBARGO_FLAG", Constants.TRUE);
		retMetadata.put("REPORTER1", "Report1");
		
		return retMetadata;
	}  	
}
