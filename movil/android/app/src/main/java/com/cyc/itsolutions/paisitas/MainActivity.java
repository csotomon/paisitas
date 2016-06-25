package com.cyc.itsolutions.paisitas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button botonConectar;
    Button botonDesconectar;
    RadioGroup grupoPaisitas;
    LocationManager locManager;
    TextView textViewEstado;
    TextView textViewPosicion;
    LocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonConectar = (Button) findViewById(R.id.buttonConectar);

        botonDesconectar = (Button) findViewById(R.id.buttonDesconectar);
        botonDesconectar.setEnabled(false);

        grupoPaisitas = (RadioGroup) findViewById(R.id.radioGroupPaisitas);
        grupoPaisitas.clearCheck();

        textViewEstado = (TextView) findViewById(R.id.textViewEstado);

        textViewPosicion = (TextView) findViewById(R.id.textViewPosicion);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                textViewPosicion.setText(String.valueOf(location.getLatitude() + "," + location.getLongitude() + "," + location.getAccuracy()));
            }

            public void onProviderDisabled(String provider) {
                textViewEstado.setText("Provider OFF");
            }

            public void onProviderEnabled(String provider) {
                textViewEstado.setText("Provider ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                textViewEstado.setText("Provider Status: " + status);
            }
        };


    }

    public void conectar(View view) {

        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            textViewEstado.setText("NO ESTA ACTIVADO EL GPS");
            return;
        }

        textViewEstado.setText("Conectado");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locListener);

        botonConectar.setEnabled(false);
        botonDesconectar.setEnabled(true);
    }

    public void desconectar(View view) {

        //locManager.removeGpsStatusListener(locListener);
        textViewEstado.setText("Desconectado");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locManager.removeUpdates(locListener);
        textViewPosicion.setText("");
        botonConectar.setEnabled(true);
        botonDesconectar.setEnabled(false);
    }
}
