package sa.ksu.swe444.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sa.ksu.swe444.Constants;
import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Student;
import sa.ksu.swe444.MySharedPreference;
import sa.ksu.swe444.R;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Parent> departmentList;

    private OnItemClickListener mListener;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;
        public RecyclerView recyclerView;
        public  TextView name ;
        public TextView email;



        public MyViewHolder(View view , final OnItemClickListener listener) {
            super(view);
            name =  view.findViewById(R.id.student_name);
            email =  view.findViewById(R.id.student_email);

            thumbnail = view.findViewById(R.id.student_image);
            recyclerView =  view.findViewById(R.id.Students_recycler_view);
            //todo

            name.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    String name;
                    name = departmentList.get(getAdapterPosition()).getFirstName()+" "+departmentList.get(getAdapterPosition()).getLastName();
                    String id = departmentList.get(getAdapterPosition()).getId();
                    if(listener!=null)
                    listener.onItemClick(name, id);

                }


            });

        } //end MyViewHolder

    }
    public ContactsAdapter(Context mContext, List<Parent> departmentList) {
        this.mContext = mContext;
        this.departmentList = departmentList;
    } // end ClassAdapter

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_card, parent, false);

        return new MyViewHolder(itemView ,mListener);
    } // end onCreateViewHolder

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Parent album = departmentList.get(position);
        if(MySharedPreference.getString(mContext , Constants.keys.APP_LANGUAGE,"en").equals("en")) {
            holder.name.setText(departmentList.get(position).getFirstName()+" "+departmentList.get(position).getLastName());
            holder.email.setText(departmentList.get(position).getEmail());


        }else{
            holder.name.setText(departmentList.get(position).getFirstName()+" "+departmentList.get(position).getLastName());

            holder.email.setText(departmentList.get(position).getEmail());
        }
//        holder.thumbnail.setImageResource(departmentList.get(position).getImg());
        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getImg()).into(holder.thumbnail);


    } //  end onBindViewHolder

    public interface OnItemClickListener {
        void onItemClick(String name, String id);
    } // end OnItemClickListener

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    } // end OnItemClickListener

    @Override
    public int getItemCount() {
        return departmentList.size();
    } // end getItemCount
}
