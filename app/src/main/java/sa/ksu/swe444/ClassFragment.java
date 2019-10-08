package sa.ksu.swe444;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Teacher;
import sa.ksu.swe444.JavaObjects.User;
import sa.ksu.swe444.adapters.ClassAdapter;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;

public class ClassFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> albumList;
    Button create_class;
    TextView class_name;
    private FirebaseFirestore fireStore;
    int position = 4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);


        albumList = new ArrayList<Class>();
        adapter = new ClassAdapter(getContext(), albumList);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//on("Action", null).show();

                final CreateClassDialog customDialog = new CreateClassDialog(getActivity());
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                create_class = customDialog.findViewById(R.id.btn_create);
                class_name = customDialog.findViewById(R.id.class_name);
                create_class.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // String itemLabel = "" + mRandom.nextInt(100);

                        // Add an item to animals list
                        Class a = new Class(class_name.getText() + "", R.drawable.apple);

                        albumList.add(a);
                        saveClass(a);
                        adapter.notifyItemInserted(++position);
                        recyclerView.scrollToPosition(position);
                        customDialog.dismiss();

                    }//End  onClick() //for forgotPassDialog_sendBtn btn
                });

            }
        });

//        adapter.setOnItemClickListener(new ClassAdapter().OnItemClickListener() {
//            @Override
////            public void onItemClick(int position) {
////
////                Intent intent;
////                Class a = albumList.get(position);
////                intent = new Intent(getActivity(), Questions.class);
////                intent.putExtra("title" ,a.getName()  );
////
////                startActivity(intent);
////
////
////            }
//        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareClasses();
        ReadClasses();
        return v;
    }

    private void saveClass(Class a) {
        fireStore = FirebaseFirestore.getInstance();
        String id = UUID.randomUUID().toString();
        String USERID = MySharedPreference.getString(getContext(), USER_ID, null);
        Class c = new Class(a.getName(),0,USERID, a.getImg());
        fireStore.collection("classes").document(id).set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    private void prepareClasses() {
        int[] covers = new int[]{
                R.drawable.butterfly,
                R.drawable.flower,
                R.drawable.apple,
                R.drawable.cat
        };

        Class a = new Class("Butterflies", covers[0]);
        albumList.add(a);

        a = new Class("Flowers", covers[1]);
        albumList.add(a);

        a = new Class("Apples", covers[2]);
        albumList.add(a);

        a = new Class("Kittens", covers[3]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

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

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void ReadClasses(){
        fireStore =FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(getContext(), USER_ID, null);
        fireStore.collection("classes")
                .whereEqualTo("teacher", USERID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Class a = new Class(document.get("name").toString(),R.drawable.apple);
                                albumList.add(a);
                                adapter.notifyItemInserted(++position);
                                recyclerView.scrollToPosition(position);
                                adapter.notifyDataSetChanged();

                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
//    public void ReadClasses() {
//        fireStore = FirebaseFirestore.getInstance();
//        String USERID = MySharedPreference.getString(getContext(), USER_ID, null);
//        if (USERID != null) {
//            fireStore.collection("teachers").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        if (doc.exists()) {
//                            User userTeacher = new Teacher(doc.get("firstName").toString(), doc.get("lastName").toString(), doc.get("email").toString(), doc.get("phone").toString());
//                            MySharedPreference.putString(getApplicationContext(), USER_NAME, userTeacher.getFirstName() + " " + userTeacher.getLastName());
//                            MySharedPreference.putString(getApplicationContext(), USER_EMAIL, userTeacher.getEmail());
//                            MySharedPreference.putString(getApplicationContext(), USER_PHONE, userTeacher.getPhone());
//                        } else {
//                            showDialog("User not registered as teacher");
//                        }
//                    }//if task successful
//                }
//            })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
//        } else {
//            showDialog("User not found. Error");
//        }
//    }//end ReadTeacher
}
