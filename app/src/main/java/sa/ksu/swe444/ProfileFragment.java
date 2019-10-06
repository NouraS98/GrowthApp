package sa.ksu.swe444;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;



public class ProfileFragment extends Fragment {


    NavigationView navigationView;
    ImageView logout;
    View v;
    private static final String LOG = ProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //initializing the views
//        int type = MySharedPreference.getInt(getContext(), Constants.Keys.sectionType, 0);
//
//        if (type == 0) { //buisness
            v = inflater.inflate(R.layout.profile, container, false);
//        } else {
//            v = inflater.inflate(R.layout.fragment_profile_company, container, false);
//
//        }

        initViews();
        executeGetProfileSetting();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutAction();
            }
        });


        return v;

    }//end onCreateView

    private void logoutAction() {
        //showing alert dialog to confirm logout action
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        // Setting Dialog Message
        alertDialog.setMessage(R.string.logout_msg);

        // Setting Negative "logout" Button
        alertDialog.setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                excuteLogout();
            }//end onClick
        });//end setPositiveButton

        // Setting Negative "cancel" Button
        alertDialog.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }//end onClick
        });//end setNegativeButton


        alertDialog.show();

    }//end logoutAction()

    private void initViews() {


        navigationView = v.findViewById(R.id.nav_view);
        getActivity().setTitle(R.string.menu_myInfo);
        logout = (ImageView) v.findViewById(R.id.logoutBtn);


    }//end initViews()


    private void executeGetProfileSetting() {
        //saved data
        MySharedPreference.putBoolean(getContext(), Constants.keys.IS_LOGIN, true);

    }//end executeGetProfileSetting()


    private void excuteLogout() {

        clearData();
        MySharedPreference.putBoolean(getContext(), Constants.keys.IS_LOGIN, false);


        Intent intent = new Intent(getContext(), LoginSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }//end excuteLogout()


    private void clearData() {

        //setting the previous language
        String language = MySharedPreference.getString(getContext(), Constants.keys.APP_LANGUAGE, "en");

        MySharedPreference.clearData(getContext());
        MySharedPreference.putString(getContext(), Constants.keys.APP_LANGUAGE, language);

    }//end cleaData()


}//end fragment class