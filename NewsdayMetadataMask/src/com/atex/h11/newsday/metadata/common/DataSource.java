package com.atex.h11.newsday.metadata.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import com.atex.h11.newsday.util.CustomException;
import com.unisys.media.cr.adapter.ncm.common.business.interfaces.INCMMetadataNodeManager;
import com.unisys.media.cr.adapter.ncm.common.data.datasource.NCMDataSourceDescriptor;
import com.unisys.media.cr.adapter.ncm.model.data.datasource.NCMDataSource;
import com.unisys.media.cr.common.data.values.NodeTypePK;
import com.unisys.media.cr.model.data.datasource.DataSourceManager;
import com.unisys.media.extension.common.constants.ApplicationConstants;
import com.unisys.media.extension.common.security.UPSUser;

public class DataSource {
	private static final Logger logger = Constants.LOGGER;
	private static NCMDataSource ds = null;
	private static INCMMetadataNodeManager metaMgr = null;
	
	public DataSource() { }
	
	public static NCMDataSource getDataSource(ConfigModel config) 
			throws FileNotFoundException, IOException, CustomException {
		logger.entering(NCMDataSource.class.getSimpleName(), "getDataSource");
		
		if (ds == null) {
			DataSourceManager dsmgr = null;
			
			Properties secProps = new Properties();
			secProps.load(new FileInputStream(config.getSecurityPropertiesFile()));
	
	        Enumeration<?> secEnum = secProps.propertyNames();
	        while (secEnum.hasMoreElements()) {
	        	String key = (String) secEnum.nextElement();
	        	String value = secProps.getProperty(key);
	        	System.setProperty(key, value);
	        	logger.finer("security: " + key + "=" + value);
	        }    		
					
			Properties jndiProps = new Properties();
	        jndiProps.load(new FileInputStream(config.getJNDIPropertiesFile()));
	        
	        // Get a DataSourceManager instance.
	        dsmgr = DataSourceManager.getInstance(jndiProps);
	        
	        Enumeration<?> jndiEnum = jndiProps.propertyNames();
	        while (jndiEnum.hasMoreElements()) {
	        	String key = (String) jndiEnum.nextElement();
	        	logger.finer("jndi: " + key + "=" + jndiProps.getProperty(key));
	        }                    	  
	        
			String user = config.getAPIUser();
			String password = config.getAPIPassword();
			logger.finer("API user=" + user + ", password=" + password);
			
			// Login
			UPSUser upsUser = 
				UPSUser.instanceUPSUserForNamedUser(user, password, ApplicationConstants.APP_MEDIA_API_ID);
			ds = (NCMDataSource) dsmgr.getDataSource(NCMDataSource.DS_PK, upsUser, ApplicationConstants.APP_MEDIA_API_ID);		
		
			if (ds == null) {
				throw new CustomException("Datasource is null");
			} else {
				logger.finer("Datasource initialized");
			}
		}
		
		logger.exiting(NCMDataSource.class.getSimpleName(), "getDataSource");
		return ds;
	}	
	
	public static INCMMetadataNodeManager getMetadataManager() {
		logger.entering(NCMDataSource.class.getSimpleName(), "getMetadataManager");
		
		if (metaMgr == null) {
			NodeTypePK PK = new NodeTypePK(NCMDataSourceDescriptor.NODETYPE_NCMMETADATA);
			metaMgr = (INCMMetadataNodeManager) ds.getNodeManager(PK);
		}
		
		logger.exiting(NCMDataSource.class.getSimpleName(), "getMetadataManager");
		return metaMgr;
	}	
}
