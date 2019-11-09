package sa.ksu.swe444.ui.notifications;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sa.ksu.swe444.CreateEventDialog;
import sa.ksu.swe444.MySharedPreference;
import sa.ksu.swe444.R;

import static sa.ksu.swe444.Constants.keys.CLICKED_CLASS;
import static sa.ksu.swe444.Constants.keys.USER_ID;
public class NotificationsFragment extends Fragment {
    private static final String TAG = "CalenderTest";
    Button addEvent;

    private FirebaseAuth firebaseAuth;
    String userId;
    private FirebaseFirestore fireStore;
    ArrayList<String> events_in_class_array = new ArrayList<>();


    CalenderEvent calenderEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        calenderEvent = root.findViewById(R.id.calender_event);
        addEvent = root.findViewById(R.id.eventButton);


        calenderEvent.clearPrefrance();
        //  ClassMainActivity m = new ClassMainActivity();
        //    m.pleaseRefreshMe();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
//        Event event = new Event(calendar.getTimeInMillis(), "Test");
//        calenderEvent.addEvent(event);
//        Event event1 = new Event(calendar.getTimeInMillis(), "Party!");
//        calenderEvent.addEvent(event1);

        calenderEvent.initCalderItemClickCallback(new CalenderDayClickListener() {
            @Override
            public void onGetDay(final DayContainerModel dayContainerModel) {

                addEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Button create_event;
                        final TextView event_name;
                        final CreateEventDialog customDialog = new CreateEventDialog(getActivity());
                        customDialog.show();
                        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        create_event = customDialog.findViewById(R.id.btn_create_event);
                        event_name = customDialog.findViewById(R.id.eventName_name);

                        create_event.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Event event1 = new Event(dayContainerModel.getTimeInMillisecond(), event_name.getText().toString(), Color.RED,MySharedPreference.getString(getContext(), CLICKED_CLASS, null));
                                if (event1 != null){
                                    calenderEvent.addEvent(event1);
                                    addEventToFirestore(event1);
                                }
                                customDialog.dismiss();
                            }//End  onClick() //for forgotPassDialog_sendBtn btn
                        });


                    }
                });

                Event current = calenderEvent.getEvent(dayContainerModel.getTimeInMillisecond());
                // ReadEvents(dayContainerModel.getTimeInMillisecond());
                Log.d(TAG, dayContainerModel.getDate());
                try {
                    if (current.getEventText() != null)
                        Toast.makeText(getContext(), current.getEventText(),
                                Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }


            }
        });
        ReadEvents();
        return root;
    }

    private void addEventToFirestore(Event event) {

        fireStore = FirebaseFirestore.getInstance();
        final String id = UUID.randomUUID().toString();
        //  String classId = MySharedPreference.getString(getContext(), CLICKED_CLASS, null);
        Event event2 = new Event(event.getTime(), event.getEventText(), event.getEventColor(),event.getClassId());
        fireStore.collection("events").document(id).set(event2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Map<String, Object> studentData = new HashMap<>();
                studentData.put("eventId", id);
                String classid = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
                fireStore.collection("classes").document(classid).collection("class_events")
                        .document(id).set(studentData, SetOptions.merge());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


    public void ReadEvents() {
        String classID = MySharedPreference.getString(getContext(), CLICKED_CLASS, "NONE");
        fireStore = FirebaseFirestore.getInstance();
        fireStore.collection("classes").document(classID).collection("class_events").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for ( final QueryDocumentSnapshot document : task.getResult()) {
                                if (document != null) {
                                    events_in_class_array.add(document.get("eventId").toString());
                                    fireStore.collection("events").document(document.get("eventId").toString()).get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        long time = Long.parseLong(task.getResult().get("time").toString());
                                                        String text = task.getResult().get("eventText").toString();
                                                        int color = Integer.parseInt(task.getResult().get("eventColor").toString());
                                                        calenderEvent.addEvent(new Event(time,text,color));
                                                    }else{

                                                    }
                                                }//end oncomplete task
                                            });// end firestore collection get
                                    Log.d("TAG_Events", "events list: " + events_in_class_array.toString());

                                } else {
                                    //doc is null
                                }
//                                    Log.d("TAG", "it is null :", task.getException());
//
//                                    return;
//                                }
//                                Student a = new Student(document.get("parentemail").toString(), document.get("name").toString(), R.drawable.flower);
//                                albumList.add(a);
//                                adapter.notifyItemInserted(++position);
//                                recyclerView.scrollToPosition(position);
//                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void removeEvents(){

    }

//   public Event initDialogEvent(final Long dateInMillieSeconds){
//       Button create_event;
//       final TextView event_name;
//       final CreateEventDialog customDialog = new CreateEventDialog(getActivity());
//        Event event1;
//       customDialog.show();
//       customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//       create_event = customDialog.findViewById(R.id.eventButton);
//       event_name = customDialog.findViewById(R.id.eventName_name);
//
//       create_event.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//                event1 = new Event(dateInMillieSeconds, event_name.getText().toString() , Color.RED);
//
//            }//End  onClick() //for forgotPassDialog_sendBtn btn
//       });
//       return event1;
//    }

}