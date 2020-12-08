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

    private ClienteDAO clienteDAO;

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

    public ArrayList<Cuenta> obtenerTodosLasCuentas() throws SQLException {
        ResultSet resultado = this.queryCuentas.executeQuery();
        ArrayList<Cuenta> listaCuentas = new ArrayList<>();
        ArrayList<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
        while (resultado.next()){
            Cuenta leido = new Cuenta();
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

    public void actualizarSaldoCuenta() {

    }


    public void guardarCuenta(Cuenta nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setInt(1,nuevo.getNumeroCuenta());
            this.cmdInsertar.setDouble(2,nuevo.getSaldo());
            this.cmdInsertar.setDate(3, Date.valueOf(nuevo.getFechaApertura()));
            this.cmdInsertar.setString(4,nuevo.getCliente().getId());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar la cuenta");
        }
    }


}
