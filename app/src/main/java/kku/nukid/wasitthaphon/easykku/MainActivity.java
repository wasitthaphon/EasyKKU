package kku.nukid.wasitthaphon.easykku;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Explicit ประกาศตัวแปร
    private Button signInButton, signUpButton;   /*Completement Key --> Ctrl+Shift+Enter
                                                   Ctrl+Alt+L --> Format Style
                                                 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        signInButton = (Button) findViewById(R.id.button);
        signUpButton = (Button) findViewById(R.id.button2); // Alt + Enter -->Cast to


        //Sign Up Controller --> Button Can Press
        signUpButton.setOnClickListener(new View.OnClickListener() { // new then Ctrl+Space -->OnclickListener
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


    }   // Main Method
}   // Main Class คลาสหลัก
