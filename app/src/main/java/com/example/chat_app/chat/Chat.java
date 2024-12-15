package com.example.chat_app.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chat_app.MemoryData;
import com.example.chat_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobilechatapp-6ad50-default-rtdb.firebaseio.com");


String getUserMobile="";
private String chatKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        final ImageView btn_back=findViewById(R.id.back_btn);
        final TextView nameTxtV = findViewById(R.id.name_msg);
        final EditText messageEditTxt = findViewById(R.id.message_edittext);
        final CircleImageView imageProfil = findViewById(R.id.profilePic);
        final ImageView btn_send=findViewById(R.id.sendBtn);

        // get data from adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfile = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");


        //get user mobile from memory
        getUserMobile = MemoryData.getData(Chat.this);
        nameTxtV.setText(getName);
        Picasso.get().load(getProfile).into(imageProfil);

        if (chatKey.isEmpty()){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //generate chat key by default chat key is 1
                    chatKey = "1";

                    if (snapshot.hasChild("chat")){
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount()+1);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String getTxtMessage = messageEditTxt.getText().toString();

                //get the current timestamps

                final String currentTimestamps = String.valueOf(System.currentTimeMillis()).substring(0,10);

MemoryData.saveLstMsg(currentTimestamps,chatKey,Chat.this);
                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamps).child("msg").setValue(getTxtMessage);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamps).child("mobile").setValue(getUserMobile);

            messageEditTxt.setText("");
            }
        });


// tue la vue et retour a la vue precedente
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}