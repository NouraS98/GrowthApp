package sa.ksu.swe444.parent;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.skyhope.eventcalenderlibrary.model.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.ksu.swe444.AddStudentDialog;
import sa.ksu.swe444.AwardsActivity;
import sa.ksu.swe444.Constants;
import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.JavaObjects.Post;
import sa.ksu.swe444.JavaObjects.Student;
import sa.ksu.swe444.MySharedPreference;
import sa.ksu.swe444.R;
import sa.ksu.swe444.adapters.StudentAdapter;

import static sa.ksu.swe444.Constants.keys.CLICKED_CLASS;
import static sa.ksu.swe444.Constants.keys.CLICKED_STUDENT;
import static sa.ksu.swe444.Constants.keys.USER_ID;

public class childrenFragment extends Fragment   implements StudentAdapter.OnItemClickListener{


    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> albumList;
    Button create_class;
    TextView studentcode;
    int position = 4;
    private FirebaseAuth firebaseAuth;
    String userId;
    private FirebaseFirestore fireStore;

    ArrayList<String> classesIDs  = new ArrayList<>();

    ArrayList<String> students_in_class_array = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_children, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.Students_recycler_view);

        albumList = new ArrayList<>();
        adapter = new StudentAdapter(getContext(), albumList);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AddStudentDialog customDialog = new AddStudentDialog(getActivity());
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                create_class = customDialog.findViewById(R.id.btn_add_student);
                studentcode = customDialog.findViewById(R.id.student_email_dlg);
                create_class.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Add an item to animals list
                        if (studentcode.getText().toString().equals("") || studentcode.getText().toString().trim().equals("")) {
                            customDialog.dismiss();
                            // showDialog("Class can't have an empty name!");
                        } else {

                            final String studentID = studentcode.getText().toString();

                            fireStore = FirebaseFirestore.getInstance();
                            String USERID = MySharedPreference.getString(getContext(), USER_ID, null);

                            if (USERID != null) {
                                DocumentReference docRef = fireStore.collection("students").document(studentID);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {

                                            searchStudentinFirebase();

                                            Student a = new Student(documentSnapshot.get("parentemail").toString(), documentSnapshot.get("name").toString(), R.drawable.apple);

                                            albumList.add(a);
                                            adapter.notifyItemInserted(++position);
                                            recyclerView.scrollToPosition(position);
                                            customDialog.dismiss();
                                        } else {
                                            //show dialog that student is not registered
                                        }//end if exists
                                    }
                                });

                            } else {
                                //showDialog("User not found. Error");
                            }
                        }//end else not empty student fields

                    }
                });

            }
        });

//
//        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String  name ,String id) {
//                Intent intent = new Intent(getContext(), AwardsActivity.class);
//                MySharedPreference.putString(getContext(), Constants.keys.CLICKED_STUDENT, "9292");
//                startActivity(intent);
//
//            }
//        });


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        //prepareAlbums();
        ReadStudents();
        return root;
    }

    private boolean StudentInClass(String studentID) {
        String classID = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
        final DocumentReference docRef = fireStore.collection("classes").document(classID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                } else {
                    //show dialog that student is not registered
                }//end if exists
            }
        });
        return false;
    }

    private ArrayList<String> searchStudentinFirebase() {

        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("classes").document().collection("class_students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                classesIDs.add(document.getId());
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
        return classesIDs;

    }


    private void prepareAlbums() {


        int[] covers = new int[]{
                R.drawable.apple,
                R.drawable.flower,
                R.drawable.apple,
                R.drawable.flower
        };

        Student a = new Student(MySharedPreference.getString(getContext(), CLICKED_CLASS, "not saved"), "Noura", covers[0]);
        albumList.add(a);

        a = new Student("rahaf@hotmail.com ", "Rahaf", covers[1]);
        albumList.add(a);

        a = new Student("shahad@hotmail.com ", "Shahad", covers[2]);
        albumList.add(a);

        a = new Student("may@hotmail.com ", "Matmouna", covers[3]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String name, String id) {
        Intent intent = new Intent(getContext(), AwardsActivity.class);
        // intent.putExtra(Constants.keys.title, departmentName);
        MySharedPreference.putString(getContext(),CLICKED_STUDENT,id);

        MySharedPreference.putString(getContext(),"STUDENT_NAME",name);

        intent.putExtra(CLICKED_CLASS, id);
        startActivity(intent);
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void ReadStudents() {
        String classID = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(getContext(), USER_ID, null);
        fireStore.collection("classes").document(classID).collection("class_students").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    students_in_class_array.add(document.get("studentId").toString());
                                    fireStore.collection("students").document(document.get("studentId").toString()).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Student a = new Student(task.getResult().get("parentemail").toString(), task.getResult().get("name").toString(), R.drawable.apple);
                                                        a.setId(task.getResult().getId());
                                                        albumList.add(a);
                                                        adapter.notifyItemInserted(++position);
                                                        recyclerView.scrollToPosition(position);
                                                        adapter.notifyDataSetChanged();
                                                    } else {

                                                    }//end else successfull
                                                }//end oncomplete task
                                            });// end firestore collection get
                                    Log.d("TAG", "students list: " + students_in_class_array.toString());

                                } else {
                                    //doc is null
                                }
//                                    Log.d("TAG", "it is null :", task.getException());
//
//                                    return;
//                                }
//                                Student a = new Student(document.get("parentemail").toString(), document.get("name").toString(), R.drawable.flower);
//                                albumList.add(a);
//                                adapter.notifyItemInserted(++position);
//                                recyclerView.scrollToPosition(position);
//                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}