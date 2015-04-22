package com.atex.h11.newsday.metadata.common;

import java.util.logging.Logger;

import com.atex.h11.newsday.metadata.sp.CustomMetadataPanel;

public interface Constants {
	//
	// Constants for logger
	//
	public static final String LOGFILE_NAME = "NewsdayCustomMetadata.log";
	public static final String LOGGER_NAME = CustomMetadataPanel.class.getName();
	public static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);
	
	//
	// Dir constants
	//
	public static final boolean STANDALONE_DEBUG = (System.getenv("DEBUG") != null && System.getenv("DEBUG").equals("1"));
	public static final String SYS_HERMES_DIR_VARIABLE = "HermesDir";
	public static final String SYS_HERMES_DIR_DEFAULT = STANDALONE_DEBUG ? System.getenv(SYS_HERMES_DIR_VARIABLE) : "c:/Hermes11/";
	public static final String SYS_HERMES_DIR = System.getProperty(SYS_HERMES_DIR_VARIABLE, SYS_HERMES_DIR_DEFAULT);

	// 
	// File Path constants
	public static final String METADATA_PROPERTIES_DIR_DEFAULT = "c:\\Hermes11\\custommetadata\\"; 
	public static final String METADATA_PROPERTIES_DIR = System.getProperty("MetadataPropertiesDir", METADATA_PROPERTIES_DIR_DEFAULT);
	public static final String METADATA_PROPERTIES_FILE = METADATA_PROPERTIES_DIR + "NewsdayMetadata.properties";
	public static final String METADATA_CONFIG_FILE_PROPERTY = "MetadataConfigFile";
	
	// boolean constants
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	
	// String constants
	public static final String ALL = "ALL";	
	public static final String LIVE = "LIVE";
	
	// formats
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	public static final String DATE_FORMAT_DB = "yyyy-MM-dd";	// format in DB
	public static final String TIME_FORMAT = "hh:mm a";	
}
