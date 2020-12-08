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
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryCuentas = cnx.prepareStatement(TEMPLATE_QRY_TODOSLASCUENTASAHORRO);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Cliente encontrarPorId(String cedula){
        return null;
    }

    public List<CuentaAhorro> obtenerTodosLasCuentasAhorro() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<CuentaAhorro> listaCuentas = new ArrayList<>();
        ArrayList<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
        while (resultado.next()){
            CuentaAhorro leido = new CuentaAhorro();
            leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            leido.setSaldo(resultado.getDouble("saldo"));
            leido.setFechaApertura(LocalDate.parse(resultado.getString("fechaApertura")));
            for (Cliente cliente: listaClientes) {
                if(cliente.getId().equals(resultado.getString("idCliente"))) {
                    leido.setCliente(cliente);
                }
            }
            listaCuentas.add(leido);
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
