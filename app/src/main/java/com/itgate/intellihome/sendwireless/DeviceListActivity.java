package com.itgate.intellihome.sendwireless;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.itgate.intellihome.R;
import com.itgate.intellihome.activites.ConnectionMode;


import java.io.IOException;
import java.util.Set;
import java.util.UUID;


public class DeviceListActivity extends Activity {
    // Debugging for LOGCAT
    private static final String TAG = "DeviceListActivity";

    private final static UUID uuid = UUID.fromString("fc5ffc49-00e3-4c8b-9cf1-6b72aad1001a");
    protected BluetoothSocket btSocket = null;


    protected BluetoothAdapter bluetoothAdapter;
    private ListView listview;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        listview = (ListView) findViewById(R.id.paired_devices);
        adapter = new ArrayAdapter<String>(this,R.layout.device_name);
        listview.setAdapter(adapter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // ListView Item Click Listener
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                String  itemValue = (String) listview.getItemAtPosition(position);

                String MAC = itemValue.substring(itemValue.length() - 17);

                //ArrayList arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
                BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC);

                try {
                    btSocket = createBluetoothSocket(bluetoothDevice);
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
                }

                // Establish the Bluetooth socket connection.
                try {
                    btSocket.connect();
                } catch (IOException e) {
                    try {
                        btSocket.close();
                    } catch (IOException e2) {
                        //insert code to deal with this
                    }
                }
            }
        });
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(uuid);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    protected void onResume() {
        super.onResume();

        /** get bonded devices into listview */

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        // Add previously paired devices to the array
        if (pairedDevices.size() > 0) {
            findViewById(R.id.connecting).setVisibility(View.VISIBLE);//make title viewable
            for (BluetoothDevice device : pairedDevices) {
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            adapter.add(noDevices);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                bluetoothAdapter.disable();
                startActivity(new Intent(getApplicationContext(), ConnectionMode.class));

        }
        return super.onKeyDown(keyCode, event);
    }
}