package cr.ac.ucenfotec.Tarea4;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHandler {
    String[] values = new String[4];
    Properties props = new Properties();

    public void loadProperties() throws IOException {
        props.load(new FileInputStream("application.properties"));
        values[0] = props.getProperty("driver");
        values[1] = props.getProperty("cnxStr");
        values[2] = props.getProperty("usr");
        values[3] = props.getProperty("pwd");
    }

    public String getDriver() {
        return values[0];
    }
    public String getCnxStr() {
        return values[1];
    }
    public String getUser() {
        return values[2];
    }
    public String getPassword() {
        return values[3];
    }
}
