package com.itgate.intellihome.activites;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itgate.intellihome.R;
import com.itgate.intellihome.sendwireless.DeviceListActivity;
import com.itgate.intellihome.config.Config;
//import com.rootkrm.authapp.R;

public class ConnectionMode extends AppCompatActivity {
    protected ImageButton wifiBtn;
    protected ImageButton btBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_mode);

        wifiBtn = (ImageButton) findViewById(R.id.ImgWifi);
        btBtn = (ImageButton) findViewById(R.id.ImgBT);

        btBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (bluetoothAdapter == null) {
                            // Device does not support Bluetooth
                            Toast.makeText(getApplicationContext(), "Oops! Your device does not support Bluetooth !", Toast.LENGTH_SHORT).show();
                        }else{
                            goToDeviceBT();
                        }
                    }
                }
        );

        wifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyWiFi();
            }
        });


    }

    public void goToDeviceBT() {
        String indiceBT = "blue";

        Intent it_BTDev = new Intent(ConnectionMode.this, DeviceListActivity.class);
        it_BTDev.putExtra(Config.EXTRA_INDICE_BT, indiceBT);
        startActivity(it_BTDev);

    }

    public void verifyWiFi() {
        String indiceWiFi = "wifi";

        WifiManager WM = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (WM == null) {
            // Device does not support Wi-Fi
            Toast.makeText(getApplicationContext(), "Oops! Your device does not support Wi-Fi", Toast.LENGTH_SHORT).show();
        }else {
            if (!isNetworkAvailable())
                    Toast.makeText(getApplicationContext(), "You're Not Connected !\nPlease Connect To Access Point to Continue", Toast.LENGTH_LONG).show();
                else {
                    Intent it_wifiDev = new Intent(ConnectionMode.this, Speak_show.class);
                    it_wifiDev.putExtra(Config.EXTRA_INDICE_WIFI, indiceWiFi);
                    startActivity(it_wifiDev);
                }
            }
        }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**String Url = "http://192.168.1.12/formation";
     RestAdapter Authadapter = new RestAdapter.Builder().setEndpoint(Url).build();
     final IAuthenticate Authrest = Authadapter.create(IAuthenticate.class);
     Authrest.checkUser(c_email.getText().toString(),
     c_pass.getText().toString(),
     new Callback<Response>() {
    @Override
    public void success(Response result, Response response) {
    BufferedReader reader = null;
    String output = "";

    try {
    //Initialisation de buffer reader
    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

    //lecture output dans string output
    output = reader.readLine();
    } catch (IOException e) {
    e.printStackTrace();
    }

    Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
    Intent it_control = new Intent(MainActivity.this, Speak_show.class);
    startActivity(it_control);

    }

    @Override
    public void failure(RetrofitError error) {
    Toast.makeText(getApplicationContext(), "Erreur : " + error.getMessage(), Toast.LENGTH_LONG).show();

    }
    });*/
}
