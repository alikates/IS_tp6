package es.unizar.eina.appPedidos;

import static es.unizar.eina.appPedidos.AppPedidosDbAdapter.KEY_PRODUCTO;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import es.unizar.eina.send.SendAbstractionImpl;
import es.unizar.eina.send.WAImplementor;

public class AppPedidos extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_SEND = 2;

    private static final int LISTAR_PRODUCTOS_ID = Menu.FIRST;
    private static final int CREAR_PRODUCTO_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int EDIT_ID = Menu.FIRST + 3;
    private static final int SEND_ID = Menu.FIRST + 4;

    private static final int ORDER_0 = 0;
    private static final int ORDER_1 = 1;
    private static final int ORDER_2 = 2;

    private AppPedidosDbAdapter mDbHelper;
    private ListView mList;
    private TabLayout mTabBar;
    private int selectedTab = 0;    //Default pedidos
    private int order_by = ORDER_0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new AppPedidosDbAdapter(this);
        mDbHelper.open();
        mList = findViewById(R.id.lista);

        mTabBar = findViewById(R.id.tabs);
        mTabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                updateListView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                updateListView();
            }
        });
        updateListView();

        registerForContextMenu(mList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        boolean result = super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, LISTAR_PRODUCTOS_ID, Menu.NONE, R.string.menu_crear_pedido);
        menu.add(Menu.NONE, CREAR_PRODUCTO_ID, Menu.NONE, R.string.menu_crear_producto);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order_by, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case LISTAR_PRODUCTOS_ID:
                crearPedido();
                return true;
            case CREAR_PRODUCTO_ID:
                crearProducto();
                return true;
            case R.id.action_order_by:
                PopupMenu menu = new PopupMenu(AppPedidos.this, findViewById(R.id.action_order_by));

                switch (selectedTab) {
                    case 0:
                        menu.getMenu().add(1, 0, Menu.NONE, "Nombre");
                        menu.getMenu().add(1, 1, Menu.NONE, "Telefono");
                        menu.getMenu().add(1, 2, Menu.NONE, "Fecha");
                        break;
                    case 1:
                    default:
                        menu.getMenu().add(1, 0, Menu.NONE, "Nombre");
                        menu.getMenu().add(1, 1, Menu.NONE, "Precio");
                        menu.getMenu().add(1, 2, Menu.NONE, "Peso");
                }

                menu.getMenu().getItem(order_by).setChecked(true);
                menu.getMenu().setGroupCheckable(1, true, true);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        order_by = menuItem.getItemId();
                        menuItem.setChecked(true);
                        updateListView();
                        return false;
                    }
                });

                menu.show();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        switch (selectedTab) {
            case 0:
                menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_eliminar_pedido);
                menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_editar_pedido);
                menu.add(Menu.NONE, SEND_ID, Menu.NONE, R.string.menu_enviar_pedido);
                break;
            case 1:
                menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_eliminar_producto);
                menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_editar_producto);
            default:
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (selectedTab) {
            case 0:
                switch (item.getItemId()) {
                    case DELETE_ID:
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        mDbHelper.deletePedido(info.id);
                        break;
                    case EDIT_ID:
                        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        editarPedido(info.position, info.id);
                        break;
                    case SEND_ID:
                        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        enviarPedido(info.position, info.id);
                }
                break;
            case 1:
            default:
                switch (item.getItemId()) {
                    case DELETE_ID:
                        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        mDbHelper.deleteProducto(info.id);
                        break;
                    case EDIT_ID:
                        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        editarProducto(info.position, info.id);
                        break;
                }
                break;
        }
        updateListView();
        return super.onContextItemSelected(item);
    }

    private void crearProducto() {
        Intent i = new Intent(this, ProductoEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    protected void editarProducto(int position, long id) {
        Intent i = new Intent(this, ProductoEdit.class);
        i.putExtra(AppPedidosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    private void crearPedido() {
        Intent i = new Intent(this, PedidoEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    protected void editarPedido(int position, long id) {
        Intent i = new Intent(this, PedidoEdit.class);
        i.putExtra(AppPedidosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    protected void enviarPedido(int position, long id) {
        Cursor mProductosPedidosCursor = mDbHelper.fetchProductosPedidos(id);

        while (mProductosPedidosCursor.moveToNext()) {
            mProductosPedidosCursor.getInt(mProductosPedidosCursor.getColumnIndex(KEY_PRODUCTO));
        }

        SendAbstractionImpl sender = new SendAbstractionImpl(this, "WA");
        sender.send("asdasd");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateListView();
    }

    protected void updateListView() {
        Cursor mCursor;
        switch (selectedTab) {
            case 0:
                ArrayList<Producto> pedidoList = new ArrayList<>();
                mCursor = mDbHelper.fetchAllPedidos(order_by);
                while(mCursor.moveToNext()) {
                    pedidoList.add(new Producto(mCursor));
                }
                ArrayAdapter<Producto> pedidos = new ArrayAdapter<Producto>(this, R.layout.item_pedido, pedidoList);
                mList.setAdapter(pedidos);
                break;
            case 1:
            default:
                ArrayList<Producto> prodList = new ArrayList<>();
                mCursor = mDbHelper.fetchAllProductos(order_by);
                while(mCursor.moveToNext()) {
                    prodList.add(new Producto(mCursor));
                }
                ArrayAdapter<Producto> prods = new ArrayAdapter<Producto>(this, R.layout.item_producto, prodList);
                mList.setAdapter(prods);
                break;
        }
    }
}
