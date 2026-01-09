package org.talamona.terminal.comunication;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.talamona.terminal.utils.ConfigurationHolder;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/08/17
 * Time: 15.34
 */
public class SerialDataManagerTest {
    private SerialDataManager sut;

    @BeforeMethod
    private void setup(){
        String resourceFile = "application.properties";
        ConfigurationHolder.getInstance();
    }

    @Test
    public void testCreateNewInstance() throws Exception {
        assertNotNull(this.sut = SerialDataManager.createNewInstance());
    }

    @Test(dependsOnMethods = {"testCreateNewInstance"})
    public void testConnectToSerialPort() throws Exception {
        assertNotNull(this.sut.connectToSerialPort());
    }
    //@Test(dependsOnMethods = {"testConnectToSerialPort"})
    public void testConnection() throws InterruptedException {
        Thread.sleep(5000);
        //SerialListenerForTest listener = SerialListenerForTest.getNewInstanceBySerialDataManager(this.sut);


        //assertTrue(this.sut.getIsDataAvalaible().getValue());
    }

    //@Test(dependsOnMethods = {"testCreateNewInstance"})
    public void testDisconnectFromSerialPort() throws Exception {
        assertTrue(this.sut.disconnectFromSerialPort());
    }


}