package es.unizar.eina.appPedidos;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 *
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class AppPedidosDbAdapter {

    public static final String KEY_FECHA_PEDIDO = "fecha";
    public static final String KEY_NOMBRE_CLIENTE_PEDIDO = "nomb_cliente";
    public static final String KEY_TELEFONO_CLIENTE_PEDIDO = "telf_cliente";

    public static final String KEY_NOM_PROD = "nombre";
    public static final String KEY_DESC_PROD = "descripcion";
    public static final String KEY_PRECIO_PROD = "precio";
    public static final String KEY_PESO_PROD = "peso";

    public static final String KEY_CANTIDAD = "cantidad";

    public static final String KEY_ROWID = "_id";

    public static final String KEY_PRODUCTO = "producto";
    public static final String KEY_PEDIDO = "pedido";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String PRODUCTOS_TABLE = "productos";
    private static final String PEDIDOS_TABLE = "pedidos";
    private static final String PRODUCTOS_PEDIDOS_TABLE = "productos_pedidos";


    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public AppPedidosDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public AppPedidosDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public int getCantidadProductoPedido(long idProducto, long idPedido) {
        Cursor c = mDb.query(PRODUCTOS_PEDIDOS_TABLE, new String[]{"cantidad"}, KEY_PRODUCTO + "=" + idProducto + " AND " + KEY_PEDIDO + "=" + idPedido, null, null, null, null);
        if (c.getCount() < 1) {
            return 0;
        }
        return c.getInt(0);
    }

    public long createProductoPedido(long idProducto, long idPedido, int cantidad) {
        if (cantidad > 0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put("producto", idProducto);
            initialValues.put("pedido", idPedido);
            initialValues.put("cantidad", cantidad);
            return mDb.insert(PRODUCTOS_PEDIDOS_TABLE, null, initialValues);
        }
        return mDb.insert(PRODUCTOS_PEDIDOS_TABLE, null, null);
    }

    public boolean updateProductoPedido(long idProducto, long idPedido, int cantidad) {
        if (cantidad > 0) {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("cantidad", cantidad);
            return mDb.update(PRODUCTOS_PEDIDOS_TABLE, updatedValues, KEY_PRODUCTO + "=" + idProducto + " AND " + KEY_PEDIDO + "=" + idPedido, null) > 0;
        }
        return false;
    }

    public boolean deleteProductoPedido(long idProducto, long idPedido) {
        return mDb.delete(PRODUCTOS_PEDIDOS_TABLE, KEY_PRODUCTO + "=" + idProducto + " AND " + KEY_PEDIDO + "=" + idPedido, null) > 0;
    }


    public Cursor fetchProductosPedidos(long idPedido) {
        String sql = "SELECT * FROM productos" +
                " JOIN productos_pedidos ON producto = _id WHERE pedido = " + idPedido;
        return mDb.rawQuery(sql, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchPedido(long rowId) throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx.getApplicationContext());
        mDb = mDbHelper.getReadableDatabase();
        // You can use any query that returns a cursor.
        return mDb.query(PEDIDOS_TABLE, new String[] {KEY_ROWID,
                        KEY_NOMBRE_CLIENTE_PEDIDO, KEY_FECHA_PEDIDO, KEY_TELEFONO_CLIENTE_PEDIDO}, KEY_ROWID + "=" + rowId, null,
                null, null, KEY_NOMBRE_CLIENTE_PEDIDO+" DESC", null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Loader<Cursor> fetchPedidoLoader(long rowId) throws SQLException {
        return new CursorLoader(mCtx.getApplicationContext(), null, new String[] {KEY_ROWID,
                KEY_NOMBRE_CLIENTE_PEDIDO, KEY_FECHA_PEDIDO, KEY_TELEFONO_CLIENTE_PEDIDO},
                KEY_ROWID + "=" + rowId, null, KEY_NOMBRE_CLIENTE_PEDIDO+" DESC")
        {
            @Override
            public Cursor loadInBackground()
            {
                DatabaseHelper dbHelper  = new DatabaseHelper(getContext());
                SQLiteDatabase mDb = dbHelper.getReadableDatabase();
                // You can use any query that returns a cursor.
                return mDb.query(PEDIDOS_TABLE, getProjection(), getSelection(), getSelectionArgs(),
                        null, null, getSortOrder(), null);
            }
        };
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllPedidos(int orderBy) {
        String order = "";
        switch (orderBy) {
            case 0:
                order = KEY_NOMBRE_CLIENTE_PEDIDO + " ASC";
                break;
            case 1:
                order = KEY_TELEFONO_CLIENTE_PEDIDO + " ASC";
                break;
            case 2:
            default:
                order = KEY_FECHA_PEDIDO + " ASC";
        }
        return mDb.query(PEDIDOS_TABLE, new String[]{KEY_ROWID, KEY_FECHA_PEDIDO,
                KEY_NOMBRE_CLIENTE_PEDIDO, KEY_TELEFONO_CLIENTE_PEDIDO}, null, null, null, null, order);
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param fecha the title of the note
     * @param nom_cliente the body of the note
     * @return rowId or -1 if failed
     */
    public long createPedido(String fecha, String nom_cliente, String telf_cliente) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_FECHA_PEDIDO, fecha);
        initialValues.put(KEY_NOMBRE_CLIENTE_PEDIDO, nom_cliente);
        initialValues.put(KEY_TELEFONO_CLIENTE_PEDIDO, telf_cliente);

        return mDb.insert(PEDIDOS_TABLE, null, initialValues);
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId
     * @param fecha
     * @param cliente
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updatePedido(long rowId, String fecha, String cliente, String telefono) {
        ContentValues args = new ContentValues();
        args.put(KEY_FECHA_PEDIDO, fecha);
        args.put(KEY_NOMBRE_CLIENTE_PEDIDO, cliente);
        args.put(KEY_TELEFONO_CLIENTE_PEDIDO, telefono);

        return mDb.update(PEDIDOS_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deletePedido(long rowId) {
        return mDb.delete(PEDIDOS_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Loader<Cursor> fetchProducto(long rowId) throws SQLException {
        return new CursorLoader(mCtx.getApplicationContext(), null, new String[] {KEY_ROWID, KEY_NOM_PROD,
                KEY_PRECIO_PROD, KEY_PESO_PROD, KEY_DESC_PROD}, KEY_ROWID + "=" + rowId, null, KEY_NOM_PROD+" DESC" )
        {
            @Override
            public Cursor loadInBackground()
            {
                DatabaseHelper mDbHelper = new DatabaseHelper(getContext());
                SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
                // You can use any query that returns a cursor.
                return mDb.query(PRODUCTOS_TABLE, getProjection(), getSelection(), getSelectionArgs(),
                        null, null, getSortOrder(), null);
            }
        };
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all notes
     */
    public Cursor fetchAllProductos(int orderBy) {
        String order = "";
        switch (orderBy) {
            case 0:
                order = KEY_NOM_PROD + " ASC";
                break;
            case 1:
                order = KEY_PRECIO_PROD + " ASC";
                break;
            case 2:
            default:
                order = KEY_PESO_PROD + " ASC";
        }
        return mDb.query(PRODUCTOS_TABLE, new String[]{KEY_ROWID, KEY_NOM_PROD,
                KEY_PRECIO_PROD, KEY_PESO_PROD}, null, null, null, null, order);
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param nombre the title of the note
     * @param desc the body of the note
     * @return rowId or -1 if failed
     */
    public long createProducto(String nombre, String desc, double precio, double peso) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM_PROD, nombre);
        initialValues.put(KEY_DESC_PROD, desc);
        initialValues.put(KEY_PRECIO_PROD, precio);
        initialValues.put(KEY_PESO_PROD, peso);

        return mDb.insert(PRODUCTOS_TABLE, null, initialValues);
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of note to update
     * @param nombre value to set note title to
     * @param desc value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateProducto(long rowId, String nombre, String desc, double precio, double peso) {
        ContentValues args = new ContentValues();
        args.put(KEY_NOM_PROD, nombre);
        args.put(KEY_DESC_PROD, desc);
        args.put(KEY_PRECIO_PROD, precio);
        args.put(KEY_PESO_PROD, peso);

        return mDb.update(PRODUCTOS_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Delete the note with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteProducto(long rowId) {
        return mDb.delete(PRODUCTOS_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
}