package com.atex.h11.newsday.metadata.sp;

import com.unisys.media.commonservices.common.location.LocationInfo;
import com.unisys.media.commonservices.dialogs.metadata.custom.NodeValueInspector;
import com.unisys.media.commonservices.dialogs.metadata.view.ICustomMetadataPanel;

import javax.swing.JPanel;

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

public class CustomMetadataPanel implements ICustomMetadataPanel {
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
			
			Level logLevel = Level.parse(config.GetConfigValue("logLevel"));	    
		    
			// log file
			String logFile = config.GetConfigValue("logFile");			
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
			logger.setLevel(Level.parse(config.GetConfigValue("logLevel")));	
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
			
			
			
			logMetadata(metadata, "Loaded from DB");

			metadataPanel = new MetadataPanel(config, metadata);
			//metadataPanel.setPreferredSize(new Dimension(720, 470));
			
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
			
			HashMap<String, String> retMetadata = metadataPanel.GetMetadataValues();
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
		switch (button) {
			case ICustomMetadataPanel.OK_BUTTON:
				return isReady();
			default:
				return true;
		}
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
		return title;
	}	
	
	@Override
	public void setCurrentLocale(Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public void setLocationValue(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}
	
	@Override
	public void setParent(Window parent) {
		this.parent = parent;
	}	
		
	private boolean isReady() {
		logger.entering(this.getClass().getSimpleName(), "isReady");
		boolean result = metadataPanel.IsReady();
		logger.exiting(this.getClass().getSimpleName(), "isReady");
		return result;
	}	
	
	private void logMetadata(HashMap<String, String> map, String msg) {
		// sort map by key
		Map<String, String> treeMap = new TreeMap<String, String>(map);
		
		// log
		logger.finer("logMetadata - " + msg + ":");
		logger.finer("<key> = <value>");
		logger.finer("----------");
		String info = "";
		for (String s : treeMap.keySet()) {
			String keyValuePair = (s + " = " + treeMap.get(s));
			logger.finer(keyValuePair);
			info += (keyValuePair + "\n");
		}
		logger.finer("----------");
		
		// for easy debugging
		if (logger.getLevel() == Level.FINEST) {
			InfoBox.ShowMessage(info, "debug");
		}
	}		
	
}
