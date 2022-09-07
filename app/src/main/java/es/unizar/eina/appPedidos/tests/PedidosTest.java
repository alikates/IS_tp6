package es.unizar.eina.appPedidos.tests;

import android.content.Context;

import java.util.ArrayList;

import es.unizar.eina.appPedidos.db.AppPedidosDbAdapter;
import es.unizar.eina.appPedidos.Producto;
import android.util.Log;

public class PedidosTest {

    private AppPedidosDbAdapter mDbHelper;

    public PedidosTest(Context parent) {
        mDbHelper = new AppPedidosDbAdapter(parent);
        mDbHelper.open();
    }

    static String getRandomString(int i)
    {
        String theAlphaNumericS;
        StringBuilder builder;

        theAlphaNumericS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";

        //create the StringBuffer
        builder = new StringBuilder(i);

        for (int m = 0; m < i; m++) {

            // generate numeric
            int myindex
                    = (int)(theAlphaNumericS.length()
                    * Math.random());

            // add the characters
            builder.append(theAlphaNumericS
                    .charAt(myindex));
        }

        return builder.toString();
    }

    public void probarPedidos()
    {
        String nombreCliente;
        String numTlf = "123456789";
        String fecha = "1/2/2022";
        Long ids[];
        ids = new Long[100];
        ArrayList<Producto> listaProductos = new ArrayList<>();
        /**
         * getCantidadProductoPedido
         * createProductoPedido
         * updateProductoPedido
         * deleteProductoPedido
         */
        for (int i = 0; i<100; i++) {
            nombreCliente = getRandomString(5);
            ids[i] = mDbHelper.createPedido(fecha, nombreCliente, numTlf);
            if (ids[i] != null) {
                Log.d("200", "Cliente numero " + i + " creado correctamente");
            }
        }
        for (int i = 0; i<100; i++) {
            nombreCliente = getRandomString(5);
            if (mDbHelper.updatePedido(ids[i],fecha, nombreCliente, numTlf)) {
                Log.d("200", "Pedido numero " + i + " actualizado correctamente");
            }
        }
        for (int i = 0; i<100; i++) {
            if (mDbHelper.deletePedido(ids[i])) {
                Log.d("200", "Pedido numero " + i + " eliminado correctamente");
            }
        }
         /**
         * createProducto
         * deleteProducto
         * updateProducto
         */

    }
}

