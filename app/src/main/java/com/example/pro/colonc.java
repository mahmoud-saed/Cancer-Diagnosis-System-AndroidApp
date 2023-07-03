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



import com.example.pro.ml.Coloon;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class colonc extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn,detail,back1;
    TextView result,explane,rec ;
    Bitmap img;
    int element;
    float maxConfidence=-1;

    float temp=-1 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colonc);


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
                if (temp== 0) {
                    Intent intd=new Intent(colonc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","Cancer that forms in the glandular tissue, which lines certain internal organs and makes and releases substances in the body, such as mucus, digestive juices, and other fluids.");
                    intd.putExtra("Reccomendation","the first line is surgery, it is done to remove cancer and some of the surrounding tissue. Chemotherapy.");
                    startActivity(intd);

                }
                else if(temp==1) {
                    Intent intd=new Intent(colonc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","usually discovered because a patient is examined for symptoms—such as rectal bleeding, changes in bowel habits ");
                    intd.putExtra("Reccomendation","Surgery should be performed, They are removed so they can be examined under a microscope to make a diagnosis. Surgery  is the usual treatment.");
                    startActivity(intd);

                }
                else{
                    Intent intd=new Intent(colonc.this, com.example.pro.detail.class);
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
                    Coloon model = Coloon.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Coloon.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    int arr[]={0,1};



                    temp=getMax(outputFeature0.getFloatArray());
                    float[] confidences = outputFeature0.getFloatArray();
                    //float maxConfidence=0;
                    for (int i=0 ; i<confidences.length;i++){
                        if(confidences[i]>maxConfidence){
                            maxConfidence=confidences[i];
                        }
                    }

//                    Toast.makeText(colonc.this, ""+maxConfidence, Toast.LENGTH_SHORT).show();
                    if (maxConfidence < 0.988) {
                        result.setText("Other , Try Another Image");

                    } else {

                        if (getMax(outputFeature0.getFloatArray()) == 0) {
                            result.setText("Adenocarcinoma");
                        } else {
                            result.setText("Benign Tissue");
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
        //maxConfidence = arr[0];



        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[element]) {
                element = i;
                //maxConfidence = arr[i];

//                Toast.makeText(this, "" + element, Toast.LENGTH_SHORT).show();
//                break;
            }
        }
        return element;
    }




    void getPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(colonc.this,new String[]{Manifest.permission.CAMERA},11);
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

