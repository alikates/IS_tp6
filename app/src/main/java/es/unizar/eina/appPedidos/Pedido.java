package es.unizar.eina.appPedidos;

import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_CANTIDAD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_FECHA_PEDIDO;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_NOM_PROD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_PRODUCTO;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_TELEFONO_CLIENTE_PEDIDO;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;

public class Pedido {
    private String cliente, fecha, telefono;
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public Pedido(String cliente, String fecha, String telefono) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.telefono = telefono;
    }

    public Pedido(Cursor cPedido, Cursor cProductos) {
        this.cliente = cPedido.getString(cPedido.getColumnIndex(KEY_NOM_PROD));
        this.fecha = cPedido.getString(cPedido.getColumnIndex(KEY_FECHA_PEDIDO));
        this.telefono = cPedido.getString(cPedido.getColumnIndex(KEY_TELEFONO_CLIENTE_PEDIDO));
        while (cProductos.moveToNext()) {
            this.listaProductos.add(new Producto(cProductos));
        }
    }

    public void addProducto(Producto p) {
        listaProductos.add(p);
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
            peso = peso + p.getPeso();
        }
        return peso;
    }
    public double getPrecioTotal() {
        double precio = 0.0;
        for(Iterator<Producto> it = listaProductos.iterator(); it.hasNext();) {
            Producto p = it.next();
            precio = precio + p.getPrecio();
        }
        return precio;
    }

    public String getNombreCliente() {
        return this.cliente;
    }
    public String getFecha() {
        return this.fecha;
    }
}
