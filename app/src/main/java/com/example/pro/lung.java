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


import com.example.pro.ml.Lung;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class lung extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn , detail,back1;
    TextView result,explane,rec;
    Bitmap img;
    int element;
    float maxConfidence=-1;

    float temp=-1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lung);


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
                if (temp == 1) {
                    Intent intd=new Intent(lung.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","A benign lung tumor is an abnormal growth of tissue that serves no purpose and is found not to be cancerous.\n" +
                            " Benign lung tumors may grow from many different structures in the lung.\n" +
                            "Determining whether a nodule is a benign tumor or an early stage of cancer is very important. \n" +
                            "That's because early detection and treatment of lung cancer can greatly enhance your survival.");
                    intd.putExtra("Reccomendation","Adults aged 50 to 80 years who have a 20 pack-year smoking history and currently smoke or have quit within the past 15 years:\n" +
                            "Screen for lung cancer with low-dose computed tomography (CT) every year.\n" +
                            "Stop screening once a person has not smoked for 15 years or has a health problem ");
                    startActivity(intd);

                } else if (temp==0) {
                    Intent intd=new Intent(lung.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","It falls under the umbrella of non-small cell lung cancer (NSCLC) and has a strong\n" +
                            "association with previous smoking. it remains the leading cause of cancer death,Adenocarcinoma of the lung usually evolves from the mucosal glands \n" +
                            "and represents about 40% of all lung cancers. It is the most common subtype to be diagnosed \n" +
                            "in people who have never smoked. Lung adenocarcinoma usually occurs in the lung periphery, \n" +
                            "and in many cases, may be found in scars or areas of chronic inflammation. This activity describes \n" +
                            "the pathophysiology of adenocarcinoma of the lung and highlights the role of the interprofessional team in its management.");
                    intd.putExtra("Reccomendation","Adults aged 50 to 80 years who have a 20 pack-year smoking history and currently smoke or have quit within the past 15 years:\n" +
                            "Screen for lung cancer with low-dose computed tomography (CT) every year.\n" +
                            "Stop screening once a person has not smoked for 15 years or has a health problem ");
                    startActivity(intd);

                }
                else if(temp==2) {
                    Intent intd=new Intent(lung.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Non-small cell lung carcinoma (NSCLC) makes up approximately 85% of all lung cancers and is characterized by transformed \n" +
                            "lung epithelium. The most common types of NSCLC are adenocarcinoma, large cell carcinoma, and squamous cell carcinoma.\n" +
                            " Lung adenocarcinoma is the most common lung cancer among non-smokers,\n" +
                            " while squamous cell carcinoma is the most closely associated type of lung cancer with smokers.\n" +
                            " Large cell carcinoma, meanwhile,\n" +
                            "encompasses a heterogeneous group of malignant neoplasms that are undifferentiated from lung epithelium. ");
                    intd.putExtra("Reccomendation","Adults aged 50 to 80 years who have a 20 pack-year smoking history and currently smoke or have quit within the past 15 years:\n" +
                            "Screen for lung cancer with low-dose computed tomography (CT) every year.\n" +
                            "Stop screening once a person has not smoked for 15 years or has a health problem ");
                    startActivity(intd);

                }
                else{
                    Intent intd=new Intent(lung.this, com.example.pro.detail.class);
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

//
//        select.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 200);
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
                    Lung model = Lung.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Lung.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    int arr[]={0,0,1};
//                    Toast.makeText(lung.this, ""+getMax(outputFeature0.getFloatArray()), Toast.LENGTH_SHORT).show();



                        temp=getMax(outputFeature0.getFloatArray());
                    if (maxConfidence < 0.70) {
                        result.setText("Other , Try Another Image");

                    } else {

                        if (getMax(outputFeature0.getFloatArray()) == 1) {
                            result.setText("Benign");
                        } else if (getMax(outputFeature0.getFloatArray()) == 0) {
                            result.setText("Adenocarcinoma");
                        } else {
                            result.setText("Squamous Cell Carcinoma");
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




    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(lung.this,new String[]{Manifest.permission.CAMERA},11);
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

