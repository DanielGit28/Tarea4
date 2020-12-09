package cr.ac.ucenfotec.Tarea4.bl.dao;

import cr.ac.ucenfotec.Tarea4.bl.entidades.Cliente;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Cuenta;
import cr.ac.ucenfotec.Tarea4.bl.entidades.CuentaAhorro;
import cr.ac.ucenfotec.Tarea4.bl.entidades.CuentaAhorroProgramado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CuentaAhorroProgramadoDAO {
    Connection cnx;
    private PreparedStatement cmdInsertar;
    private PreparedStatement queryCuentas;

    private ClienteDAO clienteDAO;
    private CuentaDAO cuentaDAO;

    private final String TEMPLATE_CMD_INSERTAR = "insert into cuenta_ahorro_programado(numeroCuenta,saldo,fechaApertura,idCliente)" +
            " values (?,?,?,?)";
    private final String TEMPLATE_QRY_TODOSLASCUENTASAHORROPROGRAMADO = "select * from cuenta_ahorro_programado";

    public CuentaAhorroProgramadoDAO(Connection conexion){
        this.cnx = conexion;
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryCuentas = cnx.prepareStatement(TEMPLATE_QRY_TODOSLASCUENTASAHORROPROGRAMADO);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public CuentaAhorroProgramado obtenerCuenta(int numeroCuenta) throws SQLException {
        CuentaAhorroProgramado cuenta = new CuentaAhorroProgramado();
        Statement query = cnx.createStatement();

        ResultSet resultado = query.executeQuery("select * from cuenta_ahorro_programado where numeroCuenta = "+numeroCuenta+"");

        if(resultado.next()) {
            cuenta.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            cuenta.setSaldo(resultado.getDouble("saldo"));
            cuenta.setFechaApertura(resultado.getDate("fechaApertura").toLocalDate());
            cuenta.setCliente(clienteDAO.obtenerCliente(resultado.getString("idCliente")));
            cuenta.setCuentaCorrienteAsociada(cuentaDAO.obtenerCuenta(resultado.getInt("cuentaCorrienteAsociada")));
        }


        return cuenta;
    }

    public ArrayList<CuentaAhorroProgramado> obtenerTodosLasCuentasAhorroProgramado() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<CuentaAhorroProgramado> listaCuentasAhorro = new ArrayList<>();
        ArrayList<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
        ArrayList<Cuenta> listaCuentas = cuentaDAO.obtenerTodosLasCuentas();
        while (resultado.next()){
            CuentaAhorroProgramado leido = new CuentaAhorroProgramado();
            leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            leido.setSaldo(resultado.getDouble("saldo"));
            leido.setFechaApertura(LocalDate.parse(resultado.getString("fechaApertura")));
            leido.setCliente(clienteDAO.obtenerCliente(resultado.getString("idCliente")));
            leido.setCuentaCorrienteAsociada(cuentaDAO.obtenerCuenta(resultado.getInt("cuentaCorrienteAsociada")));
            listaCuentasAhorro.add(leido);
        }
        return listaCuentasAhorro;
    }

    public void actualizarSaldoCuentaAhorroProgramado() {

    }

    public void guardarCuentaAhorro(CuentaAhorroProgramado nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setInt(1,nuevo.getNumeroCuenta());
            this.cmdInsertar.setDouble(2,nuevo.getSaldo());
            this.cmdInsertar.setDate(3, Date.valueOf(nuevo.getFechaApertura()));
            this.cmdInsertar.setString(4,nuevo.getCliente().getId());
            this.cmdInsertar.setInt(5,nuevo.getCuentaCorrienteAsociada().getNumeroCuenta());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar la cuenta de ahorro programado");
        }
    }
}
