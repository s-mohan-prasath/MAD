package com.legendmp.mad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileTransferActivity extends AppCompatActivity {
    static final int MESSAGE_READ = 1;
    static final int SOCKET_PORT = 8080;
    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    Button peerDiscoverBtn, sendBtn;
    TextView peerDiscoveryStatusText, msgText;
    EditText msgBox;
    ListView peerListView;
    List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_file_transfer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        AppPermissions.requestAllPermissions(this);
        initialWork();
        executeListeners();
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MESSAGE_READ) {
                byte[] readBuff = (byte[]) msg.obj;
                String tempMsg = new String(readBuff, 0, msg.arg1);
                msgText.setText(tempMsg);
            }
            return true;
        }
    });


    private void initializeViews() {
        peerDiscoverBtn = findViewById(R.id.peerDiscoverBtn);
        peerListView = findViewById(R.id.peerListView);
        peerDiscoveryStatusText = findViewById(R.id.peerDiscoveryStatusText);

        msgText = findViewById(R.id.msgText);
        msgBox = findViewById(R.id.msgBox);
        sendBtn = findViewById(R.id.sendBtn);
    }

    @SuppressLint("MissingPermission")
    private void initialWork() {
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        Toast.makeText(this, "Wifi Direct supported in this device", Toast.LENGTH_SHORT).show();
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }
    public WifiP2pManager.PeerListListener peerListListener = peerList -> {
        if (!peerList.getDeviceList().equals(peers)) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            int len = peerList.getDeviceList().size();
            deviceNameArray = new String[len];
            deviceArray = new WifiP2pDevice[len];
            int index = 0;
            for (WifiP2pDevice device : peerList.getDeviceList()) {
                deviceNameArray[index] = device.deviceName;
                deviceArray[index] = device;
                index++;
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray);
            peerListView.setAdapter(adapter);
        }

        if (peers.size() == 0) {
            Toast.makeText(getApplicationContext(), "No Device Found", Toast.LENGTH_SHORT).show();
        }
    };
    public WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {
        final InetAddress groupOwnerAddress = info.groupOwnerAddress;
        if (info.groupFormed && info.isGroupOwner) {
            peerDiscoveryStatusText.setText("HOST");
            Log.d("WIFI_ROLE", "I'm HOST");
            serverClass = new ServerClass();
            serverClass.start();
        } else if (info.groupFormed) {
            peerDiscoveryStatusText.setText("CLIENT");
            Log.d("WIFI_ROLE", "I'm CLIENT");
            clientClass = new ClientClass(groupOwnerAddress);
            clientClass.start();
        } else {
            Log.d("WIFI_ROLE", "Group not formed");
        }
    };

    @SuppressLint("MissingPermission")
    private void executeListeners() {
        peerDiscoverBtn.setOnClickListener(v -> {
            if (AppPermissions.hasAllPermissions(this)) {
                try {
                    mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            peerDiscoveryStatusText.setText("Discovery Started");
                        }

                        @Override
                        public void onFailure(int reason) {
                            String reasonString;
                            switch (reason) {
                                case WifiP2pManager.P2P_UNSUPPORTED:
                                    reasonString = "P2P not supported on this device";
                                    break;
                                case WifiP2pManager.BUSY:
                                    reasonString = "Framework busy, try again shortly";
                                    break;
                                case WifiP2pManager.ERROR:
                                    reasonString = "Internal error occurred";
                                    break;
                                default:
                                    reasonString = "Unknown error: " + reason;
                                    break;
                            }
                            peerDiscoveryStatusText.setText("Discovery Failed: " + reasonString);
                            Toast.makeText(FileTransferActivity.this, "Discovery Failed: " + reasonString, Toast.LENGTH_LONG).show();
                        }

                    });
                } catch (SecurityException e) {
                    Toast.makeText(FileTransferActivity.this, "Missing required permissions", Toast.LENGTH_SHORT).show();
                }
            } else {
                AppPermissions.requestAllPermissions(this);
            }
        });

        peerListView.setOnItemClickListener((parent, view, position, id) -> {
            final WifiP2pDevice device = deviceArray[position];
            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;

            if (!AppPermissions.hasAllPermissions(this)) {
                AppPermissions.requestAllPermissions(this);
                return;
            }

            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Connected to " + device.deviceName, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int reason) {
                    Toast.makeText(getApplicationContext(), "Connect failed. Retry " + device.deviceName, Toast.LENGTH_SHORT).show();
                }
            });
        });

        sendBtn.setOnClickListener(v -> {
            String msg = msgBox.getText().toString();
            if (sendReceive != null) {
                new Thread(()->{
                    sendReceive.write(msg.getBytes());
                }).start();
            } else {
                Toast.makeText(this, "Not connected to a peer!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppPermissions.PERMISSION_REQUEST_CODE) {
            if (AppPermissions.allPermissionsGranted(grantResults)) {
                Toast.makeText(this, "All Permissions Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions Denied. Discovery will not work.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class ServerClass extends Thread {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SOCKET_PORT);
                socket = serverSocket.accept();
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public class ClientClass extends Thread {
        Socket socket;
        String hostAddress;

        public ClientClass(InetAddress hostAddress) {
            this.hostAddress = hostAddress.getHostAddress();
            socket = new Socket();
        }
        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAddress, SOCKET_PORT), 500);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(FileTransferActivity.this, "Client connection failed", Toast.LENGTH_SHORT).show());
            }
        }

    }
    private class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket socket) {
            try {
                this.socket = socket;
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("Send Receive","Failed to get input/output stream",e);
            }
        }

        @Override
        public void run() {
            try {
                if(inputStream==null){
                    Log.e("sendreceive","inputstream is null. Ending thread");
                    return;
                }
                byte[] buffer = new byte[1024];
                int bytes;

                while (socket != null && !socket.isClosed()) {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
