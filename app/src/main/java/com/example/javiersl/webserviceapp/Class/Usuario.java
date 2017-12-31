package com.example.javiersl.webserviceapp.Class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JavierSL on 31/12/2017.
 */

public class Usuario
{
    private String nombre;
    private String profesion;

    public Usuario(String nombre, String profesion)
    {
        this.nombre = nombre;
        this.profesion = profesion;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getProfesion()
    {
        return profesion;
    }

    public void setProfesion(String profesion)
    {
        this.profesion = profesion;
    }

    //Cambia una ArrayList en una lista de Usuario
    public static List<Usuario> obtenerListaUsuario(JSONArray jsonArray)
    {
        List<Usuario> lista = new ArrayList<>();

        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Usuario usuario = new Usuario(jsonObject.getString("nombre"), jsonObject.getString("profesion"));
                lista.add(usuario);
            }
        }
        catch (JSONException e)
        {
        }

        return lista;
    }
}
