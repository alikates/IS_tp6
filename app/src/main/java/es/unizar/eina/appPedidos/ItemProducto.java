package es.unizar.eina.appPedidos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ItemProducto extends SimpleCursorAdapter {

    public ItemProducto(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
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
        TextView cantidad = listItemView.findViewById(R.id.cantidad);

        //OnClick listeners for all the buttons on the ListView Item
        decrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myCantidad = Integer.valueOf(cantidad.getText().toString());
                if (myCantidad > 0) {
                    myCantidad--;
                }
                cantidad.setText(Integer.toString(myCantidad));
                notifyDataSetChanged();
            }
        });

        incrementarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myCantidad = Integer.valueOf(cantidad.getText().toString());
                myCantidad++;
                cantidad.setText(Integer.toString(myCantidad));
                notifyDataSetChanged();
            }
        });
        return view;
    }
}
