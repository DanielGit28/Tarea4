package cr.ac.ucenfotec.Tarea4.bl.dao;

import cr.ac.ucenfotec.Tarea4.bl.entidades.Cliente;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Cuenta;
import cr.ac.ucenfotec.Tarea4.bl.entidades.CuentaAhorro;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuentaAhorroDAO {
    Connection cnx;
    private PreparedStatement cmdInsertar;
    private PreparedStatement queryCuentas;

    private ClienteDAO clienteDAO;

    private final String TEMPLATE_CMD_INSERTAR = "insert into cuenta_ahorro(numeroCuenta,saldo,fechaApertura,idCliente)" +
            " values (?,?,?,?)";
    private final String TEMPLATE_QRY_TODOSLASCUENTASAHORRO = "select * from cuenta_ahorro";

    public CuentaAhorroDAO(Connection conexion){
        this.cnx = conexion;
        clienteDAO = new ClienteDAO(cnx);
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryCuentas = cnx.prepareStatement(TEMPLATE_QRY_TODOSLASCUENTASAHORRO);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public CuentaAhorro obtenerCuentaAhorro(int numCuenta) throws SQLException {
        CuentaAhorro cuenta = new CuentaAhorro();
        Statement query = cnx.createStatement();

        ResultSet resultado = query.executeQuery("select * from cuenta_ahorro where numeroCuenta = " + numCuenta + "");
        if(resultado.next()) {
            cuenta.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            cuenta.setSaldo(resultado.getDouble("saldo"));
            cuenta.setFechaApertura(resultado.getDate("fechaApertura").toLocalDate());
            cuenta.setCliente(clienteDAO.obtenerCliente(resultado.getString("idCliente")));
        }
        return cuenta;
    }

    public ArrayList<CuentaAhorro> obtenerTodosLasCuentasAhorro() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<CuentaAhorro> listaCuentas = new ArrayList<>();
        while (resultado.next()){
            CuentaAhorro leido = new CuentaAhorro();
            if(resultado.getRow() == 0) {
                System.out.println("Resultado vacío");
            } else {
                leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
                leido.setSaldo(resultado.getDouble("saldo"));
                leido.setFechaApertura(LocalDate.parse(resultado.getString("fechaApertura")));
                leido.setCliente(clienteDAO.obtenerCliente(resultado.getString("idCliente")));
                listaCuentas.add(leido);
            }

        }
        return listaCuentas;
    }

    public void actualizarSaldoCuentaAhorro() {

    }

    public void guardarCuentaAhorro(CuentaAhorro nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setInt(1,nuevo.getNumeroCuenta());
            this.cmdInsertar.setDouble(2,nuevo.getSaldo());
            this.cmdInsertar.setDate(3, Date.valueOf(nuevo.getFechaApertura()));
            this.cmdInsertar.setString(4,nuevo.getCliente().getId());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar la cuenta de ahorro");
        }
    }
}
