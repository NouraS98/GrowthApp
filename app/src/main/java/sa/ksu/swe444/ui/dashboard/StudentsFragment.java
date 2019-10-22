package sa.ksu.swe444.ui.dashboard;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.AddStudentDialog;
import sa.ksu.swe444.CreateClassDialog;
import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.JavaObjects.Student;
import sa.ksu.swe444.R;
import sa.ksu.swe444.adapters.StudentAdapter;

public class StudentsFragment extends Fragment {


    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> albumList;
    Button create_class;
    TextView class_name;
    int position = 4;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_students, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.Students_recycler_view);

        albumList = new ArrayList<>();
        adapter = new StudentAdapter(getContext(), albumList);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//on("Action", null).show();

                final AddStudentDialog customDialog = new AddStudentDialog(getActivity());
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                create_class = customDialog.findViewById(R.id.btn_add_student);
                class_name = customDialog.findViewById(R.id.student_email_dlg);
                create_class.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // String itemLabel = "" + mRandom.nextInt(100);

                        // Add an item to animals list
                        Student a = new Student(class_name.getText() + "", class_name.getText()+"" ,R.drawable.apple);
                        if(class_name.getText().toString().equals("") || class_name.getText().toString().trim().equals("") ){
                            customDialog.dismiss();
                           // showDialog("Class can't have an empty name!");
                        } else {

                            albumList.add(a);
                            //saveClass(a);
                            adapter.notifyItemInserted(++position);
                            recyclerView.scrollToPosition(position);
                            customDialog.dismiss();
                        }

                    }//End  onClick() //for forgotPassDialog_sendBtn btn
                });

            }
        });




        adapter.setOnItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {

            }
        });


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        return root;
    }

    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.apple,
                R.drawable.flower,
                R.drawable.apple,
                R.drawable.flower
        };

        Student a = new Student("Noura@hotmail.com ","Noura", covers[0]);
        albumList.add(a);

         a = new Student("rahaf@hotmail.com ","Rahaf", covers[1]);
        albumList.add(a);

        a = new Student("shahad@hotmail.com ","Shahad", covers[2]);
        albumList.add(a);

         a = new Student("may@hotmail.com ","Matmouna", covers[3]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
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
}