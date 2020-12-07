package cr.ac.ucenfotec.Tarea4.ui;

import java.io.PrintStream;
import java.util.Scanner;

public class UI {
    private PrintStream output = new PrintStream(System.out);
    private Scanner input = new Scanner(System.in).useDelimiter("\n");

    //MENÚ PRINCIPAL
    public void mostrarMenu() {
        output.println("Bievenido a los lavadores dot com, ingrese una opción:");
        output.println("1. Crear cliente");
        output.println("2. Abrir cuenta");
        output.println("3. Realizar movimientos");
        output.println("4. Salir");
    }

    public void mostrarMenuAbrirCuenta() {
        output.println("Bievenido al menú de crear cuentas:");
        output.println("1. Crear cuenta corriente");
        output.println("2. Crear cuenta de ahorro");
        output.println("3. Crear cuenta de ahorro programado");
        output.println("4. Salir");
    }
    public void mostrarMenuMovimiento() {
        output.println("Bievenido al menú de movimientos de cuenta:");
        output.println("1. Realizar depósito");
        output.println("2. Realizar retiro");
        output.println("3. Salir");
    }


    //METODOS DE LECTURA DE DATOS
    public int leerOpcion() {
        output.println("Su opcion es: ");
        return input.nextInt();
    }
    public double leerDouble() {
        return input.nextDouble();
    }
    public void imprimirMensaje(String mensaje) {
        output.println(mensaje);
    }

    public String leerTexto() {
        return input.next();
    }
}
