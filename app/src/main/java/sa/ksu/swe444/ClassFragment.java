package sa.ksu.swe444;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.adapters.ClassAdapter;

public class ClassFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> albumList;
    Button create_class;
    TextView class_name;
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
                final CreateClassDialog customDialog = new CreateClassDialog(getActivity());
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                create_class = customDialog.findViewById(R.id.btn_create);
                class_name = customDialog.findViewById(R.id.class_name);
                create_class.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = 4;
                        // String itemLabel = "" + mRandom.nextInt(100);

                        // Add an item to animals list
                        Class a = new Class(class_name.getText()+"",  R.drawable.butterfly);

                        albumList.add(a);
                        adapter.notifyItemInserted(position);
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

        prepareAlbums();

        return v;
    }

    private void prepareAlbums() {
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
}
