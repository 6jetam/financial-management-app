	package app;
	
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.Properties;
	
	public class ConfigLoader {
		
		private static final Properties properties = new Properties();
	
		static {
		    try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
		        if (input == null) {
		            throw new RuntimeException("config.properties súbor sa nenašiel!");
		        }
		        properties.load(input);
		    } catch (IOException ex) {
		        throw new RuntimeException("Chyba pri načítaní config.properties", ex);
		    }
		}
		
		public static String getProperty(String key) {
		    return properties.getProperty(key);
		}
	
	
	    public static String getApiKey() {
	        return properties.getProperty("apiKey");
	    	}
		}
	
