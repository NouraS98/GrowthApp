package sa.ksu.swe444.JavaObjects;

import android.util.Log;

import com.google.firebase.database.ServerValue;

import java.sql.Timestamp;
import java.util.Map;

public class Chat {

    private String id;
    private String msg;
    private String senderId;
    private String recieverId;
    private long msgtime;

    public Chat() {
    }

    public Chat(String id, String msg, String senderId, String recieverId, long msgtime) {
        this.id = id;
        this.msg = msg;
        this.senderId = senderId;
        this.recieverId = recieverId;
        this.msgtime = msgtime;
    }

    public long getMsgtime() {
        return  msgtime;
    }

    public void setMsgtime(long msgtime) {
        this.msgtime = msgtime;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
