package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.*;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Teacher;
import sa.ksu.swe444.JavaObjects.User;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;
import static sa.ksu.swe444.Constants.keys.USER_TYPE;

public class LoginActivity extends BaseActivity {

    private EditText forgetPassDialog_emailEditText;
    private TextInputEditText emailEditText, passwordEditText;
    private Button loginBtn, forgotPassDialog_sendBtn;
    private TextView forgetPass;
    private FirebaseFirestore fireStore;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private RadioGroup radioGroup;
    boolean isTeacher = false;
    boolean isParent = false;

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
        radioGroup = (RadioGroup) findViewById(R.id.Role);
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
                } else if (!radioIsSelected(radioGroup)) {
                    showDialog("please choose the type access: Teacher or Parent");
                } else if (!(usernameInput.equals("") || passwordInput.equals("")) && radioIsSelected(radioGroup)) {
                    firebaseAuth.signInWithEmailAndPassword(usernameInput, passwordInput).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fireStore = FirebaseFirestore.getInstance();
                                FirebaseUser firebaseUser;
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                    userId = firebaseUser.getUid();
                                    MySharedPreference.putString(getApplicationContext(), Constants.keys.USER_ID, userId);
                                    //checkUserType();
                                    executeLogin();
                                }
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {
                                    case "ERROR_WRONG_PASSWORD":
                                        showDialog("Email and Password doesn't match the records, please enter correct email and password");
                                        break;
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

    private void checkUserType() {
        String USERID = MySharedPreference.getString(this, USER_ID, null);
        fireStore.collection("teachers").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        MySharedPreference.putString(getApplicationContext(), USER_TYPE, "Teacher");
                    } else {
                        MySharedPreference.putString(getApplicationContext(), USER_TYPE, "Parent");
                    }
                }//if task successful
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();

            }
        });
    }

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

        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton roleButton = (RadioButton) findViewById(selectedId);
        MySharedPreference.putString(getApplicationContext(), USER_TYPE,"Teacher");
        if (roleButton.getText().equals("Teacher")) {
            if (MySharedPreference.getString(getApplicationContext(), USER_TYPE, "none").equals( "Teacher")) {
                ReadTeacher();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                showDialog("User is not registered as Teacher "+MySharedPreference.getString(getApplicationContext(),USER_TYPE,"not registed"));
            }
        } else if (roleButton.getText().equals("Parent")) {
            if (MySharedPreference.getString(getApplicationContext(), USER_TYPE, "none") .equals( "Parent")) {
                ReadParent();
                Intent intent = new Intent(LoginActivity.this, ParentMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                showDialog("User is not registered as Parent");
            }
        }
        //end if radiobutton choice
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

    private boolean radioIsSelected(RadioGroup buttonGroup) {
        return (buttonGroup.getCheckedRadioButtonId() == R.id.teacher_button
                || buttonGroup.getCheckedRadioButtonId() == R.id.parent_button);
    }

    public void ReadTeacher() {
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(this, USER_ID, null);
        if (USERID != null) {
            fireStore.collection("teachers").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            User userTeacher = new Teacher(doc.get("firstName").toString(), doc.get("lastName").toString(), doc.get("email").toString(), doc.get("phone").toString());
                            MySharedPreference.putString(getApplicationContext(), USER_NAME, userTeacher.getFirstName() + " " + userTeacher.getLastName());
                            MySharedPreference.putString(getApplicationContext(), USER_EMAIL, userTeacher.getEmail());
                            MySharedPreference.putString(getApplicationContext(), USER_PHONE, userTeacher.getPhone());
                        } else {
                            showDialog("User not registered as teacher");
                        }
                    }//if task successful
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            showDialog("User not found. Error");
        }
    }//end ReadTeacher

    public void ReadParent() {
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(this, USER_ID, null);
        if (USERID != null) {
            fireStore.collection("parents").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc.exists()) {
                            User userParent = new Parent(doc.get("firstName").toString(), doc.get("lastName").toString(), doc.get("email").toString(), doc.get("phone").toString());
                            MySharedPreference.putString(getApplicationContext(), USER_NAME, userParent.getFirstName() + " " + userParent.getLastName());
                            MySharedPreference.putString(getApplicationContext(), USER_EMAIL, userParent.getEmail());
                            MySharedPreference.putString(getApplicationContext(), USER_PHONE, userParent.getPhone());
                        } else {
                            showDialog("User not registered as parent");
                        }
                    }//if task successful
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            showDialog("User not found. Error");
        }
    }//end ReadTeacher
}