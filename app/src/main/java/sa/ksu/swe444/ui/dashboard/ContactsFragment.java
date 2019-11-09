package sa.ksu.swe444.ui.dashboard;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sa.ksu.swe444.AwardsActivity;
import sa.ksu.swe444.ChatActivity;
import sa.ksu.swe444.JavaObjects.Contact;
import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Student;
import sa.ksu.swe444.MySharedPreference;
import sa.ksu.swe444.R;
import sa.ksu.swe444.adapters.ContactsAdapter;

import static sa.ksu.swe444.Constants.keys.CLICKED_CLASS;
import static sa.ksu.swe444.Constants.keys.CLICKED_PARENT;
import static sa.ksu.swe444.Constants.keys.CLICKED_STUDENT;
import static sa.ksu.swe444.Constants.keys.USER_ID;

public class ContactsFragment extends Fragment implements ContactsAdapter.OnItemClickListener {


    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Contact> albumList;
    Button create_class;
    TextView studentcode;
    int position = 4;
    private FirebaseAuth firebaseAuth;
    String userId;
    private FirebaseFirestore fireStore;
    ArrayList<String> classesIDs = new ArrayList<>();
    String stdname;
    String parentemail;
    String teacherid;
    ArrayList<String> studentsparent = new ArrayList<>();
    ArrayList<String> teacherclasses = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_students, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.Students_recycler_view);

        albumList = new ArrayList<>();
        adapter = new ContactsAdapter(getContext(), albumList);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.hide();

        //getActivity().getActionBar().setTitle("Chats");

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final AddStudentDialog customDialog = new AddStudentDialog(getActivity());
//                customDialog.show();
//                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                create_class = customDialog.findViewById(R.id.btn_add_student);
//                studentcode = customDialog.findViewById(R.id.student_email_dlg);
//                create_class.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        // Add an item to animals list
//                        if (studentcode.getText().toString().equals("") || studentcode.getText().toString().trim().equals("")) {
//                            customDialog.dismiss();
//                            // showDialog("Class can't have an empty name!");
//                        } else {
//
//                            final String studentID = studentcode.getText().toString();
//
//                            fireStore = FirebaseFirestore.getInstance();
//                            String USERID = MySharedPreference.getString(getContext(), USER_ID, null);
//
//                            if (USERID != null) {
//                                DocumentReference docRef = fireStore.collection("students").document(studentID);
//                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                    @Override
//                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                        if (documentSnapshot.exists()) {
//                                            //check if student is in the class already
//                                            //StudentInClass(studentID);
//                                            Student a = new Student(documentSnapshot.get("parentemail").toString(), documentSnapshot.get("name").toString(), R.drawable.apple);
//                                            albumList.add(a);
//
//                                            addToClassInFirestore(documentSnapshot.getId());
//                                            adapter.notifyItemInserted(++position);
//                                            recyclerView.scrollToPosition(position);
//                                            customDialog.dismiss();
//                                        } else {
//                                            //show dialog that student is not registered
//                                        }//end if exists
//                                    }
//                                });
//
//                            } else {
//                                //showDialog("User not found. Error");
//                            }
//                        }//end else not empty student fields
//
//                    }
//                });
//
//            }
//        });

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
        ReadParents();
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

    private void addToClassInFirestore(String studentID) {
        fireStore = FirebaseFirestore.getInstance();
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("studentId", studentID);
        String classid = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
        fireStore.collection("classes").document(classid).collection("class_students")
                .document(studentID).set(studentData, SetOptions.merge());

        Map<String, Object> classData = new HashMap<>();
        classData.put("classes", classid);
        fireStore.collection("students").document(studentID).collection("classes")
                .document(classid).set(classData, SetOptions.merge());

    }


//    private void prepareAlbums() {
//
//
//        int[] covers = new int[]{
//                R.drawable.apple,
//                R.drawable.flower,
//                R.drawable.apple,
//                R.drawable.flower
//        };
//
//        Student a = new Student(MySharedPreference.getString(getContext(), CLICKED_CLASS, "not saved"), "Noura", covers[0]);
//        albumList.add(a);
//
//        a = new Student("rahaf@hotmail.com ", "Rahaf", covers[1]);
//        albumList.add(a);
//
//        a = new Student("shahad@hotmail.com ", "Shahad", covers[2]);
//        albumList.add(a);
//
//        a = new Student("may@hotmail.com ", "Matmouna", covers[3]);
//        albumList.add(a);
//
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onItemClick(String name, String id) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        // intent.putExtra(Constants.keys.title, departmentName);
        MySharedPreference.putString(getContext(), CLICKED_PARENT, id);

        MySharedPreference.putString(getContext(), CLICKED_STUDENT, name);

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

    public void ReadParents() {


        String classID = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
        fireStore = FirebaseFirestore.getInstance();
        final String USERID = MySharedPreference.getString(getContext(), USER_ID, null);

//        ArrayList<String> studentsparent = new ArrayList<>();
//        ArrayList<String> teacherclasses = new ArrayList<>();

        fireStore.collection("contacts").whereEqualTo("teacherID",USERID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot contactdocument : task.getResult()){
                    Contact c = new Contact(contactdocument.get("teacherID").toString(), contactdocument.get("parentEmail").toString(), contactdocument.get("studentName").toString()+"'s parent");
                    c.setContactID(contactdocument.getId());
                    albumList.add(c);
                    adapter.notifyItemInserted(++position);
                    recyclerView.scrollToPosition(albumList.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

//        fireStore.collection("classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot classesdocument : task.getResult()) {
//                        teacherid = classesdocument.get("teacher").toString();
//                        if (teacherid.equals(USERID)) {
//                            teacherclasses.add(classesdocument.getId());
//                            Log.d("CONTACTS", "found id ****** " + classesdocument.getId());
//                        }
//                    }
//                }
//            }
//        });

////        defaultStore?.collection("Category").document("Film").collection("firstFilm").getDocuments()
//
//        for (int i = 0; i < teacherclasses.size(); i++) {
//            String s = teacherclasses.get(i);
////            String s = "a7d1337f-55e2-4a25-bc92-4f63d803eef8";
//
//            Log.d("CONTACTS", "inside for loop ****** " + s);
//            fireStore.collection("classes").document(s).collection("class_students").get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Log.d("CONTACTS", "inside if success ****** ");
//                                for (QueryDocumentSnapshot studentsdocument : task.getResult()) {
////                                  studentname = studentsdocument.getId().toString();
//                                    if (studentsdocument.get("studentname") != null)
//                                        stdname = studentsdocument.get("studentname").toString();
//                                    else
//                                        continue;
//                                    if (studentsdocument.get("parentemail") != null)
//                                        parentemail = studentsdocument.get("parentemail").toString();
//                                    else
//                                        continue;
//                                    Log.d("CONTACTS", "inside for stidentsDoc ****** " + stdname);
//                                    Parent p = new Parent();
//                                    p.setEmail(parentemail);
//                                    p.setFirstName(stdname+"'s");
//                                    p.setLastName("parent");
//                                    albumList.add(p);
//                                    adapter.notifyItemInserted(++position);
//                                    recyclerView.scrollToPosition(albumList.size()-1);
//                                    adapter.notifyDataSetChanged();
////                            if(!studentsparent.contains(studentname))
////                                studentsparent.add(studentname);
//                                }
//                            }
//                        }
//                    });
//        }
//        adapter.notifyItemInserted(++position);
//        recyclerView.scrollToPosition(position);
//        adapter.notifyDataSetChanged();
//        fireStore.collection("students").document().get()
//        fireStore.collection("students").document(studentsdocument.getId()).collection("classes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful())
//                }
//            }
//});


//            fireStore.collection("students").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot studentsdocument : task.getResult()) {
//
//                            Student s = new Student(studentsdocument.get("parentemail").toString(), studentsdocument.get("name").toString() )
//                            studentsparent.add()
//                            studentname = studentsdocument.get("name").toString();
//                            parentemail = studentsdocument.get("parentemail").toString();
//                            Log.d("CONTACTS", "inside for stidentsDoc ****** " + studentname); //THIS
////
//                            fireStore.collection("students").document(studentsdocument.getId()).collection("classes")
//                                    .whereEqualTo("teacherID", USERID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        Log.d("CONTACTS", "inside where equals"); //THIS
//                                        Log.d("CONTACTS", "***** " + studentname); //THIS
//                                        Parent p = new Parent();
//                                        p.setEmail(parentemail);
//                                        p.setFirstName("f");
//                                        p.setLastName("l");
//                                        albumList.add(p);
//                                        adapter.notifyItemInserted(++position);
//                                        recyclerView.scrollToPosition(position);
//                                        adapter.notifyDataSetChanged();
//                                    }
//
//                                }
//                            });
//                        }
//                    }
//                }
//            });
        //
        //        fireStore.collection("classes").whereEqualTo("teacher", USERID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //            @Override
        //            public void onComplete(@NonNull Task<QuerySnapshot> task) {
        //                if (task.isSuccessful()) {
        //                    for (QueryDocumentSnapshot classesdocument : task.getResult()) {
        //                        fireStore.collection("classes").document(classesdocument.getId()).collection("class_students").get()
        //                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //                                    @Override
        //                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        //                                        if (task.isSuccessful()) {
        //                                            for (final QueryDocumentSnapshot studentsdocument : task.getResult()) {
        //                                                Log.d("STUDENTS", "inside for stidentsDoc ****** " + studentsdocument.getId()); //THIS
        //
        //                                                fireStore.collection("students").document(studentsdocument.getId()).get()
        //                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        //                                                            @Override
        //                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        //                                                                fireStore.collection("parents").whereEqualTo("email", studentsdocument.get("parentemail").toString()).get()
        //                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //                                                                            @Override
        //                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
        //                                                                                if (task.isSuccessful()){
        //                                                                                    for (QueryDocumentSnapshot parentdocument : task.getResult()) {
        //                                                                                        Log.d("MOM", "parents list: " + parentdocument.toString());
        ////                                                                                        String parentFname = parentdocument.get("firstName").toString();
        ////                                                                                        String parentLname = parentdocument.get("lastName").toString();
        ////                                                                                        String parentphone = parentdocument.get("phone").toString();
        //                                                                                        Parent p = new Parent(parentdocument.get("firstName").toString(), parentdocument.get("lastName").toString(), parentdocument.get("email").toString(), parentdocument.get("phone").toString(), parentdocument.getId());
        //                                                                                        albumList.add(p);
        //                                                                                        adapter.notifyItemInserted(++position);
        //                                                                                        recyclerView.scrollToPosition(position);
        //                                                                                        adapter.notifyDataSetChanged();
        //                                                                                    }
        //                                                                                }
        //                                                                            }
        //                                                                        });
        //                                                            }
        //                                                        });
        //                                            }
        //                                        }
        //                                    }
        //                                });
        //                    }
        //                }
        //            }
        //        });

        //        fireStore.collection("classes").document(classID).collection("class_students").get()
        //                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        //                    @Override
        //                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        //                        if (task.isSuccessful()) {
        //                            for (QueryDocumentSnapshot document : task.getResult()) {
        //                                if (document != null) {
        //                                    students_in_class_array.add(document.get("studentId").toString());
        //                                    fireStore.collection("students").document(document.get("studentId").toString()).get()
        //                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        //                                                @Override
        //                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        //                                                    if (task.isSuccessful()) {
        //                                                        Student a = new Student(task.getResult().get("parentemail").toString(), task.getResult().get("name").toString(), R.drawable.apple);
        //                                                        a.setId(task.getResult().getId());
        //                                                        albumList.add(a);
        //                                                        adapter.notifyItemInserted(++position);
        //                                                        recyclerView.scrollToPosition(position);
        //                                                        adapter.notifyDataSetChanged();
        //                                                    } else {
        //
        //                                                    }//end else successfull
        //                                                }//end oncomplete task
        //                                            });// end firestore collection get
        //                                    Log.d("TAG", "students list: " + students_in_class_array.toString());
        //
        //                                } else {
        //                                    //doc is null
        //                                }
        ////                                    Log.d("TAG", "it is null :", task.getException());
        ////
        ////                                    return;
        ////                                }
        ////                                Student a = new Student(document.get("parentemail").toString(), document.get("name").toString(), R.drawable.flower);
        ////                                albumList.add(a);
        ////                                adapter.notifyItemInserted(++position);
        ////                                recyclerView.scrollToPosition(position);
        ////                                adapter.notifyDataSetChanged();
        //                            }
        //                        } else {
        //                            Log.d("TAG", "Error getting documents: ", task.getException());
        //                        }
        //                    }
        //                });

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
                                Log.d("HELP1", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("HELP", "Error getting documents: ", task.getException());
                        }

                    }
                });
        return classesIDs;

    }
}