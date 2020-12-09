package cr.ac.ucenfotec.Tarea4.bl.gestor;

import cr.ac.ucenfotec.Tarea4.PropertiesHandler;
import cr.ac.ucenfotec.Tarea4.bl.dao.*;
import cr.ac.ucenfotec.Tarea4.bl.entidades.*;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static java.time.temporal.ChronoUnit.DAYS;


public class Gestor {
    PropertiesHandler propertiesHandler = new PropertiesHandler();

    ClienteDAO clienteDao;

    CuentaDAO cuentaDAO;

    CuentaAhorroDAO cuentaAhorroDAO;

    CuentaAhorroProgramadoDAO cuentaAhorroProgramadoDAO;

    MovimientoDAO movimientoDAO;

    protected Connection connection;

    public Connection getConnection() {return connection;}

    public Gestor() {
        try {
            propertiesHandler.loadProperties();
            String driver = propertiesHandler.getDriver();
            Class.forName(driver).newInstance();
            //System.out.println("LOADED DRIVER ---> " + driver);
            String url= propertiesHandler.getCnxStr();
            connection = DriverManager.getConnection(url, propertiesHandler.getUser(), propertiesHandler.getPassword());
            //System.out.println("Conexión: "+this.connection);
            //System.out.println("CONNECTED TO ---> "+ url);

            this.clienteDao = new ClienteDAO(this.connection);
            this.cuentaDAO = new CuentaDAO(this.connection);
            this.cuentaAhorroDAO = new CuentaAhorroDAO(this.connection);
            this.cuentaAhorroProgramadoDAO = new CuentaAhorroProgramadoDAO(this.connection);
            this.movimientoDAO = new MovimientoDAO(this.connection);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }




    //----SECCION DE CLIENTES----
    public void guardarCliente(String nombre, String id, String direccion) {
        Cliente nuevo = new Cliente(nombre,id,direccion);
        try {
            clienteDao.guardarCliente(nuevo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //----FIN SECCION CLIENTES----

    //----SECCION DE CUENTAS----
    public void guardarCuenta(int numeroCuenta, double saldo, LocalDate fechaApertura,String idCliente) throws SQLException {
        Cliente cliente = clienteDao.obtenerCliente(idCliente);
        //System.out.println(cliente.toString());
        Cuenta nuevo = new Cuenta(numeroCuenta,saldo,fechaApertura,cliente);
        try {
            cuentaDAO.guardarCuenta(nuevo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void guardarCuentaAhorro(int numeroCuenta, double saldo, LocalDate fechaApertura,String idCliente) throws SQLException {
        Cliente cliente = clienteDao.obtenerCliente(idCliente);
        CuentaAhorro nuevo = new CuentaAhorro(numeroCuenta,saldo,fechaApertura,cliente);
        try {
            cuentaAhorroDAO.guardarCuentaAhorro(nuevo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void guardarCuentaAhorroProgramado(int numeroCuenta, double saldo, LocalDate fechaApertura,String idCliente,int cuentaAsociada) throws SQLException {
        Cuenta cuenta = cuentaDAO.obtenerCuenta(cuentaAsociada);
        //System.out.println(cuenta.toString() + " cuenta corriente");
        Cliente cliente = clienteDao.obtenerCliente(idCliente);
        //System.out.println(cliente.toString());

        CuentaAhorroProgramado nuevo = new CuentaAhorroProgramado(numeroCuenta,saldo,fechaApertura,cliente,cuenta);
        try {
            cuentaAhorroProgramadoDAO.guardarCuentaAhorro(nuevo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void guardarMovimiento(LocalDate fecha, String descripcion, double monto, TipoMovimiento tipoMovimiento,int numeroCuenta) {
        Movimiento nuevo = new Movimiento(fecha,descripcion,monto,tipoMovimiento,numeroCuenta);
        try {
            movimientoDAO.guardarMovimiento(nuevo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificarSaldoCuenta(int numeroCuenta, double monto, TipoMovimiento tipoMovimiento) throws  SQLException {
        //BufferedReader rd = null;
        int tipoCuenta = verificacionTipoCuenta(numeroCuenta);
        System.out.println("Tipo Cuenta "+tipoCuenta);
        double saldoNuevo = 0;
        Cuenta cuenta = new Cuenta();
        CuentaAhorro cuentaAhorro = new CuentaAhorro();
        CuentaAhorroProgramado cuentaAhorroProgramado = new CuentaAhorroProgramado();

        Statement query = connection.createStatement();
        String queryModificacion = null;


        if(tipoCuenta == 1) {
            cuenta = cuentaDAO.obtenerCuenta(numeroCuenta);
            saldoNuevo = saldoCuentaCorriente(cuenta,monto,tipoMovimiento);
            queryModificacion = "update cuenta set saldo = "+saldoNuevo+"  where numeroCuenta = "+numeroCuenta+"";
        } else if (tipoCuenta == 2) {
            cuentaAhorro = cuentaAhorroDAO.obtenerCuentaAhorro(numeroCuenta);
            saldoNuevo = saldoCuentaAhorro(cuentaAhorro,monto,tipoMovimiento);
            queryModificacion = "update cuenta_ahorro set saldo = "+saldoNuevo+"  where numeroCuenta = "+numeroCuenta+"";
        } else if(tipoCuenta == 3) {
            cuentaAhorroProgramado = cuentaAhorroProgramadoDAO.obtenerCuenta(numeroCuenta);
            saldoNuevo = saldoCuentaAhorroProgramado(cuentaAhorroProgramado,monto,tipoMovimiento);
            queryModificacion = "update cuenta_ahorro_programado set saldo = "+saldoNuevo+"  where numeroCuenta = "+numeroCuenta+"";
        }
        if(queryModificacion != null) {
            query.execute(queryModificacion);
        } else {
            System.out.println("No se puede modificar el saldo por algún problema en las cuentas");
        }

    }

    public double saldoCuentaCorriente(Cuenta cuenta, double monto, TipoMovimiento tipoMovimiento) {
        double montoNuevo = cuenta.getSaldo();
        if(tipoMovimiento.equals(TipoMovimiento.DEPOSITO)) {
            montoNuevo += monto;
        } else if(tipoMovimiento.equals(TipoMovimiento.RETIRO)) {
            montoNuevo = cuenta.getSaldo() - monto;
        }
        return montoNuevo;
    }
    public double saldoCuentaAhorro(CuentaAhorro cuenta, double monto, TipoMovimiento tipoMovimiento) {
        double montoNuevo = cuenta.getSaldo();
        double saldo = cuenta.getSaldo();
        double mitadSaldo = saldo/2;
        if(tipoMovimiento.equals(TipoMovimiento.DEPOSITO)) {
            montoNuevo += monto - (monto*0.05);
        } else if(tipoMovimiento.equals(TipoMovimiento.RETIRO)) {
            if(saldo >= 100000 && monto < mitadSaldo) {
                montoNuevo = saldo - (monto-(monto*0.05));
            }else {
                System.out.println("El saldo es menor a 100000 o el monto es mayor a la 50% del saldo total de la cuenta");
            }
        }
        return montoNuevo;
    }
    public double saldoCuentaAhorroProgramado(CuentaAhorroProgramado cuenta, double monto, TipoMovimiento tipoMovimiento) {
        double montoNuevo = cuenta.getSaldo();
        LocalDate fechaActual = LocalDate.now();
        if(tipoMovimiento.equals(TipoMovimiento.DEPOSITO)) {
            montoNuevo += monto;
        } else if(tipoMovimiento.equals(TipoMovimiento.RETIRO)) {
            if(DAYS.between(cuenta.getFechaApertura(),fechaActual) > 365) {
                montoNuevo = cuenta.getSaldo() - monto;
            }
            else {
                System.out.println("No ha pasado 1 año de la fecha de apertura de la cuenta");
            }
        }
        return montoNuevo;
    }

    public int verificacionTipoCuenta(int numCuenta) throws SQLException {
        int tipo = 0;
        Cuenta cuenta = cuentaDAO.obtenerCuenta(numCuenta);
        CuentaAhorro cuentaAhorro = cuentaAhorroDAO.obtenerCuentaAhorro(numCuenta);
        CuentaAhorroProgramado cuentaAhorroProgramado = cuentaAhorroProgramadoDAO.obtenerCuenta(numCuenta);

        if(cuenta.getNumeroCuenta() == numCuenta) {
            tipo = 1;
        } else if(cuentaAhorro.getNumeroCuenta() == numCuenta) {
            tipo = 2;
        } else if(cuentaAhorroProgramado.getNumeroCuenta() == numCuenta) {
            tipo = 3;
        }

        return tipo;
    }

    public boolean verificacionCuentaAsociada(int numeroCuenta, int cuentaCorriente) throws SQLException {
        boolean estado = false;
        CuentaAhorroProgramado cuenta = cuentaAhorroProgramadoDAO.obtenerCuenta(numeroCuenta);
        //System.out.println(cuenta.toString());
        if(cuenta.getCliente() != null) {
            if(cuenta.getCuentaCorrienteAsociada().getNumeroCuenta() == cuentaCorriente) {
                estado = true;
                System.out.println("Ya existe una cuenta asociada");
            }
        }

        return estado;
    }

    public boolean verificacionNumeroCuenta(int numCuenta) throws SQLException {
        boolean estado = false;
        Cuenta cuenta = cuentaDAO.obtenerCuenta(numCuenta);
        //System.out.println(cuenta.toString());
        CuentaAhorro cuentaAhorro = cuentaAhorroDAO.obtenerCuentaAhorro(numCuenta);
        CuentaAhorroProgramado cuentaAhorroProgramado = cuentaAhorroProgramadoDAO.obtenerCuenta(numCuenta);

        if(cuenta != null ) {
            if(cuenta.getNumeroCuenta() == numCuenta){
                estado = true;
            }

        } else if(cuentaAhorro != null ) {
            if(cuentaAhorro.getNumeroCuenta() == numCuenta) {
                estado = true;
            }

        } else if(cuentaAhorroProgramado != null ) {
            if(cuentaAhorroProgramado.getNumeroCuenta() == numCuenta) {
                estado = true;
            }

        }
        return estado;
    }

    //----FIN SECCION CUENTAS----

    /*
    //--LISTADOS DE LOS OBJETOS PARA PODER REALIZAR MOVIMIENTOS--
    public void listaClientes() throws SQLException {

        ArrayList<Cliente> result = clienteDao.obtenerTodosLosClientes();
        for (Cliente cliente: result)
            System.out.println(cliente.toString());
    }

    public void listaCuentas() throws SQLException {
        ArrayList<Cuenta> result = cuentaDAO.obtenerTodosLasCuentas();
        for (Cuenta cuenta: result)
            System.out.println(cuenta.toString());
    }






    public void listarCuentasCorrientes(){
        File archivo = new File("c:\\dev\\listOfCuentasCorrientes.csv");
        ArrayList<Cuenta> result = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("c:\\dev\\listOfCuentasCorrientes.csv"));
            String currentLine = reader.readLine();
            System.out.println(currentLine);
            while (currentLine != null) {
                result.add(new Cuenta(currentLine));
                currentLine = reader.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Cuenta cuenta: result) {
            System.out.println(cuenta.toCSVLine());
        }
    }

    public ArrayList<Cuenta> listaCuentaCorriente(){
        File archivo = new File("c:\\dev\\listOfCuentasCorrientes.csv");
        ArrayList<Cuenta> result = new ArrayList<>();
        BufferedReader reader;
        if(archivo.exists()) {
            try {
                reader = new BufferedReader(new FileReader("c:\\dev\\listOfCuentasCorrientes.csv"));
                String currentLine = reader.readLine();
                while (currentLine != null) {
                    result.add(new Cuenta(currentLine));
                    currentLine = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public ArrayList<CuentaAhorro> listaCuentaAhorro(){
        File archivo = new File("c:\\dev\\listOfCuentasAhorro.csv");
        ArrayList<CuentaAhorro> result = new ArrayList<>();
        BufferedReader reader;
        if(archivo.exists()) {
            try{
                reader = new BufferedReader(new FileReader("c:\\dev\\listOfCuentasAhorro.csv"));
                String currentLine = reader.readLine();
                while (currentLine != null) {
                    result.add(new CuentaAhorro(currentLine));
                    currentLine = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public ArrayList<CuentaAhorroProgramado> listaCuentaAhorroProgramado(){
        File archivo = new File("c:\\dev\\listOfCuentasAhorroProgramado.csv");
        ArrayList<CuentaAhorroProgramado> result = new ArrayList<>();
        BufferedReader reader;
        if(archivo.exists()) {
            try {
                reader = new BufferedReader(new FileReader("c:\\dev\\listOfCuentasAhorroProgramado.csv"));
                String currentLine = reader.readLine();
                while (currentLine != null) {
                    result.add(new CuentaAhorroProgramado(currentLine));
                    currentLine = reader.readLine();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    //--FIN LISTADO DE LOS OBJETOS--

     */
}
