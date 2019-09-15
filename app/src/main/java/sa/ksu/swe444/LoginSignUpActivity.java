package sa.ksu.swe444;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class LoginSignUpActivity extends BaseActivity {


    private Button loginBtn, signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

//        initToolbar();

        init();


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


//    private void initToolbar() {
//        /**
//         * init toolbar
//         */
//        Toolbar toolbar = findViewById(R.id.toolbar2);
////        toolbar.setTitle(getString(R.string.singles));
////        toolbar.setNavigationIcon(R.drawable.back_arrow);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }//End onClick()
//        });
//
//    }//End initToolbar()
//

}//End class
