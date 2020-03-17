package com.org.firebase.helper.runtimepermission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.org.firebase.app.AppConstants;


public class PermissionHelper implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static String[] STORAGE_PERMISSION = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static String[] CAMERA_STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

    private int requestCode;
    private Fragment fragment;
    private AppCompatActivity activity;
    private Context context;
    private PermissionListener listener;
    private String[] permissionArray;

    public PermissionHelper(int requestCode, Fragment fragment, PermissionListener listener) {
        this.requestCode = requestCode;
        this.fragment = fragment;
        this.listener = listener;
        context = getContext();
        permissionArray = getPermissionArray(requestCode);
    }

    public PermissionHelper(int requestCode, AppCompatActivity activity, PermissionListener listener) {
        this.requestCode = requestCode;
        this.activity = activity;
        this.listener = listener;
        context = getContext();
        permissionArray = getPermissionArray(requestCode);
    }

    private Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getContext();
        }
        return null;
    }

    private String[] getPermissionArray(int requestCode) {
        switch (requestCode) {
            case AppConstants.REQUEST_CODE_STORAGE:
                return STORAGE_PERMISSION;
                case AppConstants.REQUEST_CODE_CAMERA_STOREAFGE:
                return CAMERA_STORAGE;

        }
        return null;
    }

    public boolean isPermissionGranted(int requestCode) {
        String permissionArray[] = getPermissionArray(requestCode);
        boolean allPermissionsGranted = true;
        for (int i = 0, mPermissionLength = permissionArray.length; i < mPermissionLength; i++) {
            String permission = permissionArray[i];
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        return allPermissionsGranted;
    }

    public void openPermissionDialog() {
        if (permissionArray == null) {
            return;
        }
        boolean allPermissionsGranted = true;
        for (int i = 0, mPermissionLength = permissionArray.length; i < mPermissionLength; i++) {
            String permission = permissionArray[i];
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }
        if (!allPermissionsGranted) {
            if (activity != null) {
                ActivityCompat.requestPermissions(activity, permissionArray, requestCode);
            } else if (fragment != null) {
                ActivityCompat.requestPermissions(fragment.getActivity(),permissionArray, requestCode);
            }
        } else {
            listener.onPermissionGranted();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode) {
            boolean allPermissionGranted = true;
            if (grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = false;
                        break;
                    }
                }
            } else {
                allPermissionGranted = false;
            }
            if (allPermissionGranted) {
                listener.onPermissionGranted();
            } else {
                listener.onPermissionDenied();
            }
        }
    }

    public interface PermissionListener {
        void onPermissionGranted();

        void onPermissionDenied();
    }

}
