package sa.ksu.swe444;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AddChildrenDialog extends Dialog {
    public Activity activity;
    public TextView send;
    public AddChildrenDialog(@NonNull Activity context) {
        super(context);
        this.activity=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_child_dialog);

        send = findViewById(R.id.btn_add_student);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               dismiss();
//            }//End onClick
//        });

    }//End onCreate()


}
