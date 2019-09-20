package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private EditText forgetPassNumber;
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginBtn;
    private TextView forgetPass, send;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initToolbar();
        init();

    }//End onCreate()

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_passwordET);
        loginBtn = findViewById(R.id.loginPageBtn);
        forgetPass = findViewById(R.id.forget_pass);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    showDialog(getResources().getString(R.string.youre_already_logged_in));
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else showDialog(getResources().getString(R.string.you_need_to_login));
            }
        };//end AuthStateListener

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usernameInput = emailEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();

                if (usernameInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_email_dialog));
                } else if (passwordInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_password_dialog));
                } else if (usernameInput.equals("") || passwordInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_email_password_dialog));
                } else if (!(usernameInput.equals("") || passwordInput.equals(""))) {
                    firebaseAuth.signInWithEmailAndPassword(usernameInput, passwordInput).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                showDialog("error occured");
                            } else {
                                executeLogin();
                            }
                        }
                    });
                } else {

                    showDialog("ettor");
                }
//                if ((usernameInput.equals("") || passwordInput.equals(""))) {
//                    showDialog(getString(R.string.missing_info_msg));
//                } else {
//                    if (usernameInput.equals(validNId) && passwordInput.equals(validPassword)) {
//                        executeLogin();
//                    } else {
//                        if (validPasswordLength(passwordInput)) {
//                            if ((usernameInput.length() != 10) || !(usernameInput.equals(validNId) && passwordInput.equals(validPassword))) {
//                                //Alert dialog
//                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
//                                builder.setTitle(R.string.Incorrect);
//                                builder.setMessage(R.string.incorrectPassOrID);
//                                builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        emailEditText.setText("");
//                                        passwordEditText.setText("");
//                                    }//End onClick()
//                                });
//                                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        emailEditText.setText("");
//                                        passwordEditText.setText("");
//                                    }//End onClick()
//                                });
//                                builder.show();
//
//                            } //End if
//                        } else {
//
//                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
//                            builder.setTitle(R.string.Incorrect);
//
//                            builder.setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    emailEditText.setText("");
//                                    passwordEditText.setText("");
//                                }//End onClick()
//                            });
//                            builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    emailEditText.setText("");
//                                    passwordEditText.setText("");
//                                }//End onClick()
//                            });
//                            builder.show();
//                        }//End else
//                    }//End inner else
//                }//End outer else
            }//End onClick()

        }); //End loginBtn.setOnClickListener
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgetPasswordAction();
            }//end onClick()
        });


    }//End init()

    private void forgetPasswordAction() {
        final ForgotPassDialog customDialog = new ForgotPassDialog(LoginActivity.this);
        customDialog.show();

        send = customDialog.findViewById(R.id.btn_send);
        forgetPassNumber = customDialog.findViewById(R.id.forgotPass_edtNumber);
        forgetPassNumber.setText(emailEditText.toString());
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

    }//End executeLogin()

    private void initToolbar() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.LoginToolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.login));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }//End onClick()
        });

    }//End initToolbar()

}