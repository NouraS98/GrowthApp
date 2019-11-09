package sa.ksu.swe444;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
//import com.superios.firebasechatting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sa.ksu.swe444.JavaObjects.Chat;
import sa.ksu.swe444.adapters.ChatListAdapter;
// ref.child(chatList.get(chatList.size()-1).getId()).setValue(null);



public class ChatActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private List<Chat> chatList = new ArrayList();
    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private EditText edtPost;
    private ImageView imgSend;

    private static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initToolbar();

        init();


    }//end onCreate


    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(MySharedPreference.getString(getApplicationContext(),Constants.keys.CLICKED_STUDENT,"error"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }//End initToolbar()

    private void init() {
        ref = FirebaseDatabase.getInstance().getReference().child("messages");
        setListener();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatListAdapter(this, chatList);
        recyclerView.setAdapter(adapter);


        edtPost = findViewById(R.id.edtPost);
        imgSend = findViewById(R.id.imgSend);
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edtPost.getText() != null) {

                    String key = ref.push().getKey();
                    Date date = new Date();
                    ref.child(key).setValue(new Chat(key, edtPost.getText().toString(), FirebaseAuth.getInstance().getUid(), MySharedPreference.getString(getApplicationContext(),Constants.keys.CLICKED_PARENT,"error"), date.getTime()));
                   // Log.d("CHATACT", "after creating messege" );
                    edtPost.setText("");

                }


            }//onClick
        });


    }//end init


    private void setListener() {

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Chat chat = dataSnapshot.getValue(Chat.class);

                if(chat.getRecieverId().equals( MySharedPreference.getString(getApplicationContext(),Constants.keys.CLICKED_PARENT,"error"))
                        && chat.getSenderId().equals(FirebaseAuth.getInstance().getUid()))
                    chatList.add(chat);
                else if(chat.getSenderId().equals(MySharedPreference.getString(getApplicationContext(),Constants.keys.CLICKED_PARENT,"error"))
                        && chat.getRecieverId().equals(FirebaseAuth.getInstance().getUid()))
                    chatList.add(chat);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatList.size()-1);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.i(TAG, "onChildChanged");
                Chat chat = dataSnapshot.getValue(Chat.class);

                for (int i = 0; i < chatList.size(); i++) {
                    if (chatList.get(i).getId().equals(chat.getId())) {
                        chatList.set(i, chat);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i("onChildRemoved", "onChildRemoved");

                Chat chat = dataSnapshot.getValue(Chat.class);

                for (int i = 0; i < chatList.size(); i++) {
                    if (chatList.get(i).getId().equals(chat.getId())) {
                        chatList.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("onChildMoved", "onChildMoved");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("onCancelled", "onCancelled");

            }
        });
    }//end setListener

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}//end class
