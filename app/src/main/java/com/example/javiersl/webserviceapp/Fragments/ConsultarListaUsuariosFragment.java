package com.example.javiersl.webserviceapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.javiersl.webserviceapp.Adapters.UsuarioListaAdapter;
import com.example.javiersl.webserviceapp.Class.Usuario;
import com.example.javiersl.webserviceapp.R;
import com.example.javiersl.webserviceapp.Resources.IBasic;

import org.json.JSONArray;

/**
 * Created by JavierSL on 31/12/2017.
 */

public class ConsultarListaUsuariosFragment extends Fragment implements IBasic, Response.Listener<JSONArray>,
        Response.ErrorListener
{
    private ListView listUsuarios;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_consultar_lista_usuarios, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        listUsuarios = (ListView)view.findViewById(R.id.listUsuarios);

        //Manda llamar al WebService
        cargarWebService();
    }

    private void cargarWebService()
    {
        //Arma el Progress Dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Un momento, por favor...");
        progressDialog.show();

        //Crea una cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Arma la url de la peticion
        String url = HOST + "EjercicioWebService/wsJSONConsultarLista.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, url, null, this, this);

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onResponse(JSONArray response)
    {
        progressDialog.hide();
        if (response.length() > 0)
        {
            UsuarioListaAdapter adapter = new UsuarioListaAdapter(getContext(), Usuario.obtenerListaUsuario(response));
            listUsuarios.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(getContext(), "No hay elementos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error al cargar el WebService", Toast.LENGTH_SHORT).show();
    }
}
