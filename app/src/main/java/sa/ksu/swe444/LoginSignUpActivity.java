package sa.ksu.swe444;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;



public class LoginSignUpActivity extends BaseActivity {


    private Button loginBtn, signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

init();
initToolbar();


    }//End onCreate()

    private void init() {
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(LoginSignUpActivity.this, LoginActivity.class));

            }//End onClick()
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginSignUpActivity.this, SignUpActivity.class));
            }//End onClick()
        });


    }//End init()

    private void initToolbar() {
        /**
         * init toolbar
         */
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.growth));

    }//End initToolbar()

}//End class
