package com.org.firebase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


class NetworkUtils {

    static boolean isConnected(Context context) {
       //Connectivity manager instance
       ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       // Fetch Active internet connection from network info
       NetworkInfo netInfo = manager.getActiveNetworkInfo();
       // return the network connection is active or not.
       return netInfo != null && netInfo.isConnectedOrConnecting();
   }

}
