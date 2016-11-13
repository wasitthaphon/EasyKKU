package kku.nukid.wasitthaphon.easykku;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

import static android.net.wifi.WifiConfiguration.Status.strings;

public class SignUpActivity extends AppCompatActivity {

    //Explicit (ประกาศตัวแปร)
    private EditText nameEditText, phoneEditText,
            userEditText, passwordEditText;
    private ImageView imageView;
    private Button button;      // Ctrl + Space autoFill
    private String nameString, phoneString, userString, passwordString,
    imagePathString, imageNameString;
    private Uri uri;
    private boolean aBoolean = true;
    private String urlAddUser = "http://swiftcodingthai.com/kku/add_user_master.php";
    private String urlImage = "http://swiftcodingthai.com/kku/Image";
    /*
        then FileZilla --> SomeFiles --> CopyUrl --> Phase in Browser --> Clear protocol then add
        (swiftcodingthai.com/kku/Image/IMG_xxxxx.jpg)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Bind Widget
        nameEditText = (EditText) findViewById(R.id.editText);  // Alt + Enter --> cast to
        phoneEditText = (EditText) findViewById(R.id.editText2);
        userEditText = (EditText) findViewById(R.id.editText3);
        passwordEditText = (EditText) findViewById(R.id.editText4);
        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button3);


        //SignUp Controller
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get Value From Edit Text
                nameString = nameEditText.getText().toString().trim();
                phoneString = phoneEditText.getText().toString().trim();
                userString = userEditText.getText().toString().trim();
                passwordString = passwordEditText.getText().toString().trim();

                //Check Space
                if (nameString.equals("") || phoneString.equals("") ||
                        userString.equals("") || passwordString.equals("")) {
                    //Have Space
                    Log.d("12novV1", "Have Space");
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.doremon48,
                            "มีช่องว่าง", "กรุณากรอกให้ครบทุกช่องค่ะ");
                    myAlert.myDialog();
                } else if (aBoolean) {
                    //None Choose Image
                    MyAlert myAlert = new MyAlert(SignUpActivity.this, R.drawable.nobita48,
                            "ยังไม่เลือกรูป", "กรุณาเลือกรูปด้วยค่ะ");
                    myAlert.myDialog();
                } else {
                    //Choose Image OK
                    upLoadImageToServer();
                    upLoadStringToServer(); //If creat with Anonymous it means in main Method outer cant use it

                }

            }   //onClick
        });

        // Image Controller
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกโปรแกรมดูภาพ"), 0);
            }// onClick
        });
    }   // Main Method

    private void upLoadStringToServer() {

        AddNewUser addNewUser = new AddNewUser(SignUpActivity.this);
        addNewUser.execute(urlAddUser);

    }   //upLoad

    //Create Inner Class
    private class AddNewUser extends AsyncTask<String, Void, String> {

        //Explicit
        private Context context;

        public AddNewUser(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormEncodingBuilder()
                        .add("isAdd", "true")
                        .add("Name", nameString)
                        .add("Phone", phoneString)
                        .add("User", userString)
                        .add("Password", passwordString)
                        .add("Image", urlImage + imageNameString)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strings[0]).post(requestBody).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("13novV1", "e doIn ==> " + e.toString());
                return null;
            }
        }   // doInBack --> Try to connect Network Sent Result to onPost

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("13novV1", "Result ==> " + s);
            if (Boolean.parseBoolean(s)) {
                Toast.makeText(context, "Upload Success", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }   // onPost

    }//<Sent ,  , Result>   AddNewUser Class

    private void upLoadImageToServer() {
        //Change Policy
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        try {
            SimpleFTP simpleFTP = new SimpleFTP();
            simpleFTP.connect("ftp.swiftcodingthai.com", 21,
                    "kku@swiftcodingthai.com","Abc12345");

            simpleFTP.bin();
            simpleFTP.cwd("Image");
            simpleFTP.stor(new File(imagePathString));
            simpleFTP.disconnect();

        } catch (Exception e) {
            Log.d("12novV1", "e simpleFTP ==> " + e.toString());
        }


    }   //Upload

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            Log.d("12novV1", "Result OK");
            aBoolean = false;

            //Show Image
            uri  = data.getData();
            try {

                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(uri));
                imageView.setImageBitmap(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }

            //Find Path of Image
            imagePathString = myFindPath(uri); // Alt+Enter at myFindPath
            Log.d("12novV1", "imagePath ==> " + imagePathString);

            //Find Name of Image
            imageNameString = imagePathString.substring(imagePathString.lastIndexOf("/"));
            Log.d("12novV1", "imageName ==> " + imageNameString);

        } // if

    }   // onActivity

    private String myFindPath(Uri uri) {

        String result = null;
        String[] strings = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, strings, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            result = cursor.getString(index);

        } else {
            result = uri.getPath();
        }

        return result;
    }

}   // Main Class
