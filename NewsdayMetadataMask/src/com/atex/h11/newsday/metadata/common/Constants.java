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
	public static final boolean STANDALONE_DEBUG = (System.getenv("DEBUG") != null && System.getenv("DEBUG").equals("1"));
	
	//
	// Common constants
	//
	public static final String SYS_HERMES_DIR_VARIABLE = "HermesDir";
	public static final String SYS_HERMES_DIR_DEFAULT = STANDALONE_DEBUG ? System.getenv(SYS_HERMES_DIR_VARIABLE) : "c:/Hermes11/";
	public static final String SYS_HERMES_DIR = System.getProperty(SYS_HERMES_DIR_VARIABLE, Constants.SYS_HERMES_DIR_DEFAULT);

	// 
	// File Path constants
	// The path is relative to the HermesDir folder, i.e. the folder containing nroom.exe
	public static final String METADATA_GUI_PATH = "c:\\Hermes11\\custommetadata\\";
	public static final String METADATA_PROPERTIES_FILE = METADATA_GUI_PATH + "NewsdayMetadata.properties";
	public static final String METADATA_CONFIG_FILE_PROPERTY = "MetadataConfigFile";

	public static final String FALSE = "false";
	public static final String TRUE = "true";
	
	public static final String ALL = "ALL";
}
