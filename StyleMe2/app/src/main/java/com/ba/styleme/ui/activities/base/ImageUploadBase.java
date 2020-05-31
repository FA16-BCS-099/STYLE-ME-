package com.ba.styleme.ui.activities.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.ba.styleme.ui.callbacks.DialogActionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ImageUploadBase extends BaseActivity  {
    protected  File file;
    private int GALLERY = 1, CAMERA = 2;
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

    }

    public abstract void performImageAction(Bitmap bitmap, String base64String, File file);


    /*
     *
     *
     * Open Camera
     *
     * */

    public void openCamera() {
        if (isWriteStoragePermissionGranted(this)) {
            dispatchTakeImageIntent();
        } else {

            showInfoToast( "Permission Require!");
        }
    }


    /*
     *
     *
     * Open Camera For Image
     *
     * */

    String filePath = "";

    public void dispatchTakeImageIntent() {
        try {
            Intent takeImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = createImageFile(filePath);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            // Save a file: path for use with ACTION_VIEW intents
            filePath = "file:" + file.getAbsolutePath();
            takeImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(takeImageIntent, CAMERA);
        } catch (IOException e) {
            Log.e("ioexception", e.toString());
        }
    }


    /*
     *
     *
     * Open Gallary For Image
     *
     * */

    public void openGallery() {
        if (isWriteStoragePermissionGranted(this)) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY);
        }else {

            showInfoToast( "Permission Require!");
        }
    }


    /*
     *
     *
     * Getting Path from URI
     *
     * */

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        } else if (requestCode == CAMERA && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(filePath);

            try {
                File file = new File(imageUri.getPath());
                InputStream imageStream = new FileInputStream(file);

                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);

               // bitmap = Compressor.getDefault(this).compressToBitmap(file);

                bitmap = rotateImageIfRequired(context, bitmap, imageUri);

                performImageAction(bitmap, getBase64String(bitmap), file);

            } catch (IOException e) {
                return;
            }

        } else if (requestCode == GALLERY) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);

                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                File file = new File(getPathFromURI(imageUri));

               // bitmap = Compressor.getDefault(this).compressToBitmap(file);

                bitmap = rotateImageIfRequired(context, bitmap, imageUri);

                performImageAction(bitmap, getBase64String(bitmap),file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*
     *
     *
     * Request Permission Result
     *
     * */

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d("TAG", "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAG", "Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                    dispatchTakeImageIntent();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        if (!showRationale) {
                            // user also CHECKED "never ask again"
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            openPermissionActivityFromSetting(this);
                        }
                    }

                }else {
                    //progress.dismiss();
                }
                break;

        }
    }



    @Override
    public void galleryCallback(AlertDialog alertDialog) {
        alertDialog.dismiss();
        openGallery();

    }

    @Override
    public void cameraCallback(AlertDialog alertDialog) {
        alertDialog.dismiss();
        openCamera();
    }



    public boolean isWriteStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted2");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked2");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted2");
            return true;
        }
    }


    public File createImageFile(String mCurrentPhotoPath) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        return image;
    }

    public Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {
        try {
            InputStream input = context.getContentResolver().openInputStream(selectedImage);
            ExifInterface ei;
            if (Build.VERSION.SDK_INT > 23)
                ei = new ExifInterface(input);
            else
                ei = new ExifInterface(selectedImage.getPath());

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return img;
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public String getBase64String(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos); //bm is the bitmap object
        byte[] byteArrayImage = baos.toByteArray();
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT).replaceAll("\\s", "");
    }

    public void openPermissionActivityFromSetting(Activity activity){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

}

