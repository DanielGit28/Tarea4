package cr.ac.ucenfotec.Tarea4;

import cr.ac.ucenfotec.Tarea4.controlador.Controlador;

import java.io.IOException;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        Controlador controlador = new Controlador();
        controlador.ejecutarPrograma();
    }
}
