package com.arara.smartwardrobe;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ScanLabelActivity extends AppCompatActivity implements View.OnClickListener {

    String labelTag;

    Button bConnect, bBluetooth;

    TextView tvDebug;

    private BluetoothAdapter bluetoothAdapter;

    static final String ARDUINO_ADDRESS = "20:15:03:30:01:97";
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    ConnectThread connectThread;
    private InputStream inStream;
    boolean stopWorker;
    int delimiter;
    int readBufferPosition;
    byte[] readBuffer;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_label);

        inStream = null;
        stopWorker = false;
        delimiter = 10;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        handler = new Handler();

        labelTag = "";

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        bConnect = (Button) findViewById(R.id.bConnect);
        bBluetooth = (Button) findViewById(R.id.bBluetooth);

        tvDebug = (TextView) findViewById(R.id.tvDebug);

        bConnect.setOnClickListener(this);
        bBluetooth.setOnClickListener(this);
    }

    private void turnBluetoothOff() {
        tvDebug.setText("");
        Iterator i;
        i = bluetoothAdapter.getBondedDevices().iterator();
        while(i.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice) i.next();
            tvDebug.append("Device is paired with " + pairedDevice.getName() + "\n");
            try {
                Method m = pairedDevice.getClass().getMethod("removeBond", (Class[]) null);
                m.invoke(pairedDevice, (Object[]) null);
                tvDebug.append("Device is now unpaired with " + pairedDevice.getName() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bluetoothAdapter.disable();
        tvDebug.append("Bluetooth is off\n");
    }

    private void connectToArduino() {
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(ARDUINO_ADDRESS);
        tvDebug.setText("");

        if(!bluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            while(!bluetoothAdapter.isEnabled());
        }
        tvDebug.append("Bluetooth is turned on\n");

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE), 0);
            while (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
        }
        tvDebug.append("Device is visible\n");

        connectThread = new ConnectThread(bluetoothDevice);
        connectThread.run();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try{
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                tvDebug.append("Socket created\n");
            } catch (IOException e) {
                e.printStackTrace();
                tvDebug.append("Unable to create socket\n");
            }
            mmSocket = tmp;
        }

        @Override
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                tvDebug.append("Device is connected to " + mmDevice.getName() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                tvDebug.append("Unable to connect to " + mmDevice.getName() + "\n");
                try {
                    mmSocket.close();
                    tvDebug.append("Socket is closed\n");
                } catch (IOException c) {
                    c.printStackTrace();
                    tvDebug.append("Unable to close socket\n");
                }
                return;
            }
            Log.d("connected", "connected to socket");
            beginListenForData(mmSocket);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void beginListenForData(BluetoothSocket mmSocket) {
        final String searchThis = "UID da tag : ";
        tvDebug.append("Waiting for data...\n");

        try {
            inStream = mmSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread workerThread = new Thread(new Runnable() {
            public void run() {
                while(!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = inStream.available();
                        if(bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for(int i = 0 ; i < bytesAvailable ; i++) {
                                byte b = packetBytes[i];
                                if(b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {
                                        public void run() {
                                            tvDebug.append(data + "\n");
                                            if(data.contains(searchThis) && !labelTag.equals(data.substring(data.indexOf(searchThis) + searchThis.length()))) {
                                                labelTag = data.substring(data.indexOf(searchThis)+searchThis.length());
                                                Log.d("labelTag", "tag is " + labelTag);
                                                checkLabelTemp();
                                                //checkLabel();
                                            }
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        connectThread.cancel();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bBluetooth:
                turnBluetoothOff();
                break;
            case R.id.bConnect:
                connectToArduino();
                break;
        }
    }

    private void checkLabelTemp() {
        final String formattedTag = getFormattedTag(labelTag);
        Misc.showAlertMsg(formattedTag, "Ok", ScanLabelActivity.this);
    }

    private void checkLabel() {
        final String formattedTag = getFormattedTag(labelTag);
        Log.d("formattedTag", formattedTag);
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchTagDataInBackground(formattedTag, new Callback() {
            @Override
            public void done(String serverResponse) {
                Log.d("serverResponseTag", serverResponse);
                if (serverResponse.equals("error")) {
                    Misc.showAlertMsg("An error occurred while trying to connect.", "Ok", ScanLabelActivity.this);
                    finish();
                } else if (serverResponse.equals("no tag")) {
                    //Misc.showAlertMsg("Tag not found.", "Ok", ScanLabelActivity.this);
                    Intent intent = new Intent(ScanLabelActivity.this, CreateTagActivity.class);
                    intent.putExtra("tag", formattedTag);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ScanLabelActivity.this, ViewWearableActivity.class);
                    intent.putExtra("selectedWearable", buildWearable(serverResponse));
                    intent.putExtra("showName", true);
                    startActivity(intent);
                }
            }
        });
    }

    private String getFormattedTag(String inputTag) {

        return (inputTag.toLowerCase()).replace(" ", "");
    }

    private Wearable buildWearable(String wearableString) {

        List<String> wearableData = Arrays.asList(wearableString.split("#"));

        Wearable newWearable = new Wearable();

        newWearable.id = wearableData.get(0);
        newWearable.colors = (wearableData.get(1)).replace("&", ", ");
        newWearable.type = wearableData.get(2);
        newWearable.brand = wearableData.get(3);
        newWearable.description = wearableData.get(4);
        newWearable.owner = wearableData.get(5);

        return newWearable;
    }
}
