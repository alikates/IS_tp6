package es.unizar.eina.appPedidos;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;


public class PedidoEdit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mDateText;
    private EditText mClientText;
    private EditText mTelfText;
    private Long mRowId;
    private ListView mList;
    private AppPedidosDbAdapter mDbHelper;

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
                (Long) savedInstanceState.getSerializable(AppPedidosDbAdapter.KEY_ROWID);

        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mRowId = extras.getLong(AppPedidosDbAdapter.KEY_ROWID);
                getLoaderManager().initLoader(0, null, this);
            } else {
                mRowId = null;
            }
        }

        updateProductListView();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
    }

    protected void updateProductListView () {
        Cursor mProductsCursor;
        SimpleCursorAdapter notes;
        String[] from;
        int[] to;
        int layoutId;
        mProductsCursor = mDbHelper.fetchProductosPedidos() ;
        from = new String[] {
                AppPedidosDbAdapter.KEY_NOM_PROD,
                AppPedidosDbAdapter.KEY_CANTIDAD
        };
        to = new int[] {
                R.id.nombre,
                R.id.cantidad
        };
        layoutId = R.layout.item_producto_con_botones;
        notes = new SimpleCursorAdapter(this, layoutId, mProductsCursor, from, to, 0);
        mList.setAdapter(notes);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(AppPedidosDbAdapter.KEY_ROWID, mRowId);
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
                }
            }
        } else {
            mDbHelper.updatePedido(mRowId, fecha, nomCliente, tlfCliente);
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
