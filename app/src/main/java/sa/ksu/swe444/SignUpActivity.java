package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import sa.ksu.swe444.JavaObjects.Parent;

import static sa.ksu.swe444.Constants.keys.IS_LOGIN;
import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_NATIONAL_ID;
import static sa.ksu.swe444.Constants.keys.USER_NEW;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;
import static sa.ksu.swe444.Constants.keys.USER_TYPE;

public class SignUpActivity extends BaseActivity {

    private Button signUpBtn;
    private TextInputEditText firstNameEditText, lastNameEditText, idEditText, emailEditText, phoneEditText, passwordEditText, confirmPassEditText;

    private FirebaseAuth firebaseAuth;
    String userId;

    private FirebaseFirestore fireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initToolbar();
        init();
    }//End onCreate()

    private void init() {
        firstNameEditText = findViewById(R.id.First_name);
        lastNameEditText = findViewById(R.id.Last_name);
        idEditText = findViewById(R.id.nationalid_signup);
        emailEditText = findViewById(R.id.signup_email);
        phoneEditText = findViewById(R.id.phone_signup);
        passwordEditText = findViewById(R.id.signup_passwordET);
        confirmPassEditText = findViewById(R.id.signup_confirm_passwordET);
        signUpBtn = findViewById(R.id.signupPageBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstNameEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                lastNameEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                idEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                emailEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                phoneEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                passwordEditText.setBackground(getResources().getDrawable(R.drawable.shape));
                confirmPassEditText.setBackground(getResources().getDrawable(R.drawable.shape));

                String firstNameInput = firstNameEditText.getText().toString();
                String lastNameInput = lastNameEditText.getText().toString();
                String idInput = idEditText.getText().toString();
                String emailAddressInput = emailEditText.getText().toString();
                String phoneInput = phoneEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();
                String confirmPassInput = confirmPassEditText.getText().toString();


                if (firstNameInput.matches("")
                        || lastNameInput.matches("")
                        || idInput.matches("")
                        || emailAddressInput.matches("")
                        || phoneInput.matches("")
                        || passwordInput.matches("")
                        || confirmPassInput.matches("")) {

                    if (firstNameInput.matches(""))
                        firstNameEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (lastNameInput.matches(""))
                        lastNameEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (idInput.matches(""))
                        idEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (emailAddressInput.matches(""))
                        emailEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (phoneInput.matches(""))
                        phoneEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (passwordInput.matches(""))
                        passwordEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    if (confirmPassInput.matches(""))
                        confirmPassEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));

                    showDialog(getResources().getString(R.string.all_fields_are_required), true);

                } else {
                    if (idInput.length() != 10) {
                        showDialog(getResources().getString(R.string.enter_valid_id), true);
                        idEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    } else if (!validateEmail(emailAddressInput)) {
                        showDialog(getString(R.string.enter_valid_email), true);
                        emailEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    } else if (!validatePhone(phoneInput)) {
                        showDialog(getResources().getString(R.string.phone_msg), true);
                        phoneEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    } else if (!(passwordInput.equals(confirmPassInput))) {
                        showDialog(getString(R.string.passwords_msg), true);
                        confirmPassEditText.setBackground(getResources().getDrawable(R.drawable.focus_shape));
                    } else {
                        executeSignUp(emailAddressInput, passwordInput);
                    }//End inner else
                } //End big else
            }//End onClick()
        });//End signUpBtn.setOnClickListener

    }//End init()


    public void executeSignUp(String email, String password) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!(task.isSuccessful())) {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                    switch (errorCode) {

                        case "ERROR_INVALID_EMAIL":
                            showDialog("The email address is badly formatted.");
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            showDialog("An account already exists with the same email address. Sign in using this email address.");
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            showDialog("The email address is already in use.");
                            break;

                        case "ERROR_USER_DISABLED":
                            showDialog("The user account has been disabled by an administrator.");
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            showDialog("The given password is invalid. it must 6 characters at least");
                            break;
                        default:
                            showDialog("Error has Occurred, please try again");
                    }//end switch
                } else {
                    executeSaveUser();
                    Intent intent = new Intent(SignUpActivity.this, ParentMainActivity.class);
                    MySharedPreference.putBoolean(getApplicationContext(),IS_LOGIN,true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }//End executeSignUp()

    private void executeSaveUser() {
        final String firstNameInput = firstNameEditText.getText().toString();
        final String lastNameInput = lastNameEditText.getText().toString();
        final String idInput = idEditText.getText().toString();
        final String emailAddressInput = emailEditText.getText().toString();
        final String phoneInput = phoneEditText.getText().toString();

        fireStore = FirebaseFirestore.getInstance();
        final FirebaseUser firebaseUser;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //showDialog("Verification email has been sent to"+firebaseUser.getEmail());
                    } else {
                        Log.e("EmailVerification", "sendEmailVerification", task.getException());
                        //showDialog("Failed to send verification email.");
                    }
                }
            });

            Parent parent = new Parent(firstNameInput, lastNameInput, emailAddressInput, phoneInput, idInput);
            fireStore.collection("parents").document(userId).set(parent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showDialog("there has been an error, please try again later");
                }
            });
        } else {
            showDialog("there has been an error, please try again later");
        }
        MySharedPreference.putString(getApplicationContext(),USER_ID,userId);
        MySharedPreference.putString(getApplicationContext(),USER_TYPE,"Parent");
        MySharedPreference.putString(getApplicationContext(),USER_NAME,firstNameInput+" "+lastNameInput);
        MySharedPreference.putString(getApplicationContext(),USER_EMAIL,emailAddressInput);
        MySharedPreference.putString(getApplicationContext(),USER_PHONE,phoneInput);
        MySharedPreference.putString(getApplicationContext(), USER_NATIONAL_ID,idInput);
        MySharedPreference.putBoolean(getApplicationContext(),USER_NEW,true);
    }

    private boolean validatePhone( String phone) {
        String regexStr = "^05\\d{8}$";
        if (phone.length() < 10 || phone.length() > 13 || phone.matches(regexStr) == false) {
            return false;
        } else return true;
    }//End validatePhone()

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }// end validateEmail()

    public void showDialog(String msg, boolean dismiss) {
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
        if (dismiss) {
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }
            };
            handler.postDelayed(runnable, 5000);
        }
    }//End showDialog()

    private void initToolbar() {
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

    public void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}