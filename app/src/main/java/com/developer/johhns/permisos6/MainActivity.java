package com.developer.johhns.permisos6;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.CallLog;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.developer.johhns.permisos6.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0 ;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private View vista_principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        vista_principal = findViewById(R.id.vista_principal);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrar_llamada();
            }
        });
    }

    private void borrar_llamada() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            getContentResolver().delete(CallLog.Calls.CONTENT_URI, "number=25617897", null);
            Snackbar.make(vista_principal, "La llamada fue eliminada", Snackbar.LENGTH_LONG).show();
        } else {
            solicitarPermiso( Manifest.permission.WRITE_CALL_LOG,"Sin el permiso administrar "+
                    "llamadas no puedo borrar llamadas del registro",SOLICITUD_PERMISO_WRITE_CALL_LOG, this );
        }
    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso) || 1 == 1) {
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(actividad,new String[] {permiso} , requestCode);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(actividad,new String[] {permiso},requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Snackbar.make(vista_principal," grantResults[0] = " +  grantResults[0]  ,Snackbar.LENGTH_LONG).show();

        if ( requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG ) {
            if ( grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                borrar_llamada();
            } else {
                Snackbar.make(vista_principal,"Sin el permiso no puedo borrar la llamada",Snackbar.LENGTH_LONG).show();
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}