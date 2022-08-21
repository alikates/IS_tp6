package es.unizar.eina.appPedidos;

import java.util.ArrayList;
import java.util.Iterator;

public class Pedido {
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public Pedido(String cliente, String fecha, String telefono) {

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
}
