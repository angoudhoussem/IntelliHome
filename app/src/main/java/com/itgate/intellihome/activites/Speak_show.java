package com.itgate.intellihome.activites;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.itgate.intellihome.R;
import com.itgate.intellihome.config.Config;
import com.itgate.intellihome.model.Equipement;
import com.itgate.intellihome.service.IEquipement;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class Speak_show extends AppCompatActivity {
    public ImageButton speakBtn;

    public ImageButton imgShowHome;
    public ImageButton imgShowEnt;
    public ImageButton AddHome;
    public ImageButton AddEnt;

    // String for MAC address
    private static String address;

    private ListView drawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    String msgToSend = null;

    Handler bluetoothIn;
    final int handlerState = 0;
    private ConnectedThread mConnectedThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_show);

        Intent intent = getIntent();
        String indicebt = intent.getStringExtra(Config.EXTRA_INDICE_BT);

        imgShowHome = (ImageButton) findViewById(R.id.imgHomeBtn);
        imgShowEnt = (ImageButton) findViewById(R.id.imgEntBtn);
        AddHome = (ImageButton) findViewById(R.id.imgAddHome);
        AddEnt = (ImageButton) findViewById(R.id.imgAddEnt);

        drawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        setupDrawer();
        addDrawerItems();

        showDesc();
        function_add();
        clickToSpeak();

    }

    /** début du désigne du slide menu */

    private void addDrawerItems() {
        ListView list = (ListView) findViewById(R.id.navList);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(mAdapter);

        drawerList.setAdapter(mAdapter);
    }

    public void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state.*/
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle("Navigation!");

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.*/
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if (getSupportActionBar() != null)
                    getSupportActionBar().setTitle(mActivityTitle);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //no inspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** fin du désigne du slide menu*/


    /**
     * #### début control de reconnaissance vocale ####
     */

    public void clickToSpeak() {
        speakBtn = (ImageButton) findViewById(R.id.imgButton);
        speakBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.imgButton) {
                            showSendSpeech();
                        }

                    }
                }
        );

    }

    public void showSendSpeech() {
        Intent it = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        it.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now !");

        try {
            startActivityForResult(it, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(Speak_show.this, "Sorry, your SmartPhone doesn't support Speech Recognizer !!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int req_code, int res_code, Intent it) {
        super.onActivityResult(req_code, res_code, it);
        switch (req_code) {
            case 100:
                if (res_code == RESULT_OK && it != null) {
                    ArrayList<String> result = it.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(Speak_show.this, "You Said : " + result.get(0), Toast.LENGTH_LONG).show();
                    msgToSend = result.get(0).toString();
                    mConnectedThread.write(msgToSend);
                }
        }
    }

    /** #### fin control de reconnaissance vocale #### */

    /** control image button */

    public void showDesc() {
        imgShowHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (v.getId() == R.id.imgHomeBtn) {

                            Intent i = new Intent(getApplicationContext(), Piece_list.class);
                            startActivity(i);

                            String Url = "http://10.0.3.2/formation";

                            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Url).build();

                            final IEquipement rest = restAdapter.create(IEquipement.class);
                            rest.getEquipement(100, 100, new Callback<List<Equipement>>() {
                                @Override
                                public void success(List<Equipement> equipements, Response response) {

                                    for (com.itgate.intellihome.model.Equipement Equipement : equipements) {

                                        System.out.println(Equipement.getName_eq());
                                        Toast.makeText(getApplicationContext(), "Equipement Name : " + Equipement.getName_eq(), Toast.LENGTH_LONG).show();

                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("erreur !", error.getMessage());
                                    Toast.makeText(getApplicationContext(), "Equipement Name " + error.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });


                        }
                    }
                });
    }

    public void function_add() {
        AddHome.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }
        );
    }

    /**begin class needed to manage Bluetooth configuration */

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }


        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }

    /** end class needed to manage Bluetooth configuration */
}
