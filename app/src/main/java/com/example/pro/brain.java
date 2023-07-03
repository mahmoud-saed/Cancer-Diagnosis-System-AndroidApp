package com.example.pro;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pro.ml.Brainn;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.schema.Model;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class brain extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn,detail , back1;
    TextView result , explane,rec ;
    Bitmap img;
    int element;
    float maxConfidence=-1;
    float temp=-1 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain);


        getPermission();


        imageView = findViewById(R.id.imageView);
        result = findViewById(R.id.result);
        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        captureBtn = findViewById(R.id.captureBtn);
        back1=findViewById(R.id.back1);


        detail= findViewById(R.id.detail);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (temp== 1) {
                    Intent intd=new Intent(brain.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation"," is a tumor that forms on membranes that cover the brain and spinal cord just inside the skull.");
                    intd.putExtra("Reccomendation","see a doctor;may order a brain scan: an MRI and/or a CT scan. These will allow the doctor to locate the meningioma and determine its size");
                    startActivity(intd);



                } else if (temp== 0) {
                    Intent intd=new Intent(brain.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation"," primary brain tumors , can affect your brain function and be life-threatening depending on its location and rate of growth..");
                    intd.putExtra("Reccomendation","treatment options include surgery, radiation therapy, chemotherapy, targeted therapy and experimental clinical trials.");
                    startActivity(intd);



                } else if (temp==2) {
                    Intent intd=new Intent(brain.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Normal");
                    intd.putExtra("Reccomendation","Avoid dangerous radiation: If the body is exposed to some radiation, such as: cancer radiation, nuclear bomb radiation, it leads to cell distortion and disorder.");
                    startActivity(intd);

                }else if (temp==3){
                    Intent intd=new Intent(brain.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","abnormal growths that develop in your pituitary gland");
                    intd.putExtra("Reccomendation","If you know that multiple endocrine neoplasia, type 1 (MEN 1) runs in your family, talk to your doctor about periodic tests that may help detect a pituitary tumor early.");
                    startActivity(intd);


                }
                else{
                    Intent intd=new Intent(brain.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();

                    intd.putExtra(" ", " ");
                    intd.putExtra(" ", " ");
                    startActivity(intd);



                }


            }
        });








        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 100);

            }
        });


//        rec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//
//
//            }
//        });


        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    img = Bitmap.createScaledBitmap(img, 224, 224, true);
//                    Bitmap image=androidGrayScale(img);
                    Brainn model = Brainn.newInstance(getApplicationContext());


                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

//                    ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*80*80*1);
//                    byteBuffer.order(ByteOrder.nativeOrder());
//                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
//                    tensorImage.load(androidGrayScale(img));
//                    ByteBuffer byteBuffer = tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Brainn.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    int arr[]={0,0,0,1};



                    temp=getMax(outputFeature0.getFloatArray());

                    if (maxConfidence < 0.93) {
                        result.setText("Other , Try Another Image");

                    } else {

                        if (getMax(outputFeature0.getFloatArray()) == 1) {
                            result.setText("Meningioma");


                        } else if (getMax(outputFeature0.getFloatArray()) == 0) {
                            result.setText("Glioma");


                        } else if (getMax(outputFeature0.getFloatArray()) == 2) {
                            result.setText("No Tumor");

                        } else if (getMax(outputFeature0.getFloatArray()) == 3) {
                            result.setText("Pituatary");

                        }
                    }










                    // Releases model resources if no longer used.
                    model.close();





                    /* result.setText(getMax(outputFeature0.getFloatArray())+" ");*/





                } catch (IOException e) {
                    // TODO Handle the exception
                }

            }
        });

    }
    float getMax(float[] arr) {
        element = 0;
        maxConfidence = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[element]) {
                element = i;
                maxConfidence = arr[i];
//                Toast.makeText(this, "" + element, Toast.LENGTH_SHORT).show();
//                break;
            }
        }
        return element;
    }

//    private Bitmap androidGrayScale(final Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix colorMatrix = new ColorMatrix();
//        colorMatrix.setSaturation(0);
//        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
//        paint.setColorFilter(colorMatrixFilter);
//        canvas.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }




    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(brain.this,new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==11){
            if(grantResults.length>0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getPermission();

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100)
        {
            imageView.setImageURI(data.getData());

            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

//        }else if(requestCode==200){
//
//
        }

        else if(requestCode==12){
            img=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(img);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
