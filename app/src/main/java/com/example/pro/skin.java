package com.example.pro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pro.ml.Skinn;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class skin extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn,detail,back1;
    TextView result,explane,rec;
    Bitmap img;
    int element;
    float maxConfidence=-1;
    float temp=-1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);


        getPermission();

        imageView = findViewById(R.id.imageView);

        back1=findViewById(R.id.back1);

        result = findViewById(R.id.result);
        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        captureBtn = findViewById(R.id.captureBtn);

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
                if (temp == 1) {
                    Intent intd=new Intent(skin.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Malignant skin growths refer to abnormal and cancerous growths that occur in the skin. These growths can invade nearby tissues and have the potential to spread to other parts of the body, making them a serious health concern.");
                    intd.putExtra("Reccomendation","Seek immediate medical attention for malignant skin growths with a dermatologist or oncologist for a thorough evaluation. Develop a personalized treatment plan based on growth type, stage, and location, including surgery, radiation therapy, chemotherapy, immunotherapy, targeted therapy, or a combination.");
                    startActivity(intd);



                } else if (temp == 0) {
                    Intent intd=new Intent(skin.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","A non-cancerous tumour of the skin is a growth or abnormal area on the skin that does not spread to other parts of the body,not usually life-threatening. They usually don’t need any treatment but may be removed with surgery in some cases.");
                    intd.putExtra("Reccomendation","Visit a dermatologist to diagnose and monitor non-cancerous skin tumors, guiding on characteristics and potential risks. Discuss treatment options, including surgery, cryotherapy, or laser therapy.");
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
                    Skinn model = Skinn.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Skinn.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    int arr[]={0,1};




                    temp=getMax(outputFeature0.getFloatArray());
                    float[] confidences = outputFeature0.getFloatArray();
//                        float maxConfidence=0;
                    for (int i=0 ; i<confidences.length;i++){
                        if(confidences[i]>maxConfidence){
                            maxConfidence=confidences[i];
                        }
                    }

//                    Toast.makeText(skin.this, ""+maxConfidence, Toast.LENGTH_SHORT).show();
                    if (maxConfidence < 0.898) {
                        result.setText("Other , Try Another Image");

                    } else {


                        if (temp == 0) {
                            result.setText("Benign ");
                        } else {
                            result.setText("Malignant");
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
//        maxConfidence = arr[0];



        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[element]) {
                element = i;
//                maxConfidence = arr[i];

//                Toast.makeText(this, "" + element, Toast.LENGTH_SHORT).show();
//                break;
            }
        }
        return element;
    }




    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(skin.this,new String[]{Manifest.permission.CAMERA},11);
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

        }
//        else if(requestCode==200){
//
//
//        }

        else if(requestCode==12){
            img=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(img);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}

