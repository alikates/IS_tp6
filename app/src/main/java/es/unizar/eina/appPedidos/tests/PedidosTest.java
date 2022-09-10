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
        String nombre;
        String numTlf = "123456789";
        String fecha = "1/2/2022";
        Long ids[];
        Long id;
        boolean result;
        ids = new Long[1050];
        ArrayList<Producto> listaProductos = new ArrayList<>();

        Log.d("200","Create producto");

        id = mDbHelper.createProducto("Pedido1", "Des_1", 3, 2.5);
        Log.d("200","Clases de prueba 1,2,3,4, resultado: " + id);

        id = mDbHelper.createProducto("", "Des_2", 3, 2.5);
        Log.d("401","Clases de prueba 5, resultado: " + id);

        id = mDbHelper.createProducto("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqr", "Des_3", 3, 2.5);
        Log.d("402","Clases de prueba 6, resultado: " + id);

        id = mDbHelper.createProducto("Producto2", "Des_4", -1, 2.5);
        Log.d("403","Clases de prueba 7, resultado: " + id);

        id = mDbHelper.createProducto("Producto3", "Des_5", 1, -2.5);
        Log.d("403","Clases de prueba 8, resultado: " + id);

        id = mDbHelper.createProducto("Producto2", "", 1, 2.5);
        Log.d("404","Clases de prueba 9, resultado: " + id);

        Log.d("200","Update producto");

        /** ************************************************************************************************************/
        Log.d("200","Update producto");
        result = mDbHelper.updateProducto(1, "Pedresulto1", "Des_1", 3, 2.5);
        Log.d("200","Clases de prueba 1,2,3,4,5 resultado: " + result);

        result = mDbHelper.updateProducto(0, "Producto2", "", 1, 2.5);
        Log.d("405","Clases de prueba 6, resultado: " + result);

        result = mDbHelper.updateProducto(1, "", "Des_2", 3, 2.5);
        Log.d("401","Clases de prueba 7, resultado: " + result);

        result = mDbHelper.updateProducto(1, "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqr", "Des_3", 3, 2.5);
        Log.d("402","Clases de prueba 8, resultado: " + result);

        result = mDbHelper.updateProducto(1, "Producto2", "Des_4", -1, 2.5);
        Log.d("403","Clases de prueba 9, resultado: " + result);

        result = mDbHelper.updateProducto(1, "Producto3", "Des_5", 1, -2.5);
        Log.d("403","Clases de prueba 10, resultado: " + result);

        result = mDbHelper.updateProducto(1, "Producto2", "", 1, 2.5);
        Log.d("404","Clases de prueba 11, resultado: " + result);

        /** ************************************************************************************************************/
        Log.d("200","Eliminar producto producto");
        result = mDbHelper.deleteProducto(1);
        Log.d("200","Clases de prueba 1 resultado: " + result);

        result = mDbHelper.deleteProducto(0);
        Log.d("400","Clases de prueba 2 resultado: " + result);

        //Prueba de volumen
        for (int i = 0; i<1000; i++) {
            nombre= getRandomString(5);
            ids[i] = mDbHelper.createProducto(nombre, nombre, 1, 1);
            if (ids[i] != null) {
                Log.d("200", "Producto numero " + i + " creado correctamente");
            }
        }
        nombre= getRandomString(5);
        id = mDbHelper.createProducto(nombre, nombre, 1, 1);
        if (id != null) {
            Log.d("200", "Producto numero " + 1001 + " creado correctamente");
        } else {
            Log.d("400", "No se pudo crear el producto 1001");
        }
        for (int i = 0; i<202; i++) {
            nombre= getRandomString(5);
            ids[i] = mDbHelper.createPedido(nombre, fecha, numTlf);
            if (ids[i] != null) {
                Log.d("200", "Pedido numero " + i + " creado correctamente");
            }
        }
         /**
         * createProducto
         * deleteProducto
         * updateProducto
         */

    }
}

