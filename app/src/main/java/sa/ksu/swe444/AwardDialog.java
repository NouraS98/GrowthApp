package sa.ksu.swe444;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AwardDialog extends Dialog{


    public Activity activity;
    public Button yes, cancel;

    public AwardDialog(Activity a) {
        super(a);
        this.activity = a;
    }//End ForgotPassDialog() constructor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.award_dialog);

        yes = findViewById(R.id.btn_yes);
        cancel = findViewById(R.id.btn_cancel);

//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               dismiss();
//            }//End onClick
//        });

    }//End onCreate()


}