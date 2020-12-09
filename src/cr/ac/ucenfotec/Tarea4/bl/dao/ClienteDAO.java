package cr.ac.ucenfotec.Tarea4.bl.dao;

import cr.ac.ucenfotec.Tarea4.bl.entidades.Cliente;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Cuenta;
import cr.ac.ucenfotec.Tarea4.bl.entidades.CuentaAhorroProgramado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    Connection cnx;
    private PreparedStatement cmdInsertar;
    private PreparedStatement queryClientes;

    private final String TEMPLATE_CMD_INSERTAR = "insert into cliente(nombre,identificacion,direccion)" +
            " values (?,?,?)";
    private final String TEMPLATE_QRY_TODOSLOSCLIENTES = "select * from cliente";

    public ClienteDAO(Connection conexion){
        this.cnx = conexion;
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryClientes = cnx.prepareStatement(TEMPLATE_QRY_TODOSLOSCLIENTES);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Cliente obtenerCliente(String idCliente) throws SQLException {
        Cliente cliente = new Cliente();
        Statement query = cnx.createStatement();
        ResultSet resultado = query.executeQuery("select * from cliente where identificacion = '" + idCliente + "';");

        if(resultado.next()) {
            //resultado.beforeFirst();
            cliente.setNombre(resultado.getString("nombre"));
            System.out.println(cliente.getNombre());
            cliente.setId(resultado.getString("identificacion"));
            cliente.setDireccion(resultado.getString("direccion"));
        } else {
            System.out.println("Cliente vacío");
        }
        System.out.println("Cliente: "+cliente.toString());
        return cliente;
    }

    public ArrayList<Cliente> obtenerTodosLosClientes() throws SQLException {
        ResultSet resultado = this.queryClientes.executeQuery();
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        while (resultado.next()){
            Cliente leido = new Cliente();
            leido.setNombre(resultado.getString("nombre"));
            leido.setId(resultado.getString("identificacion"));
            leido.setDireccion(resultado.getString("direccion"));
            listaClientes.add(leido);
        }
        return listaClientes;
    }

    public void guardarCliente(Cliente nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setString(1,nuevo.getNombre());
            this.cmdInsertar.setString(2,nuevo.getId());
            this.cmdInsertar.setString(3, nuevo.getDireccion());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar el cliente");
        }
    }
}
