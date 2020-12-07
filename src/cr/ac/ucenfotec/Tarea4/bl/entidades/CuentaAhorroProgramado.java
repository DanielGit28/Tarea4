package cr.ac.ucenfotec.Tarea4.bl.entidades;

import java.time.LocalDate;

public class CuentaAhorroProgramado extends Cuenta{
    private int cuentaCorrienteAsociada;

    public int getCuentaCorrienteAsociada() {
        return cuentaCorrienteAsociada;
    }

    public void setCuentaCorrienteAsociada(int cuentaCorrienteAsociada) {
        this.cuentaCorrienteAsociada = cuentaCorrienteAsociada;
    }

    public CuentaAhorroProgramado(){}

    public CuentaAhorroProgramado(int numeroCuenta, double saldo, LocalDate fechaApertura, String idCliente, int cuentaCorrienteAsociada) {
        super(numeroCuenta,saldo,fechaApertura,idCliente);
        this.cuentaCorrienteAsociada = cuentaCorrienteAsociada;
    }

    @Override
    public String toString() {
        return "CuentaAhorroProgramado{" +
                "cuentaCorrienteAsociada=" + getCuentaCorrienteAsociada() +
                ", numeroCuenta=" + numeroCuenta +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", idCliente=" + idCliente +
                '}';
    }
}
