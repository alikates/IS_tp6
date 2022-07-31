package es.unizar.eina.appPedidos;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Loader;

public class ProductoEdit extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mNameText;
    private EditText mDescText;
    private EditText mPriceText;
    private EditText mWeightText;
    private EditText mIdText;
    private Long mRowId;
    private AppPedidosDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_producto);
        setTitle(R.string.editar_producto);

        mDbHelper = new AppPedidosDbAdapter(this);
        mDbHelper.open();

        mNameText = (EditText) findViewById(R.id.nombre);
        mDescText = (EditText) findViewById(R.id.descripcion);
        mPriceText = (EditText) findViewById(R.id.precio);
        mWeightText = (EditText) findViewById(R.id.peso);

        Button confirmButton = (Button) findViewById(R.id.confirm);

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

        confirmButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }

        });
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
        String name = mNameText.getText().toString();
        String desc = mDescText.getText().toString();
        double precio = Double.valueOf(mPriceText.getText().toString());
        double peso = Double.valueOf(mWeightText.getText().toString());

        if(mRowId == null) {
            long id = mDbHelper.createProducto(name, desc, precio, peso);
            if(id > 0) {
                mRowId = id;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mDbHelper.fetchProducto(mRowId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mNameText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_NOM_PROD)));
        mDescText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_DESC_PROD)));
        mPriceText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_PRECIO_PROD)));
        mWeightText.setText(data.getString(data.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_PESO_PROD)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
