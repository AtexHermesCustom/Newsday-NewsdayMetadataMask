package com.atex.h11.newsday.metadata.sp;

import com.unisys.media.commonservices.common.location.LocationInfo;
import com.unisys.media.commonservices.dialogs.metadata.custom.NodeValueInspector;
import com.unisys.media.commonservices.dialogs.metadata.view.ICustomMetadataPanel;
import com.unisys.media.cr.adapter.ncm.common.business.interfaces.INCMMetadataNodeManager;
import com.unisys.media.cr.adapter.ncm.common.data.pk.NCMCustomMetadataPK;
import com.unisys.media.cr.adapter.ncm.common.data.pk.NCMObjectPK;
import com.unisys.media.cr.adapter.ncm.common.data.types.NCMObjectNodeType;
import com.unisys.media.cr.adapter.ncm.common.data.values.NCMCustomMetadataJournal;
import com.unisys.media.cr.adapter.ncm.common.data.values.NCMMetadataPropertyValue;
import com.unisys.media.cr.adapter.ncm.common.data.values.NCMObjectBuildProperties;
import com.unisys.media.cr.adapter.ncm.model.data.datasource.NCMDataSource;
import com.unisys.media.cr.adapter.ncm.model.data.values.NCMObjectValueClient;
import com.unisys.media.cr.common.data.interfaces.INodePK;
import com.unisys.media.cr.common.data.types.IPropertyDefType;
import com.unisys.media.extension.common.exception.NodeAlreadyLockedException;
import com.unisys.media.ncm.cfg.common.data.values.MetadataSchemaValue;
import com.unisys.media.ncm.cfg.model.values.UserHermesCfgValueClient;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import com.atex.h11.newsday.metadata.common.ConfigModel;
import com.atex.h11.newsday.metadata.common.Constants;
import com.atex.h11.newsday.metadata.common.DataSource;
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
			
			// get H11 data source
			ds = DataSource.getDataSource(config);
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
			
			// retrieve package
			sp = getPackage(Integer.parseInt(objId));

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
		
		if (retVal) {
			//updateChildMetadata(NCMObjectNodeType.OBJ_TEXT, "WEB", "BYLINE", retMetadata.get("REPORTER1"));
			updateChildMetadata(NCMObjectNodeType.OBJ_TEXT, "WEB", "BYLINE", "test byline");
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
	
	protected NCMObjectValueClient getPackage(int objId) {
		NCMObjectBuildProperties objProps = new NCMObjectBuildProperties();
		objProps.setGetByObjId(true);
		objProps.setIncludeSpChild(true);
		objProps.setDoNotChekPermissions(true);
		//objProps.setIncludeMetadataGroups(new Vector<String>());
		NCMObjectPK pk = new NCMObjectPK(objId);
		NCMObjectValueClient sp = (NCMObjectValueClient) ((NCMDataSource)ds).getNode(pk, objProps);
		logger.finer("Package retrieved: name=" + sp.getNCMName() + ", type=" + sp.getType() + ", pk=" + sp.getPK().toString());
		return sp;
	}
	
	protected void updateChildMetadata(int objType, String metaSchema, String metaField, String metaValue) {
		// get child objects
		INodePK[] childPKs = sp.getChildPKs();
		if (childPKs != null) {
			NCMObjectBuildProperties objProps = new NCMObjectBuildProperties();
			objProps.setGetByObjId(true);
			objProps.setDoNotChekPermissions(true);
			objProps.setIncludeMetadataGroups(new Vector<String>());		
	
			for (int i = 0; i < childPKs.length; i++ ) {
				NCMObjectPK childPK = new NCMObjectPK(getObjIdFromPK(childPKs[i]));
				NCMObjectValueClient child = (NCMObjectValueClient) ds.getNode(childPK, objProps);
				
				// text objects
				if (child.getType() == objType) {
					setMetadata(child, metaSchema, metaField, metaValue);
				}				
			}
		}
	}
	
	protected void setMetadata(NCMObjectValueClient objVC, String metaSchema, String metaField, String metaValue) {
		String objName = objVC.getNCMName();
		Integer objId = getObjIdFromPK(objVC.getPK());
		logger.finer("setMetadata: Object [" + objId.toString() + "," + objName + "," + Integer.toString(objVC.getType()) + "]" +
			", Meta=" + metaSchema + "." + metaField + ", Value=" + metaValue);
		
		UserHermesCfgValueClient cfg = ds.getUserHermesCfg();
		
		// Get from configuration the schemaId using schemaName for metadata
		MetadataSchemaValue schema = cfg.getMetadataSchemaByName(metaSchema);
		int schemaId = schema.getId();
		
		// Get metadata property
		IPropertyDefType metaGroupDefType = ds.getPropertyDefType(metaSchema);
		//IPropertyValueClient metaGroupPK = objVC.getProperty(metaGroupDefType.getPK());		
		
		INCMMetadataNodeManager metaMgr = DataSource.getMetadataManager();
		NCMMetadataPropertyValue pValue = new NCMMetadataPropertyValue(
				metaGroupDefType.getPK(), null, schema);
		pValue.setMetadataValue(metaField, metaValue);
		NCMCustomMetadataPK cmPk = new NCMCustomMetadataPK(
				objId, (short) objVC.getType(), schemaId);
		schemaId = schema.getId();
		NCMCustomMetadataPK[] nodePKs = new NCMCustomMetadataPK[] { cmPk };
		
		try {
			try {
				metaMgr.lockMetadataGroup(schemaId, nodePKs);
			} catch (NodeAlreadyLockedException e) {
			}
			NCMCustomMetadataJournal j = new NCMCustomMetadataJournal();
			j.setCreateDuringUpdate(true);
			metaMgr.updateMetadataGroup(schemaId, nodePKs, pValue, j);
			logger.finer("setMetadata: Update metadata successful for [" + objId.toString() + "," + objName + "," + Integer.toString(objVC.getType()) + "]");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "setMetadata: Update metadata failed for [" + objId.toString() + "," + objName + "," + Integer.toString(objVC.getType()) + "]: ", 
				e);
		} finally {
			try {
				metaMgr.unlockMetadataGroup(schemaId, nodePKs);
			} catch (Exception e) {
			}
		}			
	}	
	
	protected int getObjIdFromPK(INodePK pk) {
		String s = pk.toString();
		int delimIdx = s.indexOf(":");
		if (delimIdx >= 0)
			s = s.substring(0, delimIdx);
		return Integer.parseInt(s);
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
