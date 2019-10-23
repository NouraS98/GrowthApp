package sa.ksu.swe444;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class AddStudentDialog  extends Dialog {
    public Activity activity;
    public TextView send;
    public AddStudentDialog(@NonNull Activity context) {
        super(context);
        this.activity=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_student_dialog);

        send = findViewById(R.id.btn_add_student);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               dismiss();
//            }//End onClick
//        });

    }//End onCreate()


}
