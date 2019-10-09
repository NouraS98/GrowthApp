package sa.ksu.swe444;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


public class EditProfileDialog extends Dialog {

  public Activity activity;
  public TextView send;

  public EditProfileDialog(Activity a) {
    super(a);
    this.activity = a;
  }//End ForgotPassDialog() constructor

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.edit_profile_dialog);

    send = findViewById(R.id.btn_save);
    send.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }//End onClick
    });

  }//End onCreate()

}//End class