package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUpActivity extends BaseActivity {

    private Button signUpBtn;
    private TextInputEditText firstNameEditText, lastNameEditText,emailEditText, phoneEditText, passwordEditText, confirmPassEditText;

    private FirebaseAuth firebaseAuth;

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
        emailEditText = findViewById(R.id.signup_email);
        phoneEditText = findViewById(R.id.phone_signup);
        passwordEditText = findViewById(R.id.signup_passwordET);
        confirmPassEditText = findViewById(R.id.signup_confirm_passwordET);
        signUpBtn = findViewById(R.id.signupPageBtn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstNameInput = firstNameEditText.getText().toString();
                String lastNameInput = lastNameEditText.getText().toString();
                String emailAddressInput = emailEditText.getText().toString();
                String phoneInput = phoneEditText.getText().toString();
                String passwordInput = passwordEditText.getText().toString();
                String confirmPassInput = confirmPassEditText.getText().toString();


                if (firstNameInput.matches("")||lastNameInput.matches("") || emailAddressInput.matches("")
                        || phoneInput.matches("")
                        || passwordInput.matches("")
                        || confirmPassInput.matches("")) {
                    showAlertDialog(getString(R.string.missing_info_msg));

                } else {

                    if (!validate(phoneInput)) {
                        showAlertDialog(getString(R.string.phone_msg));
                    } else if (!(passwordInput.equals(confirmPassInput))) {
                        showAlertDialog(getString(R.string.passwords_msg));
                    } else {
                        executeSignUp(emailAddressInput,passwordInput);
                    }//End inner else

                } //End big else

            }//End onClick()
        });//End signUpBtn.setOnClickListener


    }//End init()



    public void executeSignUp(String email, String password) {

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!(task.isSuccessful())){
                    Log.e("Signup Error", "onCancelled", task.getException());
                    showDialog("Error has Occurred, please try again");
                }else{
                    //User user = new Teacher(first,last,email,phone);
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


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