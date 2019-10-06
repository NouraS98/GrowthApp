package sa.ksu.swe444;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SplashActivity extends BaseActivity implements View.OnClickListener {
    ConstraintLayout splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        splashLayout = findViewById(R.id.splash_layout);
        splashLayout.setOnClickListener(this);

        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
                                      public void run() {
                                          startActivity(new Intent(getApplicationContext(),LoginSignUpActivity.class));
                                          finish();
                                      }//end of run
                                  }//end of postDelayed
                , secondsDelayed * 2000);

    }

    public void onClick(View view) {
        startActivity(new Intent(getApplicationContext(),LoginSignUpActivity.class));
        finish();
    }
}
