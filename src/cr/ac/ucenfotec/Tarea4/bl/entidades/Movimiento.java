package cr.ac.ucenfotec.Tarea4.bl.entidades;

import java.time.LocalDate;

public class Movimiento {
    private LocalDate fecha;
    private String descripcion;
    private double monto;
    private TipoMovimiento tipoMovimiento;
    private int numeroCuenta;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Movimiento(){}

    public Movimiento(LocalDate fecha, String descripcion, double monto, TipoMovimiento tipoMovimiento, int numeroCuenta) {
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
        this.numeroCuenta = numeroCuenta;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", monto=" + monto +
                ", tipoMovimiento=" + tipoMovimiento +
                ", numeroCuenta=" + numeroCuenta +
                '}';
    }
}
