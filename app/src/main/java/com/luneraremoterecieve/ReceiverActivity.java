package com.luneraremoterecieve;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.luneraremoterecieve.atbeaconview.AltBeacon;
import com.luneraremoterecieve.atbeaconview.BeaconScanner;
import com.luneraremoterecieve.atbeaconview.BeaconScannerCallback;

import java.util.ArrayList;

/**
 * Created by shvet on 03/08/2017,LuneraRemoteRecieve
 */

public class ReceiverActivity extends AppCompatActivity implements BeaconScannerCallback {
    private final String TAG = "BeaconScannerView";
    RecyclerView recyclerView;

    ReceiverAdapter receiverAdapter;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    BluetoothManager mBluetoothManager;
    ArrayList<Model> arrayList;
    BeaconScanner beaconScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_activity);

        mBluetoothManager = (BluetoothManager) this.getSystemService(BLUETOOTH_SERVICE);
        if (!mBluetoothManager.getAdapter().isEnabled()) {
            mBluetoothManager.getAdapter().enable();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //we need to get user to grant access to the ACCESS_COARSE_LOCATION, beacon scanning requires this
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // so if its not granted yet, we do need the request it
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });
                builder.show();
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = new ArrayList<>();
        receiverAdapter = new ReceiverAdapter(this, arrayList);
        recyclerView.setAdapter(receiverAdapter);
        receiverAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            startActivity(new Intent(ReceiverActivity.this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //"coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void StartScanner() {
        beaconScanner = new BeaconScanner(getApplicationContext(), this, this.mBluetoothManager);
        beaconScanner.Start();
    }

    @Override
    public void BeaconDiscovered(AltBeacon beacon) {
        if (beacon == null && beacon.getDevice() != null) {
            return;
        }
        debugData("BeaconDiscovered : " + beacon.getDevice().getAddress());
        debugData("Id1:" + beacon.getId1());
        debugData("Id2:" + beacon.getId2() + ", Id2:" + beacon.getId2());
        debugData("getBeaconCode:" + beacon.getBeaconCode());

        Model model = new Model();
        model.setTitle("Id1");
        model.setSubtitle(beacon.getId1());
        arrayList.add(model);

        model = new Model();
        model.setTitle("UUID");
        model.setSubtitle(beacon.getDevice().getUuids().toString());
        arrayList.add(model);

        model = new Model();
        model.setTitle("RSSI");
        model.setSubtitle(beacon.getRefRSSI() + " ");
        arrayList.add(model);

        receiverAdapter.notifyDataSetChanged();
    }

    @Override
    public void debugData(String data) {
        Log.i(TAG, data);
    }
}
