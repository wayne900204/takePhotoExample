package com.example.takepictureeee;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int AAA = 1002;
    private static final int BBB = 1003;
    private static final String TAG = "!23";


    Button mCaptureBtn,choose;
    ImageView mImageView;
    Uri image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCaptureBtn = findViewById(R.id.CaptureBtn);
        mImageView = findViewById(R.id.imageView);
        choose = findViewById(R.id.choose);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED){
                        //permission not enable request it
                        String[] permission ={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else{
                        openCamera();
                    }

                }
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                        //permission not enable request it
                        String[] permission ={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, AAA);
                    }
                    else{
                        pickImageFromGallery();
                    }

                }
                else{
                    pickImageFromGallery();
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,BBB);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NEW Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
    }
    // Handing permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called ,when user presses Allow or Deny form Permission RequestPopup
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    pickImageFromGallery();
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this,"Permission denied...",Toast.LENGTH_SHORT).show();
                }
            }
            case AAA:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                    //permission from popup was granted
//                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this,"Permission denied...",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requetCode, int resultCode, Intent data) {
        super.onActivityResult(requetCode, resultCode, data);
        Log.d(TAG, "onbB"+requetCode);
        Log.d(TAG, "onA"+resultCode);
        if (resultCode == RESULT_OK && requetCode == BBB) {
            mImageView.setImageURI(data.getData());
        }
        if (resultCode == RESULT_OK && requetCode == IMAGE_CAPTURE_CODE) {
            mImageView.setImageURI(image_uri);
        }
//        else{
//            Toast.makeText(this,"Permission denied...",Toast.LENGTH_SHORT).show();        }

    }
}