package sa.ksu.swe444;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ClassFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassAdapter adapter;
    private List<Class> albumList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.home, container, false);

        //    v.initCollapsingToolbar();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);


        albumList = new ArrayList<>();
        adapter = new ClassAdapter(getContext(), albumList);
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
