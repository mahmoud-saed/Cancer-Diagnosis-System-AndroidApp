package com.example.pro;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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


import com.example.pro.ml.Breast;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Breastc extends AppCompatActivity {

    ImageView imageView,imageView6;
    Button selectBtn, predictBtn, captureBtn,detail,back1 ;
    TextView result,explane,rec;
    Bitmap img;
    int element;
    float maxConfidence=-1;

    float temp=-1 ;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breastc);


        getPermission();
        back1=findViewById(R.id.back1);



        imageView = findViewById(R.id.imageView);
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
                    Intent intd=new Intent(Breastc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","(non-cancerous) breast conditions are very common, and most women have them. In fact, most breast changes are benign. Unlike breast cancers, benign breast conditions are not life-threatening. But some are linked with a higher risk of getting breast cancer later on. ");
                    intd.putExtra("Reccomendation","Most types of benign breast disease don't require treatment. Your healthcare provider may recommend treatment if you have atypical hyperplasia or a different kind of benign breast disease that increases your future risk of breast cancer. Some benign breast changes may cause signs or symptoms . consult a doctor or a specialist in this case.");
                    startActivity(intd);

                }
                else if(temp==1){
                    Intent intd=new Intent(Breastc.this, com.example.pro.detail.class);
                    Bundle b= new Bundle();
                    intd.putExtra("Explanation","tumor that grows in or around the breast tissue, mainly in the milk ducts and glands. A tumor usually starts as a lump or calcium deposit that develops as a result of abnormal cell growth.");
                    intd.putExtra("Reccomendation","An early detection, frequently medical therapy, such as endocrine therapy or chemotherapy will be recommended first to decrease the size of the tumor in the breast, or decrease the disease and the lymph nodes, and importantly to evaluate the response of the cancer to the treatment");
                    startActivity(intd);

                }
                else{
                    Intent intd=new Intent(Breastc.this, com.example.pro.detail.class);
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
                    img = Bitmap.createScaledBitmap(img, 227, 227, true);
                    Breast model = Breast.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 227, 227, 3}, DataType.FLOAT32);

                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();

                    inputFeature0.loadBuffer(byteBuffer);
//                    (outputFeature0.getFloatArray())
                    // Runs model inference and gets result.
                    Breast.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    int arr[]={0,1};
//                    Toast.makeText(Breastc.this, ""+getMax(outputFeature0.getFloatArray()), Toast.LENGTH_SHORT).show();


                    temp=getMax(outputFeature0.getFloatArray());
                    float[] confidences = outputFeature0.getFloatArray();
                    //float maxConfidence=0;
                    for (int i=0 ; i<confidences.length;i++){
                        if(confidences[i]>maxConfidence){
                            maxConfidence=confidences[i];
                        }
                    }

//                    Toast.makeText(Breastc.this, ""+maxConfidence, Toast.LENGTH_SHORT).show();
                    if (maxConfidence < 0.98) {
                        result.setText("Other , Try Another Image");

                    } else {

                        if (getMax(outputFeature0.getFloatArray()) == 0) {
                            result.setText("Benign Masses");
                        } else {
                            result.setText("Malignant Masses");
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
                ActivityCompat.requestPermissions(Breastc.this,new String[]{Manifest.permission.CAMERA},11);
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

