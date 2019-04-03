package com.example.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.*;

public class SharingImageActivity extends AppCompatActivity {

    private File mSavedPictureFile1, mSavedPictureFile2;
    private int REQUEST_PICTURE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_image);

        findViewById(R.id.open_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        findViewById(R.id.share_image_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageFromPrivateSpace();
            }
        });

        findViewById(R.id.share_image_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageFromInternalMemory();
            }
        });
    }

    private void shareImageFromInternalMemory() {
        if(mSavedPictureFile2 ==null){
            Toast.makeText(this, "Please Click a Picture First", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent kShareIntent = new Intent();
        kShareIntent.setAction(Intent.ACTION_SEND);
        kShareIntent.setType("image/*");
        kShareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,
                getPackageName() + ".provider", mSavedPictureFile2));
        kShareIntent.putExtra(Intent.EXTRA_TEXT, "This image was saved inside 'Internal Memory'");
        kShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(kShareIntent, "Send to"));
    }

    private void shareImageFromPrivateSpace() {
        if(mSavedPictureFile1 ==null){
            Toast.makeText(this, "Please Click a Picture First", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent kShareIntent = new Intent();
        kShareIntent.setAction(Intent.ACTION_SEND);
        kShareIntent.setType("image/*");
        kShareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this,
                getPackageName() + ".provider", mSavedPictureFile1));
        kShareIntent.putExtra(Intent.EXTRA_TEXT, "This image was inside 'App's Private Space'");
        kShareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(kShareIntent, "Send to"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show();
            mSavedPictureFile1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + getPackageName() + "/images/" + mSavedPictureFile2.getName());
            try {
                FileUtils.copyFile(mSavedPictureFile2, mSavedPictureFile1);
            } catch (IOException e) {
                Log.v("yash", e.getMessage());
            }
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mSavedPictureFile2 = getOutputMediaFile();
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(this,
                        getPackageName() + ".provider", mSavedPictureFile2));
        startActivityForResult(pictureIntent, REQUEST_PICTURE_CAPTURE);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir1 = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        if (!mediaStorageDir1.exists()) {
            if (!mediaStorageDir1.mkdirs()) {
                return null;
            }
        }
        String kTimeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir1.getPath() + File.separator +
                "Img_" + kTimeStamp + ".jpg");
    }

}
