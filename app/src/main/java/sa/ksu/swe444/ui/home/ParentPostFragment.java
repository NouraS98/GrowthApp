package sa.ksu.swe444.ui.home;

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
import sa.ksu.swe444.adapters.RecyclerAdapterParent;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;

public class ParentPostFragment extends Fragment implements RecyclerAdapterParent.OnItemClickListener {


    private RecyclerView mRecyclerView;
    private RecyclerAdapterParent mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Post> mTeachers;
    private ArrayList<String> classesList = new ArrayList<>();
    private ArrayList<String> studentsList = new ArrayList<>();

    private FirebaseFirestore fireStore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_post, container, false);


        FloatingActionButton fab = root.findViewById(R.id.gotoupload);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), Upload_post.class);
//                startActivity(i);
//            }
//        });

        fab.hide();

        mRecyclerView = root.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mProgressBar = root.findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapterParent(getContext(), mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        Log.d("HELLO", "I'm inside class: ");

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("teachers_uploads");
        ReadPosts();
        Log.d("HELLO", "I'm inside class: ");
        Log.d("HELLO", "IN MAIN the array is " + classesList.toString());

        return root;
    }




    public void ReadPosts() {

        fireStore = FirebaseFirestore.getInstance();
        String USEREMAIL = MySharedPreference.getString(getContext(), USER_EMAIL, null);
        fireStore.collection("students")
                .whereEqualTo("parentemail", USEREMAIL)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("HELLO", "student I'm have documents that match parent id: ");
                            for (QueryDocumentSnapshot studentDocument : task.getResult()) {
                                if (!studentsList.contains(studentDocument.getId())) {
                                    Log.d("HELLO", "student I'm adding it to array: ");
                                    Log.d("HELLO", studentDocument.getId());
                                    studentsList.add(studentDocument.getId());
                                }

                                fireStore.collection("students").document(studentDocument.getId()).collection("classes").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("HELLO", "class I have documents of classes: ");

                                                    for (QueryDocumentSnapshot classesDocument : task.getResult()) {
                                                        if (!classesList.contains(classesDocument.getId())) {
                                                            Log.d("HELLO", "class I'm adding it to array: ");
                                                            Log.d("HELLO", classesDocument.getId());

                                                            classesList.add(classesDocument.getId());
                                                        }

                                                        fireStore.collection("posts").whereEqualTo("classId", classesDocument.getId()).get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            for (QueryDocumentSnapshot postDocument : task.getResult()) {
                                                                                Log.d("HELLO", "post I have a result: " + postDocument.get("name"));

                                                                                Post upload = new Post(postDocument.get("name").toString(), postDocument.get("imageUrl").toString(), postDocument.get("description").toString(), postDocument.get("classId").toString(), postDocument.getId());
                                                                                mTeachers.add(upload);
                                                                                Log.d("TAG", postDocument.getId() + " => " + postDocument.getData());
                                                                            }
                                                                            mAdapter.notifyDataSetChanged();
                                                                            mProgressBar.setVisibility(View.GONE);
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                    Log.d("HELLO", "class the array  after for loop is " + classesList.toString());

                                                }
                                            }
                                        });

                            }
                        }
                    }
                });
    }


    @Override
    public void onItemClick(int position) {
        Post clickedTeacher = mTeachers.get(position);
        String[] teacherData = {clickedTeacher.getName(), clickedTeacher.getDescription(), clickedTeacher.getImageUrl()};
        //   openDetailActivity(teacherData);

    }

    @Override
    public void onShowItemClick(int position) {
        Post clickedTeacher = mTeachers.get(position);
        String[] teacherData = {clickedTeacher.getName(), clickedTeacher.getDescription(), clickedTeacher.getImageUrl()};
        //   openDetailActivity(teacherData);
    }
//    private void openDetailActivity(String[] data){
//        Intent intent = new Intent(this, DetailsActivity.class);
//        intent.putExtra("NAME_KEY",data[0]);
//        intent.putExtra("DESCRIPTION_KEY",data[1]);
//        intent.putExtra("IMAGE_KEY",data[2]);
//        startActivity(intent);
//    }


//    public void onDestroy() {
//        super.onDestroy();
//        mDatabaseRef.removeEventListener(mDBListener);
//    }
}