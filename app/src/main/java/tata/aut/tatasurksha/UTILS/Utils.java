package tata.aut.tatasurksha.UTILS;

import java.util.ArrayList;

import tata.aut.tatasurksha.beacon;

public class Utils {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
    public  static  String ByteArrayTohex(byte scanRecord[])
    {
        String msg ="";
        for (byte b : scanRecord)
            msg += String.format("%02x", b);
        return  msg;
    }

    public  static beacon findMaxBeacon(ArrayList<beacon> be)
    {
        int i=0;

        beacon bObject=be.get(0);
        for(i=0;i<be.size();i++)
        {
       if((Math.abs(bObject.rssi))< (Math.abs(be.get(i).rssi)))
       {
           bObject=be.get(i);
       }
        }
        return bObject;

    }
}
