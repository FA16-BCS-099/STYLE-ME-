package com.ba.styleme.ui.activities.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;

import com.ba.styleme.R;
import com.ba.styleme.data.prefs.AppPreference;
import com.ba.styleme.ui.activities.home.account.AccountActivity;
import com.ba.styleme.ui.activities.home.account.update_password.UpdatePasswordActivity;
import com.ba.styleme.ui.activities.home.account.update_profile.UpdateProfileActivity;
import com.ba.styleme.ui.activities.home.account.wardrobe.WardrobeActivity;
import com.ba.styleme.ui.activities.home.calendar.CalendarActivity;
import com.ba.styleme.ui.activities.home.calendar.admin.RecomendationAdminActivity;
import com.ba.styleme.ui.activities.home.community.CommunityActivity;
import com.ba.styleme.ui.activities.home.community.add_for_excahnge.AddForExchangeActivity;
import com.ba.styleme.ui.activities.home.community.community_detail.ShowProduct;
import com.ba.styleme.ui.activities.user_registration.signin.forgot_password.ForgotPasswordActivity;
import com.ba.styleme.ui.activities.home.HomeActivity;
import com.ba.styleme.ui.activities.home.notification.NotificationActivity;
import com.ba.styleme.ui.activities.user_registration.signin.SigninActivity;
import com.ba.styleme.ui.activities.user_registration.signin_or_signup.SigninOrSignupActivity;
import com.ba.styleme.ui.activities.user_registration.signup.SignupActivity;
import com.ba.styleme.ui.callbacks.DialogActionListener;
import com.ba.styleme.utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity implements DialogActionListener {

    ProgressBar loading;
    protected AppPreference appPreference;
    protected Context context;
    ProgressDialog progressDialog;

    protected FirebaseAuth mAuth;
    public String STROAGE_PATH = "gs://style-me-3bc03.appspot.com";
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    protected StorageReference storageRef = storage.getReferenceFromUrl(STROAGE_PATH);    //change the url according to your firebase app

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        appPreference = new AppPreference(this);
        context = this;

        if (!CommonUtils.isOnline(context)) {
            CommonUtils.showInternetAlertDialog(context);
        }
        FirebaseApp.initializeApp(context);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        // Setting Title
        progressDialog.setTitle("Please Wait!");
        // Setting Message
        progressDialog.setMessage("Loading...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // Progress Dialog Style Spinner
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);

    }

    public void setSlidingPane() {
        final SlidingPaneLayout slidingPaneLayout = findViewById(R.id.SlidingPanel);
        slidingPaneLayout.setSliderFadeColor(Color.TRANSPARENT);
        findViewById(R.id.img_sliding_btnClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (slidingPaneLayout.isOpen()) {
                    slidingPaneLayout.closePane();
                } else {
                    slidingPaneLayout.openPane();
                }
            }
        });
    }

    public void setTitle(String title) {
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    public void openSigninOrSignupActivity(View view) {
        Intent intent = new Intent(this, SigninOrSignupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openSigninActivity(View view) {
        Intent intent = new Intent(this, SigninActivity.class);
        startActivity(intent);
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    public void openForgotPasswordActivity(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void openHomeActivity(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openAdminRecomendationActivity(View view) {
        Intent intent = new Intent(this, RecomendationAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void openAccountActivity(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        finish();
        startActivity(intent);
    }

    public void openUpdateProfileActivity(View view) {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    public void openUpdatePasswordActivity(View view) {
        Intent intent = new Intent(this, UpdatePasswordActivity.class);
        startActivity(intent);
    }

    public void openWardrobeActivity(View view) {
        Intent intent = new Intent(this, WardrobeActivity.class);
        startActivity(intent);
    }


    public void openCommunityActivity(View view) {
        Intent intent = new Intent(this, CommunityActivity.class);
        finish();
        startActivity(intent);
    }

    public void openAddForExcahngeActivity(View view) {
        Intent intent = new Intent(this, AddForExchangeActivity.class);
        startActivity(intent);
    }

    public void openCalendarActivity(View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        finish();
        startActivity(intent);
    }
    public void openARActivity(View view) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.styleme.app");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    public void openShowProduct(View view) {
        Intent intent = new Intent(this, ShowProduct.class);
        startActivity(intent);
    }

    public void openNotificationActivity(View view) {
        Intent intent = new Intent(this, NotificationActivity.class);
        finish();
        startActivity(intent);
    }

    public void onBackButtonClick(View view) {
        this.finish();
    }



    public String getResourceString(int id) {
        return this.getString(id);
    }

    public void showLoading() {
        if (progressDialog != null && (!progressDialog.isShowing())) {
            progressDialog.show();
        }
    }

    public void hideLoading() {
        if (progressDialog != null && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }


    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(imageView);
    }

    protected boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        //final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W]){1,})(?!.*\\s).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void showInfoToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    //Showing picture dailoge

    public AlertDialog showPictureDialog() {

        final DialogActionListener dialogActionListener = (DialogActionListener) context;
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_select_picture, null);
        TextView tv_gallery = (TextView) dialogView.findViewById(R.id.tv_gallery);
        TextView tv_camera = (TextView) dialogView.findViewById(R.id.tv_camera);

        RelativeLayout dialogLayout = (RelativeLayout) dialogView.findViewById(R.id.dialog_vu);
        builder.setView(dialogView);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionListener.galleryCallback(dialog);
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActionListener.cameraCallback(dialog);
            }
        });
        if (!dialog.isShowing())
            dialog.show();

        return dialog;
    }

    public void showOutfitList(final View view) {
        final EditText editText = ((EditText) view);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select " + getResourceString(R.string.out_fit_type));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getResourceString(R.string.western));
        arrayAdapter.add(getResourceString(R.string.eastern));
        arrayAdapter.add(getResourceString(R.string.both));

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                editText.setText(strName);
            }
        });
        builderSingle.show();

    }

    public void showEventTypeList(final View view) {
        final EditText editText = ((EditText) view);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select " + getResourceString(R.string.event_type));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getResourceString(R.string.event_meeting));
        arrayAdapter.add(getResourceString(R.string.event_everyday));
        arrayAdapter.add(getResourceString(R.string.event_birthparty_wedding));
        arrayAdapter.add(getResourceString(R.string.non_of_above));

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                editText.setText(strName);
            }
        });
        builderSingle.show();

    }

    public void showWeahterList(final View view) {
        final EditText editText = ((EditText) view);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Select " + getResourceString(R.string.weather));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getResourceString(R.string.cold));
        arrayAdapter.add(getResourceString(R.string.moderate));
        arrayAdapter.add(getResourceString(R.string.warm));

        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                editText.setText(strName);
            }
        });
        builderSingle.show();

    }

    public void onBtnLogoutUser(View view) {
        appPreference.logoutUser();
        openSigninOrSignupActivity(view);
    }

    public void showHideNoRecordFound(int size) {
        if (size > 0) {
            findViewById(R.id.ll_no_record_found).setVisibility(View.GONE);
        } else {
            findViewById(R.id.ll_no_record_found).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void galleryCallback(AlertDialog alertDialog) {

    }

    @Override
    public void cameraCallback(AlertDialog alertDialog) {

    }

    @Override
    public void internetConnectionCallback(AlertDialog dialog) {
        if (CommonUtils.isOnline(context) && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            this.finish();
            startActivity(getIntent());
        }
    }
}
