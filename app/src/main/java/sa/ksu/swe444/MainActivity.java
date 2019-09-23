package sa.ksu.swe444;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;

import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.JavaObjects.Parent;
import sa.ksu.swe444.JavaObjects.Student;
import sa.ksu.swe444.JavaObjects.Teacher;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseFirestore fireStore;
    private FirebaseAuth firebaseAuth;
    String userId;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        t = findViewById(R.id.firstname_display);


//        AddStudents("farah","farah","0123456789","0123456789","987654321",":D");
//        AddStudents("maha","maha","4444444444","0144444444","0244444444",":D");
//        AddStudents("noura","noura","6666666666","0166666666","0266666666",":D");
//        AddStudents("nasser","nasser","9999999999","0199999999","0299999999",":D");

//        AddClasses("flowers",":O");
//        AddClasses("butterflies",":O");


//        ArrayList<String> classes = new ArrayList<String>();
//        classes.add("mg1WZ4liJ7TkjmZoG998");
//        classes.add("nyIFYOwm1ehlkXQPMnNt");
//
//        AddTeacher("mona", "mona", "mona@arua.ar","0123345678","female",classes);
//        AddTeacher("fahad", "fahad", "fahad@arua.ar","0123225678","male",classes);

//        AddTeacher("jewel", "jewel", "jewel@arua.ar","0588845678","female");
//        AddTeacher("Saleh", "Saleh", "saleh@arua.ar","0512399978","male");
//        AddTeacher("Majid", "Majid", "majid@arua.ar","0512345111","male");
        ReadSingleTeacher();

    }

    private void AddStudents(String firstName, String lastName, String nationalID, String motherID, String fatherID, String classID) {

        fireStore = FirebaseFirestore.getInstance();
        Student student = new Student(firstName, lastName, nationalID, motherID, fatherID, classID);
        fireStore.collection("students").add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void AddClasses(String name, String teacher) {

        fireStore = FirebaseFirestore.getInstance();
        Class aClass = new Class(name, teacher);
        fireStore.collection("classes").add(aClass).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void AddTeacher(String firstName, String lastName, String email, String phone, String gender, ArrayList<String> classes) {

        fireStore = FirebaseFirestore.getInstance();
        Teacher teacher = new Teacher(firstName, lastName, email, phone, gender, classes);
        fireStore.collection("teachers").add(teacher).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
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

    public void ReadSingleTeacher() {
        fireStore = FirebaseFirestore.getInstance();

        fireStore.collection("teachers").document("uQSh0RvJ1ybaoJQCRIRW").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Name: ").append(doc.get("firstName"));
                    fields.append("\nEmail: ").append(doc.get("email"));
                    fields.append("\nPhone: ").append(doc.get("phone"));
                    fields.append("\nClasses").append(doc.get("classes"));
                    t.setText(fields.toString());
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
