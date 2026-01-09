package org.talamona.terminal.utils;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class PomReader {
    private Model model;



    public static PomReader getNewInstance() {
        return new PomReader();
    }
    public static PomReader getNewInstanceByPomFile(String pomFile) {
        return new PomReader(pomFile);
    }
    public static PomReader getNewInstanceByInputStream(InputStream stream) {
        return new PomReader(stream);
    }



    public PomReader(){
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            this.model = reader.read(new FileReader("pom.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
    public PomReader(String pomFile) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            this.model = reader.read(new FileReader(pomFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
    public PomReader(InputStream stream) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            this.model = reader.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }


    public String readProjectVersionFromPom() {
        return model.getVersion();
    }
    public String readGroupIdFromPom() {
        return model.getGroupId();
    }
    public String readArtifactIdFromPom() {
        return model.getArtifactId();
    }
    public String getPropertyByKeyFromPomProperties(String key){
        return model.getProperties().getProperty(key);

    }
}
