package com.lospibescompany.sensoresejemplo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;

import com.lospibescompany.sensoresejemplo.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private LeeSensor listener = new LeeSensor();
    private SensorManager manager;
    private StringBuilder string=new StringBuilder();



    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager=(SensorManager) getSystemService(SENSOR_SERVICE);

        //List<Sensor> sensores=manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        List<Sensor> sensores=manager.getSensorList(Sensor.TYPE_PROXIMITY);
        if(sensores.size()>0){
            Sensor proximidad=sensores.get(0);
            manager.registerListener(listener,proximidad,SensorManager.SENSOR_DELAY_GAME);
            //Sensor acelerometro=sensores.get(0);
            //manager.registerListener(new LeeSensor(),acelerometro,SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        solicitarPermisos();
    }
    public void solicitarPermisos(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 100);
        }
    }
    private class LeeSensor implements SensorEventListener{
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            //Lo que quiero que haga cuando se produzca el evento.
            /*string.append("x "+sensorEvent.values[0]);
            string.append("y "+sensorEvent.values[1]);
            string.append("z "+sensorEvent.values[2]);
            string.delete(0,string.length()-1);*/

            string.append(sensorEvent.values[0]);
            binding.cartel.setText(string);
            string.delete(0,string.length()-1);
            if(sensorEvent.values[0] == 0){
                // Realizar la llamada
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:2664553747"));
                startActivity(callIntent);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
        //registrar listener en onresume
        //desregistrar en onpause
}