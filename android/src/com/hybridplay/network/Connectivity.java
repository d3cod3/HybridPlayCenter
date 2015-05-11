package com.hybridplay.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class Connectivity {

	public Context _context;
    
    public Connectivity(Context context){
        this._context = context;
    }
    
    /**
     * Get the network info
     * @param context
     * @return
     */
    public NetworkInfo getNetworkInfo(){
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
 
    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public boolean isConnected(){
        NetworkInfo info = getNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @param type
     * @return
     */
    public boolean isConnectedWifi(){
        NetworkInfo info = getNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @param type
     * @return
     */
    public boolean isConnectedMobile(){
        NetworkInfo info = getNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public boolean isConnectedFast(){
        NetworkInfo info = getNetworkInfo();
        return (info != null && info.isConnected() && isConnectionFast(info.getType(),info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public boolean isConnectionFast(int type, int subType){
        if(type==ConnectivityManager.TYPE_WIFI){
            return true;
        }else if(type==ConnectivityManager.TYPE_MOBILE){
            switch(subType){
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                return false; // ~25 kbps 
            // Unknown
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                return false;
            }
        }else{
            return false;
        }
    }
	
}
