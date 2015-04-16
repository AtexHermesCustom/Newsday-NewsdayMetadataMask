package com.atex.h11.newsday.metadata.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import com.atex.h11.newsday.util.CustomException;
import com.unisys.media.cr.adapter.ncm.model.data.datasource.NCMDataSource;
import com.unisys.media.cr.model.data.datasource.DataSourceManager;
import com.unisys.media.extension.common.constants.ApplicationConstants;
import com.unisys.media.extension.common.security.UPSUser;

public class DataSource {
	private static final Logger logger = Constants.LOGGER;
	
	public DataSource() { }
	
	public static NCMDataSource getDataSource(ConfigModel config) 
			throws FileNotFoundException, IOException, CustomException {
		logger.entering(NCMDataSource.class.getSimpleName(), "getDataSource");
		DataSourceManager dsmgr = null;
		
		Properties connectProps = new Properties();
		connectProps.load(new FileInputStream(config.getConnectionPropertiesFile()));

        Enumeration<?> connectEnum = connectProps.propertyNames();
        while (connectEnum.hasMoreElements()) {
        	String key = (String) connectEnum.nextElement();
        	String value = connectProps.getProperty(key);
        	System.setProperty(key, value);
        	logger.finer("connect: " + key + "=" + value);
        }    		
				
		Properties jndiProps = new Properties();
        jndiProps.load(new FileInputStream(System.getProperty(Constants.JNDI_PROPERTIES)));
        
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
		NCMDataSource ds = 
			(NCMDataSource) dsmgr.getDataSource(NCMDataSource.DS_PK, upsUser, ApplicationConstants.APP_MEDIA_API_ID);		
	
		if (ds == null) {
			throw new CustomException("Datasource is null");
		} else {
			logger.finer("Datasource initialized");
		}
		
		logger.exiting(NCMDataSource.class.getSimpleName(), "getDataSource");
		return ds;
	}	
}
