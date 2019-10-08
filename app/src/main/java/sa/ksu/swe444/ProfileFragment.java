package sa.ksu.swe444;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_NATIONAL_ID;
import static sa.ksu.swe444.Constants.keys.USER_NEW;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;
import static sa.ksu.swe444.Constants.keys.USER_TYPE;


public class ProfileFragment extends Fragment {


    NavigationView navigationView;
    ImageView logout;
    View v;
    TextView nametxtview,emailtxtview,phonetxtview;
    private static final String LOG = ProfileFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.profile, container, false);

        initViews();
        executeGetProfileSetting();


        return v;

    }//end onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String name = MySharedPreference.getString(getContext(),USER_NAME,":(");
        String email = MySharedPreference.getString(getContext(),USER_EMAIL,":(");
        String phone = MySharedPreference.getString(getContext(),USER_PHONE,":(");
        String type = MySharedPreference.getString(getContext(),USER_TYPE,":(");
        String uid = MySharedPreference.getString(getContext(),USER_ID,":(");
        String nid = MySharedPreference.getString(getContext(), USER_NATIONAL_ID,":(");
        Boolean newuser = MySharedPreference.getBoolean(getContext(),USER_NEW,false);
        nametxtview = getView().findViewById(R.id.nameProfile);
        nametxtview.setText(name);
        phonetxtview = getView().findViewById(R.id.phoneProfile);
        phonetxtview.setText(phone);
        emailtxtview = getView().findViewById(R.id.EmailProfile);
        emailtxtview.setText(email);
    }



    private void initViews() {


        navigationView = v.findViewById(R.id.nav_view);
        getActivity().setTitle(R.string.menu_myInfo);


    }//end initViews()


    private void executeGetProfileSetting() {
        //saved data


        MySharedPreference.putBoolean(getContext(), Constants.keys.IS_LOGIN, true);



    }//end executeGetProfileSetting()


}//end fragment class