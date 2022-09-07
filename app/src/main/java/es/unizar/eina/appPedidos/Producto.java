package es.unizar.eina.appPedidos;

import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_NOM_PROD;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_PESO_PROD;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_PRECIO_PROD;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_ROWID;

import android.database.Cursor;

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

    public Producto(Cursor c) {
        this.cantidad = 0; //c.getInt(c.getColumnIndex(KEY_CANTIDAD));
        this.idProducto = c.getInt(c.getColumnIndex(KEY_ROWID));
        this.nombre = c.getString(c.getColumnIndex(KEY_NOM_PROD));
        this.peso = c.getDouble(c.getColumnIndex(KEY_PESO_PROD));
        this.precio = c.getDouble(c.getColumnIndex(KEY_PRECIO_PROD));
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

    public double getPrecio() {
        return precio;
    }

    public double getPeso() {
        return peso;
    }
}
