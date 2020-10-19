package com.example.practicaimagenes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private Button btnAddPermissions=null;
    private Button btnLoadImage=null;
    private int cont=0;
    private Button btnSaveImage=null;
    private TimerTask mTimerTask=null;
    private int currentTime=3;
    private int numero=-1;
    private Timer mTimer = null;
    private String [] images={"https://www.lahiguera.net/musicalia/artistas/varios/disco/7749/skillet_unleashed-portada.jpg",
            "https://photos.bandsintown.com/thumb/8385452.jpeg",
            "https://img.discogs.com/JgpIP_cU7hPeKDwQTjuBtVdeaUc=/fit-in/300x300/filters:strip_icc():format(jpeg):mode_rgb():quality(40)/discogs-images/R-670340-1384697266-6840.jpeg.jpg"};
    private int countBanner=1;
    private ImageView imageView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAddPermissions = (Button) findViewById(R.id.btnAddPermissions);

        //Evento en el que pedimos al usuario permisos para guardar datos en almacenamiento externo
        btnAddPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Comprobamos si los permisos fueron concedidos anteriormente
                if(PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                    //En caso de que no fueran concedidos con anterioridad mostramos el diálogo para concederlos
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                    Toast.makeText(getApplicationContext(), "Permiso concedido!!!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Permiso concedido en el pasado!!!",Toast.LENGTH_SHORT).show();
                }
            }

        });

        //cargamos las imagenes
        btnLoadImage = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagenes();
                numero=0;
            }
        });

        //guardamos la imagen
        btnSaveImage = (Button) findViewById(R.id.button3);
        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(numero==-1){
                    Toast.makeText(getApplicationContext(), "No hay imagen que guardar", Toast.LENGTH_SHORT).show();
                } else if (cont!=0&&numero!=-1){
                    numero=cont-1;
                   saveImage(images[cont-1]);
                }else{
                    numero=cont+2;
                    saveImage(images[cont+2]);

                }
            }
        });

    }


    //metodo para cargar imagenes
    public void loadImage(String image){
        Picasso.get().load(image).resize(400,400).into(imageView);
    }

    public void saveImage(String image){

        Picasso.get().load(image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try{
                    String nombre="";
                    if(numero==0){
                        nombre="Skillet";
                    }else if(numero==1){
                        nombre="LP";
                    }else{
                        nombre="Three Days Grace";
                    }
                    File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                    if (!directory.exists()){
                        directory.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(new File(directory, new Date().toString().concat(nombre).concat(".jpg")));
                    bitmap.compress(Bitmap.CompressFormat.JPEG,90, fos);
                    Toast.makeText(getApplicationContext(),"Imagen guardada de " + nombre,Toast.LENGTH_SHORT).show();
                    fos.flush();
                    fos.close();
                }catch(FileNotFoundException e){

                }catch (IOException e){

                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    //metodo que utilizamos para cargar las imagenes en el imageview
    public void cargarImagenes(){
        mTimerTask = new TimerTask(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentTime++;
                        if(currentTime%4==0){
                            loadImage(images[cont]);
                            abrirYoutube();
                            cont++;
                        }
                        if (cont==3){
                            cont=0;
                        }
                    }
                });

            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask,1,1000);
    }

    //metodo que utilizamos para abrir los enlaces de youtube dependiendo del contador
    public void abrirYoutube(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri [] uri = {Uri.parse("https://www.youtube.com/watch?v=gXo5-Q6xOtk&ab_channel=Skillet"),
                        Uri.parse("https://www.youtube.com/watch?v=ScNNfyq3d_w&ab_channel=LinkinPark"),
                        Uri.parse("https://www.youtube.com/watch?v=d8ekz_CSBVg&ab_channel=ThreeDaysGraceVEVO")};
                Intent intent=null;
                if (cont!=0){
                    intent = new Intent(Intent.ACTION_VIEW, uri[cont-1]);
                }else{
                    intent = new Intent(Intent.ACTION_VIEW, uri[cont+2]);
                }
                startActivity(intent);
            }
        });
    }


}