package cr.ac.ucenfotec.Tarea4.bl.gestor;

import cr.ac.ucenfotec.Tarea4.bl.entidades.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class Gestor {
    //----SECCION DE CLIENTES----
    public void guardarCliente(String nombre, String id, String direccion) {
        Cliente cliente = new Cliente(nombre,id,direccion);
        ArrayList<String> lines = new ArrayList<>();
        lines.add(cliente.toCSVLine());
        try {                          /* /dev/listOfMaterial.csv   */
            Files.write(Paths.get("c:\\dev\\listOfClientes.csv"),lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----FIN SECCION CLIENTES----

    //----SECCION DE CUENTAS----
    public void guardarCuentaCorriente(int numeroCuenta, double saldo, LocalDate fechaApertura, String idCliente) {
        Cuenta cuenta = new Cuenta(numeroCuenta,saldo,fechaApertura,idCliente);
        ArrayList<String> lines = new ArrayList<>();
        lines.add(cuenta.toCSVLine());
        try {                          /* /dev/listOfMaterial.csv   */
            Files.write(Paths.get("c:\\dev\\listOfCuentasCorrientes.csv"),lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void guardarCuentaAhorro(int numeroCuenta, double saldo, LocalDate fechaApertura, String idCliente) {
        CuentaAhorro cuenta = new CuentaAhorro(numeroCuenta,saldo,fechaApertura, idCliente);
        ArrayList<String> lines = new ArrayList<>();
        lines.add(cuenta.toCSVLine());
        try {                          /* /dev/listOfMaterial.csv   */
            Files.write(Paths.get("c:\\dev\\listOfCuentasAhorro.csv"),lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarCuentaAhorroProgramado(int numeroCuenta, double saldo, LocalDate fechaApertura,String idCliente,int cuentaCorrienteAsociada) {
        CuentaAhorroProgramado cuenta = new CuentaAhorroProgramado(numeroCuenta,saldo,fechaApertura,idCliente,cuentaCorrienteAsociada);
        ArrayList<String> lines = new ArrayList<>();
        lines.add(cuenta.toCSVLine());
        try {                          /* /dev/listOfMaterial.csv   */
            Files.write(Paths.get("c:\\dev\\listOfCuentasAhorroProgramado.csv"),lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void guardarMovimiento(LocalDate fecha, String descripcion, double monto, TipoMovimiento tipoMovimiento,int numeroCuenta) {
        Movimiento movimiento = new Movimiento(fecha,descripcion,monto,tipoMovimiento,numeroCuenta);
        ArrayList<String> lines = new ArrayList<>();
        lines.add(movimiento.toCSVLine());
        try {                          /* /dev/listOfMaterial.csv   */
            Files.write(Paths.get("c:\\dev\\listOfMovimientos.csv"),lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modificarSaldoCuenta(int numeroCuenta, double monto, TipoMovimiento tipoMovimiento) throws FileNotFoundException {
        //BufferedReader rd = null;
        int tipoCuenta = verificacionTipoCuenta(numeroCuenta);
        String path = null;
        double saldoNuevo = 0;
        Cuenta cuentaBuscada = encontrarCuenta(numeroCuenta);
        ArrayList<String> lineasNuevas = new ArrayList<>();
        int pos = 0;
        int contador = 0;

        if(tipoCuenta == 1) {
            path = "c:\\dev\\listOfCuentasCorrientes.csv";
            //rd = new BufferedReader( new FileReader ("c:\\dev\\listOfCuentasCorrientes.csv"));
            saldoNuevo = saldoCuentaCorriente(cuentaBuscada,monto,tipoMovimiento);
            for (Cuenta cuenta: listaCuentaCorriente()) {
                if(cuenta.getNumeroCuenta() == numeroCuenta) {
                    cuentaBuscada = cuenta;
                    lineasNuevas.add(cuenta.toCSVLine());
                    pos = contador;
                    break;
                }
                contador += 1;
            }
        } else if (tipoCuenta == 2) {
            path = "c:\\dev\\listOfCuentasAhorro.csv";
            //rd = new BufferedReader( new FileReader ("c:\\dev\\listOfCuentasAhorro.csv"));
            saldoNuevo = saldoCuentaAhorro(cuentaBuscada,monto,tipoMovimiento);
            for (CuentaAhorro cuenta: listaCuentaAhorro()) {
                if(cuenta.getNumeroCuenta() == numeroCuenta) {
                    cuentaBuscada = cuenta;
                    lineasNuevas.add(cuenta.toCSVLine());
                    pos = contador;
                    break;
                }
                contador += 1;
            }
        } else if(tipoCuenta == 3) {
            path = "c:\\dev\\listOfCuentasAhorroProgramado.csv";
            //rd = new BufferedReader( new FileReader ("c:\\dev\\listOfCuentasAhorroProgramado.csv"));
            saldoNuevo = saldoCuentaAhorroProgramado(cuentaBuscada,monto,tipoMovimiento);
            for (CuentaAhorroProgramado cuenta: listaCuentaAhorroProgramado()) {
                if(cuenta.getNumeroCuenta() == numeroCuenta) {
                    cuentaBuscada = cuenta;
                    lineasNuevas.add(cuenta.toCSVLine());
                    pos = contador;
                    break;
                }
                contador += 1;
            }
        }
        cuentaBuscada.setSaldo(saldoNuevo);
        //System.out.println(cuentaBuscada.toCSVLine());
        lineasNuevas.set(pos, cuentaBuscada.toCSVLine());

        try {
            Files.write(Paths.get(path),lineasNuevas, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
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
    public double saldoCuentaAhorro(Cuenta cuenta, double monto, TipoMovimiento tipoMovimiento) {
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
    public double saldoCuentaAhorroProgramado(Cuenta cuenta, double monto, TipoMovimiento tipoMovimiento) {
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

    //----FIN SECCION CUENTAS----

    //--LISTADOS DE LOS OBJETOS PARA PODER REALIZAR MOVIMIENTOS--
    public ArrayList<Cliente> listaClientes(){
        File archivo = new File("c:\\dev\\listOfCuentasCorrientes.csv");
        ArrayList<Cliente> result = new ArrayList<>();
        BufferedReader reader;
        if(archivo.exists()) {
            try {
                reader = new BufferedReader(new FileReader("c:\\dev\\listOfClientes.csv"));
                String currentLine = reader.readLine();
                while (currentLine != null) {
                    result.add(new Cliente(currentLine));
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
    public Cuenta encontrarCuenta(int numeroCuenta) {
        Cuenta cuentaBuscada = null;
        for (Cuenta cuenta: listaCuentaCorriente()) {
            if(cuenta.getNumeroCuenta() == numeroCuenta) {
                cuentaBuscada = cuenta;
            }
        }
        for (CuentaAhorro ahorro: listaCuentaAhorro()) {
            if(ahorro.getNumeroCuenta() == numeroCuenta) {
                return ahorro;
            }
        }
        for (CuentaAhorroProgramado ahorroProgramado: listaCuentaAhorroProgramado()) {
            if(ahorroProgramado.getNumeroCuenta() == numeroCuenta) {
                return ahorroProgramado;
            }
        }
        return cuentaBuscada;
    }
    public int verificacionTipoCuenta(int numCuenta) {
        int tipo = 0;
        List<Cuenta> corrientes = listaCuentaCorriente();
        List<CuentaAhorro> ahorros = listaCuentaAhorro();
        List<CuentaAhorroProgramado> ahorrosProgramados = listaCuentaAhorroProgramado();
        for (Cuenta cuenta: corrientes) {
            if(cuenta.getNumeroCuenta() == numCuenta) {
                tipo = 1;
            }
        }
        for (CuentaAhorro ahorro: ahorros) {
            if(ahorro.getNumeroCuenta() == numCuenta) {
                tipo = 2;
            }
        }
        for (CuentaAhorroProgramado ahorroProgramado: ahorrosProgramados) {
            if(ahorroProgramado.getNumeroCuenta() == numCuenta) {
                tipo = 3;
            }
        }
        return tipo;
    }
    public boolean verificacionNumeroCuenta(int numCuenta) {
        boolean estado = false;
        List<Cuenta> corrientes = listaCuentaCorriente();
        for(Cuenta cuenta: corrientes) {
            System.out.println(cuenta.toCSVLine());
        }
        List<CuentaAhorro> ahorros = listaCuentaAhorro();
        List<CuentaAhorroProgramado> ahorrosProgramados = listaCuentaAhorroProgramado();
        for (Cuenta cuenta: corrientes) {
            if(cuenta.getNumeroCuenta() == numCuenta) {
                estado = true;
            }
        }
        for (CuentaAhorro ahorro: ahorros) {
            if(ahorro.getNumeroCuenta() == numCuenta) {
                estado = true;
            }
        }
        for (CuentaAhorroProgramado ahorroProgramado: ahorrosProgramados) {
            if(ahorroProgramado.getNumeroCuenta() == numCuenta) {
                estado = true;
            }
        }
        return estado;
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
}
