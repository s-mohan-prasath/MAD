package com.legendmp.mad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private final WifiP2pManager mManager;
    private final WifiP2pManager.Channel mChannel;
    private final FileTransferActivity activity;

    public WifiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, FileTransferActivity activity) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Wi-Fi P2P state changed
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "Wi-Fi is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wi-Fi is OFF", Toast.LENGTH_SHORT).show();
            }
        }
        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Peer list changed, request updated list
            if (mManager != null) {
                if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
                        ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(context, "Missing required permissions to discover peers", Toast.LENGTH_SHORT).show();
                    return;
                }
                mManager.requestPeers(mChannel, activity.peerListListener);
            }
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (mManager == null) return;

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.isConnected()) {
                // Connected to another device via Wi-Fi P2P
                mManager.requestConnectionInfo(mChannel, activity.connectionInfoListener);
                Toast.makeText(context, "Connected to P2P device", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Disconnected from P2P device", Toast.LENGTH_SHORT).show();
            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {

        }
    }
}
