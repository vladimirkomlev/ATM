package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigurationOfProperty {
	
	private static final Logger LOG=Logger.getLogger(ConfigurationOfProperty.class);
	private static InputStream inputStream=ConfigurationOfProperty.class.getClassLoader().getResourceAsStream("banknote.properties");
	private static ConfigurationOfProperty instanceConfigurationOfProperty=null;
	private Properties properties=null;
	
	private ConfigurationOfProperty(){
		try {
			properties=new Properties();
			properties.load(inputStream);
		} catch (IOException e) {
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
			System.exit(1);
		}
	}
	
	public static synchronized ConfigurationOfProperty getInstance() {
		if(instanceConfigurationOfProperty==null){
			instanceConfigurationOfProperty=new ConfigurationOfProperty();
		}
		return instanceConfigurationOfProperty;
	}
	
	public int getValueOfPropertyWithName(String inputValue){
		return Integer.parseInt(properties.getProperty(inputValue));
	}
	
	public int[] getArrayValuesOfProperty(String inputValue) throws IOException {
		int arrayResultValues[] = new int[0];
		try {
			String arrayStrValues[] = properties.getProperty(inputValue).split(";");
			arrayResultValues = new int[arrayStrValues.length];
			for (int index = 0; index < arrayResultValues.length; index++) {
				arrayResultValues[index] = Integer.valueOf(arrayStrValues[index]);
			}
		} catch (Exception e) {
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		}
		return arrayResultValues;
	}
}
