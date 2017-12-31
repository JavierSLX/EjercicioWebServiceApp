package com.example.javiersl.webserviceapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.javiersl.webserviceapp.Class.Usuario;
import com.example.javiersl.webserviceapp.R;

import java.util.List;

/**
 * Created by JavierSL on 31/12/2017.
 */

public class UsuarioListaAdapter extends BaseAdapter
{
    private Context context;
    private List<Usuario> lista;

    public UsuarioListaAdapter(Context context, List<Usuario> lista)
    {
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount()
    {
        return lista.size();
    }

    @Override
    public Usuario getItem(int position)
    {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.item_basico_usuario, null);

        TextView txtNombre = (TextView)view.findViewById(R.id.txtNombre);
        TextView txtProfesion = (TextView)view.findViewById(R.id.txtProfesion);

        txtNombre.setText(getItem(position).getNombre());
        txtProfesion.setText(getItem(position).getProfesion());

        return view;
    }
}
