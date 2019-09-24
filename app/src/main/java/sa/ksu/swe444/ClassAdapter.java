package sa.ksu.swe444;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {

    private Context mContext;
    private List<Class> departmentList;

    private OnItemClickListener mListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public RecyclerView recyclerView;
        public  TextView title ;



        public MyViewHolder(View view , final OnItemClickListener listener) {
            super(view);
            title =  view.findViewById(R.id.title);

            thumbnail = view.findViewById(R.id.departmentImg);
            recyclerView =  view.findViewById(R.id.recycler_view);
            //todo
//
//            title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String name;
//                    if(MySharedPrefrence.getString(mContext,Constants.keys.APP_LANGUAGE,"en").equals("en")) {
//                        name = departmentList.get(getAdapterPosition()).getName();
//                    }else { //end if
//                        name = departmentList.get(getAdapterPosition()).getNameAr();
//                    }
//                    long id = departmentList.get(getAdapterPosition()).getId();
//                    listener.onItemClick(name,id);
//                }
//
//            });

        } //end MyViewHolder

    }
    public ClassAdapter(Context mContext, List<Class> departmentList) {
        this.mContext = mContext;
        this.departmentList = departmentList;
    } // end ClassAdapter

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_card, parent, false);

        return new MyViewHolder(itemView ,mListener);
    } // end onCreateViewHolder

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Class album = departmentList.get(position);
        if(MySharedPrefrence.getString(mContext ,Constants.keys.APP_LANGUAGE,"en").equals("en")) {
            holder.title.setText(departmentList.get(position).getName());
        }else{
            holder.title.setText(departmentList.get(position).getNameAr());
        }
        holder.thumbnail.setImageResource(departmentList.get(position).getImg());
        // loading album cover using Glide library
        Glide.with(mContext).load(album.getImg()).into(holder.thumbnail);


    } //  end onBindViewHolder

    public interface OnItemClickListener {
        void onItemClick(String name , long id);
    } // end OnItemClickListener

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    } // end OnItemClickListener

    @Override
    public int getItemCount() {
        return departmentList.size();
    } // end getItemCount
}