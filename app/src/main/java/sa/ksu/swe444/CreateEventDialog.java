package sa.ksu.swe444;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class CreateEventDialog extends Dialog {


    public Activity activity;
    public TextView send;

    public CreateEventDialog(Activity a) {
        super(a);
        this.activity = a;
    }//End ForgotPassDialog() constructor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.create_event_dialog);

        send = findViewById(R.id.eventButton);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               dismiss();
//            }//End onClick
//        });

    }//End onCreate()


}