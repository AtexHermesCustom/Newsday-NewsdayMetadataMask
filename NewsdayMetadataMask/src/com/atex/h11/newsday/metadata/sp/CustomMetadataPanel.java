package com.atex.h11.newsday.metadata.sp;

import com.unisys.media.commonservices.common.location.LocationInfo;
import com.unisys.media.commonservices.dialogs.metadata.custom.NodeValueInspector;
import com.unisys.media.commonservices.dialogs.metadata.view.ICustomMetadataPanel;
import com.unisys.media.cr.adapter.ncm.model.data.datasource.NCMDataSource;
import com.unisys.media.cr.adapter.ncm.model.data.values.NCMObjectValueClient;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.common.Constants;
import com.atex.h11.newsday.metadata.common.CustomException;
import com.atex.h11.newsday.util.InfoBox;

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
	
	private NCMDataSource ds = null;
	private NCMObjectValueClient sp = null;
	
	private String objId;
	private String objName; 		
	private String objLevel;
	private String objLevelId;
	private String pub;	
    private boolean isDone = false;	
	
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
			
			objId = "";
			objName = ""; 		
			objLevel = "";
			objLevelId = "";
			pub = "";				
			
			// for testing only
			String testMode = System.getProperty("Metadata.Test");
			if (testMode != null && testMode.equals("1")) {
				System.out.println("metadata test mode");
				objId = "5465:00";
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
			metadataPanel.setPreferredSize(new Dimension(690, 600));
			
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
			
			metadata.clear();
			metadata = metadataPanel.getMetadataValues();
			logMetadata(metadata, "Save to DB");
			
			logger.exiting(this.getClass().getSimpleName(), "getMetadataValues");
			return metadata;
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
		
		parent.addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {
				if (!isDone) {
					doFinalTasks();
					isDone = true;	// to make sure this part is only called once
				}
			}

			@Override
			public void windowClosing(WindowEvent arg0) {}

			@Override
			public void windowDeactivated(WindowEvent arg0) {}

			@Override
			public void windowDeiconified(WindowEvent arg0) {}

			@Override
			public void windowIconified(WindowEvent arg0) {}

			@Override
			public void windowOpened(WindowEvent arg0) {}
		});		
	}	
	
	protected void doFinalTasks() {
		logger.entering(this.getClass().getSimpleName(), "doFinalTasks");
		updateChildMetadata();
		logger.exiting(this.getClass().getSimpleName(), "doFinalTasks");
	}
	
	protected void updateChildMetadata() {
		logger.entering(this.getClass().getSimpleName(), "updateChildMetadata");
		
		// for specific child object metadata to be updated
		String textByline = metadata.get("TEXT_BYLINE");
		if (textByline != null && !textByline.isEmpty()) {			
			HttpURLConnection httpCon = null;
			
			try {
				String targetURL = "http://cvldndhermapp:8080/H11CustomWeb/UpdateChildMetadata?id=" + objId 
					+ "&schema=WEB&field=BYLINE&value=" + URLEncoder.encode(textByline, "UTF-8");
	            URL url = new URL(targetURL);
	            
	            // Connect
	            httpCon = (HttpURLConnection) url.openConnection();       
	            httpCon.setDoOutput(false);
	            httpCon.setDoInput(true);
	            httpCon.setRequestMethod("GET");
	            httpCon.setInstanceFollowRedirects(true);
	            httpCon.connect();
	            
	            //Map<String, List<String>> hdrs = httpCon.getHeaderFields();
	            //for (String k : hdrs.keySet()) {
	            //  System.out.println("header key=" + k + ", value=" + hdrs.get(k));
				//}
	           
	            if (httpCon.getResponseCode() == 200) { 	// 200 = OK			
	            	logger.fine("Updated child metadata field through web app: url=" + targetURL);
	            } else {
	            	throw new CustomException("Error encountered while calling web app: url=" + targetURL 
	            		+ ". Error: "+ httpCon.getResponseCode() + " - " + httpCon.getResponseMessage());
	            }				
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error encountered", e);
			} finally {
	            if (httpCon != null) {
	            	httpCon.disconnect();
	            }
			}
		} else {
			logger.finer("Child metadata not updated. No value passed.");
		}
		
		logger.exiting(this.getClass().getSimpleName(), "updateChildMetadata");
	}
		
	protected boolean isReady() {
		boolean retVal = metadataPanel.isReady();
		logger.finer("Return value=" + retVal);
		return retVal;
	}	
	
	protected String getPubFromLevel(String objLevel) 
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
	
	protected void logMetadata(HashMap<String, String> map, String msg) {
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
