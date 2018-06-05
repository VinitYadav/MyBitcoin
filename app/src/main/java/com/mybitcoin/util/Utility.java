package com.mybitcoin.util;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mybitcoin.R;

public class Utility {

    /**
     * Show toast
     */
    public static void showToast(Activity activity, String msg) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) activity.findViewById(R.id.linearLayout));

        TextView text = layout.findViewById(R.id.textView);
        text.setText(msg);

        Toast toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER | Gravity.AXIS_CLIP, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
