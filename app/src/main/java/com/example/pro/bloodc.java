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



import com.example.pro.ml.Bloood;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class bloodc extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn ,detail ,back1;
    TextView result,explane,rec ;
    Bitmap img;
    int element;
    float maxConfidence=-1;
    float temp=-1 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodc);


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


                if (temp == 0) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","When your body produces too many basophils.which are a white blood cell type that protects your body from infections,may be a sign you have an infection, or it may be a sign of serious medical conditions like leukemia or autoimmune disease.");
                    intd.putExtra("Reccomendation","Once you know what’s causing your basophilia,You should contact your healthcare provider any time,your basophilia symptoms or other conditions’ symptoms worsen or you develop new symptoms");
                    startActivity(intd);



                }  else if (temp == 1) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Are one of several white blood cells that support your immune system. Chronic eosinophilic leukemia is a subtype of clonal eosinophilia, meaning it is caused by a new genetic mutation or change in the blood cells.");
                    intd.putExtra("Reccomendation","Treatment options and recommendations depend on several factors, including the type of leukemia and possible side effects. Take time to learn about all of your treatment options and be sure to ask questions about things that are unclear.");
                    startActivity(intd);
                }
                else if (temp == 2) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","It’s a rare subtype of acute myeloid leukemia (AML) and is sometimes called acute erythroid leukemia. ");
                    intd.putExtra("Reccomendation","If a doctor has diagnosed you with erythroleukemia or you’re not sure which subtype of AML you have, talk with your doctor to learn how your AML subtype might affect your treatment and outlook.");
                    startActivity(intd);


                }
                else if(temp == 3) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Is the most common leukemia in adults. It's a type of cancer that starts in cells that become certain white blood cells in the bone marrow. The cancer cells start in the bone marrow but then go into the blood.");
                    intd.putExtra("Reccomendation"," Talk to your medical team about ways to relieve any symptoms you have. Also ask your doctor about changes to your diet and exercise that can help you feel better during your treatment. ");
                    startActivity(intd);


                }
                else if(temp == 4) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();

                    intd.putExtra("Explanation","Cancer in monocytes is called chronic myelomonocytic leukemia which is having too many monocytes, and can settle in the spleen or liver causing an enlargement of these organs.");
                    intd.putExtra("Reccomendation","Eat a well-balanced diet that limits foods that cause inflammation like red meats, try to stay well-rested,Reduce stress,and Protect yourself against infections");
                    startActivity(intd);
                }
                else if(temp == 5) {
                    Intent intd=new Intent(bloodc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","When you have cancer, your levels of certain blood cells can go below normal. Platelets are one of these types of blood cells.");
                    intd.putExtra("Reccomendation","When you have a low platelet count, take extra care to avoid situations that could cause bleeding. Ask your doctor before drinking alcohol or taking any new medication, including over the counter pain relievers.");
                    startActivity(intd);
                }
//

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
                    Bloood model = Bloood.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Bloood.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    //int arr[]={0,0,0,0,1};
//                    Toast.makeText(bloodc.this, ""+getMax(outputFeature0.getFloatArray()), Toast.LENGTH_SHORT).show();

                    temp=getMax(outputFeature0.getFloatArray());

                    if (maxConfidence < 0.70) {
                        result.setText("Other , Try Another Image");

                    } else{
                        if (temp == 1) {
                            result.setText("Eosinophil'");

                        } else if (temp == 0) {
                            result.setText("Basophil");
                        }else if (temp==2){
                            result.setText("Erythroblast");

                        }
                        else if(temp==3){
                            result.setText("lymphocyte");

                        }else if(temp==4) {
                            result.setText("Monocyte");
                        }
//
                        else if(temp==5){
                            result.setText("Platelet");
                        }
                    }

                    // Releases model resources if no longer used.
                    model.close();

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
                //break;
                maxConfidence = arr[i];
            }
        }
        return element;
    }




    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(bloodc.this,new String[]{Manifest.permission.CAMERA},11);
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

        else if(requestCode==12){
            img=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(img);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}

