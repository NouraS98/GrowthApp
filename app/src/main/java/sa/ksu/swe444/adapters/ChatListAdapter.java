package sa.ksu.swe444.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
//import com.superior.firebasechatting.R;

import java.util.Date;
import java.util.List;

import sa.ksu.swe444.JavaObjects.Chat;
import sa.ksu.swe444.R;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatHolder> {

    private List<Chat> chatList;
    private Context context;



    public ChatListAdapter(Context context, List<Chat> chatList) {
        this.chatList = chatList;
        this.context = context;
    }//end ChatListAdapter

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.msg_item_layout, parent, false);

        return new ChatHolder(v);

    }//end onCreateViewHolder

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {

        holder.txttime.setText("");
        holder.txtMsg.setText("");

        //check if this msg from current user
        if(chatList.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid()))
        {
            holder.llMsg.setBackgroundResource(R.drawable.chat_msg_sender_shape);
            holder.txtMsg.setGravity(Gravity.END);;
            holder.llMsg.setGravity(Gravity.END);
            holder.stupid.setVisibility(View.VISIBLE);
//            holder.txtMsg.setTextAlignment(Gravity.V);;
        }
        else {
            holder.llMsg.setBackgroundResource(R.drawable.chat_msg_shape);
            holder.txtMsg.setGravity(Gravity.START);;
            holder.llMsg.setGravity(Gravity.START);
            holder.stupid.setVisibility(View.GONE);

        }//end else


        if(chatList.get(position).getMsg()!=null) {
            holder.txtMsg.setText(chatList.get(position).getMsg());
            Date d = new Date(chatList.get(position).getMsgtime());
            holder.txttime.setText(d.getHours() + ":" + d.getMinutes() + "");
        }





    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    class ChatHolder extends RecyclerView.ViewHolder {

        TextView txttime,txtMsg;
        LinearLayout llMsg, stupid;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            txtMsg=itemView.findViewById(R.id.txtBody);
            llMsg=itemView.findViewById(R.id.llMsg);
            txttime=itemView.findViewById(R.id.txttime);
            stupid=itemView.findViewById(R.id.stupidmsg);



        }


    }//end ChatHolder



}//end class
