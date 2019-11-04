package sa.ksu.swe444;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginSignUpActivity extends BaseActivity {


    private Button loginBtn, signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        init();
        //initToolbar();

    }//End onCreate()

    private void init() {
        loginBtn = findViewById(R.id.loginBtn);
        signUpBtn = findViewById(R.id.signUpBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }//End onClick()
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSignUpActivity.this, SignUpActivity.class);
                startActivity(intent);
            }//End onClick()
        });
    }//End init()

//    private void initToolbar() {
//        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle(getString(R.string.growth));
//
//    }//End initToolbar()

}//End class
