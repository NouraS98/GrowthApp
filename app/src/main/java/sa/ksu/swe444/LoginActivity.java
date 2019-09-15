package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends BaseActivity {

    private String validNId = "1234567890";
    private String validPassword = "123456";


    private EditText forgetPassNumber;
    private TextInputEditText nId, pass;
    private Button loginBtn;
    private TextView forgetPass, send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        initToolbar();

        init();

    }//End onCreate()

    private void init() {

        nId = findViewById(R.id.login_nId);
        pass = findViewById(R.id.login_passwordET);
        loginBtn = findViewById(R.id.loginPageBtn);
        forgetPass = findViewById(R.id.forget_pass);

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPasswordAction();
            }//end onClick()
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String usernameInput = nId.getText().toString();
                String passwordInput = pass.getText().toString();


                if ((usernameInput.equals("") || passwordInput.equals(""))) {
                    showDialog(getString(R.string.missing_info_msg));
                } else {

                    if (usernameInput.equals(validNId) && passwordInput.equals(validPassword)) {
                        executeLogin();
                    } else {
                        if (validPasswordLength(passwordInput)) {
                            if ((usernameInput.length() != 10) || !(usernameInput.equals(validNId) && passwordInput.equals(validPassword))) {
                                //Alert dialog
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                                builder.setTitle(R.string.Incorrect);
                                builder.setMessage(R.string.incorrectPassOrID);
                                builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        nId.setText("");
                                        pass.setText("");
                                    }//End onClick()
                                });
                                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        nId.setText("");
                                        pass.setText("");
                                    }//End onClick()
                                });
                                builder.show();

                            } //End if
                        } else {

                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
                            builder.setTitle(R.string.Incorrect);

                            builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    nId.setText("");
                                    pass.setText("");
                                }//End onClick()
                            });
                            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    nId.setText("");
                                    pass.setText("");
                                }//End onClick()
                            });
                            builder.show();
                        }//End else
                    }//End inner else
                }//End outer else
            }//End onClick()

        }); //End loginBtn.setOnClickListener

    }//End init()


//    }//End initToolbar()

    private void forgetPasswordAction() {
        final ForgotPassDialog customDialog = new ForgotPassDialog(LoginActivity.this);
        customDialog.show();

        send = customDialog.findViewById(R.id.btn_send);
        forgetPassNumber = customDialog.findViewById(R.id.forgotPass_edtNumber);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(forgetPassNumber.getText().toString().matches("")) &&
                        (validate(forgetPassNumber.getText().toString()))) {
                    customDialog.dismiss();
                }//End if block
                else {
                    showDialog(getString(R.string.invalid_phone_msg));
                }//End else block
            }//End  onClick() //for send btn
        });
    }//End forgetPasswordAction

    public void showDialog(String msg) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }//End onClick()
                });//End BUTTON_POSITIVE
        alertDialog.show();

    }//End showDialog()

    private boolean validate(String phone) {
        String regexStr = "^[0-9]{10,13}";

        if (phone.length() < 10 || phone.length() > 13 || phone.matches(regexStr) == false) {
            return false;
        } else return true;
    }//End validate()

    private boolean validPasswordLength(String password) {

        if (password.length() < 6) {
            return false;
        } else return true;


    }//End validPasswordLength()

    public void executeLogin() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }//End executeSignUp()
}