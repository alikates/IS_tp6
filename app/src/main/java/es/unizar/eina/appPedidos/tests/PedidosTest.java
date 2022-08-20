package es.unizar.eina.appPedidos.tests;

import android.content.Context;

import es.unizar.eina.appPedidos.AppPedidosDbAdapter;

public class PedidosTest {

    private AppPedidosDbAdapter mDbHelper;

    public PedidosTest(Context parent) {
        mDbHelper = new AppPedidosDbAdapter(parent);
        mDbHelper.open();
    }

    public void probarPedidos()
    {
        /**
         * getCantidadProductoPedido
         * createProductoPedido
         * updateProductoPedido
         * deleteProductoPedido
         *
         * createPedido
         * updatePedido
         * deletePedido
         *
         * createProducto
         * deleteProducto
         * updateProducto
         */
    }
}

