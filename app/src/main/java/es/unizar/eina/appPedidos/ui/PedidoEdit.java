package es.unizar.eina.appPedidos.ui;

import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_CANTIDAD;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_PRODUCTO;
import static es.unizar.eina.appPedidos.db.AppPedidosDbAdapter.KEY_ROWID;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

import es.unizar.eina.appPedidos.db.AppPedidosDbAdapter;
import es.unizar.eina.appPedidos.Producto;
import es.unizar.eina.appPedidos.R;


public class PedidoEdit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    private EditText mDateText;
    private EditText mClientText;
    private EditText mTelfText;
    private Long mRowId;
    private ListView mList;
    private EditText mDatePicker;
    private AppPedidosDbAdapter mDbHelper;

    private ArrayList<Producto> listaProductos = new ArrayList<>();

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fecha) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
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

        mDatePicker = findViewById(R.id.fecha);
        mDatePicker.setOnClickListener(this);

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
        Cursor mProductsCursor, mProductosPedidosCursor;
        ItemProductoAdapter productos;

        mProductsCursor = mDbHelper.fetchAllProductos(0);

        while (mProductsCursor.moveToNext()) {
            listaProductos.add(new Producto(mProductsCursor));
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

        productos = new ItemProductoAdapter(listaProductos, this);
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
                if(id >= 0) {
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
        ItemProductoAdapter productoAdapter = (ItemProductoAdapter) mList.getAdapter();
        Iterator<Producto> productos = productoAdapter.getProductosActualizados().iterator();
        while(productos.hasNext()){
            Producto p = productos.next();
            if (p.getCantidad() <= 0) {
                mDbHelper.deleteProductoPedido(p.getIdProducto(), mRowId);
            } else {
                if(!mDbHelper.updateProductoPedido(p.getIdProducto(), mRowId, p.getCantidad())) {
                    mDbHelper.createProductoPedido(p.getIdProducto(), mRowId, p.getCantidad());
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
        return mDbHelper.fetchPedidoLoader(mRowId);
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
