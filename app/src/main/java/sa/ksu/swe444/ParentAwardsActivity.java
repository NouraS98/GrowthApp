package sa.ksu.swe444;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static sa.ksu.swe444.Constants.keys.USER_ID;

public class ParentAwardsActivity extends AppCompatActivity {


    String studentID;
    TextView student_name;
    ImageView helpingothersimg, hardworkerimg, ontaskimg, teamworkimg, disrepecfulimg;
    TextView helpingotherspoints, hardworkerpoints, ontaskpoints, teamworkpoints, disrepecfulpoints;

    FirebaseFirestore fireStore;

    Button yes, cancel;
    TextView message;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);

        studentID = MySharedPreference.getString(getApplicationContext(), Constants.keys.CLICKED_STUDENT, "NONE");

        student_name = findViewById(R.id.student_name);
        student_name.setText(MySharedPreference.getString(getApplicationContext(),"STUDENT_NAME","NONE"));


        helpingothersimg = findViewById(R.id.helpingothersimg);
        hardworkerimg = findViewById(R.id.hardworkimg);
        ontaskimg = findViewById(R.id.ontaskimg);
        teamworkimg = findViewById(R.id.teamworkimg);
        disrepecfulimg = findViewById(R.id.disrespectimg);

        helpingotherspoints = findViewById(R.id.helpingotherspoints);
        hardworkerpoints = findViewById(R.id.hardworkpoints);
        ontaskpoints = findViewById(R.id.ontaskpoints);
        teamworkpoints = findViewById(R.id.teamworkpoints);
        disrepecfulpoints = findViewById(R.id.disrespectpoints);

        readAwards();


    }

    public void readAwards() {
        fireStore = FirebaseFirestore.getInstance();
        String USERID = MySharedPreference.getString(getApplicationContext(), USER_ID, null);
        fireStore.collection("students").document(studentID).collection("awards").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    switch (document.getId()) {
                                        case "helping_others":
                                            helpingotherspoints.setText(document.get("awards").toString());
                                            break;
                                        case "hard_work":
                                            hardworkerpoints.setText(document.get("awards").toString());
                                            break;
                                        case "on_task":
                                            ontaskpoints.setText(document.get("awards").toString());
                                            break;
                                        case "teamwork":
                                            teamworkpoints.setText(document.get("awards").toString());
                                            break;
                                        case "disrespectful":
                                            disrepecfulpoints.setText(document.get("awards").toString());
                                            break;
                                        default:
                                            break;
                                    }
//                                    Log.d("TAG", "students list: " + awardsList.toString());

                                } else {
                                    //doc is null
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void writeAwards(String awardName, int points,int inc) {

        fireStore = FirebaseFirestore.getInstance();
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("awards", points + inc);
        fireStore.collection("students").document(studentID).collection("awards")
                .document(awardName).set(studentData, SetOptions.merge());
        //documentReference.addSnapshotListener()

//        fireStore = FirebaseFirestore.getInstance();
//        Map<String, Object> awardData = new HashMap<>();
//        awardData.put("points", studentID);
//        final DocumentReference documentReference = fireStore.collection("students").document(studentID).collection("awards").document(awardName);
//        documentReference.update("points", FieldValue.increment(1));
    }


}
