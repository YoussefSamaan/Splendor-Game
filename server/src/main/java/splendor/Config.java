package splendor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * A class for loading configuration files.
 */
public class Config {
  private static final String CONFIG_FILE = "src/main/java/splendor/config.properties";
  private static final Properties properties = loadConfig();

  /**
   * Loads the config file.
   *
   * @return the properties
   */
  private static Properties loadConfig() {
    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(CONFIG_FILE));
      return properties;
    } catch (FileNotFoundException e) {
      System.err.println("Config file not found: " + CONFIG_FILE);
      System.exit(1);
    } catch (Exception e) {
      System.err.println("Error loading config file: " + CONFIG_FILE);
      System.exit(1);
    }
    assert false;
    return null;
  }

  /**
   * Returns the value of the given property.
   *
   * @param property the property
   * @return the value
   */
  public static String getProperty(String property) {
    return properties.getProperty(property);
  }
}
