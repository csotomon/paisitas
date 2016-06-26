package com.cyc.itsolutions.paisitas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button botonConectar;
    Button botonDesconectar;
    RadioGroup grupoPaisitas;
    LocationManager locManager;
    TextView textViewEstado;
    TextView textViewPosicion;
    LocationListener locListener;
    FirebaseDatabase database;


    DatabaseReference myConnectionsRef;

    DatabaseReference lastOnlineRef;

    DatabaseReference connectedRef;

    boolean conectadoDB=false;

    ValueEventListener conexionListener;

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
        database = FirebaseDatabase.getInstance();


        locListener = getLocationListener();

        //conexionListener=getConexEventListener();

    }

    public void conectar(View view) {

        if(grupoPaisitas.getCheckedRadioButtonId()==-1)
            return;


        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            textViewEstado.setText("NO ESTA ACTIVADO EL GPS");
            return;
        }

        textViewEstado.setText("Conectando...");


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
        connectedRef.removeEventListener(conexionListener);
        conexionListener=null;
        database.goOffline();
    }


    private LocationListener getLocationListener(){
        return new LocationListener() {

            Location localizacion;

            public void onLocationChanged(Location location) {
                textViewPosicion.setText(String.valueOf(location.getLatitude() + "," + location.getLongitude() + "," + location.getAccuracy()));
                localizacion=location;
                if(conectadoDB)
                    guardarLocalizacion(location);
            }

            public void onProviderDisabled(String provider) {
                textViewEstado.setText("Provider OFF");
            }

            public void onProviderEnabled(String provider) {
                textViewEstado.setText("Provider ON");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                textViewEstado.setText("Provider Status: " + status);
                if(status==2){
                    conectarBaseDatos();
                    Log.i("Estado", "Available");
                }
                else if (status==0)
                    Log.i("Estado","Fuera de servicio");
                else if (status == 1)
                    Log.i("Estado", "Temporalmente no disponible");
                else
                    Log.i("Estado", "Otro:"+status);

                //Si esta desconectado y tiene señal GPS
                if((status==1 || status==2))
                    conectarBaseDatos();

                //Si no tiene señal de GPS
                if(status==0) {
                    database.goOffline();
                    conectadoDB = false;
                    conexionListener = null;
                }

            }
        };
    }

    private void guardarLocalizacion(Location localizacion){
        int idSelect = grupoPaisitas.getCheckedRadioButtonId();
        RadioButton radioSelect = (RadioButton)findViewById(idSelect);
        String texto = (String)radioSelect.getText();

        DatabaseReference dbRef = database.getReference("vehiculos/"+texto);
        Map<String, Object> actualizaciones = new HashMap<String, Object>();
        actualizaciones.put("longitud", localizacion.getLongitude());
        actualizaciones.put("latitud", localizacion.getLatitude());

        dbRef.updateChildren(actualizaciones);

    }

    private void conectarBaseDatos(){
        int idSelect = grupoPaisitas.getCheckedRadioButtonId();
        RadioButton radioSelect = (RadioButton)findViewById(idSelect);
        String texto = (String)radioSelect.getText();


        myConnectionsRef = database.getReference("vehiculos/"+texto+"/conexiones");

        lastOnlineRef = database.getReference("/vehiculos/"+texto+"/ultimaConexion");

        connectedRef = database.getReference(".info/connected");


        database.goOffline();
        database.goOnline();

        //connectedRef.removeEventListener(conexionListener);
        if(conexionListener == null) {
            conexionListener = getConexEventListener();
            connectedRef.addValueEventListener(conexionListener);
        }

    }

    private ValueEventListener getConexEventListener(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // add this device to my connections list
                    // this value could contain info about the device or a timestamp too
                    DatabaseReference con = myConnectionsRef.push();
                    con.setValue(Boolean.TRUE);

                    // when this device disconnects, remove it
                    con.onDisconnect().removeValue();

                    // when I disconnect, update the last time I was seen online
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                    conectadoDB = true;
                }
                else{
                    conectadoDB = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        };
    }
}
