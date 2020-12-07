package cr.ac.ucenfotec.Tarea4.bl.entidades;

import java.time.LocalDate;

public class Cuenta {
    protected int numeroCuenta;
    protected double saldo;
    protected LocalDate fechaApertura;
    protected String idCliente;

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Cuenta() {
    }

    public Cuenta(int numeroCuenta, double saldo, LocalDate fechaApertura, String idCliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.fechaApertura = fechaApertura;
        this.idCliente = idCliente;
    }


    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta=" + numeroCuenta +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", idCliente=" + this.idCliente +
                '}';
    }
}
