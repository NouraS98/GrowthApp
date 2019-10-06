package sa.ksu.swe444;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Teacher;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ClassFragment fr1;
    ProfileFragment fr2;
    private static final String TAG_HOME = "TAG_HOME";
    private static final String TAG_CLASSES = "TAG_CLASSES";
    private NavigationView navigationView;
    TextView profileNameTextView;
    Toolbar toolbar;
    static Parent userParent;
    static Teacher userTeacher;
    private static FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fr1 = new ClassFragment();
        fr2= new ProfileFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String userType = "teacher";
//        String userType= MySharedPreference.getString(this,"USERTYPE","");
        if(userType.equals("teacher")){
            toolbar.setTitle("Home");
            replaceFragment(fr1, TAG_CLASSES);
            ReadSingleTeacher();
        } else if (userType.equals("parent")){
            //ReadSingleParent();
//            replaceFragment(fr1, TAG_CLASSES);
        }


        ProfileInfo();
    }

    private void ProfileInfo() {
        profileNameTextView = navigationView.getHeaderView(0).findViewById(R.id.profile_name);
        if (!MySharedPreference.getString(this, "USERNAME", "").equals("")) {
            profileNameTextView.setText(MySharedPreference.getString(this, "USERNAME", ""));
        } else {
            profileNameTextView.setText("");
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            toolbar.setTitle("Home");
//            replaceFragment(fr1, TAG_CLASSES);
        } else if (id == R.id.nav_classes) {
            toolbar.setTitle("My Classes");
            replaceFragment(fr1, TAG_CLASSES);

        } else if (id == R.id.nav_chat) {
            toolbar.setTitle("Chats");

        } else if (id == R.id.nav_logout) {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
            MySharedPreference.clearData(getApplicationContext());
            MySharedPreference.clearValue(this, "USERNAME");
            MySharedPreference.clearValue(this, "USERTYPE");
            MySharedPreference.clearValue(this, "USERID");
            Intent intent = new Intent(MainActivity.this, LoginSignUpActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_profile){
            replaceFragment(fr2,TAG_CLASSES);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment frag, String Tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, frag, Tag).addToBackStack(null).commit();
    } // end replaceFragment


    //
//    private void AddStudents(String firstName, String lastName, String nationalID, String motherID, String fatherID, String classID) {
//
//        fireStore = FirebaseFirestore.getInstance();
//        Student student = new Student(firstName, lastName, nationalID, motherID, fatherID, classID);
//        fireStore.collection("students").add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
//
//    private void AddClasses(String name, String teacher) {
//
//        fireStore = FirebaseFirestore.getInstance();
//        Class aClass = new Class(name, teacher);
//        fireStore.collection("classes").add(aClass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
//
//    private void AddTeacher(String firstName, String lastName, String email, String phone, String gender, ArrayList<String> classes) {
//
//        fireStore = FirebaseFirestore.getInstance();
//        Teacher teacher = new Teacher(firstName, lastName, email, phone, gender, classes);
//        fireStore.collection("teachers").add(teacher).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
//

    public void ReadSingleTeacher() {
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(this, "USERID", null);
        if (USERID != null) {
            fireStore.collection("teachers").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                    //    userTeacher = new Teacher(doc.get("firstName").toString(), doc.get("lastName").toString(), doc.get("email").toString(), doc.get("phone").toString());
//                        MySharedPreference.putString(getApplicationContext(), "USERNAME", userTeacher.getFirstName() + " " + userTeacher.getLastName());

                        //                    StringBuilder fields = new StringBuilder("");
//                    fields.append("Name: ").append(doc.get("firstName"));
//                    fields.append("\nEmail: ").append(doc.get("email"));
//                    fields.append("\nPhone: ").append(doc.get("phone"));
//                    fields.append("\nClasses").append(doc.get("classes"));
//                    t.setText(fields.toString());
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
        }
    }

    public void ReadSingleParent() {
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(this, "USERID", null);
        if (USERID != null) {
            fireStore.collection("parents").document(USERID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        userParent = new Parent(doc.get("firstName").toString(), doc.get("lastName").toString(), doc.get("email").toString(), doc.get("phone").toString());
                        MySharedPreference.putString(getApplicationContext(), "USERNAME", userParent.getFirstName() + " " + userParent.getLastName());

                        //                    StringBuilder fields = new StringBuilder("");
//                    fields.append("Name: ").append(doc.get("firstName"));
//                    fields.append("\nEmail: ").append(doc.get("email"));
//                    fields.append("\nPhone: ").append(doc.get("phone"));
//                    fields.append("\nClasses").append(doc.get("classes"));
//                    t.setText(fields.toString());
                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to read user info", Toast.LENGTH_SHORT).show();

                        }
                    });
        } else {
            Toast.makeText(this, "USER ID is null", Toast.LENGTH_SHORT).show();
        }

    }
}
