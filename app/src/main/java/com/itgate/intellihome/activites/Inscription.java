package com.itgate.intellihome.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itgate.intellihome.R;
import com.itgate.intellihome.service.IUser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Inscription extends AppCompatActivity {
    public Button btn_cr;

    public EditText champ_nom;
    public EditText champ_mail;
    public EditText champ_pass;
    public EditText champ_numTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        btn_cr = (Button)findViewById(R.id.btn_create);

        champ_nom = (EditText)findViewById(R.id.name);
        champ_mail = (EditText)findViewById(R.id.mail);
        champ_pass = (EditText)findViewById(R.id.pass);
        champ_numTel = (EditText)findViewById(R.id.numTel);

                btn_cr.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String n = champ_nom.getText().toString();
                                String m = champ_mail.getText().toString();
                                String p = champ_pass.getText().toString();
                                String tel = champ_numTel.getText().toString();

                                if (n.equals("") || n.length()<3) {
                                    Toast.makeText(Inscription.this, "Name must not be empty or less than 3 characters !", Toast.LENGTH_LONG).show();
                                }else
                                if (m.equals("") || !m.contains("@")){
                                    Toast.makeText(Inscription.this, "E-mail must not be empty or without at-sign !", Toast.LENGTH_LONG).show();
                                } else
                                if (p.equals("") || p.length()<8){
                                    Toast.makeText(Inscription.this, "Password must not be empty or less than 8 characters !", Toast.LENGTH_LONG).show();
                                } else
                                if (tel.equals("") || tel.length()<8){
                                    Toast.makeText(Inscription.this, "Password must not be empty or less than 8 characters !", Toast.LENGTH_LONG).show();
                                } else
                                    onCreateAccount();
                            }
                        }
                );

    }

    public void onCreateAccount(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        final EditText code_input = new EditText(this);
        code_input.setInputType(InputType.TYPE_CLASS_NUMBER);

        build.setCancelable(true);
        build.setTitle("Verify Your Identity");
        build.setMessage("You'll Receive a Code on Your Phone\nPlease Enter it to Complete the Operation !");
        build.setView(code_input);
        build.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /** link for web service here
                         *  or some instructions instead*/
                    }
                });
        build.show();

        String Url = "http://10.0.3.2/formation";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(Url).build();
        final IUser rest = adapter.create(IUser.class);
        rest.insertUser(
                champ_nom.getText().toString(),
                champ_mail.getText().toString(),
                champ_pass.getText().toString(),
                champ_numTel.getText().toString(),
                new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                BufferedReader reader = null;
                String output = "";

                try {
                    //Initialisation de buffer reader
                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                    //lecture output dans string output
                    output = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Erreur : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}
