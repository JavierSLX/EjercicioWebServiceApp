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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.javiersl.webserviceapp.R;
import com.example.javiersl.webserviceapp.Resources.IBasic;

import org.json.JSONArray;

/**
 * Created by JavierSL on 30/12/2017.
 */

public class RegistroUsuarioFragment extends Fragment implements IBasic, View.OnClickListener,
        Response.Listener<JSONArray>, Response.ErrorListener
{
    private EditText edtNombre, edtProfesion;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_registro_usuario, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        edtNombre = (EditText) view.findViewById(R.id.edtNombre);
        edtProfesion = (EditText) view.findViewById(R.id.edtProfesion);
        Button btRegistrar = (Button)view.findViewById(R.id.btRegistrar);

        btRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btRegistrar:
                cargarWebService();
                break;

            default:
                break;
        }
    }

    private void cargarWebService()
    {
        //Arma el dialogo de proceso
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Espere, por favor");
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //Crea la cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(getContext());

        //Arma la url de peticion
        String url = HOST + "EjercicioWebService/wsJSONRegistro.php";
        String cadena = "?nombre=" + edtNombre.getText().toString() + "&profesion=" +
                edtProfesion.getText().toString();
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
        //Esconde el dialogo de proceso
        progressDialog.hide();

        //Anuncia que se hizo de manera correcta
        Toast.makeText(getContext(), "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();
        edtNombre.setText("");
        edtProfesion.setText("");
    }

    //Error al conectarse al WebService
    @Override
    public void onErrorResponse(VolleyError error)
    {
        //Esconde el dialogo de progreso
        progressDialog.hide();

        //Anuncia el error
        Log.i("Error", error.toString());
        Toast.makeText(getContext(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
    }
}
