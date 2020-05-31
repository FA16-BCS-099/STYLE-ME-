package com.ba.styleme.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.ba.styleme.R;
import com.ba.styleme.ui.callbacks.DialogActionListener;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CommonUtils {

//   Showing Internet alert dialog

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static AlertDialog showInternetAlertDialog(final Context context) {

        final DialogActionListener dialogActionListener = (DialogActionListener) context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_no_internet_connection, null);
        final TextView tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        final TextView tv_description = (TextView) dialogView.findViewById(R.id.tv_description);
        final TextView tv_detail_description = (TextView) dialogView.findViewById(R.id.tv_detail_description);
        final Button btn_tryagain = (Button) dialogView.findViewById(R.id.btn_tryagain);

        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();

        btn_tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionListener.internetConnectionCallback(dialog);
            }
        });
        if (dialog!=null&&!dialog.isShowing())
            dialog.show();
        return dialog;
    }
}
