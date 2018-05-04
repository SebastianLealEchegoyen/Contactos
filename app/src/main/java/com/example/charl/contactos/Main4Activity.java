package com.example.charl.contactos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class Main4Activity extends AppCompatActivity {

    private static final int PICK_IMAGE = 101;

    Uri imgu;

    EditText name;
    EditText Lname;
    EditText phone;
    EditText Id;
    EditText adress;
    TextView calen;
    ImageView imgv;
    Button boton;
    EditText mail;
    Uri imgp;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        imgv = (ImageView)findViewById(R.id.editimg);
        name = (EditText) findViewById(R.id.editname);
        Lname = (EditText) findViewById(R.id.editapellido);
        phone = (EditText) findViewById(R.id.edittel);
        Id =  (EditText) findViewById(R.id.editid);
        adress= (EditText) findViewById(R.id.editad);
        calen= (TextView) findViewById(R.id.editcal);
        mail= (EditText) findViewById(R.id.editmail);
        boton= (Button) findViewById(R.id.clicker);

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        calen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast1 =
                        Toast.makeText(getApplicationContext(),
                                "Elige el cumpleaños", Toast.LENGTH_SHORT);

                toast1.show();

                Intent add = new Intent(getApplicationContext(),calendario.class);
                startActivityForResult(add,1);



            }
        });
        
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imgu = data.getData();
            imgv.setImageURI(imgu);

            if ( PermissionChecker.checkSelfPermission(getApplicationContext(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&  PermissionChecker.checkSelfPermission(getApplicationContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {

                bitmap = decodeSampledBitmapFromUri(getApplicationContext(),imgu,0,500);

                if (bitmap != null) {

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(Main4Activity.this.getContentResolver(), bitmap, "Title", null);
                    imgp = Uri.parse(path);
                }
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
            }
        }



        if(resultCode == RESULT_OK && requestCode == 1){
            if(data.hasExtra("Fecha")==true){
                calen.setText(data.getStringExtra("Fecha"));
            }

        }
    }
    public static Bitmap decodeSampledBitmapFromUri(Context context,
                                                    Uri imageUri, int rotate, int maxDimension) {
        //

        try {// First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream is;
            options.inJustDecodeBounds = true;

            is = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(is, null, options);
            is.close();

            // rotate as necessary
            int rotatedWidth, rotatedHeight;

            int orientation = 0;

            // if we have a rotation use it otherwise look at the EXIF
            if (rotate > -1) {
                orientation = rotate;
            }
            if (orientation == 90 || orientation == 270) {
                rotatedWidth = options.outHeight;
                rotatedHeight = options.outWidth;
            } else {
                rotatedWidth = options.outWidth;
                rotatedHeight = options.outHeight;
            }

            Bitmap srcBitmap;
            is = context.getContentResolver().openInputStream(imageUri);
            if (rotatedWidth > maxDimension || rotatedHeight > maxDimension) {
                float widthRatio = ((float) rotatedWidth)
                        / ((float) maxDimension);
                float heightRatio = ((float) rotatedHeight)
                        / ((float) maxDimension);
                float maxRatio = Math.max(widthRatio, heightRatio);

                // Create the bitmap from file
                options = new BitmapFactory.Options();
                options.inSampleSize = (int) Math.round(maxRatio);

                srcBitmap = BitmapFactory.decodeStream(is, null, options);
            } else {
                srcBitmap = BitmapFactory.decodeStream(is);
            }

            is.close();
            if (srcBitmap != null) {

                if (orientation > 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(orientation);

                    srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                            srcBitmap.getWidth(), srcBitmap.getHeight(),
                            matrix, true);

                }
            }

            return srcBitmap;
        }

        catch (Exception e) {
        }
        return null;

    }
}
