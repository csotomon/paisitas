package com.cyc.itsolutions.paisitas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    Button botonConectar;
    RadioGroup grupoPaisitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonConectar = (Button)findViewById(R.id.buttonConectar);
        grupoPaisitas = (RadioGroup)findViewById(R.id.radioGroupPaisitas);
        grupoPaisitas.clearCheck();

        botonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=1;
                int b=2;
            }
        });

    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        int a=1;
        int b=2;
    }
}
