package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;


public class SignUpActivity extends BaseActivity {

    private Button signup;
    private TextInputEditText nID, phoneN, pass, confirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

   initToolbar();

        init();

    }//End onCreate()

    private void init() {
        nID = findViewById(R.id.signup_Nid);
        phoneN = findViewById(R.id.phone_numberET);
        pass = findViewById(R.id.signup_passwordET);
        confirmPass = findViewById(R.id.signup_confirm_passwordET);
        signup = findViewById(R.id.signupPageBtn);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nationalIdInput = nID.getText().toString();
                String phoneInput = phoneN.getText().toString();
                String passwordInput = pass.getText().toString();
                String confirmPassInput = confirmPass.getText().toString();

                if (nationalIdInput.matches("")
                        || phoneInput.matches("")
                        || passwordInput.matches("")
                        || confirmPassInput.matches("")) {
                    showAlertDialog(getString(R.string.missing_info_msg));

                } else {

                    if (nationalIdInput.length() != 10) {
                        showAlertDialog(getString(R.string.nationalId_msg));
                    } else if (!validate(phoneInput)) {
                        showAlertDialog(getString(R.string.phone_msg));
                    } else if (!(passwordInput.equals(confirmPassInput))) {
                        showAlertDialog(getString(R.string.passwords_msg));
                    } else {
                        executeSignUp();
                    }//End inner else

                } //End big else

            }//End onClick()
        });//End signup.setOnClickListener


    }//End init()



    public void executeSignUp() {


        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }//End executeSignUp()


    private boolean validate(String phone) {
        String regexStr = "^[0-9]{10,13}";

        if (phone.length() < 10 || phone.length() > 13 || phone.matches(regexStr) == false) {
            return false;
        } else return true;
    }//End validate()

    private void showAlertDialog(String msg) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }//End onClick()
        });//End positive button
        alertDialog.show();
    }//End showAlertDialog()
    private void initToolbar() {
        /**
         * init toolbar
         */
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.sign_up));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }//End onClick()
        });

    }//End initToolbar()

}