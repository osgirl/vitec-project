package fr.vitec.site.cinemotion.connection;

import java.util.ResourceBundle;

public class SitePropertiesManager {
	ResourceBundle rb;
	
	public SitePropertiesManager(String fileName){
		rb = ResourceBundle.getBundle(fileName);
	}
	
	public String getValue(String key){
		return rb.getString(key);
	}
}
