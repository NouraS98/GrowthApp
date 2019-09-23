package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

    private EditText forgetPassDialog_emailEditText;
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginBtn, forgotPassDialog_sendBtn;
    private TextView forgetPass;

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

                if (usernameInput.equals("") && passwordInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_email_password_dialog));
                } else if (usernameInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_email_dialog));
                } else if (!validateEmail(usernameInput)) {
                    showDialog(getResources().getString(R.string.enter_valid_email));
                } else if (passwordInput.equals("")) {
                    showDialog(getResources().getString(R.string.enter_password_dialog));
                } else if (!(usernameInput.equals("") || passwordInput.equals(""))) {
                    firebaseAuth.signInWithEmailAndPassword(usernameInput, passwordInput).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                executeLogin();
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {

                                    case "ERROR_USER_MISMATCH":
                                        showDialog("The supplied credentials do not correspond to the previously signed in user.");
                                        break;

                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        showDialog("This credential is already associated with a different user account.");
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        showDialog("The user account has been disabled by an administrator.");
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        showDialog("There is no user record corresponding to this identifier.");
                                        break;
                                    default:
                                        showDialog("some error has occurred, error code: " + errorCode);
                                        break;
                                }
                            }
                        }
                    });
                } else {
                    showDialog("Error has occurred, please try again later");
                }
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
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgotPassDialog_sendBtn = customDialog.findViewById(R.id.btn_send);
        forgetPassDialog_emailEditText = customDialog.findViewById(R.id.forgotPass_edtEmail);
        forgetPassDialog_emailEditText.setText(emailEditText.getText().toString());
        forgotPassDialog_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(forgetPassDialog_emailEditText.getText().toString().matches("")) && validateEmail(forgetPassDialog_emailEditText.getText().toString())) {
                    firebaseAuth.sendPasswordResetEmail(forgetPassDialog_emailEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        customDialog.dismiss();
                                        showDialog("reset password link has been sent to your email");
                                    } else {
                                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                        if (errorCode.equals("ERROR_USER_NOT_FOUND"))
                                            showDialog("Parent not found, please enter your registered email address");
                                        else
                                            showDialog("sorry there has been an error: " + errorCode);
                                    }
                                }
                            });
                    customDialog.dismiss();
                }//End if block
                else if (!validateEmail(forgetPassDialog_emailEditText.getText().toString())) {
                    showDialog(getResources().getString(R.string.enter_valid_email));
                } else {
                    showDialog(getResources().getString(R.string.enter_email_dialog));
                }//End else block
            }//End  onClick() //for forgotPassDialog_sendBtn btn
        });
    }//End forgetPasswordAction

    public void showDialog(String msg) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialogbackground));
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

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }// end validateEmail()

    public void executeLogin() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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