package com.example.javiersl.webserviceapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.javiersl.webserviceapp.R;

/**
 * Created by JavierSL on 30/12/2017.
 */

public class RegistroUsuarioFragment extends Fragment implements View.OnClickListener
{
    private EditText editNombre, editProfesion;

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

        editNombre = (EditText) view.findViewById(R.id.edtNombre);
        editProfesion = (EditText) view.findViewById(R.id.edtProfesion);
        Button btRegistrar = (Button)view.findViewById(R.id.btRegistrar);

        btRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btRegistrar:
                break;

            default:
                break;
        }
    }
}
