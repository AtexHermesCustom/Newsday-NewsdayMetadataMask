package com.atex.h11.newsday.metadata.sp;

import java.util.HashMap;
import java.util.Properties;

import javax.swing.JPanel;

import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.component.DateLabelFormatter;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.xml.xpath.XPathExpressionException;

public class MetadataPanel extends JPanel {
	private ConfigModel config = null;
	private HashMap<String, String> metadata = null;
	
	private JTextField txtEmail1;
	private JTextField txtEmail2;
	private JTextField txtEmail3;
	private JTextField txtAssignLength;
	private JTextField txtContributor;
	private JTextField txtStoryGroup;
	private JTextField txtCustomKeyword;
	private JComboBox cmbPub;
	private JComboBox cmbReporter1;
	private JComboBox cmbDesk;
	private JComboBox cmbReporter2;
	private JComboBox cmbReporter3;
	private JComboBox cmbPriority;
	private JTextArea txtrDescription;
	private JComboBox cmbStoryType;
	private JComboBox cmbLabel;
	private JTextArea txtrPrintExtra;
	private JTextArea txtrDigitalExtra1;
	private JTextArea txtrDigitalExtra2;
	private JComboBox cmbPrintSection;
	private JComboBox cmbPrintSequence;
	private JComboBox cmbHomepage;
	private JComboBox cmbArrivalStatus;
	private JCheckBox chckbxEmbargo;
	private JDatePickerImpl dtpckEmbargo;
	private JSpinner spnEmbargoTime;
	private JTree trCategories;
	private JTree trCommunities;
	private JTextArea txtrSelCategories;
	private JTextArea txtrSelCommunities;
	
	// constructor
	public MetadataPanel(ConfigModel config, HashMap<String, String> metadata) 
			throws XPathExpressionException {
		
		this.config = config;
		this.metadata = metadata;		
		
		// initialize panel
		InitPanel();
		
		// set component values, read from the metadata hash
		SetComponentValues();
		
		// check and highlight mandatory fields that are missing 
		IsReady();		
	}
	
	void InitPanel() {
		setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(6dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(41dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(57dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(48dlu;default)"),
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(53dlu;default):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("left:max(27dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(9dlu;default)"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("max(13dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(91dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(51dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblTitle = new JLabel("Story Package Metadata for <PACKAGE NAME>");
		add(lblTitle, "4, 2, 7, 1");
		
		JLabel lblPublication = new JLabel("Pub");
		add(lblPublication, "22, 2, right, default");
		
		cmbPub = new JComboBox();
		add(cmbPub, "24, 2, 9, 1, fill, default");
		
		JLabel lblReporter = new JLabel("Reporter");
		add(lblReporter, "4, 4, right, default");
		
		cmbReporter1 = new JComboBox();
		add(cmbReporter1, "6, 4, 5, 1, fill, default");
		
		txtEmail1 = new JTextField();
		add(txtEmail1, "12, 4, 9, 1, fill, default");
		txtEmail1.setColumns(10);
		
		JLabel lblDesk = new JLabel("Desk");
		add(lblDesk, "22, 4, right, default");
		
		cmbDesk = new JComboBox();
		add(cmbDesk, "24, 4, 9, 1, fill, default");
		
		JLabel lblndReporter = new JLabel("2nd Reporter");
		add(lblndReporter, "4, 6, right, default");
		
		cmbReporter2 = new JComboBox();
		add(cmbReporter2, "6, 6, 5, 1, fill, default");
		
		txtEmail2 = new JTextField();
		txtEmail2.setColumns(10);
		add(txtEmail2, "12, 6, 9, 1, fill, default");
		
		JLabel lblAssignedLength = new JLabel("Assigned Length");
		add(lblAssignedLength, "22, 6, 5, 1");
		
		JLabel lblPriority = new JLabel("Priority");
		add(lblPriority, "28, 6, 5, 1");
		
		JLabel lblrdReporter = new JLabel("3rd Reporter");
		add(lblrdReporter, "4, 8, right, default");
		
		cmbReporter3 = new JComboBox();
		add(cmbReporter3, "6, 8, 5, 1, fill, default");
		
		txtEmail3 = new JTextField();
		txtEmail3.setColumns(10);
		add(txtEmail3, "12, 8, 9, 1, fill, default");
		
		txtAssignLength = new JTextField();
		txtAssignLength.setColumns(10);
		add(txtAssignLength, "22, 8, 3, 1");
		
		cmbPriority = new JComboBox();
		add(cmbPriority, "28, 8, 5, 1, fill, default");
		
		JLabel lblContributor = new JLabel("Contributor");
		add(lblContributor, "4, 10, right, default");
		
		txtContributor = new JTextField();
		txtContributor.setColumns(10);
		add(txtContributor, "6, 10, 5, 1, fill, default");
		
		JLabel lblDescription = new JLabel("Description");
		add(lblDescription, "12, 10");
		
		JLabel lblStoryGroup = new JLabel("Story Group");
		add(lblStoryGroup, "4, 12, right, default");
		
		txtStoryGroup = new JTextField();
		txtStoryGroup.setColumns(10);
		add(txtStoryGroup, "6, 12, 5, 1, fill, default");
		
		txtrDescription = new JTextArea();
		add(txtrDescription, "12, 12, 21, 3, fill, fill");
		
		JLabel lblStoryType = new JLabel("Story Type");
		add(lblStoryType, "4, 14, right, default");
		
		cmbStoryType = new JComboBox();
		add(cmbStoryType, "6, 14, 5, 1, fill, default");
		
		JLabel lblLabel = new JLabel("Label");
		add(lblLabel, "4, 16, right, default");
		
		cmbLabel = new JComboBox();
		add(cmbLabel, "6, 16, 5, 1, fill, default");
		
		JLabel lblDigitalExtras = new JLabel("Digital Extras");
		add(lblDigitalExtras, "12, 16, right, default");
		
		txtrDigitalExtra1 = new JTextArea();
		add(txtrDigitalExtra1, "14, 16, 19, 3, fill, fill");
		
		JLabel lblPrintExtra = new JLabel("Print Extra");
		add(lblPrintExtra, "4, 18, right, default");
		
		txtrPrintExtra = new JTextArea();
		add(txtrPrintExtra, "6, 18, 5, 5, fill, fill");
		
		txtrDigitalExtra2 = new JTextArea();
		add(txtrDigitalExtra2, "14, 20, 19, 3, fill, fill");
		
		JLabel lblPrint = new JLabel("PRINT");
		add(lblPrint, "4, 24");
		
		JLabel lblSection = new JLabel("Section");
		add(lblSection, "6, 24, right, default");
		
		cmbPrintSection = new JComboBox();
		add(cmbPrintSection, "8, 24, 3, 1, fill, default");
		
		JLabel lblSequenceOr = new JLabel("Sequence or page");
		add(lblSequenceOr, "12, 24, right, default");
		
		cmbPrintSequence = new JComboBox();
		add(cmbPrintSequence, "14, 24, 13, 1, fill, default");
		
		JLabel lblWeb = new JLabel("WEB");
		add(lblWeb, "4, 26");
		
		JLabel lblHomePage = new JLabel("Home page");
		add(lblHomePage, "6, 26, right, default");
		
		cmbHomepage = new JComboBox();
		add(cmbHomepage, "8, 26, 3, 1, fill, default");
		
		JLabel lblArrivalStatus = new JLabel("Arrival Status");
		add(lblArrivalStatus, "12, 26, right, default");
		
		cmbArrivalStatus = new JComboBox();
		add(cmbArrivalStatus, "14, 26, 13, 1, fill, default");
		
		chckbxEmbargo = new JCheckBox("Embargo");
		add(chckbxEmbargo, "6, 28");
		
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
		add(dtpckEmbargo, "8, 28, 3, 1");		
		
		spnEmbargoTime = new JSpinner();
		add(spnEmbargoTime, "12, 28");
		
		JLabel lblCategories = new JLabel("Categories");
		add(lblCategories, "4, 30");
		
		trCategories = new JTree();
		add(trCategories, "6, 30, 5, 1, fill, fill");
		
		JLabel lblCommunities = new JLabel("Communities");
		add(lblCommunities, "12, 30");
		
		trCommunities = new JTree();
		add(trCommunities, "14, 30, 19, 1, fill, fill");
		
		JLabel lblSelectedCategories = new JLabel("<html><p>Selected</p><p>Categories</p></html>");
		lblSelectedCategories.setVerticalAlignment(SwingConstants.TOP);
		add(lblSelectedCategories, "4, 32");
		
		txtrSelCategories = new JTextArea();
		add(txtrSelCategories, "6, 32, 5, 1, fill, fill");
		
		JLabel lblselectedcommunities = new JLabel("<html><p>Selected</p><p>Communities</p></html>");
		lblselectedcommunities.setVerticalAlignment(SwingConstants.TOP);
		add(lblselectedcommunities, "12, 32");
		
		txtrSelCommunities = new JTextArea();
		add(txtrSelCommunities, "14, 32, 19, 1, fill, fill");
		
		JLabel lblcustomkeyword = new JLabel("<html><p>Custom</p><p>Keyword</p></html>");
		lblcustomkeyword.setVerticalAlignment(SwingConstants.TOP);
		add(lblcustomkeyword, "4, 34");
		
		txtCustomKeyword = new JTextField();
		txtCustomKeyword.setColumns(10);
		add(txtCustomKeyword, "6, 34, 3, 1, fill, default");
		
		JButton btnAddKeyword = new JButton("Add Keyword");
		add(btnAddKeyword, "10, 34");		
	}
	
	private void SetComponentValues() 
			throws XPathExpressionException {
		
	}
	
	public boolean IsReady() {
		/*
		 * put any required validations here
		 */
		boolean isReady = true;
		
		return isReady;
	}
	
	public HashMap<String,String> GetMetadataValues() 
			throws XPathExpressionException {
		HashMap<String,String> retMetadata = new HashMap<String,String>();
		
		return retMetadata;
	}
		
}
