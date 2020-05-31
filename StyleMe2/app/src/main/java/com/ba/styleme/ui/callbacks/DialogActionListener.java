package com.ba.styleme.ui.callbacks;

import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

/**
 * Created by ahtisham.hassan on 3/28/2018.
 */

public interface DialogActionListener {

    void galleryCallback(AlertDialog alertDialog);

    void cameraCallback(AlertDialog alertDialog);

    void internetConnectionCallback(AlertDialog dialog);
}
