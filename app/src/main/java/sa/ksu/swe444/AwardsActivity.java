package sa.ksu.swe444;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sa.ksu.swe444.JavaObjects.Student;

import static sa.ksu.swe444.Constants.keys.CLICKED_CLASS;
import static sa.ksu.swe444.Constants.keys.USER_ID;

public class AwardsActivity extends AppCompatActivity {


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


        helpingothersimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AwardDialog customDialog = new AwardDialog(AwardsActivity.this);
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yes = customDialog.findViewById(R.id.btn_yes);
                cancel = customDialog.findViewById(R.id.btn_cancel);

                message = customDialog.findViewById(R.id.txt_exit);
                message.setText("Award student for helping others?");
                img = customDialog.findViewById(R.id.awardimg);
                img.setImageResource(R.drawable.hand_helpother);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int points = Integer.parseInt(helpingotherspoints.getText().toString());
                        writeAwards("helping_others", points,1);
                        helpingotherspoints.setText(points + 1 + "");
                        customDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }); //end on click

        hardworkerimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AwardDialog customDialog = new AwardDialog(AwardsActivity.this);
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yes = customDialog.findViewById(R.id.btn_yes);
                cancel = customDialog.findViewById(R.id.btn_cancel);

                message = customDialog.findViewById(R.id.txt_exit);
                message.setText("Award student for hard work?");
                img = customDialog.findViewById(R.id.awardimg);
                img.setImageResource(R.drawable.hammer_hardwork);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int points = Integer.parseInt(hardworkerpoints.getText().toString());
                        writeAwards("hard_work", points,1);
                        hardworkerpoints.setText(points + 1 + "");
                        customDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }); //end on click

        ontaskimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AwardDialog customDialog = new AwardDialog(AwardsActivity.this);
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yes = customDialog.findViewById(R.id.btn_yes);
                cancel = customDialog.findViewById(R.id.btn_cancel);

                message = customDialog.findViewById(R.id.txt_exit);
                message.setText("Award student for staying on task?");
                img = customDialog.findViewById(R.id.awardimg);
                img.setImageResource(R.drawable.notepad_ontask);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int points = Integer.parseInt(ontaskpoints.getText().toString());
                        writeAwards("on_task", points,1);
                        ontaskpoints.setText(points + 1 + "");
                        customDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }); //end on click

        teamworkimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AwardDialog customDialog = new AwardDialog(AwardsActivity.this);
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yes = customDialog.findViewById(R.id.btn_yes);
                cancel = customDialog.findViewById(R.id.btn_cancel);

                message = customDialog.findViewById(R.id.txt_exit);
                message.setText("Award student for teamwork?");
                img = customDialog.findViewById(R.id.awardimg);
                img.setImageResource(R.drawable.teamwork_shirt);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int points = Integer.parseInt(teamworkpoints.getText().toString());
                        writeAwards("teamwork", points,1);
                        teamworkpoints.setText(points + 1 + "");
                        customDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }); //end on click

        disrepecfulimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AwardDialog customDialog = new AwardDialog(AwardsActivity.this);
                customDialog.show();
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                yes = customDialog.findViewById(R.id.btn_yes);
                cancel = customDialog.findViewById(R.id.btn_cancel);

                message = customDialog.findViewById(R.id.txt_exit);
                message.setText("Deduct student's points for being disrespectful?");
                img = customDialog.findViewById(R.id.awardimg);
                img.setImageResource(R.drawable.thunder_disrespect);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int points = Integer.parseInt(disrepecfulpoints.getText().toString());
                        writeAwards("disrespectful", points,-1);
                        disrepecfulpoints.setText(points - 1 + "");
                        customDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }); //end on click
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
