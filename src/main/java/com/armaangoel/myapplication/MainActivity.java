package com.armaangoel.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int DISCOVERY_REQUEST = 1;
    public boolean btConnectionEstablished = false;
    private static final BluetoothAdapter BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice bluetoothDevice;
    public static int gameMode = 1;
    private Canvas canvas;

    private Context context = this;
    private MainActivity mainActivity = this;

    private Button singlePlayerButton;
    private Button twoPlayerButton;
    private Button bluetoothMultiplayerButton;
    //private Button hostButton;
    //private Button joinButton;
    private Button backButton;
    private Button refreshButton;

    private ArrayList<String> devices;
    private String []  x = {"no devices found"};


    private GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gamePanel = new GamePanel(context, mainActivity);

        menuSetup();
    }

    public void bluetoothMenu() {
        setContentView(R.layout.bluetooth_menu);

        //btOn();
        //establishBTConnection();


        String[] testArray = {"hi"};


        if (devices != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.textview, devices); //devices);

            ListView deviceList = (ListView) findViewById(R.id.deviceList);
            deviceList.setAdapter(adapter);
        }

        refreshButton = (Button) findViewById(R.id.refresh);
        backButton = (Button) findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuSetup();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (devices != null) devices.clear();
            }
        });

    }

    public void restart() {
        /*
        while (!gamePanel.restart) {

        }
        menuSetup();
        */
    }

    private void menuSetup() {
        setContentView(R.layout.activity_main);

        singlePlayerButton = (Button) findViewById(R.id.oneP);
        twoPlayerButton = (Button) findViewById(R.id.twoP);
        bluetoothMultiplayerButton = (Button) findViewById(R.id.bluetoothP);

        singlePlayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameMode = 2;
                setContentView(gamePanel);
                restart();

            }
        });
        twoPlayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameMode = 3;
                setContentView(gamePanel);
                restart();

            }
        });
        bluetoothMultiplayerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                gameMode = 4;
                establishBTConnection();
                //bluetoothMenu();
                restart();
            }
        });
    }

    public int btOn() {
        if(BLUETOOTH_ADAPTER == null) {
            Toast.makeText(context, "BT Unavailable", Toast.LENGTH_SHORT).show();
            return 1;
        } else if (BLUETOOTH_ADAPTER.isEnabled()) {
            Toast.makeText(context, "BT Already On", Toast.LENGTH_SHORT).show();
            return 2;
        } else {
            Toast.makeText(context, "BT Now On", Toast.LENGTH_SHORT).show();
            BLUETOOTH_ADAPTER.enable();
            return 3;
        }
    }
    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String deviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            BluetoothDevice remoteDevice;
            bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            int numOfDevices;
            if (devices == null) numOfDevices = 0;
            else numOfDevices = devices.size();

            if (deviceName != null) {
                for (int i = 0; i < numOfDevices; i++){
                    if (devices.indexOf( bluetoothDevice.getName())< 0) {
                        //System.out.println(bluetoothDevice);
                        devices.add(deviceName);
                        Toast.makeText(context, deviceName, Toast.LENGTH_SHORT).show();
                        System.out.println("1: " + deviceName + " 2: " + bluetoothDevice.getName());
                        System.out.println("worked");
                        x = new String[numOfDevices];
                        devices.toArray(x);
                        bluetoothMenu();
                    }
                }
            }

            //bluetoothDevice.createBond();
        }
    };
    public void establishBTConnection() {
        String beDiscoverable = BLUETOOTH_ADAPTER.ACTION_REQUEST_DISCOVERABLE;
        String scanModeChanged = BLUETOOTH_ADAPTER.ACTION_SCAN_MODE_CHANGED;
        //BLUETOOTH_ADAPTER.startDiscovery();
        IntentFilter filter = new IntentFilter(scanModeChanged);
        registerReceiver(discoveryResult, filter);
        startActivityForResult(new Intent(beDiscoverable), DISCOVERY_REQUEST);

        //System.out.println("test");

        btConnectionEstablished = true;

       bluetoothMenu();
    }

}
