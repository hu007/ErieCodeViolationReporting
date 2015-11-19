package com.eriecodeviolationreporting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by MY on 11/2/2015.
 */
public class ImagePreview extends Activity implements View.OnClickListener {

    TextView continueBtn, cancelBtn;
    ImageView imageView, imageView2, imageView3, imageView4, imageView5,
            imageView6, imageView7, imageView8, imageView9;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int SELECT_PICTURE = 200;
    private static final String IMAGE_DIRECTORY_NAME = "Erie";
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    Uri fileAttachment;

    ImageView getImageView;
    int imageId;
    private String selectedImagePath;

    ArrayList<String> uriList = new ArrayList<String>();

    String latitude, longtiude, address, date, time, addDetail;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        imageView = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);
        imageView4 = (ImageView) findViewById(R.id.image4);
        imageView5 = (ImageView) findViewById(R.id.image5);
        imageView6 = (ImageView) findViewById(R.id.image6);
        imageView7 = (ImageView) findViewById(R.id.image7);
        imageView8 = (ImageView) findViewById(R.id.image8);
        imageView9 = (ImageView) findViewById(R.id.image9);
        continueBtn = (TextView) findViewById(R.id.continueBtn);
        cancelBtn = (TextView) findViewById(R.id.cancelBtn);

        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
        imageView9.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        latitude = user.get(SessionManager.LATITUDE);
        longtiude = user.get(SessionManager.LONGITUDE);
        address = user.get(SessionManager.ADDRESS);
        date = user.get(SessionManager.DATE);
        time = user.get(SessionManager.TIME);

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }

    }

    public boolean isDeviceSupportCamera() {

        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }

    }

    private void CaptureImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ImagePreview.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }

                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);

                }

                else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        previewCaptureImage();
                        fileAttachment = Uri.parse("file://" + fileUri.getPath());
                        uriList.add("file://" + fileUri.getPath());
                        break;

                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getApplicationContext(),
                                "User cancelled image capture", Toast.LENGTH_SHORT)
                                .show();
                        break;

                }
                break;
        }

        switch (requestCode) {
            case SELECT_PICTURE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = getSelectGalleryPath(selectedImageUri);
                        uriList.add("file://" + selectedImagePath);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        final Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath, options);
                        getImageView.setImageBitmap(bitmap);
                        break;

                }
                break;
        }

    }

    public String getSelectGalleryPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void previewCaptureImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            getImageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.image1:
                imageId = R.id.image1;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image2:
                imageId = R.id.image2;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image3:
                imageId = R.id.image3;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image4:
                imageId = R.id.image4;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image5:
                imageId = R.id.image5;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image6:
                imageId = R.id.image6;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image7:
                imageId = R.id.image7;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image8:
                imageId = R.id.image8;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.image9:
                imageId = R.id.image9;
                getImageView = (ImageView)v;
                getImageView.findViewById(imageId);
                CaptureImage();
                break;

            case R.id.continueBtn:
                Intent intent = new Intent(ImagePreview.this, Preview.class);
                intent.putExtra("uri_list", uriList);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longtiude);
                intent.putExtra("address", address);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                startActivity(intent);
                break;
            case R.id.cancelBtn:
                Intent intent2 = new Intent(ImagePreview.this, Report.class);
                startActivity(intent2);
                break;

            default:
                break;
        }

    }


}
