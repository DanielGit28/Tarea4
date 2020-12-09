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

    public Cuenta obtenerCuenta(int numCuenta) throws SQLException {
        Cuenta cuenta = new Cuenta();
        Statement query = cnx.createStatement();

        ResultSet resultado = query.executeQuery("select * from cuenta where numeroCuenta = " + numCuenta + "");
        //System.out.println("IdCliente: "+resultado.getString("idCliente"));
        if(resultado.next()) {
            cuenta.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            System.out.println("numeroCuenta: "+cuenta.getNumeroCuenta());
            //System.out.println(resultado.getInt("numeroCuenta"));
            cuenta.setSaldo(resultado.getDouble("saldo"));
            cuenta.setFechaApertura(resultado.getDate("fechaApertura").toLocalDate());
            System.out.println("Fecha: "+cuenta.getFechaApertura());
            String idCliente = resultado.getString("idCliente");
            Cliente resultadoCliente = clienteDAO.obtenerCliente(idCliente);
            cuenta.setCliente(resultadoCliente);
            //cuenta.setCliente(clienteDAO.obtenerCliente(resultado.getString("idCliente")));
            System.out.println( "cliente: "+cuenta.getCliente().toString());
        }
        //System.out.println(cuenta.toString());
        return cuenta;
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
