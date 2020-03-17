package com.org.firebase.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.org.firebase.R;
import com.org.firebase.helper.loading.RotateLoading;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UiUtils {

    public static Uri getImageUri(Context mContext, Bitmap mBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), mBitmap, "Title", null);
        return Uri.parse(path);
    }

    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return "";
        }
        String[] fileArray = filePath.split(".");
//        String extension = fileArray[(fileArray.length - 1)];
        String ext = "";
        int pos = filePath.lastIndexOf('.');
        if (pos != -1)
            ext = filePath.substring(filePath.lastIndexOf('.') + 1,
                    filePath.length());
        else return "*/*";
        return ext;
    }

    public static void showToast(Context context, String input) {
        Toast.makeText(context, input, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View view, String message, int length) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        View v = snackbar.getView();
        TextView textView = (TextView) v.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(4);
        snackbar.show();
    }

    public static void showSnackBarWithAction(View view, String message, int length, String action, View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        View v = snackbar.getView();
        TextView textView = (TextView) v.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(4);
        snackbar.setAction(action, actionClickListener);
        snackbar.show();
    }

    public static void showSnackBarWithAction(View view, int messageResId, int length, int actionResId, View.OnClickListener actionClickListener) {
        Snackbar snackbar = Snackbar.make(view, messageResId, length);
        View v = snackbar.getView();
        TextView textView = (TextView) v.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(4);
        snackbar.setAction(actionResId, actionClickListener);
        snackbar.show();
    }

    public static void showSnackBar(View view, int message, int length) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        View v = snackbar.getView();
        TextView textView = (TextView) v.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setMaxLines(4);
        snackbar.show();
    }

    public static void showToast(AppCompatActivity mActivity, String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    public static AlertDialog createSyncErrorDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, title, message, "Dismiss", onClickListener);
    }

    public static AlertDialog createRemoveItemDialog(Context context, String title, String message, String btnText, DialogInterface.OnClickListener onClickListener) {
        return createAlertDialog(context, title, message, btnText, onClickListener);
    }

    public static AlertDialog createAlertDialog(Context context, String title, String message, String buttonText, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(buttonText, onClickListener)
                .create();
        if (TextUtils.isEmpty(title)) {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        return alertDialog;
    }


    public static AlertDialog showLoadingAlertDialog(Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialogBuilder.setView(dialogView);

        RotateLoading rotateLoading = dialogView.findViewById(R.id.rotateView);
        rotateLoading.start();

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return alertDialog;
    }


    /*Email valid*/
    public static boolean isEmailValid(String email) {
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
