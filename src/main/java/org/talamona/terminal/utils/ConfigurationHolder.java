package org.talamona.terminal.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: Jan 08, 2026
 * Time: 14.33
 */
public class ConfigurationHolder {

    public static final String DEBUG="DEBUG";

// SERIAL PORT CONNECTION ---
    public static final String PORT="PORT";
    public static final String BAUD_RATE="BAUD_RATE";
    public static final String CRC="CRC";

// END OF LINE VALUE ---
    public static final String END_OF_LINE="END_OF_LINE";





    private final String configurationUri = "application.properties";

    private Properties properties;
    private static ConfigurationHolder singleInstance;

    private ConfigurationHolder(){
        this.createProperties();
    }
    private void createProperties() {

        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(this.configurationUri);
            this.properties = new Properties();
            this.properties.load(is);
            is.close();
        }catch (Exception ex){
            // TODO: log file
            System.out.println(ex);
        }

    }



    public static ConfigurationHolder getInstance(){
        if (singleInstance == null){
            singleInstance = new ConfigurationHolder();
        }
        return singleInstance;
    }

    public Properties getProperties() {
        return this.properties;
    }
}
