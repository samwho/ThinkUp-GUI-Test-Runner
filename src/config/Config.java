package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The configuration file for this program. Parses the config.txt file in the
 * root directory for key=value pairs and stores them in a HashMap<String, String>.
 *
 * This class follows the singleton design pattern.
 * 
 * @author Sam Rose <samwho@lbak.co.uk>
 */
public class Config {
    private static File configFile = new File("config.txt");
    private Pattern matchOption = Pattern.compile("^(.+)=(.+)$");
    private static Config singleton = new Config();
    private HashMap<String, String> options = new HashMap<String, String>();
    private static String errorMessage = null;

    /**
     * Get the singleton instance of the Config class.
     *
     * @return Singleton instance of this class.
     */
    public static Config getInstance() {
        return singleton;
    }

    public static File getConfigFile() {
        return configFile;
    }

    /**
     * Gets the error message that was generated if the config file failed to load.
     *
     * Will return null if the config file loaded properly.
     *
     * @return Null if the config file loaded properly, an error message if it did not.
     */
    public static String getErrorMessage() {
        return errorMessage;
    }

    /**
     * The constructor to this class parses the config.txt file and
     * extracts key=value pairs using a regular expression.
     */
    private Config() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));

            String line = br.readLine();
            while (line != null) {
                Matcher m = matchOption.matcher(line);
                if (m.find()) {
                    options.put(m.group(1), m.group(2));
                }

                line = br.readLine();
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
    }

    /**
     * Get a value from the config.txt file.
     *
     * @param key The key of the option.
     * @return The value of the option.
     */
    public String getValue(String key) {
        return options.get(key) != null ? options.get(key) : "";
    }
}
