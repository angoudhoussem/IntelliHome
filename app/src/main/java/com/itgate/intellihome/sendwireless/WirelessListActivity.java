package com.itgate.intellihome.sendwireless;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.itgate.intellihome.R;
import com.itgate.intellihome.activites.Piece_list;


import java.util.List;

public class WirelessListActivity extends Activity {
    private WifiManager wifiManager;
    private BroadcastReceiver wifiReciever;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wireless_list);

        ListView listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        listview.setAdapter(adapter);

        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);

        wifiReciever = new WiFiScanReceiver();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int x = position;

                    VerifyConnection(x);
            }
        });
    }

    public void VerifyConnection(int p){

        final List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
        ScanResult accessPoint = wifiScanResultList.get(p);
        final String Network = accessPoint.SSID;

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        final EditText code_input = new EditText(this);

        build.setCancelable(true);
        build.setTitle("Connect To " +Network);
        build.setMessage("Please Enter Key to Connect to Access Points !");
        build.setView(code_input);
        build.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /** some instructions for network connection configuration  */
                WifiConfiguration conf = new WifiConfiguration();
                conf.SSID = "\"" + "Network" + "\"";

                /** configuration for WEP Network */
                conf.wepKeys[0] = "\"" + code_input.getText() + "\"";
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

                /** configuration for WPA Network */
                conf.preSharedKey = "\"" + code_input.getText() + "\"";

                /** configuration for Open Network */
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

                /** other parameters */
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.LEAP);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

                wifiManager.addNetwork(conf);

                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration i : list) {
                    if (i.SSID != null && i.SSID.equals("\"" + "Network" + "\"")) {
                        wifiManager.disconnect();
                        wifiManager.enableNetwork(i.networkId, true);
                        wifiManager.reconnect();

                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getActiveNetworkInfo();
                        if (mWifi != null) {
                            if (mWifi.getType() == ConnectivityManager.TYPE_WIFI) {
                                Toast.makeText(getApplicationContext(), mWifi.getTypeName(), Toast.LENGTH_SHORT).show();
                            }
                        //NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {

                            Toast.makeText(WirelessListActivity.this, "Successfully Connected !", Toast.LENGTH_SHORT).show();
                            Intent it_connect_ctrl = new Intent(WirelessListActivity.this, Piece_list.class);
                            startActivity(it_connect_ctrl);
                            break;

                        }
                        } else {
                            Toast.makeText(WirelessListActivity.this, "Not Connected !", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(WirelessListActivity.this, "Connection Failure !", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        build.show();

    }


    public void onToggleClicked(View view) {

        adapter.clear();

        ToggleButton toggleButton = (ToggleButton) view;

        if (wifiManager == null) {
            // Device does not support Wi-Fi
            Toast.makeText(getApplicationContext(), "Oops! Your device does not support Wi-Fi", Toast.LENGTH_SHORT).show();
            toggleButton.setChecked(false);

        } else {
            if (toggleButton.isChecked()) { // To turn on Wi-Fi
                if (!wifiManager.isWifiEnabled()) {

                    Toast.makeText(getApplicationContext(), "Wi-Fi is turned on." +
                                    "\n" + "Scanning for access points...",
                            Toast.LENGTH_SHORT).show();

                    wifiManager.setWifiEnabled(true);

                } else {
                    Toast.makeText(getApplicationContext(), "Wi-Fi is already turned on." +
                                    "\n" + "Scanning for access points...",
                            Toast.LENGTH_SHORT).show();
                }
                wifiManager.startScan();

            } else { // To turn off Wi-Fi
                Toast.makeText(getApplicationContext(), "Wi-Fi is turned off.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    class WiFiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> wifiScanResultList = wifiManager.getScanResults();
                for(int i = 0; i < wifiScanResultList.size(); i++){
                    ScanResult accessPoint = wifiScanResultList.get(i);
                    String listItem = accessPoint.SSID+" - "+accessPoint.BSSID;
                    adapter.add(listItem);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the BroadcastReceiver for SCAN_RESULTS_AVAILABLE_ACTION
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReciever, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiReciever);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wi_fi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
