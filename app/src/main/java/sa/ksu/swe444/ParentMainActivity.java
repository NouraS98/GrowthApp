package sa.ksu.swe444;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_NATIONAL_ID;
import static sa.ksu.swe444.Constants.keys.USER_NEW;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;
import static sa.ksu.swe444.Constants.keys.USER_TYPE;

public class ParentMainActivity extends BaseActivity {
Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main);
        logout= findViewById(R.id.logoutparent);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                fAuth.signOut();
                MySharedPreference.clearData(getApplicationContext());
                MySharedPreference.clearValue(getApplicationContext(), USER_NAME);
                MySharedPreference.clearValue(getApplicationContext(), USER_TYPE);
                MySharedPreference.clearValue(getApplicationContext(), USER_ID);
                Intent intent = new Intent(ParentMainActivity.this, LoginSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String name = MySharedPreference.getString(getApplicationContext(),USER_NAME,":(");
        String email = MySharedPreference.getString(getApplicationContext(),USER_EMAIL,":(");
        String phone = MySharedPreference.getString(getApplicationContext(),USER_PHONE,":(");
        String type = MySharedPreference.getString(getApplicationContext(),USER_TYPE,":(");
        String uid = MySharedPreference.getString(getApplicationContext(),USER_ID,":(");
        String nid = MySharedPreference.getString(getApplicationContext(), USER_NATIONAL_ID,":(");
        Boolean newuser = MySharedPreference.getBoolean(getApplicationContext(),USER_NEW,false);

        //showDialog("hello "+name+" "+email+" "+phone+" "+type+" "+uid+" "+nid+" "+newuser);
    }
}
