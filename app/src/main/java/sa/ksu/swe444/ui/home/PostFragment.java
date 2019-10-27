package sa.ksu.swe444.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.Constants;
import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.JavaObjects.Post;
import sa.ksu.swe444.MySharedPreference;
import sa.ksu.swe444.R;
import sa.ksu.swe444.Upload_post;
import sa.ksu.swe444.adapters.RecyclerAdapter;

import static sa.ksu.swe444.Constants.keys.USER_ID;

public class PostFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Post> mTeachers;

    private FirebaseFirestore fireStore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);

        FloatingActionButton fab = root.findViewById(R.id.gotoupload);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Upload_post.class);
                startActivity(i);
            }
        });


        mRecyclerView = root.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter (getContext(), mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("teachers_uploads");
        ReadPosts();

//        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                mTeachers.clear();
//
//                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
//                    Post upload = teacherSnapshot.getValue(Post.class);
//                    upload.setKey(teacherSnapshot.getKey());
//                }
//                ReadPosts();
//
//                mAdapter.notifyDataSetChanged();
//                mProgressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//                mProgressBar.setVisibility(View.INVISIBLE);
//            }
//        });

        return root;
    }

    public void ReadPosts(){
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("posts")
                .whereEqualTo("classId", MySharedPreference.getString(getContext(), Constants.keys.CLICKED_CLASS,"NONE"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class a = new Class(document.get("name").toString(),R.drawable.wooden,document.getId());
                                Post upload = new Post(document.get("name").toString(),document.get("imageUrl").toString(), document .get("description").toString(), document.get("classId").toString(),document.getId());
                                mTeachers.add(upload);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                                            mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        Post clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl()};
     //   openDetailActivity(teacherData);

    }

    @Override
    public void onShowItemClick(int position) {
        Post clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getName(),clickedTeacher.getDescription(),clickedTeacher.getImageUrl()};
     //   openDetailActivity(teacherData);
    }
//    private void openDetailActivity(String[] data){
//        Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra("NAME_KEY",data[0]);
//        intent.putExtra("DESCRIPTION_KEY",data[1]);
//        intent.putExtra("IMAGE_KEY",data[2]);
//        startActivity(intent);
//    }

    @Override
    public void onDeleteItemClick(final int position) {
        Post selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getKey();
        final String selectedId= selectedItem.getPostId();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               fireStore.collection("posts").document(selectedId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                       mTeachers.remove(position);
                       mAdapter.notifyDataSetChanged();
                       mProgressBar.setVisibility(View.GONE);
                   }
               });
            }
        });
    }
//    public void onDestroy() {
//        super.onDestroy();
//        mDatabaseRef.removeEventListener(mDBListener);
//    }
}