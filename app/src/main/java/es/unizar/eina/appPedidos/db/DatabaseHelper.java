package es.unizar.eina.appPedidos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private static final String TAG = "DatabaseHelper";

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_PRODUCTOS =
            "create table productos (_id integer primary key autoincrement,"
                    + "nombre text not null, descripcion text not null, " +
                    "precio text not null, peso text not null);";
    private static final String DATABASE_CREATE_PEDIDOS =
            "create table pedidos (_id integer primary key autoincrement,"
                    + "fecha text not null, nomb_cliente text not null, telf_cliente text not null);";
    private static final String DATABASE_CREATE_PRODUCTOS_PEDIDOS =
            "create table productos_pedidos (producto integer not null, pedido integer not null," +
                    "cantidad integer default 0," +
                    "CONSTRAINT clave_primaria PRIMARY KEY (producto, pedido)," +
                    "CONSTRAINT producto FOREIGN KEY (producto) REFERENCES productos (_id) ON DELETE CASCADE," +
                    "CONSTRAINT pedido FOREIGN KEY (pedido) REFERENCES pedidos (_id) ON DELETE CASCADE" +
                    ");";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_PRODUCTOS);
        db.execSQL(DATABASE_CREATE_PEDIDOS);
        db.execSQL(DATABASE_CREATE_PRODUCTOS_PEDIDOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS productos");
        db.execSQL("DROP TABLE IF EXISTS pedidos");
        db.execSQL("DROP TABLE IF EXISTS productos_pedidos");
        onCreate(db);
    }
}