package org.talamona.terminal.utils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PomReaderTest {
    private final String pomFileForTest = "test-pom.xml";
    private final String url;

    public PomReaderTest(){
        this.url = this.getClass().getClassLoader().getResource(pomFileForTest).getFile();
    }
    @DataProvider(name = "propertiesData")
    private Object[][] getPropertiesData(){
        return new Object[][]{
                {"description","Serial monitor"},
                {"releaseDate","08-01-2026"}
        };
    }

    @Test
    public void testGetNewInstance() {
        assertNotNull(PomReader.getNewInstanceByPomFile(this.url));
    }
    @Test
    public void testGetNewInstanceByInputStream() {
        assertNotNull(PomReader.getNewInstanceByInputStream(this.getClass().getClassLoader().getResourceAsStream(pomFileForTest)));
    }

    @Test
    public void testReadProjectVersionFromPom() {
        assertEquals(PomReader.getNewInstanceByPomFile(this.url).readProjectVersionFromPom(), "1.0.0");
    }

    @Test
    public void testReadGroupIdFromPom() {
        assertEquals(PomReader.getNewInstanceByPomFile(this.url).readGroupIdFromPom(), "TRZ");
    }

    @Test
    public void testReadArtifactIdFromPom() {
        assertEquals(PomReader.getNewInstanceByPomFile(this.url).readArtifactIdFromPom(), "SerialMonitor");
    }

    @Test(dataProvider = "propertiesData")
    public void testGetPropertyByKeyFromPomProperties(String key, String value) {
        assertEquals(PomReader.getNewInstanceByPomFile(this.url).getPropertyByKeyFromPomProperties(key),value);
    }
}