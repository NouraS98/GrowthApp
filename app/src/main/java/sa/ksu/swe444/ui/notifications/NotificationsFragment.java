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

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.skyhope.eventcalenderlibrary.CalenderEvent;
import com.skyhope.eventcalenderlibrary.listener.CalenderDayClickListener;
import com.skyhope.eventcalenderlibrary.model.DayContainerModel;
import com.skyhope.eventcalenderlibrary.model.Event;

import java.util.Calendar;

import sa.ksu.swe444.CreateClassDialog;
import sa.ksu.swe444.CreateEventDialog;
import sa.ksu.swe444.JavaObjects.Class;
import sa.ksu.swe444.R;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "CalenderTest";
    Button addEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        final CalenderEvent calenderEvent = root.findViewById(R.id.calender_event);
        addEvent = root.findViewById(R.id.eventButton);

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Event event = new Event(calendar.getTimeInMillis(), "Test");
        calenderEvent.addEvent(event);
        Event event1 = new Event(calendar.getTimeInMillis(), "Party!");
        calenderEvent.addEvent(event1);

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
                                Event event1 = new Event(dayContainerModel.getTimeInMillisecond(), event_name.getText().toString(), Color.RED);
                                if (event1 != null)
                                    calenderEvent.addEvent(event1);
                                customDialog.dismiss();
                            }//End  onClick() //for forgotPassDialog_sendBtn btn
                        });


                    }
                });

                Event current = calenderEvent.getEvent(dayContainerModel.getTimeInMillisecond());
                Log.d(TAG, dayContainerModel.getDate());
                try {
                    if (current.getEventText() != null)
                        Toast.makeText(getContext(), current.getEventText(),
                                Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }

            }
        });
        return root;
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
