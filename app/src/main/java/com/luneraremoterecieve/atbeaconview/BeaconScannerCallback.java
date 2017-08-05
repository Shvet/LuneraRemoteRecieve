package com.luneraremoterecieve.atbeaconview;



public interface BeaconScannerCallback {
    void BeaconDiscovered(AltBeacon beacon);
    void debugData(String data);
}

