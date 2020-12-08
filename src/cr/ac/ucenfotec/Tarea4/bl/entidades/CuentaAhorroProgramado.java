package cr.ac.ucenfotec.Tarea4.bl.entidades;

import com.sun.xml.internal.ws.client.ClientTransportException;

import java.time.LocalDate;

public class CuentaAhorroProgramado extends Cuenta{
    private Cuenta cuentaCorrienteAsociada;

    public Cuenta getCuentaCorrienteAsociada() {
        return cuentaCorrienteAsociada;
    }

    public void setCuentaCorrienteAsociada(Cuenta cuentaCorrienteAsociada) {
        this.cuentaCorrienteAsociada = cuentaCorrienteAsociada;
    }

    public CuentaAhorroProgramado(){}

    public CuentaAhorroProgramado(int numeroCuenta, double saldo, LocalDate fechaApertura, Cliente cliente, Cuenta cuentaCorrienteAsociada) {
        super(numeroCuenta,saldo,fechaApertura,cliente);
        this.cuentaCorrienteAsociada = cuentaCorrienteAsociada;
    }

    @Override
    public String toString() {
        return "CuentaAhorroProgramado{" +
                "cuentaCorrienteAsociada=" + cuentaCorrienteAsociada +
                ", numeroCuenta=" + numeroCuenta +
                ", saldo=" + saldo +
                ", fechaApertura=" + fechaApertura +
                ", Cliente=" + cliente +
                '}';
    }
}
