package com.example.chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    // Write a message to the database
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobilechatapp-6ad50-default-rtdb.firebaseio.com/");


    //    DatabaseReference myRef = database.;

//    myRef.setValue("Hello, World!");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        final EditText name_inp = findViewById(R.id.inp_name);
        final EditText phone_inp = findViewById(R.id.inp_phone);
        final EditText email_inp = findViewById(R.id.inp_email);
        final AppCompatButton btn_register = findViewById(R.id.btn_register);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

// verifier si l'utilisateur est deja connecter
        if(!MemoryData.getData(this).isEmpty()){


            Intent intent = new Intent(Register.this, MainActivity.class);

            intent.putExtra("num",MemoryData.getData(this));
            intent.putExtra("nom",MemoryData.getName(this));
            intent.putExtra("email","");
            startActivity(intent);

            // cette methode pour tué l'activite Register
            finish();

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.show();


                    final String user_name = name_inp.getText().toString();
                    final String user_phone = phone_inp.getText().toString();
                    final  String user_email = email_inp.getText().toString();

                    if (user_name.isEmpty() || user_phone.isEmpty() || user_email.isEmpty()){
                        Toast.makeText(Register.this,"Tout les champs sont obligatoires",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                    else {
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                progressDialog.dismiss();

                                if (snapshot.child("users").hasChild(user_phone)){
                                    Toast.makeText(Register.this,"ce numero est déja utiliser",Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseRef.child("users").child(user_phone).child("email").setValue(user_email);
                                    databaseRef.child("users").child(user_phone).child("nom").setValue(user_name);
//                                    databaseRef.child("users").child(user_phone).child("profile_pic").setValue("");

                                    // enregistrer le numero dans la memoire
                                   MemoryData.saveData(user_phone,Register.this);
                                    // enregistrer le nom dans la memoire
                                    MemoryData.saveName(user_name,Register.this);

                                    Toast.makeText(Register.this,"l'utilisateur est crée avec succès",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    intent.putExtra("num",user_phone);
                                    intent.putExtra("nom",user_name);
                                    intent.putExtra("email",user_email);
                                    startActivity(intent);

                                    // cette methode pour tué l'activite Register
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            });

        }
        else {
            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.show();


                    final String user_name = name_inp.getText().toString();
                    final String user_phone = phone_inp.getText().toString();
                    final  String user_email = email_inp.getText().toString();

                    if (user_name.isEmpty() || user_phone.isEmpty() || user_email.isEmpty()){
                        Toast.makeText(Register.this,"Tout les champs sont obligatoires",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                    else {
                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                progressDialog.dismiss();

                                if (snapshot.child("users").hasChild(user_phone)){
                                    Toast.makeText(Register.this,"ce numero est déja utiliser",Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseRef.child("users").child(user_phone).child("email").setValue(user_email);
                                    databaseRef.child("users").child(user_phone).child("nom").setValue(user_name);

                                    // enregistrer le numero dans la memoire
                                   MemoryData.saveData(user_phone,Register.this);
                                    // enregistrer le nom dans la memoire
                                    MemoryData.saveName(user_name,Register.this);

                                    Toast.makeText(Register.this,"l'utilisateur est crée avec succès",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    intent.putExtra("num",user_phone);
                                    intent.putExtra("nom",user_name);
                                    intent.putExtra("email",user_email);
                                    startActivity(intent);

                                    // cette methode pour tué l'activite Register
                                    finish();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                            }
                        });
                    }

                }
            });
        }




    }
}