package cr.ac.ucenfotec.Tarea4;

import java.io.IOException;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        PropertiesHandler propertiesHandler = new PropertiesHandler();
        propertiesHandler.loadProperties();

        String driver = propertiesHandler.getDriver();
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("LOADED DRIVER ---> " + driver);
        String url= propertiesHandler.getCnxStr();
        Connection con = DriverManager.getConnection(url, propertiesHandler.getUser(), propertiesHandler.getPassword());
        System.out.println("CONNECTED TO ---> "+ url);

        Statement insertcmd = con.createStatement();

        //Es mejor utilizar esta forma con los nombres de las columnas, para evitar los posibles desacomodos en la tabla de la base de datos
        ResultSet results2 = insertcmd.executeQuery("select * from tpersona");
        while(results2.next()) {
            System.out.println(results2.getString("cedula") + " " + results2.getString("nombre") + " "  + results2.getString("apellido1") + " "  + results2.getString("apellido2"));
        }
        con.close();
        System.out.println("Cerrada?"+con.isClosed());
    }
}
