package es.unizar.eina.appPedidos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemProducto extends ArrayAdapter<Producto> {
    private ArrayList<Producto> listaProductos;

    public ItemProducto(ArrayList<Producto> data, Context context) {
        super(context, R.layout.item_producto_con_botones, data);
        this.listaProductos = data;
    }

    public ArrayList<Producto> getProductosActualizados() {
        return listaProductos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Producto p = getItem(position);

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_producto_con_botones,parent,false
            );
        }
        ImageButton decrementarButton = listItemView.findViewById(R.id.decrementar);
        ImageButton incrementarButton = listItemView.findViewById(R.id.incrementar);

        TextView cant = listItemView.findViewById(R.id.cantidad);
        cant.setText(Integer.toString(p.getCantidad()));
        TextView nombre = listItemView.findViewById(R.id.nombre);
        nombre.setText(p.getNombre());

        decrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = p.getCantidad();
                if (cantidad > 0) {
                    p.setCantidad(cantidad - 1);
                    notifyDataSetChanged();
                }
            }
        });

        incrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cantidad = p.getCantidad();
                p.setCantidad(cantidad + 1);
                notifyDataSetChanged();
            }
        });
        return listItemView;
    }

    /*
    private AppPedidosDbAdapter mDbHelper;

    private List<Producto> listaProductos;

    public ItemProducto(PedidoEdit context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mDbHelper = new AppPedidosDbAdapter(context);
        mDbHelper.open();
    }

    public void saveProductos() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_producto_con_botones,parent,false
            );
        }

        ImageButton decrementarButton = listItemView.findViewById(R.id.decrementar);
        ImageButton incrementarButton = listItemView.findViewById(R.id.incrementar);

        //OnClick listeners for all the buttons on the ListView Item
        TextView cant = listItemView.findViewById(R.id.cantidad);
        int cantidad = 0;
        if (pedidoId >= 0) {
            cantidad = mDbHelper.getCantidadProductoPedido(getItemId(position), pedidoId);
        }
        cant.setText(Integer.toString(cantidad));

        decrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long pedidoId = pedido.getId();
                int cantidad = 0;
                if (pedidoId >= 0) {
                    cantidad = mDbHelper.getCantidadProductoPedido(getItemId(position), pedidoId);
                }
                if (cantidad <= 0) {
                    mDbHelper.deleteProductoPedido(getItemId(position), pedido.getId());
                } else {
                    mDbHelper.updateProductoPedido(getItemId(position), pedido.getId(), cantidad - 1);
                }
                cant.setText(Integer.toString(cantidad));
                notifyDataSetChanged();
            }
        });

        incrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long pedidoId = pedido.getId();
                int cantidad = 0;
                if (pedidoId >= 0) {
                    cantidad = mDbHelper.getCantidadProductoPedido(getItemId(position), pedidoId);
                }
                if (cantidad == 0) {
                    mDbHelper.addProductoPedido(getItemId(position), pedido.getId(), 1);
                } else {
                    mDbHelper.updateProductoPedido(getItemId(position), pedido.getId(), cantidad + 1);
                }
                cant.setText(Integer.toString(cantidad));
                notifyDataSetChanged();
            }
        });
        return view;
    }*/
}
