package com.atex.h11.newsday.metadata.sp;

import com.unisys.media.commonservices.common.location.LocationInfo;
import com.unisys.media.commonservices.dialogs.metadata.custom.NodeValueInspector;
import com.unisys.media.commonservices.dialogs.metadata.view.ICustomMetadataPanel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Dimension;
import java.awt.Window;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.common.Constants;
import com.atex.h11.newsday.metadata.common.InfoBox;

public class CustomMetadataPanel extends JPanel implements ICustomMetadataPanel {
	private ResourceBundle bundle;
	private static final long serialVersionUID = 1L;
	
	private NodeValueInspector inspector;
	private HashMap<String, String> metadata;
	private LocationInfo locationInfo;
	private Locale locale;
	private Window parent;
	
	private MetadataPanel metadataPanel = null;
	
	private ConfigModel config = null;
	
	private static final Logger logger = Constants.LOGGER;
	private static FileHandler fileLog;
    private static SimpleFormatter simpleFormatter;
	
	/**
	 * This is the default constructor
	 */
	public CustomMetadataPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		try {
		    // Configuration file
		    config = new ConfigModel("sp");	// pass the metadata group
			
			Level logLevel = Level.parse(config.getConfigValue("logLevel"));	    
		    
			// log file
			String logFile = config.getConfigValue("logFile");			
			if (logFile == null || logFile.isEmpty()) {
				logFile = Constants.LOGFILE_NAME;	// default log file
			}
			
			if (logLevel != Level.OFF) {
				fileLog = new FileHandler(logFile);		
			    
				// Create Formatter
			    simpleFormatter = new SimpleFormatter();
			    fileLog.setFormatter(simpleFormatter);
			    logger.addHandler(fileLog);				
			}
		    
		    // log level
			logger.setLevel(Level.parse(config.getConfigValue("logLevel")));	
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error encountered", e);
			InfoBox.ShowException(e);
		}	
	}
	
	/**
	 * Main entry point for the plugin<br/>
	 * This method is called by the plug-in to initialise the panel. <br/>
	 * @param inspector
	 * 		provides information on the selected H11 object
	 * @param metadata
	 * 		Pass the current set of metadata values. Hash indexed by the metadata field.
	 * @param readonly
	 * 		Set to true by the plugin if changes are not allowed. Based
	 * 		on permissions set in the current calling context
	 */	
	@Override
	public JPanel getPanel(NodeValueInspector inspector, HashMap<String, String> metadata, boolean readonly)
			throws Exception {
		try {
			logger.entering(this.getClass().getSimpleName(), "getPanel");
			
			this.inspector = inspector;
			this.metadata = metadata;

			// For debugging
			/*
			if (inspector == null)
				this.validator = new TestValidator();
			else
				this.validator = new HermesValidator(inspector);
			*/
						
			String objId = "";
			String objName = ""; 		
			String objLevel = "";
			String objLevelId = "";
			String pub = "";
			
			// for testing only
			String testMode = System.getProperty("metadata.test");
			if (testMode != null && testMode.equals("1")) {
				System.out.println("metadata test mode");
				objId = "99999:00";
				objId = objId.replaceAll(":.*$", "");
				objName = "TESTPACKAGE";
				objLevel = "ND-WRITERS/NEWS";
				pub = getPubFromLevel(objLevel);
			}

			if (inspector != null) {
				objId = inspector.getID();
				if (objId != null && !objId.isEmpty()) {
					objId = objId.replaceAll(":.*$", "");
				}
				objName = inspector.getProperty(NodeValueInspector.NAME);
				objLevel = inspector.getProperty(NodeValueInspector.LEVEL_PATH);
				objLevelId = inspector.getProperty(NodeValueInspector.LEVEL_ID);
				pub = getPubFromLevel(objLevel);
				logger.fine("Object: name=" + objName + ", id=" + objId + ", level=" + objLevel + ", level id=" + objLevelId + ", pub=" + pub);
			}
			else {
				logger.log(Level.WARNING, "inspector is null");
			}
			
			logMetadata(metadata, "Loaded from DB");

			metadataPanel = new MetadataPanel(config, metadata, logger, objId, objName, objLevel, pub);
			metadataPanel.setPreferredSize(new Dimension(690, 580));
			
			logger.exiting(this.getClass().getSimpleName(), "getPanel");
			return metadataPanel;			
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error encountered", e);
			InfoBox.ShowException(e);
			throw e;
		}
	}	
	
	/**
	 * This method is called from MediaDesktop to get the from the user entered
	 * metadata from this pane.
	 * 
	 * @return HashMap containing the new metadata values
	 */	
	@Override
	public HashMap<String, String> getMetadataValues() {
		try {
			logger.entering(this.getClass().getSimpleName(), "getMetadataValues");
			
			HashMap<String, String> retMetadata = null;
			
			retMetadata = metadataPanel.getMetadataValues();
			logMetadata(retMetadata, "Save to DB");
			
			logger.exiting(this.getClass().getSimpleName(), "getMetadataValues");
			return retMetadata;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error encountered", e);
			InfoBox.ShowException(e);
			return null;
		}
	}
	
	/**
	 * Checks, if values are valid.
	 */
	@Override
	public boolean canActionBePerformed(int button) {
		// Only OK-Button has to be checked.
		boolean retVal;
		
		switch (button) {
			case ICustomMetadataPanel.OK_BUTTON:
				retVal = isReady();
				break;
			default:
				retVal = true;
				break;
		}
		logger.finer("Return value=" + retVal);
		return retVal;
	}	
	
	@Override
	public String getTitle() {
		// Just return the configured string
		String title = "";
		String objName = "";
		
		if (inspector != null) {
			objName = inspector.getProperty(NodeValueInspector.NAME);
		}

		if (objName != null && !objName.equals("")) {
			title = "Story Package Metadata for " + objName;
		} else {
			title = "Object not passed";
		}

		//return bundle.getString("title") + objectName;
		logger.finer("Title value=" + title);
		return title;
	}	
	
	@Override
	public void setCurrentLocale(Locale locale) {
		this.locale = locale;
		logger.finer("Locale Info: name=" + locale.getDisplayName());
	}
	
	@Override
	public void setLocationValue(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
		logger.finer("Location Info: name=" + locationInfo.getLocationName() + ", user=" + locationInfo.getUsername());
	}
	
	@Override
	public void setParent(Window parent) {
		this.parent = parent;
		logger.finer("Window parent info: name=" + parent.getName());
	}	
		
	private boolean isReady() {
		boolean retVal = metadataPanel.isReady();
		logger.finer("Return value=" + retVal);
		return retVal;
	}	
	
	private String getPubFromLevel(String objLevel) 
			throws XPathExpressionException {
		String pub = null;
		
		NodeList nl = config.getListItems(Constants.ALL, "publicationConfiguration");
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			String pattern = n.getAttributes().getNamedItem("levelMatchPattern").getNodeValue();
			if (objLevel.matches(pattern)) {
				logger.finer("Level: " + objLevel + " matches pattern: " + pattern);
				pub = n.getTextContent().trim();
				break;
			}
		}
		
		logger.finer("Return value=" + pub);
		return pub;
	}
	
	private void logMetadata(HashMap<String, String> map, String msg) {
		// sort map by key
		Map<String, String> treeMap = new TreeMap<String, String>(map);
		
		// log
		logger.finer("logMetadata - " + msg + ":");
		logger.finer("<key>=<value>");
		logger.finer("----------");
		String info = "";
		for (String s : treeMap.keySet()) {
			String keyValuePair = (s + "=" + treeMap.get(s));
			logger.finer(keyValuePair);
			info += (keyValuePair + "\n");
		}
		logger.finer("----------");
		
		// for easy debugging
		if (logger.getLevel() == Level.FINEST) {
			InfoBox.showMessage(this, info, "debug", JOptionPane.INFORMATION_MESSAGE);
		}
	}			
}
