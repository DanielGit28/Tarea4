package cr.ac.ucenfotec.Tarea4.bl.dao;

import cr.ac.ucenfotec.Tarea4.bl.entidades.Cliente;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Cuenta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {
    Connection cnx;
    private PreparedStatement cmdInsertar;
    private PreparedStatement queryCuentas;

    private final String TEMPLATE_CMD_INSERTAR = "insert into cuenta(numeroCuenta,saldo,fechaApertura,idCliente)" +
            " values (?,?,?,?)";
    private final String TEMPLATE_QRY_TODOSLASCUENTAS = "select * from cuenta";

    public CuentaDAO(Connection conexion){
        this.cnx = conexion;
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryCuentas = cnx.prepareStatement(TEMPLATE_QRY_TODOSLASCUENTAS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Cliente encontrarPorId(String cedula){
        return null;
    }

    public List<Cuenta> obtenerTodosLasCuentas() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<Cuenta> listaCuentas = new ArrayList<>();
        while (resultado.next()){
            Cuenta leido = new Cuenta();
            leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            leido.setSaldo(resultado.getDouble("saldo"));
            leido.setFechaApertura(LocalDate.parse(resultado.getString("fechaApertura")));
            leido.setIdCliente(resultado.getString("idCliente"));
            listaCuentas.add(leido);
        }
        return listaCuentas;
    }

    public void guardarCuenta(Cuenta nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setInt(1,nuevo.getNumeroCuenta());
            this.cmdInsertar.setDouble(2,nuevo.getSaldo());
            this.cmdInsertar.setDate(3, convertToSqlDate(nuevo.getFechaApertura()));
            this.cmdInsertar.setString(4,nuevo.getIdCliente());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar el cliente");
        }
    }

    private java.sql.Date convertToSqlDate(java.util.Date fechaAConvertir) {
        return new java.sql.Date(fechaAConvertir.getTime());
    }
}
