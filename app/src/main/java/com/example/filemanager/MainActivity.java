package com.example.filemanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_READ_WRITE_EXTERNAL_STORAGE_CAMERA = 202;
    private Button mBtnFileExplorer;
    private Button mBtnShareImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        mBtnFileExplorer = findViewById(R.id.file_explorer_btn);
        mBtnFileExplorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("deepak", "clicked");
                Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
                Intent kIntent = new Intent(MainActivity.this, FileExplorerActivity.class);
                startActivity(kIntent);
            }
        });

        mBtnShareImage = findViewById(R.id.sharing_image_btn);
        mBtnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kIntent = new Intent(MainActivity.this, SharingImageActivity.class);
                startActivity(kIntent);
            }
        });
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSIONS_READ_WRITE_EXTERNAL_STORAGE_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_READ_WRITE_EXTERNAL_STORAGE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission granted..", Toast.LENGTH_SHORT).show();
                } else {
                    askPermissions();
                }
                break;
        }
    }
}
