package com.example.javiersl.webserviceapp.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class ConsultaUsuarioFragment extends Fragment implements IBasic, View.OnClickListener, Response.Listener<JSONArray>,
        Response.ErrorListener
{
    private EditText edtNombre;
    private ListView listUsuarios;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_consulta_usuario, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        edtNombre = (EditText)view.findViewById(R.id.edtNombre);
        Button btConsultar = (Button)view.findViewById(R.id.btConsultar);
        listUsuarios = (ListView)view.findViewById(R.id.listUsuarios);

        btConsultar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btConsultar:
                cargarWebService();
                break;

            default:
                break;
        }
    }

    //Método que carga el WebService
    private void cargarWebService()
    {
        //Arma el ProgressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Un momento, por favor...");
        progressDialog.show();

        //Crea una cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Arma la url de la peticion
        String url = HOST + "EjercicioWebService/wsJSONConsultaUsuarioImagen.php";
        String cadena = "?nombre=" + edtNombre.getText().toString();
        cadena = cadena.replace(" ", "%20");
        url += cadena;

        //Arma el objeto de respuesta y agrega la petición
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, url, null, this, this);
        queue.add(jsonArrayRequest);
    }

    //La respuesta del WebService
    @Override
    public void onResponse(JSONArray response)
    {
        if (response.length() > 0)
        {
            progressDialog.hide();
            UsuarioListaAdapter adapter = new UsuarioListaAdapter(getContext(), Usuario.obtenerListaUsuario(response));
            listUsuarios.setAdapter(adapter);
        }
        else
        {
            progressDialog.hide();
            Toast.makeText(getContext(), "No se encontró resultado", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error)
    {
        progressDialog.hide();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("error", error.toString());
    }
}
