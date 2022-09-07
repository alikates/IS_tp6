package es.unizar.eina.appPedidos.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import es.unizar.eina.appPedidos.Producto;
import es.unizar.eina.appPedidos.R;

public class ItemProductoAdapter extends ArrayAdapter<Producto> {
    private ArrayList<Producto> listaProductos;

    public ItemProductoAdapter(ArrayList<Producto> data, Context context) {
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
}
