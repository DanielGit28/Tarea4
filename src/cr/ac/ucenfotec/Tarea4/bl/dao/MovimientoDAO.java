package cr.ac.ucenfotec.Tarea4.bl.dao;

import cr.ac.ucenfotec.Tarea4.bl.entidades.Cliente;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Cuenta;
import cr.ac.ucenfotec.Tarea4.bl.entidades.Movimiento;
import cr.ac.ucenfotec.Tarea4.bl.entidades.TipoMovimiento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {
    Connection cnx;
    private PreparedStatement cmdInsertar;
    private PreparedStatement queryMovimientos;

    private final String TEMPLATE_CMD_INSERTAR = "insert into movimiento(fecha,descripcion,monto,tipoMovimiento,numeroCuenta)" +
            " values (?,?,?,?,?)";
    private final String TEMPLATE_QRY_TODOSLOSMOVIMIENTOS = "select * from movimiento";

    public MovimientoDAO(Connection conexion){
        this.cnx = conexion;
        try {
            this.cmdInsertar = cnx.prepareStatement(TEMPLATE_CMD_INSERTAR);
            this.queryMovimientos = cnx.prepareStatement(TEMPLATE_QRY_TODOSLOSMOVIMIENTOS);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Cliente encontrarPorId(String cedula){
        return null;
    }

    public List<Movimiento> obtenerTodosLosMovimientos() throws SQLException {
        ResultSet resultado = this.queryMovimientos.executeQuery();
        ArrayList<Movimiento> listaMovimientos = new ArrayList<>();
        while (resultado.next()){
            Movimiento leido = new Movimiento();
            leido.setFecha(LocalDate.parse(resultado.getString("fecha")));
            leido.setDescripcion(resultado.getString("descripcion"));
            leido.setMonto(resultado.getDouble("monto"));
            leido.setTipoMovimiento(TipoMovimiento.valueOf(resultado.getString("tipoMovimiento")));
            leido.setNumeroCuenta(resultado.getInt("numeroCuenta"));
            listaMovimientos.add(leido);
        }
        return listaMovimientos;
    }

    public void guardarMovimiento(Movimiento nuevo) throws SQLException{
        if(this.cmdInsertar != null) {
            this.cmdInsertar.setDate(1,Date.valueOf(nuevo.getFecha()));
            this.cmdInsertar.setString(2,nuevo.getDescripcion());
            this.cmdInsertar.setDouble(3, nuevo.getMonto());
            this.cmdInsertar.setString(4, String.valueOf(nuevo.getTipoMovimiento()));
            this.cmdInsertar.setInt(5,nuevo.getNumeroCuenta());
            this.cmdInsertar.execute();
        } else {
            System.out.println("No se pudo guardar el movimiento");
        }
    }
}
