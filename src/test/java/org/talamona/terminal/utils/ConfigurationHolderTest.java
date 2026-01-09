package org.talamona.terminal.utils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 29/07/17
 * Time: 15.58
 */
public class ConfigurationHolderTest {
    private String configurationUri = "application.properties";
    private String realPathConfigurationUri;

    @DataProvider(name = "values")
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {ConfigurationHolder.DEBUG,"production"},
                {ConfigurationHolder.CRC,"KERMIT"},
                {ConfigurationHolder.PORT,"/dev/ttyUSB0"},
                {ConfigurationHolder.BAUD_RATE, "115200"},
                {ConfigurationHolder.END_OF_LINE, "0D"}
        };
    }


    @BeforeClass
    private void setup(){
        ConfigurationHolder.getInstance();
    }
    @Test
    public void testCreateSingleInstanceByConfigUri() throws Exception {
        assertNotNull(ConfigurationHolder.getInstance());
    }
    @Test(dataProvider = "values")
    public void testGetProperties(String key, String value) throws Exception {
        assertEquals(value, ConfigurationHolder.getInstance().getProperties().getProperty(key));
    }
}

/*
#DEBUG=debug
DEBUG=production

# CRC CONFIGURATION
# OR CRC=CCITT
CRC=KERMIT

# SERIAL PORT CONNECTION ---
# PORT=/dev/ttyACM0
PORT=/dev/ttyUSB0
BAUD_RATE=115200
END_OF_LINE=0D

 */