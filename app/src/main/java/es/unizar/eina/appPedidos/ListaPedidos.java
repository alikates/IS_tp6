package es.unizar.eina.appPedidos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListaPedidos extends SimpleCursorAdapter {
    private ArrayList<Pedido> listaPedidos;
    private AppPedidosDbAdapter mDbAdapter;

    private MaterialTextView mPeso, mPrecio;

    public ListaPedidos(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mDbAdapter = new AppPedidosDbAdapter(context);
        mDbAdapter.open();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        long pedidoId = cursor.getLong(cursor.getColumnIndex(AppPedidosDbAdapter.KEY_ROWID));
        Cursor cProductos = mDbAdapter.fetchProductosPedidos(pedidoId);
        Pedido p = new Pedido(cursor, cProductos);
        mPeso = view.findViewById(R.id.peso);
        mPrecio = view.findViewById(R.id.precio);
        mPeso.setText(Double.toString(p.getPesoTotal()));
        mPrecio.setText(Double.toString(p.getPrecioTotal()));
    }
}