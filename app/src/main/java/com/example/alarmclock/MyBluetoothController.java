package com.example.alarmclock;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MyBluetoothController extends AppCompatActivity {
    static final String DEVICE_ADDRESS = "00:14:03:05:F0:E6";
    static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    boolean connected;
    BluetoothAdapter bTAdapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    OutputStream send;

    public MyBluetoothController() {
        connected = false;
        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        send = null;
        socket = null;
        device = null;
        activateBluetooth();
    }

    public void connectDevice() {
        if (activateBluetooth()) {
            device = bTAdapter.getRemoteDevice(DEVICE_ADDRESS);
            try {
                Log.d("mbc", "11failfailfail");
                socket = device.createInsecureRfcommSocketToServiceRecord(PORT_UUID);//create a RFCOMM (SPP) connection
                Log.d("mbc", "22failfailfail");
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                Log.d("mbc", "33failfailfail");
                socket.connect();//start connection
                Log.d("mbc", "wfdsadsf");
                if (socket != null) {
                    Log.d("mbc", "woooo");
                    connected = true;
                    send = socket.getOutputStream();
                    send.write("5".getBytes());
                    send.write("0".getBytes());
                } else {
                    connected = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mbc", "Failed to connect device");
                connected = false;
            }
        }
    }

    public void disconnectDevice() {
        //if (activateBluetooth()) {
            try {
                socket.close();
                connected = false;
                if (send != null) {
                    send.write("6".getBytes());
                    send.close();
                }
                if (socket != null) {
                    socket.close();
                }
                if (device != null) {
                    device = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mbc", "Failed to disconnect device");
                connected = false;
            }
        //}
    }

    public boolean activateFunction(String option) {
        if (activateBluetooth()) {
            try {
                if (!connected) {
                    connectDevice();
                }
                if (connected) {
                    send.write(option.getBytes());
                    Log.d("mbc", "Command sent successfully!");
                    return true;
                } else {

                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mbc", "Failed to send command");
                connected = false;
                return false;
            }
        }
        return false;
    }

    public boolean activateBluetooth() {
        bTAdapter.enable();
        if (bTAdapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }
}
