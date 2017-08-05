package com.luneraremoterecieve.atbeaconview;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;

// We will need this to scan, since the AltBeacon library does not support ad hoc scan, it only support being trigger by beacon when in range.

public class AltBeaconFactory {

    public static boolean isBLESupported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public static double calculateDistanceFromRssi(double rawSignalStrengthInDBm, int measuredPower)
    {
        double distance = 0d;
        double near = rawSignalStrengthInDBm / measuredPower;

        if (near < 1.0f)
        {
            distance = Math.pow(near, 10);
        }
        else
        {
            distance = ((0.89976f) * Math.pow(near, 7.7095f) + 0.111f);
        }

        return distance;
    }

    static public boolean isLengthAndTypeOk(byte[] scanRecord){
        if(scanRecord == null || scanRecord.length  < 26){
            return false;
        }

        //if the actual data record length is smaller than what we expect
        // or if the tyoe is set to something else than what we expect
        if(scanRecord[0] < 26 && scanRecord[1] != 0xFF){
            return false;
        }

        return true;
    }

    static public String getManufacturer(byte[] scanRecord){
        return getStringPart(3, 3, scanRecord) + getStringPart(2, 2, scanRecord); //little endian
    }

    static public String getBeaconCode(byte[] scanRecord){
        return getStringPart(4, 5, scanRecord);
    }

    /// The expected specification of the data is as follows:
    /// L-BEACON Fields
    /// Byte(s)     Name
    /// --------------------------
    /// 0-1         Manufacturer ID (16-bit unsigned integer, big endian)
    /// 2-3         Beacon code (two 8-bit unsigned integers, but can be considered as one 16-bit unsigned integer in little endian)
    /// 4-19        ID1 (UUID)
    /// 20       ID2 (8-bit unsigned integer, big endian)
    /// 21-23       ID3 (32-bit unsigned integer, big endian)
    /// 24          Measured Power (signed 8-bit integer)
    /// 25          Reserved for use by the manufacturer to implement special features (optional)
    ///


    public static AltBeacon getBeaconFromScanrecord(BluetoothDevice device, byte[] scanRecord, final int rssi) {

        String id1 = getStringPart(6, 9, scanRecord) + "-" + getStringPart(10, 11, scanRecord) + "-" + getStringPart(12, 13, scanRecord) + "-" + getStringPart(14, 15, scanRecord) + "-" + getStringPart(16, 21, scanRecord);
        int id2 = (((scanRecord[22] & 0xFF) << 8)); // Uint16
        int id3 = ((scanRecord[25] & 0xFF) + ((scanRecord[24] & 0xFF)+  (scanRecord[23] & 0xFF) << 8)); // Uint16

        long Distance = Math.round(AltBeaconFactory.calculateDistanceFromRssi(rssi, scanRecord[26]));
        return new AltBeacon(device,scanRecord[1],getManufacturer(scanRecord),getBeaconCode(scanRecord),id1,id2,id3,scanRecord[26],scanRecord[27],Distance);
    }


    static private String getStringPart(int start, int end, byte[] record){

        end = end + 1;
        if(start < 0 || end < start){
            return null;
        }

        if(record.length < end){
            return null;
        }

        StringBuilder hex = new StringBuilder((end - start) * 2);

        for(int i = start; i < end ; i++){
            hex.append(String.format("%02X", record[i]));
        }

        return hex.toString();
    }
}
