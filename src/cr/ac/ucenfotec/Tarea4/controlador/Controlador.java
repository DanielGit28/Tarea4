package cr.ac.ucenfotec.Tarea4.controlador;

import cr.ac.ucenfotec.Tarea4.bl.entidades.CuentaAhorroProgramado;
import cr.ac.ucenfotec.Tarea4.bl.entidades.TipoMovimiento;
import cr.ac.ucenfotec.Tarea4.bl.gestor.Gestor;
import cr.ac.ucenfotec.Tarea4.ui.UI;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controlador {
    private UI ui = new UI();

    //private MaterialGestor gestor = new MaterialGestor();
    private Gestor gestor = new Gestor();

    public void ejecutarPrograma(){
        int opcion = 0;
        do {
            ui.mostrarMenu();
            opcion = ui.leerOpcion();
            ejecutarOpcion(opcion);
        } while (opcion != 4);
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion){
            case 1:
                crearCliente();
                break;
            case 2:
                ejecutarAbrirCuenta();
                break;
            case 3:
                ejecutarMovimiento();
                break;
            case 4:
                break;
        }

    }

    public void ejecutarAbrirCuenta(){
        int opcion = 0;
        do {
            ui.mostrarMenuAbrirCuenta();
            opcion = ui.leerOpcion();
            ejecutarOpcionAbrirCuenta(opcion);
        } while (opcion != 4);
    }

    private void ejecutarOpcionAbrirCuenta(int opcion) {
        switch (opcion){
            case 1:
                crearCuentaCorriente();
                break;
            case 2:
                crearCuentaAhorro();
                break;
            case 3:
                crearCuentaAhorroProgramado();
                break;
            case 4:
                break;
        }
    }
    public void ejecutarMovimiento(){
        int opcion = 0;
        do {
            ui.mostrarMenuMovimiento();
            opcion = ui.leerOpcion();
            ejecutarOpcionMovimiento(opcion);
        } while(opcion != 3);
    }

    private void ejecutarOpcionMovimiento(int opcion) {
        switch (opcion){
            case 1:
                realizarDeposito();
                break;
            case 2:
                realizarRetiro();
                break;
            case 3:
                break;

        }

    }

    //--SECCION CREACIONES DE OBJETOS--
    private void crearCliente() {
        ui.imprimirMensaje("Nombre: ");
        String nombre = ui.leerTexto();
        ui.imprimirMensaje("Identificación: ");
        String id = ui.leerTexto();
        ui.imprimirMensaje("Dirección: ");
        String direccion = ui.leerTexto();
        gestor.guardarCliente(nombre,id,direccion);
        System.out.println("Cliente creado con éxito");
    }
    private void crearCuentaCorriente() {
        ui.imprimirMensaje("Ingrese el número de cuenta: ");
        int numCuenta = ui.leerOpcion();
        if(gestor.verificacionNumeroCuenta(numCuenta) == false) {
            double saldo = 0;
            LocalDate fechaApertura = LocalDate.now();
            ui.imprimirMensaje("Ingrese la identificación del cliente dueño de la cuenta: ");
            String idCliente = ui.leerTexto();
            gestor.guardarCuentaCorriente(numCuenta,saldo,fechaApertura,idCliente);
            System.out.println("Cuenta creada con éxito");
        } else {
            System.out.println("Ya exise una cuenta con ese número");
        }
    }
    private void crearCuentaAhorro() {
        ui.imprimirMensaje("Ingrese el número de cuenta: ");
        int numCuenta = ui.leerOpcion();
        if(gestor.verificacionNumeroCuenta(numCuenta) == false) {
            double saldo = 0;
            LocalDate fechaApertura = LocalDate.now();
            ui.imprimirMensaje("Ingrese la identificación del cliente dueño de la cuenta: ");
            String idCliente = ui.leerTexto();
            gestor.guardarCuentaAhorro(numCuenta,saldo,fechaApertura,idCliente);
            System.out.println("Cuenta creada con éxito");
        } else {
            System.out.println("Ya existe una cuenta con ese número");
        }
    }
    private void crearCuentaAhorroProgramado() {
        ui.imprimirMensaje("Ingrese el número de cuenta: ");
        int numCuenta = ui.leerOpcion();
        if(gestor.verificacionNumeroCuenta(numCuenta) == false) {
            double saldo = 0;
            LocalDate fechaApertura = LocalDate.now();
            ui.imprimirMensaje("Ingrese la identificación del cliente dueño de la cuenta: ");
            String idCliente = ui.leerTexto();
            ui.imprimirMensaje("Ingrese el número de cuenta corriente que desea asociar: ");
            int cuentaAsociada = ui.leerOpcion();
            if(verificacionCuentaAsociada(cuentaAsociada) == false) {
                gestor.guardarCuentaAhorroProgramado(numCuenta,saldo,fechaApertura,idCliente,cuentaAsociada);
                System.out.println("Cuenta creada con éxito");
            }
        } else {
            System.out.println("Ya existe una cuenta con ese número");
        }
    }
//--FIN SECCIONES OBJETOS--

    //---SECCION DE MOVIMIENTOS---
    public void realizarDeposito() {
        ui.imprimirMensaje("Ingrese el número de cuenta asociado: ");
        int numCuenta = ui.leerOpcion();
        LocalDate fecha = LocalDate.now();
        ui.imprimirMensaje("Ingrese la descripción del depósito: ");
        String descripcion = ui.leerTexto();
        ui.imprimirMensaje("Ingrese el monto de depósito: ");
        double montoDeposito = ui.leerDouble();
        TipoMovimiento movimiento = TipoMovimiento.DEPOSITO;
        //gestor.listarCuentasCorrientes();
        gestor.guardarMovimiento(fecha,descripcion,montoDeposito,movimiento,numCuenta);
        //int tipo = gestor.verificacionTipoCuenta(numCuenta);
        //String idCliente = gestor.encontrarCuenta(numCuenta).getIdCliente();
        try {
            gestor.modificarSaldoCuenta(numCuenta,montoDeposito,TipoMovimiento.DEPOSITO);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Depósito realizado con éxito");

    }
    public void realizarRetiro() {
        ui.imprimirMensaje("Ingrese el número de cuenta asociado: ");
        int numCuenta = ui.leerOpcion();
        LocalDate fecha = LocalDate.now();
        ui.imprimirMensaje("Ingrese la descripción del retiro: ");
        String descripcion = ui.leerTexto();
        ui.imprimirMensaje("Ingrese el monto del retiro: ");
        double montoRetiro = ui.leerDouble();
        TipoMovimiento movimiento = TipoMovimiento.RETIRO;
        //gestor.listaCuentaCorriente();
        gestor.guardarMovimiento(fecha,descripcion,montoRetiro,movimiento,numCuenta);
        //int tipo = gestor.verificacionTipoCuenta(numCuenta);
        //System.out.println("Tipo "+tipo);
        try {
            gestor.modificarSaldoCuenta(numCuenta,montoRetiro,TipoMovimiento.RETIRO);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Retiro realizado con éxito");

    }

    //---FIN SECCION DE MOVIMIENTOS---

    public boolean verificacionCuentaAsociada(int cuentaCorriente) {
        boolean estado = false;
        ArrayList<CuentaAhorroProgramado> ahorros = gestor.listaCuentaAhorroProgramado();
        for (CuentaAhorroProgramado ahorro: ahorros) {
            if(ahorro.getCuentaCorrienteAsociada() == cuentaCorriente) {
                estado = true;
                System.out.println("Ya existe una cuenta corriente asociada");
            }
        }
        return estado;
    }
}
