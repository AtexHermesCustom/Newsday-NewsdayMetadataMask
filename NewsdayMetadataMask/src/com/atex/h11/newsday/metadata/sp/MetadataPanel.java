package com.atex.h11.newsday.metadata.sp;

import java.util.HashMap;
import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.xml.xpath.XPathExpressionException;

import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.component.CheckBoxList;

public class MetadataPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ConfigModel config = null;
	private HashMap<String, String> metadata = null;
	private JTextField jTextKeywords = null;
	private JRadioButton jRadPositiveSentiment = null;
	private JRadioButton jRadNegativeSentiment = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel11 = null;
	private JLabel jLabel111 = null;
	private CheckBoxList cbListSectors = null;
	private CheckBoxList cbListIndustries = null;
	private JScrollPane jScrollSectors = null;
	private JScrollPane jScrollIndustries = null;
	private JLabel jLabel1111 = null;
	private JRadioButton jRadNeutralSentiment = null;
	private JLabel jLabelSectorsMandate = null;
	private JLabel jLabelIndustriesMandate = null;	
	/**
	 * This method initializes jScrollSectors	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollSectors() 
			throws XPathExpressionException {
		if (jScrollSectors == null) {
			// initialize
			DefaultListModel<JCheckBox> listModel = config.InitCheckBoxListModel("sector");	
			cbListSectors = new CheckBoxList();
			cbListSectors.setModel(listModel);
			cbListSectors.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(cbListSectors.getSelectedListString().trim() == ""){
						jLabelSectorsMandate.setVisible(true);
					}
					else {
						jLabelSectorsMandate.setVisible(false);
					}
				}				
			});
			jScrollSectors = new JScrollPane(cbListSectors);
			jScrollSectors.setBounds(new Rectangle(17, 34, 300, 289));
			jScrollSectors.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jScrollSectors;
	}

	/**
	 * This method initializes jScrollIndustries	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollIndustries() 
			throws XPathExpressionException {
		if (jScrollIndustries == null) {
			// initialize
			DefaultListModel<JCheckBox> listModel = config.InitCheckBoxListModel("industry");
			cbListIndustries = new CheckBoxList();
			cbListIndustries.setModel(listModel);
			cbListIndustries.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(cbListIndustries.getSelectedListString().trim() == ""){
						jLabelIndustriesMandate.setVisible(true);
					}
					else {
						jLabelIndustriesMandate.setVisible(false);
					}
				}				
			});			
			jScrollIndustries = new JScrollPane(cbListIndustries);
			jScrollIndustries.setBounds(new Rectangle(332, 33, 300, 291));
			jScrollIndustries.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jScrollIndustries;
	}		
	
	/**
	 * This method initializes jTextKeywords	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextKeywords() {
		if (jTextKeywords == null) {
			jTextKeywords = new JTextField();
			jTextKeywords.setBounds(new Rectangle(14, 422, 614, 23));
			jTextKeywords.setVisible(false);	// invisible -- not used here??
		}
		return jTextKeywords;
	}
	
	/**
	 * This method initializes jRadNeutralSentiment	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadNeutralSentiment() {
		if (jRadNeutralSentiment == null) {
			jRadNeutralSentiment = new JRadioButton();
			jRadNeutralSentiment.setBounds(new Rectangle(15, 397, 68, 24));
			jRadNeutralSentiment.setText("Neutral");
			jRadNeutralSentiment.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jRadNeutralSentiment;
	}	

	/**
	 * This method initializes jRadPositiveSentiment	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadPositiveSentiment() {
		if (jRadPositiveSentiment == null) {
			jRadPositiveSentiment = new JRadioButton();
			jRadPositiveSentiment.setBounds(new Rectangle(114, 397, 89, 24));
			jRadPositiveSentiment.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadPositiveSentiment.setText("Positive");
		}
		return jRadPositiveSentiment;
	}

	/**
	 * This method initializes jRadNegativeSentiment	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadNegativeSentiment() {
		if (jRadNegativeSentiment == null) {
			jRadNegativeSentiment = new JRadioButton();
			jRadNegativeSentiment.setBounds(new Rectangle(216, 397, 96, 24));
			jRadNegativeSentiment.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadNegativeSentiment.setText("Negative");
		}
		return jRadNegativeSentiment;
	}

	/**
	 * This is the default constructor
	 */
	public MetadataPanel(ConfigModel config, HashMap<String, String> metadata) 
			throws XPathExpressionException {
		super();
		
		this.config = config;
		this.metadata = metadata;
		
		initialize();
		
		// set component values, read from the metadata hash
		SetComponentValues();
		
		// check and highlight mandatory fields that are missing 
		isReady();
	}	
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() 
			throws XPathExpressionException {
		this.setLayout(null);
		this.setSize(720, 470);

		jLabelSectorsMandate = new JLabel();
		jLabelSectorsMandate.setBounds(new Rectangle(17, 327, 281, 16));
		jLabelSectorsMandate.setForeground(Color.red);
		jLabelSectorsMandate.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabelSectorsMandate.setText("<html>Sub-editors or Content producers, please fill this in.</html>");
		jLabelSectorsMandate.setVerticalAlignment(SwingConstants.TOP);
		jLabelSectorsMandate.setVerticalTextPosition(SwingConstants.TOP);
		jLabelSectorsMandate.setVisible(true);		
		jLabelIndustriesMandate = new JLabel();
		jLabelIndustriesMandate.setBounds(new Rectangle(332, 327, 281, 16));
		jLabelIndustriesMandate.setForeground(Color.red);
		jLabelIndustriesMandate.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabelIndustriesMandate.setText("<html>Sub-editors or Content producers, please fill this in.</html>");
		jLabelIndustriesMandate.setVerticalAlignment(SwingConstants.TOP);
		jLabelIndustriesMandate.setVerticalTextPosition(SwingConstants.TOP);
		jLabelIndustriesMandate.setVisible(true);
		jLabel1111 = new JLabel();
		jLabel1111.setBounds(new Rectangle(76, 449, 227, 16));
		jLabel1111.setText("- separate keywords using commas");
		jLabel1111.setFont(new Font("Dialog", Font.ITALIC, 11));		
		jLabel1111.setVisible(false);
		jLabel111 = new JLabel();
		jLabel111.setBounds(new Rectangle(14, 449, 60, 16));
		jLabel111.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabel111.setText("Keywords");
		jLabel111.setVisible(false);
		jLabel11 = new JLabel();
		jLabel11.setBounds(new Rectangle(14, 376, 99, 16));
		jLabel11.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabel11.setText("Sentiment");
		jLabel1 = new JLabel();
		jLabel1.setBounds(new Rectangle(332, 8, 63, 16));
		jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabel1.setText("Industry");
		jLabel = new JLabel();
		jLabel.setBounds(new Rectangle(17, 10, 48, 15));
		jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		jLabel.setText("Sector");
		this.add(jLabel, null);
		this.add(jLabel1, null);
		this.add(jLabel11, null);
		this.add(jLabel111, null);
		this.add(jLabel1111, null);
		this.add(jLabelIndustriesMandate, null);
		this.add(jLabelSectorsMandate, null);

		this.add(getJTextKeywords(), null);
		
        //Group the radio buttons.
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(getJRadNeutralSentiment());				
        radioButtonGroup.add(getJRadPositiveSentiment());
        radioButtonGroup.add(getJRadNegativeSentiment());				
		this.add(getJRadPositiveSentiment(), null);
		this.add(getJRadNegativeSentiment(), null);
		this.add(getJRadNeutralSentiment(), null);
		// check "Positive" initially
		getJRadNeutralSentiment().setSelected(true);
		
		// lists
		this.add(getJScrollSectors(), null);
		this.add(getJScrollIndustries(), null);
	}
	
	private void SetComponentValues() 
			throws XPathExpressionException {
		cbListSectors.setSelectedFromListString(metadata.get(config.GetMetadataName("sectors")));
		cbListIndustries.setSelectedFromListString(metadata.get(config.GetMetadataName("industries")));
		jTextKeywords.setText(metadata.get(config.GetMetadataName("keywords")));
		jRadNeutralSentiment.setSelected(metadata.get(config.GetMetadataName("sentiment")).equalsIgnoreCase("Neutral"));
		jRadPositiveSentiment.setSelected(metadata.get(config.GetMetadataName("sentiment")).equalsIgnoreCase("Positive"));
		jRadNegativeSentiment.setSelected(metadata.get(config.GetMetadataName("sentiment")).equalsIgnoreCase("Negative"));
	}
	
	public boolean isReady() {
		/*
		 * put any required validations here
		 */
		boolean isReady = true;
		
		// init
		jLabelSectorsMandate.setVisible(false);
		jLabelIndustriesMandate.setVisible(false);
		
		if (cbListSectors.getSelectedListString().trim() == "") {
			jLabelSectorsMandate.setVisible(true);
//			isReady = false;
		}
		
		if (cbListIndustries.getSelectedListString().trim() == "") {
			jLabelIndustriesMandate.setVisible(true);
//			isReady = false;
		}

		return isReady;
	}	
	
	public HashMap<String,String> GetMetadataValues() 
			throws XPathExpressionException {
		HashMap<String,String> retMetadata = new HashMap<String,String>();
		
	    retMetadata.put(config.GetMetadataName("sectors"), cbListSectors.getSelectedListString());
	    retMetadata.put(config.GetMetadataName("industries"), cbListIndustries.getSelectedListString());
	    
	    // normalize keywords list
	    String keywordList = "";
	    int i = 0;
	    for (String s : jTextKeywords.getText().split(",")) {
	    	s = s.trim();			// trim any leading & trailing spaces
	    	if (! s.isEmpty()) {
	    		if (i > 0) { keywordList += ","; }
	    		keywordList += s;
	    		i++;
	    	}
	    }	    
	    retMetadata.put(config.GetMetadataName("keywords"), keywordList);
		
	    retMetadata.put(config.GetMetadataName("sentiment"), 
		    	getJRadNeutralSentiment().isSelected() ? "Netural" : (getJRadPositiveSentiment().isSelected() ? "Positive" : "Negative"));

	    //	    retMetadata.put(config.GetMetadataName("sentiment"), 
//	    	getJRadPositiveSentiment().isSelected() ? "Positive" : "Negative");
	    
		// combo boxes
		return retMetadata;
	}



	
}  //  @jve:decl-index=0:visual-constraint="10,10"
