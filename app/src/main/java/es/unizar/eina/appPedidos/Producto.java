package es.unizar.eina.appPedidos;

public class Producto {
    private int cantidad;
    private long idProducto;
    private String nombre;
    private double peso,precio;

    public Producto(long idProducto, int cantidad, String nombre, double peso, double precio) {
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.peso = peso;
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cant) {
        cantidad = cant;
    }

    public long getIdProducto() {
        return this.idProducto;
    }

    public String getNombre() { return this.nombre;}
}
