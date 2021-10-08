package com.max.deffe.tron.facedetector;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.sql.Time;

public class MainActivity extends AppCompatActivity
{

    private ImageView imageView;
    private Uri mHighQualityImageUri = null;
    private final int REQUEST_CODE_CAPTURE_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.image_view);

        Button captureButton = findViewById(R.id.capture_button);

        captureButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mHighQualityImageUri = generateTimeStampPhotoFileUri();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_IMAGE);
            }
        });
    }

    private Uri generateTimeStampPhotoFileUri()
    {

        Uri photoFileUri = null;
        File outputDir = getPhotoDirectory();
        if (outputDir != null)
        {
            File photoFile = new File(outputDir, System.currentTimeMillis() + ".jpg");
            photoFileUri = Uri.fromFile(photoFile);
        }
        return photoFileUri;
    }

    private File getPhotoDirectory() {
        File outputDir = null;
        String externalStorageStagte = Environment.getExternalStorageState();
        if (externalStorageStagte.equals(Environment.MEDIA_MOUNTED)) {
            File photoDir = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            outputDir = new File(photoDir, getString(R.string.app_name));
            if (!outputDir.exists())
                if (!outputDir.mkdirs()) {
                    Toast.makeText(
                            this,
                            "Failed to create directory "
                                    + outputDir.getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                    outputDir = null;
                }
        }
        return outputDir;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAPTURE_IMAGE)
        {
            if (data != null)
            {
                imageView.setImageURI(mHighQualityImageUri);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://"
                                + Environment.getExternalStorageDirectory())));
            }
        }
    }
}
