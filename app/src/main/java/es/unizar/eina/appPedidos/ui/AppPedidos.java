package es.unizar.eina.appPedidos.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

import es.unizar.eina.appPedidos.db.AppPedidosDbAdapter;
import es.unizar.eina.appPedidos.Pedido;
import es.unizar.eina.appPedidos.R;
import es.unizar.eina.appPedidos.send.SendAbstractionImpl;
import es.unizar.eina.appPedidos.tests.PedidosTest;

public class AppPedidos extends AppCompatActivity {

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int ACTIVITY_SEND = 2;

    private static final int LISTAR_PRODUCTOS_ID = Menu.FIRST;
    private static final int CREAR_PRODUCTO_ID = Menu.FIRST + 1;
    private static final int TESTEAR_ID = Menu.FIRST + 2;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int EDIT_ID = Menu.FIRST + 3;
    private static final int SEND_ID = Menu.FIRST + 4;

    private static final int ORDER_0 = 0;
    private static final int ORDER_1 = 1;
    private static final int ORDER_2 = 2;

    private AppPedidosDbAdapter mDbHelper;
    private ListView mList;
    private TabLayout mTabBar;
    private PedidosTest test;
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
        menu.add(Menu.NONE, TESTEAR_ID, Menu.NONE, "Testear");

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
            case TESTEAR_ID:
                test = new PedidosTest(this);
                test.probarPedidos();
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
        updateListView();
    }

    protected void editarPedido(int position, long id) {
        Intent i = new Intent(this, PedidoEdit.class);
        i.putExtra(AppPedidosDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
        updateListView();
    }

    protected void enviarPedido(int position, long id) {
        Pedido miPedido = new Pedido(mDbHelper.fetchPedido(id), mDbHelper.fetchProductosPedidos(id));
        //Producto misProductos = new Producto((mDbHelper.fetchProductosPedidos(id)));
        String nomCliente = miPedido.getNombreCliente();
        String fecha = miPedido.getFecha();
        Double precio = miPedido.getPrecioTotal();
        Double peso = miPedido.getPesoTotal();
        String mensaje = "Buenos dias " + nomCliente + " su pedido estará listo para el día " + fecha +
                " con un precio total de " + precio + " euros, y un peso de " + peso + " Kg";

        SendAbstractionImpl sender = new SendAbstractionImpl(this, "WA");
        sender.send(mensaje);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        updateListView();
    }

    protected void updateListView() {
        Cursor mCursor;
        String[] from;
        int[] to;
        int layoutId;
        switch (selectedTab) {
            case 0:
                mCursor = mDbHelper.fetchAllPedidos(order_by);
                from = new String[]{
                        AppPedidosDbAdapter.KEY_NOMBRE_CLIENTE_PEDIDO,
                        AppPedidosDbAdapter.KEY_TELEFONO_CLIENTE_PEDIDO,
                        AppPedidosDbAdapter.KEY_FECHA_PEDIDO
                };
                to = new int[]{
                        R.id.nombre,
                        R.id.telefono,
                        R.id.fecha
                };
                layoutId = R.layout.item_pedido;
                ListaPedidos adapter = new ListaPedidos(this, layoutId, mCursor, from ,to, 0, mDbHelper);
                mList.setAdapter(adapter);
                break;
            case 1:
            default:
                mCursor = mDbHelper.fetchAllProductos(order_by);
                from = new String[]{
                        AppPedidosDbAdapter.KEY_NOM_PROD,
                        AppPedidosDbAdapter.KEY_PRECIO_PROD,
                        AppPedidosDbAdapter.KEY_PESO_PROD
                };
                to = new int[]{
                        R.id.nombre,
                        R.id.precio,
                        R.id.peso
                };
                layoutId = R.layout.item_producto;
                SimpleCursorAdapter adapter2 = new SimpleCursorAdapter(this, layoutId, mCursor, from ,to, 0);
                mList.setAdapter(adapter2);
                break;
        }
    }
}
