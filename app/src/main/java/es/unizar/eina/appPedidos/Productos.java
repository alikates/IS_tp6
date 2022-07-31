package es.unizar.eina.appPedidos;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class Productos extends AppCompatActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_SEND=2;

    private static final int CREAR_PEDIDO_ID = Menu.FIRST;
    private static final int CREAR_PRODUCTO_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int EDIT_ID = Menu.FIRST + 3;
    private static final int SEND_ID = Menu.FIRST + 4;

    private AppPedidosDbAdapter mDbHelper;
    private ListView mList;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_productos);

        mDbHelper = new AppPedidosDbAdapter(this);
        mDbHelper.open();
        mList = (ListView)findViewById(R.id.list);
        fillDataProductos();

        registerForContextMenu(mList);

    }

    private void fillDataProductos() {
//        // Get all of the notes from the database and create the item list
//        Cursor mNotesCursor = mDbHelper.fetchAllProductos(0);
//
//        // Create an array to specify the fields we want to display in the list (only TITLE)
//        String[] from = new String[] { AppPedidosDbAdapter.KEY_NOM_PROD };
//        // and an array of the fields we want to bind those fields to (in this case just text1)
//        int[] to = new int[] { R.id.text1 };
//
//        // Now create an array adapter and set it to display using our row
//        SimpleCursorAdapter notes =
//                new SimpleCursorAdapter(this, R.layout.item_lista, mNotesCursor, from, to, 0);
//        mList.setAdapter(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, CREAR_PEDIDO_ID, Menu.NONE, R.string.menu_crear_pedido);
        menu.add(Menu.NONE, CREAR_PRODUCTO_ID, Menu.NONE, R.string.menu_crear_producto);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CREAR_PEDIDO_ID:

            case CREAR_PRODUCTO_ID:
                createNote();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete);
//        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit);
//        menu.add(Menu.NONE, SEND_ID, Menu.NONE, R.string.menu_send);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//            case DELETE_ID:
//                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                mDbHelper.deleteNote(info.id);
//                fillData();
//                return true;
//            case EDIT_ID:
//                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                editNote(info.position, info.id);
//                return true;
//            case SEND_ID:
//                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//                sendNote(info.position, info.id);
//                return true;
//        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, PedidoEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }


    protected void editNote(int position, long id) {
        Intent i = new Intent(this, PedidoEdit.class);
        i.putExtra(AppPedidosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

//    protected void sendNote(int position, long id) {
//        SendAbstractionImpl sender = new SendAbstractionImpl(this, "SMS");
//        Cursor note = mDbHelper.fetchProducto(id);
//        sender.send(note.getString(note.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_TITLE)),
//                note.getString(note.getColumnIndexOrThrow(AppPedidosDbAdapter.KEY_BODY)));
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillDataProductos();
    }
}
