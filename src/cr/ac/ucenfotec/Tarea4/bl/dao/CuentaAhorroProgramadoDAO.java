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

    public Cliente encontrarPorId(String cedula){
        return null;
    }

    public List<CuentaAhorroProgramado> obtenerTodosLasCuentasAhorroProgramado() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<CuentaAhorroProgramado> listaCuentasAhorro = new ArrayList<>();
        ArrayList<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
        ArrayList<Cuenta> listaCuentas = cuentaDAO.obtenerTodosLasCuentas();
        while (resultado.next()){
            CuentaAhorroProgramado leido = new CuentaAhorroProgramado();
            leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            leido.setSaldo(resultado.getDouble("saldo"));
            leido.setFechaApertura(LocalDate.parse(resultado.getString("fechaApertura")));
            for (Cliente cliente: listaClientes) {
                if(cliente.getId().equals(resultado.getString("idCliente"))) {
                    leido.setCliente(cliente);
                }
            }
            for (Cuenta cuenta : listaCuentas ) {
                if(cuenta.getNumeroCuenta() == leido.getNumeroCuenta()) {
                    leido.setCuentaCorrienteAsociada(cuenta);
                }
            }
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
