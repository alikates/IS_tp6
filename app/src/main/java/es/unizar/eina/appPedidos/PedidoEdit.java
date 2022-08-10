package es.unizar.eina.appPedidos;

import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_CANTIDAD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_NOM_PROD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_PESO_PROD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_PRECIO_PROD;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_PRODUCTO;
import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_ROWID;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class PedidoEdit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mDateText;
    private EditText mClientText;
    private EditText mTelfText;
    private Long mRowId;
    private ListView mList;
    private AppPedidosDbAdapter mDbHelper;

    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public long getId() {
        if (mRowId == null) {
            return -1;
        }
        return mRowId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_pedido);
        setTitle(R.string.editar_pedido);

        mDbHelper = new AppPedidosDbAdapter(this);
        mDbHelper.open();

        mDateText = findViewById(R.id.fecha);
        mClientText = findViewById(R.id.nombre);
        mTelfText = findViewById(R.id.telefono);
        Button confirmButton = findViewById(R.id.confirm);
        mList = (ListView)findViewById(R.id.list);

        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(KEY_ROWID);

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mRowId = extras.getLong(KEY_ROWID);
                getLoaderManager().initLoader(0, null, this);
            } else {
                mRowId = null;
            }
        }

        updateProductListView();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveState();
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    protected void updateProductListView () {
        Cursor mProductsCursor;
        Cursor mProductosPedidosCursor;
        ItemProducto productos;
        String[] from;
        int[] to;
        int layoutId;
        mProductsCursor = mDbHelper.fetchAllProductos(0);


        while (mProductsCursor.moveToNext()) {
            listaProductos.add(new Producto(mProductsCursor.getInt(mProductsCursor.getColumnIndex(KEY_ROWID)), 0, mProductsCursor.getString(mProductsCursor.getColumnIndex(KEY_NOM_PROD)),mProductsCursor.getDouble(mProductsCursor.getColumnIndex(KEY_PESO_PROD)),mProductsCursor.getDouble(mProductsCursor.getColumnIndex(KEY_PRECIO_PROD))));
        }

        if (mRowId != null) {
            mProductosPedidosCursor = mDbHelper.fetchProductosPedidos(mRowId);

            while (mProductosPedidosCursor.moveToNext()) {
                Iterator<Producto> itProductos = listaProductos.iterator();
                while(itProductos.hasNext()){
                    Producto p = itProductos.next();
                    if (p.getIdProducto() == mProductosPedidosCursor.getInt(mProductosPedidosCursor.getColumnIndex(KEY_PRODUCTO))) {
                        p.setCantidad(mProductosPedidosCursor.getInt(mProductosPedidosCursor.getColumnIndex(KEY_CANTIDAD)));
                    }
                }
            }
        }

        layoutId = R.layout.item_producto_con_botones;
        productos = new ItemProducto(listaProductos, this);
        mList.setAdapter(productos);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String fecha = mDateText.getText().toString();
        String nomCliente = mClientText.getText().toString();
        String tlfCliente = mTelfText.getText().toString();

        if(mRowId == null){
            if (!fecha.equals("") && !nomCliente.equals("") && !tlfCliente.equals("")) {
                Log.d("AppPedidos", fecha + nomCliente + tlfCliente);
                long id = mDbHelper.createPedido(fecha, nomCliente, tlfCliente);
                if(id > 0) {
                    mRowId = id;
                    saveProductos();
                }
            }
        } else {
            mDbHelper.updatePedido(mRowId, fecha, nomCliente, tlfCliente);
            saveProductos();
        }
    }

    private void saveProductos() {
        ItemProducto productoAdapter = (ItemProducto) mList.getAdapter();
        Iterator<Producto> productos = productoAdapter.getProductosActualizados().iterator();
        while(productos.hasNext()){
            Producto p = productos.next();
            if (p.getCantidad() <= 0) {
                mDbHelper.deleteProductoPedido(p.getIdProducto(), mRowId);
            } else {
                if(!mDbHelper.updateProductoPedido(p.getIdProducto(), mRowId, p.getCantidad())) {
                    mDbHelper.addProductoPedido(p.getIdProducto(), mRowId, p.getCantidad());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDbHelper.fetchPedido(mRowId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mDateText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_FECHA_PEDIDO)));
        mClientText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_NOMBRE_CLIENTE_PEDIDO)));
        mTelfText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_TELEFONO_CLIENTE_PEDIDO)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}
