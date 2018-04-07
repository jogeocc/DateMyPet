package com.example.jgchan.datemypet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jgchan.datemypet.conexion.RetrofitBuilder;
import com.example.jgchan.datemypet.conexion.Utils;
import com.example.jgchan.datemypet.conexion.apiService;
import com.example.jgchan.datemypet.entidades.AccessToken;
import com.example.jgchan.datemypet.entidades.ApiError;
import com.example.jgchan.datemypet.entidades.Permiso;
import com.example.jgchan.datemypet.entidades.Success;
import com.example.jgchan.datemypet.entidades.Usuarios;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.view.UCropView;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearMascotactivity extends MenuActivity {

    private AccessToken datosAlamcenados;

    public String mCurrentPhotoPath = null;
    Call<Success> call;
    Success success;
    String respuesta;
    apiService service;
    String id_user = null;
    private TokenManager tokenManager;
    TextView nombreUsuario, correoUsuario;
    ImageView fotoRegistroMascota;
    TextView txtRegMasNombre,txtRegMasRaza,txtRegMasSenPar,txtRegMasHobbie,txtRegMasEdad;
    Button btnRegMasRegistrar;
    RadioGroup rgRegMasSex;
    RadioButton rbRegMasMacho, rbRegMasHembra;
    Spinner spRegMasTipo;
    ProgressDialog progress;
    Uri imagenCortada;

    public int MY_REQUEST_CODE = 0;
    public int MY_ARCHIVOS = 1;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};

    Permiso objPermiso = new Permiso();



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_mascotactivity);


        //SOLICITANDO PERMISOS DE ESCRITURA Y CAMARA

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //para versiones con android 6.0 o superior.

                if(!objPermiso.hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }

        }
        //----------------------------------------------------------------------------

        // DEFINIENDO CONTROLLERS
            fotoRegistroMascota = (ImageView) findViewById(R.id.imgRegMas);
            txtRegMasNombre=(TextView)findViewById(R.id.txtRegMasNombre);
            txtRegMasRaza=(TextView)findViewById(R.id.txtRegMasRaza);
            txtRegMasSenPar=(TextView)findViewById(R.id.txtRegMasSenPar);
            txtRegMasHobbie=(TextView)findViewById(R.id.txtRegMasHobbie);
            txtRegMasEdad=(TextView)findViewById(R.id.txtRegMasEdad);
            btnRegMasRegistrar=(Button)findViewById(R.id.btnRegMasRegistrar);
            rbRegMasMacho=(RadioButton)findViewById(R.id.rbRegMasMacho);
            rbRegMasHembra=(RadioButton)findViewById(R.id.rbRegMasHembra);
            spRegMasTipo=(Spinner)findViewById(R.id.spnRegMasTipoMascota);
        //-----------------------------------------------------------------------------

        //INFLAR SPINNER CON LAS OPCIONES

            String []tipos={"Seleccione el tipo de mascota","Perro","Gato","Ave","Otros"};
            ArrayAdapter <String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, tipos);
            spRegMasTipo.setAdapter(adapter);


        //-------------------------------------------------------------------------------------------------------------


        //-------------- HABILITANDO BOTON REGISTRAR ----------------------------------------

            btnRegMasRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registrarMascota();
                }
            });


        //-----------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //RECUPERANDO DATOS DEL PREF
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        //PARSEARLO
        datosAlamcenados = tokenManager.getToken();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        //BUSCANDO CONTROLLERS MEDIANTE EL PADRE QUE LOS CONTIENE
        nombreUsuario = (TextView) headerView.findViewById(R.id.tvNombreCompleto);
        correoUsuario = (TextView) headerView.findViewById(R.id.tvCorreoUsuario);

        //SETEANDO LOS VALORES DE CORREO Y NOMBRE COMPLETO EN EL HEADER BUSCADOR
        nombreUsuario.setText("Bienvenido " + datosAlamcenados.getName_user());
        correoUsuario.setText(datosAlamcenados.getEmail());

        service = RetrofitBuilder.createService(apiService.class); //HABILITAMOS EL SERVICIO DE PETICION

        id_user = tokenManager.getToken().getId_user();


        //AGREGANDO EL METODO ONCLICK PARA LA FOTO DE LA MASCOTA

        fotoRegistroMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CrearMascotactivity.this, fotoRegistroMascota);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.menu_foto_mascota, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.pop_menu_camara:



                                try {
                                    dispatchTakePictureIntent(); //METODO PARA LLAMAR A LA CAMARA
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                break;

                            case R.id.pop_menu_galeria:

                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
                                break;

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(requestCode!= UCrop.REQUEST_CROP) msjCargando();
       if(resultCode!=RESULT_OK) progress.dismiss();

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = getIntent().getExtras();

                    Log.w("EXtras","Extras tiene"+tokenManager.getSaveFoto());

                    Uri imageUri = Uri.parse(tokenManager.getSaveFoto());
                    File file = new File(imageUri.getPath());


                    try {
                        InputStream ims = new FileInputStream(file);
                        fotoRegistroMascota.setImageBitmap(BitmapFactory.decodeStream(ims));
                    } catch (FileNotFoundException e) {
                        return;
                    }

                    galleryAddPic(imageUri);
                    cortarImagen(imageUri);


                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    //Toast.makeText(this, "Entro a gal", Toast.LENGTH_SHORT).show();
                    fotoRegistroMascota.setImageURI(imageUri);


                    //Toast.makeText(this, ""+Uri.parse(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+File.separator+"fotocortada.jpg")), Toast.LENGTH_LONG).show();


                    cortarImagen(imageUri);



                }
                break;

            case  UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {

                    progress.dismiss();
                    final Uri resultUri = UCrop.getOutput(data);
                    imagenCortada =resultUri;
                    fotoRegistroMascota.setImageURI(resultUri);
                    galleryAddPic(resultUri);
                    //Toast.makeText(this, ""+resultUri, Toast.LENGTH_SHORT).show();
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                    //Toast.makeText(this, ""+cropError, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void cortarImagen(Uri imageUri) {

        progress.dismiss();

        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        options.setToolbarTitle("Cortar Imagen");
        options.setActiveWidgetColor(ContextCompat.getColor(this,R.color.colorAccent));

        UCrop.of(imageUri, Uri.parse(crearNombreFoto()))
                .withAspectRatio(1, 1)
                .withOptions(options)
                .withMaxResultSize(500, 500)
                .start(CrearMascotactivity.this);
    }


    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra("ruta",mCurrentPhotoPath);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(getApplicationContext(),
                                "com.example.jgchan.datemypet.provider",
                                photoFile));
                startActivityForResult(takePictureIntent, 0);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "DMP_MIMASCOTA" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory()
                .getPath()+File.separator+Environment.DIRECTORY_PICTURES+File.separator+"DateMyPet", "Mis DateMyPet Fotos");

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
       // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        //Guardando ruta en el archivo persistente
        tokenManager.auxSaveFoto(mCurrentPhotoPath);
        return image;
    }


    private String crearNombreFoto(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "DMP_MIMASCOTA_CORT" + timeStamp + "_";
        String nombre =Environment.getExternalStorageDirectory()
                .getPath()+File.separator+Environment.DIRECTORY_PICTURES+File.separator+"DateMyPet"+File.separator+"Perfil_Mascotas"+File.separator+imageFileName+".jpg";
        return nombre;
    }

    private void msjCargando(){
        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Cargando foto...");
        progress.setCancelable(false);
        progress.show();
    }



    private void registrarMascota(){

        progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Registrando Mascota...");
        progress.setCancelable(false);
        progress.show();


        MultipartBody.Part masFotoFile=null;

        if(imagenCortada!=null) {
            File file =new File(imagenCortada.getPath());
            masFotoFile = MultipartBody.Part.createFormData("masFotoFile", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }


        RequestBody idUsuario = RequestBody.create(MediaType.parse("text/plain"), id_user);
        RequestBody masNombre = RequestBody.create(MediaType.parse("text/plain"), txtRegMasNombre.getText().toString());
        RequestBody masRaza = RequestBody.create(MediaType.parse("text/plain"), txtRegMasRaza.getText().toString());
        RequestBody masTipo = RequestBody.create(MediaType.parse("text/plain"), spRegMasTipo.getSelectedItem().toString());
        RequestBody masSexo = RequestBody.create(MediaType.parse("text/plain"), sexoMascota());
        RequestBody masEdad = RequestBody.create(MediaType.parse("text/plain"), txtRegMasEdad.getText().toString());
        RequestBody masSenaPart = RequestBody.create(MediaType.parse("text/plain"), txtRegMasSenPar.getText().toString());
        RequestBody masHobbie = RequestBody.create(MediaType.parse("text/plain"), txtRegMasHobbie.getText().toString());

        call= service.registrarMascota(
                idUsuario,
                masFotoFile,
                masNombre,
                masRaza,
                masTipo,
                masSexo,
                masEdad,
                masSenaPart,
                masHobbie
        );

        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {

                progress.dismiss();
                // Toast.makeText(IngresarActivity.this, "Codigo: "+response.body().getAccessToken() , Toast.LENGTH_LONG).show();
                //Toast.makeText(EditarPerfilActivity.this, "Codigo: "+response , Toast.LENGTH_LONG).show();
                //return;
                //Log.w(TAG, "onResponse: "+response);
                if(response.isSuccessful()){
                    progress.dismiss();
                    respuesta=response.body().getSuccess();
                    tokenManager.eliminarSaveFoto();
                    msjExito(respuesta);


                }else{
                    progress.dismiss();

                    if(response.code()==401){
                        handleErrors(response.errorBody() );
                    }else {
                        Log.e("Error Server",response.message()+ " "+response.code());
                    }




                }

            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                //Log.w(TAG,"onFailure: "+t.getMessage());

                progress.dismiss();
                Toast.makeText(CrearMascotactivity.this, "Error vuelva intentarlo mas tarde" , Toast.LENGTH_LONG).show();
            }
        });


    }

    private String sexoMascota() {

        if(rbRegMasMacho.isChecked()) return  "M";
        else return "H";

    }


    public void msjErrores(String Error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡UPPS!")
                .setMessage("La cuenta no se pudo registrar por los siguientes motivos: \n\n"+Error+"")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void handleErrors(ResponseBody response){
        progress.dismiss();
        String errores="";

        ApiError apiError = Utils.converErrors(response);


        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()){
            if (error.getKey().equals("masNombre")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("masRaza")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("masTipo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("masSexo")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("masEdad")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

            if (error.getKey().equals("masSenaPart")){
                errores+="- "+error.getValue().get(0)+"\n";
            }

        }

        msjErrores(errores);


    }

    public void msjExito(String respuesta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¡FELICIDADES!")
                .setMessage(respuesta)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(CrearMascotactivity.this,ListaMascotasActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void galleryAddPic(Uri direccionFoto) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(direccionFoto);
        this.sendBroadcast(mediaScanIntent);
    }


}
