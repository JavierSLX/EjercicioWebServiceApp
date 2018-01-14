package com.example.javiersl.webserviceapp.Class;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

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
    private Bitmap imagen;
    private String ruta;

    public Usuario(String nombre, String profesion, Bitmap imagen, String ruta)
    {
        this.nombre = nombre;
        this.profesion = profesion;
        this.imagen = imagen;
        this.ruta = ruta;
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

    public Bitmap getImagen()
    {
        return imagen;
    }

    public void setImagen(Bitmap imagen)
    {
        this.imagen = imagen;
    }

    public String getRuta()
    {
        return ruta;
    }

    public void setRuta(String ruta)
    {
        this.ruta = ruta;
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

                //Saca el dato de la imagen (y checa si existe)
                String dato = jsonObject.getString("imagen");
                Bitmap img;
                if (dato.length() > 0)
                {
                    byte[] byteCode = Base64.decode(jsonObject.getString("imagen"), Base64.DEFAULT);
                    img = BitmapFactory.decodeByteArray(byteCode, 0, byteCode.length);
                }
                else
                    img = null;

                //Crea un objeto Usuario y lo coloca en la lista
                Usuario usuario = new Usuario(jsonObject.getString("nombre"), jsonObject.getString("profesion"),
                        img, jsonObject.getString("ruta_imagen"));
                lista.add(usuario);
            }
        }
        catch (JSONException e)
        {
        }

        return lista;
    }
}
