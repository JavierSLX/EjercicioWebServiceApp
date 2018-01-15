package com.example.javiersl.webserviceapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.javiersl.webserviceapp.Fragments.ConsultaUsuarioFragment;
import com.example.javiersl.webserviceapp.Fragments.ConsultarListaUsuariosFragment;
import com.example.javiersl.webserviceapp.Fragments.InicioAppFragment;
import com.example.javiersl.webserviceapp.Fragments.RegistroUsuarioFragment;
import com.example.javiersl.webserviceapp.Fragments.RegistroUsuarioImagenFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Elige por default el fragmento de inicio
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contenedor, new InicioAppFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Al elegir una opcion del menu de navegacion
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        boolean presionoOpcion = false;
        Fragment fragment = null;

        switch(item.getItemId())
        {
            case R.id.inicio:
                fragment = new InicioAppFragment();
                presionoOpcion = true;
                break;

            //Opción del menu Registrar Usuario
            case R.id.registrarUsuario:
                //fragment = new RegistroUsuarioFragment();
                fragment = new RegistroUsuarioImagenFragment();
                presionoOpcion = true;
                fab.hide();
                break;

            //Opcion del menu Consultar Usuario
            case R.id.consultarUsuario:
                fragment = new ConsultaUsuarioFragment();
                presionoOpcion = true;
                fab.hide();
                break;

            case R.id.consultarListaUsuarios:
                fragment = new ConsultarListaUsuariosFragment();
                presionoOpcion = true;
                fab.hide();
                break;

            case R.id.desarrollador:
                break;

            default:
                fab.show();
                break;
        }

        //Cuando se eligió un opción valida
        if (presionoOpcion)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.contenedor, fragment).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

