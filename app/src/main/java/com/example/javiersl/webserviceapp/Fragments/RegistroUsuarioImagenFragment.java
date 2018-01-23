package com.example.javiersl.webserviceapp.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.javiersl.webserviceapp.R;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;

/**
 * Created by Morpheus on 14/01/2018.
 */

public class RegistroUsuarioImagenFragment extends Fragment implements View.OnClickListener
{
    private static final String CARPETA_PRINCIPAL = "misImagenesApp/"; //Directorio Principal
    private static final String CARPETA_IMAGEN = "imagenes"; //Carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN; //Ruta carpeta directorio
    private String path; //Almacena la ruta de la imagen
    private File fileImagen;
    private Bitmap bitmap;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    private ProgressDialog progressDialog;
    private EditText edtNombre, edtProfesion;
    private ImageView imgUsuario;
    private StringRequest stringRequest;

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

        //Checa los permisos
        validaPermisos();
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

    //Método que checa si existen los permisos
    private boolean validaPermisos()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if ((getContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(CAMERA)) || (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)))
            cargarDialogoRecomendacion();
        else
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
        return false;
    }

    //Carga el dialogo de recomendación de permisos
    private void cargarDialogoRecomendacion()
    {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la app");
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialogo.show();
    }

    //Al requerir la opción del dialogo de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100)
        {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
            }
            else
                solicitarPermisosManual();
        }
    }

    //Método que da los permisos de manera manual
    private void solicitarPermisosManual()
    {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("¿Desea configurar los permisos de forma manual?");
        builder.setItems(opciones, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (opciones[which].equals("Si"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        builder.show();
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));

            startActivityForResult(intent, COD_FOTO);
        }
    }

    //Cuando se elige una opción del diálogo
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Matrix matrix;
        Bitmap imagen;

        switch (requestCode)
        {
            case COD_SELECCIONA:
                Uri miPath = data.getData();
                try
                {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), miPath);
                } catch (IOException e)
                {
                    e.printStackTrace();
                    bitmap = null;
                }

                //Rota la imagen
                matrix = new Matrix();
                matrix.postRotate(90);
                imagen = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imgUsuario.setImageBitmap(imagen);
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

                //Rota la imagen
                matrix = new Matrix();
                matrix.postRotate(90);
                imagen = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                imgUsuario.setImageBitmap(imagen);
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
        String url = "http://morpheusdss.com/EjercicioWebService/wsJSONRegistroMovil.php?";

        //Para enviar por método POST
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("Guardado"))
                {
                    edtNombre.setText("");
                    edtProfesion.setText("");
                    Toast.makeText(getContext(), "Se ha registrado con éxito", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "No se ha registrado", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                progressDialog.hide();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                String nombre = edtNombre.getText().toString();
                String profesion = edtProfesion.getText().toString();
                String imagen = convertirImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("nombre", nombre);
                parametros.put("profesion", profesion);
                parametros.put("imagen", imagen);

                return parametros;
            }
        };

        queue.add(stringRequest);
    }

    //Convierte una imagen en String
    private String convertirImgString(Bitmap bitmap)
    {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }
}
