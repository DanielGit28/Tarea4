package cr.ac.ucenfotec.Tarea4.bl.entidades;

import java.time.LocalDate;

public class Cuenta {
    protected int numeroCuenta;
    protected double saldo;
    protected LocalDate fechaApertura;
    protected Cliente cliente;

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente pcliente) {
        this.cliente = pcliente;
    }

    public Cuenta() {
    }

    public Cuenta(int numeroCuenta, double saldo, LocalDate fechaApertura, Cliente pcliente) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.fechaApertura = fechaApertura;
        this.cliente = pcliente;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta=" + numeroCuenta +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", cliente=" + cliente +
                '}';
    }
}
