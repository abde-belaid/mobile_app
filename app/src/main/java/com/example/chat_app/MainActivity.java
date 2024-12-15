package com.example.chat_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat_app.messages.MessagesAdapter;
import com.example.chat_app.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private  final List<MessagesList> messagesLists = new ArrayList<>();
    private String numero,nom,email;
    private int unseenMessage = 0;
    private String lastMessage = "";
    private String chatKey="";
    private Boolean dataSet = false;

    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;



    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://mobilechatapp-6ad50-default-rtdb.firebaseio.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        final CircleImageView userProfilePic = findViewById(R.id.UserProfilePic);

        messagesRecyclerView = findViewById(R.id.recycle_view_messages);

        // recuperer les données dans l'activité de Register.class

        numero = getIntent().getStringExtra("num");
        nom = getIntent().getStringExtra("nom");
        email = getIntent().getStringExtra("email");



        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set message adapter to recycle view
        messagesAdapter = new MessagesAdapter(messagesLists,MainActivity.this);


        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        // recuperer la photo de profile dans firestore

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                // changer le photo de profile en circulaire
                final String profilePicUrl = snapshot.child("users").child(numero).child("profile_pic").getValue(String.class);


                if (profilePicUrl != null && !profilePicUrl.isEmpty()){

                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                    Log.d("ProfilePic", "URL de l'image : " + profilePicUrl);

                }
                else {
                    // Placeholder ou comportement alternatif
                    userProfilePic.setImageResource(R.drawable.user_icon);
                }
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesLists.clear();
                unseenMessage=0;
                lastMessage = "";
                chatKey="";

                for(DataSnapshot dataSnapshot:snapshot.child("users").getChildren()){

                    //il permet de recuperer la cle de document qui est le numero de telephone

                    final String getMobile=dataSnapshot.getKey();

                    dataSet =false;

                    if (getMobile!=null && !getMobile.equals(numero)){


                        final String getName = dataSnapshot.child("nom").getValue(String.class);
                        final String getProfile = dataSnapshot.child("profile_pic").getValue(String.class);



                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int getChatCount = (int) snapshot.getChildrenCount();

                                if (getChatCount > 0){
                                    for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                        final String getkey = dataSnapshot1.getKey();
                                        chatKey= getkey;

                                        if (dataSnapshot1.hasChild("user_1") && dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")){


                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if (( getUserOne.equals(getMobile) && getUserTwo.equals(numero)) || (getUserOne.equals(numero) && getUserTwo.equals(getMobile)) ){
                                                for(DataSnapshot chatDataSnapshot:dataSnapshot1.child("messages").getChildren()){

                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final  long getLastSeenMessage = Long.parseLong(MemoryData.getLstMsg(MainActivity.this,getkey));


                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                    if (getMessageKey > getLastSeenMessage){
                                                        unseenMessage++;

                                                    }

                                                }
                                            }

                                        }


                                    }
                                }


                                if (!dataSet){
                                    dataSet=true;
                                    MessagesList messagesList = new MessagesList(getName,getMobile,lastMessage,getProfile,unseenMessage,chatKey);

                                    messagesLists.add(messagesList);
                                    messagesAdapter.updateData(messagesLists);

                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


//                        MessagesList messagesList = new MessagesList(getName,getMobile,lastMessage,getProfile,unseenMessage);
//
//                        messagesLists.add(messagesList);

                    }


                }

//                messagesRecyclerView.setAdapter(new MessagesAdapter(messagesLists,MainActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}