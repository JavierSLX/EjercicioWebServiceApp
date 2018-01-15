package com.example.javiersl.webserviceapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.javiersl.webserviceapp.R;

import org.json.JSONArray;

import java.io.File;

/**
 * Created by Morpheus on 14/01/2018.
 */

public class RegistroUsuarioImagenFragment extends Fragment implements View.OnClickListener, Response.Listener<JSONArray>, Response.ErrorListener
{
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/"; //Directorio Principal
    private static final String CARPETA_IMAGEN = "imagenes"; //Carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN; //Ruta carpeta directorio
    private String path; //Almacena la ruta de la imagen
    private File fileImagen;
    private Uri imagen;
    private Bitmap bitmap;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private ProgressDialog progressDialog;
    private EditText edtNombre, edtProfesion;
    private ImageView imgUsuario;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_registro_usuario_imagen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        edtNombre = (EditText)view.findViewById(R.id.edtNombre);
        edtProfesion = (EditText)view.findViewById(R.id.edtProfesion);
        imgUsuario = (ImageView)view.findViewById(R.id.imgUsuario);
        Button btRegistrar = (Button)view.findViewById(R.id.btRegistrar);

        imgUsuario.setOnClickListener(this);
        btRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgUsuario:
                mostrarDialogOpciones();
                break;

            case R.id.btRegistrar:
                cargarWebService();
                break;

            default:
                break;
        }
    }

    //Construye el dialogo de opciones
    private void mostrarDialogOpciones()
    {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(opciones[which].equals("Tomar Foto"))
                {
                    //Llamado a metodo para activar la camara
                    abrirCamara();
                }
                else if (opciones[which].equals("Elegir de Galeria"))
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                }
                else
                {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    //Evento de tomar foto
    private void abrirCamara()
    {
        checarPermisos();
        File miFile = new File(Environment.getExternalStorageDirectory(), DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();
        Log.i("creada", String.valueOf(isCreada));

        //Si no existe, crea el directorio
        if(!isCreada)
        {
            isCreada = miFile.mkdirs();
            Log.i("archivo", miFile.toString());
            Log.i("creada", String.valueOf(isCreada));
        }

        if (isCreada)
        {
            //Le da nombre a la foto
            String nombre = (System.currentTimeMillis()/1000) + ".jpg";

            //Arma la ruta de almacenamiento
            path = Environment.getExternalStorageDirectory() + File.separator + DIRECTORIO_IMAGEN + File.separator +
                    nombre;
            Log.i("mipath", path);

            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            startActivityForResult(intent, COD_FOTO);
            //startActivity(intent);
        }
    }

    private void checarPermisos()
    {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) && shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
            {
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 100:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                }
                break;
        }
    }

    //Cuando se elige una opción del diálogo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                imgUsuario.setImageURI(miPath);
                break;

            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener()
                        {
                            @Override
                            public void onScanCompleted(String path, Uri uri)
                            {
                                Log.i("Path", ""+path);
                            }
                        });

                bitmap = BitmapFactory.decodeFile(path);
                imgUsuario.setImageBitmap(bitmap);
                break;

            default:
                break;
        }
    }

    private void cargarWebService()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Cargando");
        progressDialog.setMessage("Un momento...");
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());
    }

    @Override
    public void onResponse(JSONArray response)
    {

    }

    @Override
    public void onErrorResponse(VolleyError error)
    {

    }
}
