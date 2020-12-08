package cr.ac.ucenfotec.Tarea4.bl.entidades;

import java.time.LocalDate;

public class CuentaAhorro extends Cuenta{
    private int tasaInteres = 5;

    public int getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(int tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public CuentaAhorro() {}

    public CuentaAhorro(int numeroCuenta, double saldo, LocalDate fechaApertura, Cliente cliente) {
        super(numeroCuenta, saldo, fechaApertura,cliente);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public String toString() {
        return "CuentaAhorro{" +
                "tasaInteres=" + tasaInteres +
                ", numeroCuenta=" + numeroCuenta +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", Cliente=" + cliente +
                '}';
    }
}
