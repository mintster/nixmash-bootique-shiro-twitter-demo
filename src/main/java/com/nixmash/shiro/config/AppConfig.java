package com.nixmash.shiro.config;

import com.google.inject.Singleton;
import com.nixmash.shiro.utils.ShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

@Singleton
public class AppConfig implements Serializable {

	// region properties

	private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
	private static final long serialVersionUID = -4129304441129842839L;

	public String applicationId;
	public String datasourceName;
	public String unauthorizedUrl;

    // endregion

	// region getResourceBundle()

	public AppConfig() {

		Properties properties = new Properties();

		try {
			String propertiesFile = !ShiroUtils.isInTestingMode() ? "application" : "test";
			properties.load(getClass().getResourceAsStream(String.format("/%s.properties", propertiesFile)));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		this.applicationId = properties.getProperty("application.id");
		this.datasourceName = properties.getProperty("datasource.name");
		this.unauthorizedUrl = properties.getProperty("shiro.unauthorizedurl");

	}

	// endregion

}
