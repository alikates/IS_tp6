package es.unizar.eina.appPedidos;

import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_CANTIDAD;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_FECHA_PEDIDO;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_NOMBRE_CLIENTE_PEDIDO;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_ROWID;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_TELEFONO_CLIENTE_PEDIDO;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;

public class Pedido {
    private String cliente, fecha, telefono;
    private long id;
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public Pedido(String cliente, String fecha, String telefono) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.telefono = telefono;
    }

    public Pedido(Cursor cPedido, Cursor cProductos) {
        this.id = cPedido.getLong(cPedido.getColumnIndex(KEY_ROWID));
        this.cliente = cPedido.getString(cPedido.getColumnIndex(KEY_NOMBRE_CLIENTE_PEDIDO));
        this.fecha = cPedido.getString(cPedido.getColumnIndex(KEY_FECHA_PEDIDO));
        this.telefono = cPedido.getString(cPedido.getColumnIndex(KEY_TELEFONO_CLIENTE_PEDIDO));
        while (cProductos.moveToNext()) {
            Producto p = new Producto(cProductos);
            p.setCantidad(cProductos.getColumnIndex(KEY_CANTIDAD));
            this.listaProductos.add(p);
        }
    }

    public Pedido(Cursor cPedido) {
        this.id = cPedido.getLong(cPedido.getColumnIndex(KEY_ROWID));
        this.cliente = cPedido.getString(cPedido.getColumnIndex(KEY_NOMBRE_CLIENTE_PEDIDO));
        this.fecha = cPedido.getString(cPedido.getColumnIndex(KEY_FECHA_PEDIDO));
        this.telefono = cPedido.getString(cPedido.getColumnIndex(KEY_TELEFONO_CLIENTE_PEDIDO));
    }

    public void addProductos(Cursor cProductos) {
        while (cProductos.moveToNext()) {
            Producto p = new Producto(cProductos);
            p.setCantidad(cProductos.getColumnIndex(KEY_CANTIDAD));
            this.listaProductos.add(new Producto(cProductos));
        }
    }

    public Producto getProducto(long id) {
        for(Iterator<Producto> it = listaProductos.iterator(); it.hasNext();) {
            Producto p = it.next();
            if (p.getIdProducto() == id) {
                return p;
            }
        }
        return null;
    }

    public void deleteProducto(long id) {
        for(Iterator<Producto> it = listaProductos.iterator(); it.hasNext();) {
            Producto p = it.next();
            if (p.getIdProducto() == id) {
                listaProductos.remove(p);
            }
        }
    }

    public double getPesoTotal() {
        double peso = 0.0;
        for(Iterator<Producto> it = listaProductos.iterator(); it.hasNext();) {
            Producto p = it.next();
            peso = peso + p.getPeso() * p.getCantidad();
        }
        return peso;
    }
    public double getPrecioTotal() {
        double precio = 0.0;
        for(Iterator<Producto> it = listaProductos.iterator(); it.hasNext();) {
            Producto p = it.next();
            precio = precio + p.getPrecio() * p.getCantidad();
        }
        return precio;
    }

    public String getNombreCliente() {
        return this.cliente;
    }
    public String getFecha() {
        return this.fecha;
    }

    public String getTelefono() {
        return telefono;
    }

    public long getId() {
        return id;
    }
}
