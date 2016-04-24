package com.itgate.intellihome.activites;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itgate.intellihome.R;


public class MainActivity extends AppCompatActivity {
    public Button b_connect;
    public Button b_new_ac;
    private EditText c_email;
    private EditText c_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_connect = (Button) findViewById(R.id.btn_connect);
        b_new_ac = (Button) findViewById(R.id.btn_new_ac);
        c_email = (EditText) findViewById(R.id.email);
        c_pass = (EditText) findViewById(R.id.pass);

        b_connect.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        connexion();
                    }
                }
        );

        b_new_ac.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToInscrit();
                    }
                }
        );

    }

    public void connexion() {
        String mail = c_email.getText().toString();
        String mdp = c_pass.getText().toString();

        if(!(mail.equals("cc")) && !(mdp.equals("az"))) {
            Toast.makeText(MainActivity.this, "Please check your E-mail and Password !", Toast.LENGTH_LONG).show();
        }else{
        Intent it_conMod = new Intent(MainActivity.this, ConnectionMode.class);
            startActivity(it_conMod);
        }
    }

    public void goToInscrit() {
        Intent it_inscrit = new Intent(MainActivity.this, Inscription.class);
        startActivity(it_inscrit);

    }

}
