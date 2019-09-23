package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.widget.Button;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Teacher;

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
        //addTeacher("Shahad","Nasser","super.shahad1@gmail.com","0543332345","female");

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
                        showDialog(getResources().getString(R.string.enter_valid_id));
                    } else if (!validateEmail(emailAddressInput)) {
                        showDialog(getString(R.string.enter_valid_email));
                    } else if (!validatePhone(phoneInput)) {
                        showDialog(getResources().getString(R.string.phone_msg));
                    } else if (!(passwordInput.equals(confirmPassInput))) {
                        showDialog(getString(R.string.passwords_msg));
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
                    executeSaveUser2();
                    //executeSaveUser();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }//End executeSignUp()

    private void executeSaveUser2() {

        String firstNameInput = firstNameEditText.getText().toString();
        String lastNameInput = lastNameEditText.getText().toString();
        String idInput = idEditText.getText().toString();
        String emailAddressInput = emailEditText.getText().toString();
        String phoneInput = phoneEditText.getText().toString();

        fireStore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            userId = firebaseUser.getUid();
            Parent parent = new Parent(firstNameInput, lastNameInput, emailAddressInput, phoneInput, idInput);
            fireStore.collection("parents").document(userId).set(parent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
//?????????????????
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//???????????????????
                }
            });
        } else {
            showDialog("there has been an error, please try again later");
        }
    }

    private void executeSaveUser() {

        String firstNameInput = firstNameEditText.getText().toString();
        String lastNameInput = lastNameEditText.getText().toString();
        String idInput = idEditText.getText().toString();
        String emailAddressInput = emailEditText.getText().toString();
        String phoneInput = phoneEditText.getText().toString();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        Parent parent = new Parent(firstNameInput, lastNameInput, emailAddressInput, phoneInput, idInput);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference("parents");
        dbReference.child(userId).setValue(parent);
    }

    private boolean validatePhone(String phone) {
        String regexStr = "^[0-9]{10,13}";
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
            handler.postDelayed(runnable, 1300);

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

    private void addTeacher(String firstName, String lastName, String email, String phone, String gender) {
        userId = "QxoLCilTIhNYbGveYN0Zfn6PBmz1";
        Teacher teacher = new Teacher(firstName, lastName, email, phone, gender);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbReference = database.getReference("teachers");
        dbReference.child(userId).setValue(teacher);
    }

}