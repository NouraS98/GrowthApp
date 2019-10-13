package sa.ksu.swe444;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import static sa.ksu.swe444.Constants.keys.USER_EMAIL;
import static sa.ksu.swe444.Constants.keys.USER_ID;
import static sa.ksu.swe444.Constants.keys.USER_NAME;
import static sa.ksu.swe444.Constants.keys.USER_NATIONAL_ID;
import static sa.ksu.swe444.Constants.keys.USER_NEW;
import static sa.ksu.swe444.Constants.keys.USER_PHONE;
import static sa.ksu.swe444.Constants.keys.USER_TYPE;


public class ProfileFragment extends Fragment {

    private Button save_button_dailog;
    private EditText editprofile_email, editprofile_name, editprofile_phone;
    NavigationView navigationView;
    ImageView logout;
    View v;
    String userId;
    FirebaseFirestore fireStore;
    FirebaseAuth firebaseAuth;
    TextView nametxtview, emailtxtview, phonetxtview;
    ImageView editButton;
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

        String name = MySharedPreference.getString(getContext(), USER_NAME, ":(");
        String email = MySharedPreference.getString(getContext(), USER_EMAIL, ":(");
        String phone = MySharedPreference.getString(getContext(), USER_PHONE, ":(");
        String type = MySharedPreference.getString(getContext(), USER_TYPE, ":(");
        String uid = MySharedPreference.getString(getContext(), USER_ID, ":(");
        String nid = MySharedPreference.getString(getContext(), USER_NATIONAL_ID, ":(");
        Boolean newuser = MySharedPreference.getBoolean(getContext(), USER_NEW, false);
        nametxtview = getView().findViewById(R.id.nameProfile);
        nametxtview.setText(name);
        phonetxtview = getView().findViewById(R.id.phoneProfile);
        phonetxtview.setText(phone);
        emailtxtview = getView().findViewById(R.id.EmailProfile);
        emailtxtview.setText(email);

        editButton = getView().findViewById(R.id.edit_button_profile);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });


    }


    private void initViews() {


        navigationView = v.findViewById(R.id.nav_view);
        getActivity().setTitle(R.string.menu_myInfo);


    }//end initViews()


    private void executeGetProfileSetting() {
        //saved data


        MySharedPreference.putBoolean(getContext(), Constants.keys.IS_LOGIN, true);


    }//end executeGetProfileSetting()

    private void editProfile() {
        final EditProfileDialog customDialog = new EditProfileDialog(getActivity());
        customDialog.show();
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        save_button_dailog = customDialog.findViewById(R.id.btn_save);
        editprofile_name = customDialog.findViewById(R.id.editprofile_edtname);
        editprofile_email = customDialog.findViewById(R.id.editprofile_edteame);
        editprofile_phone = customDialog.findViewById(R.id.editprofile_edtphone);
        editprofile_name.setText(nametxtview.getText().toString());
        editprofile_email.setText(emailtxtview.getText().toString());
        editprofile_phone.setText(phonetxtview.getText().toString());
        save_button_dailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(editprofile_name.getText().toString().matches("")) && !(editprofile_email.getText().toString().matches("")) && !(editprofile_phone.getText().toString().matches(""))) {
//                    if(validateEmail(editprofile_email.getText().toString())&& !editprofile_email.getText().toString().equals(emailtxtview)){
//                        FirebaseUser firebaseUser;
//                        firebaseAuth = FirebaseAuth.getInstance();
//                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                            firebaseUser = firebaseAuth.getCurrentUser();
//                            userId = firebaseUser.getUid();
//                            firebaseUser.updateEmail(editprofile_email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if(task.isSuccessful()) {
//                                        TextView emailchanged = customDialog.findViewById(R.id.email_changed);
//                                        emailchanged.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            });
//                        }
//                    }else{
//                        TextView emailchanged = customDialog.findViewById(R.id.email_changed);
//                        emailchanged.setText("changes not made");
//                        emailchanged.setTextColor(getResources().getColor(R.color.colorPink));
//                        emailchanged.setVisibility(View.VISIBLE);
//                    }
                    if (validatePhone(editprofile_phone.getText().toString()) && !editprofile_phone.getText().toString().equals(phonetxtview)) {
                        fireStore = FirebaseFirestore.getInstance();
                        firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser;
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            firebaseUser = firebaseAuth.getCurrentUser();
                            userId = firebaseUser.getUid();
                            fireStore.collection("teachers").document(userId).update("phone", editprofile_phone.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                TextView phonechanged = customDialog.findViewById(R.id.phone_changed);
                                                phonechanged.setVisibility(View.VISIBLE);
                                                phonetxtview.setText(editprofile_phone.getText().toString());
                                                customDialog.dismiss();
                                                showDialog("phone changed successfully");

                                            }
                                        }
                                    });
                        }
                    } else {
                        TextView phonechanged = customDialog.findViewById(R.id.phone_changed);
                        phonechanged.setText("incorrect format");
                        phonechanged.setTextColor(getResources().getColor(R.color.colorPink));
                        phonechanged.setVisibility(View.VISIBLE);
                        showDialog("please enter correct phone number");
                    }
                } else {
                    showDialog("changes not made successfully");
                }
                //customDialog.dismiss();
            }//End  onClick() //for forgotPassDialog_sendBtn btn
        });
    }//End forgetPasswordAction

    public void showDialog(String msg) {
        final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity()).create();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialogbackground));
        alertDialog.setMessage(msg);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }//End onClick()
                });//End BUTTON_POSITIVE
        if (!getActivity().isFinishing()) {
            alertDialog.show();
        }

    }//End showDialog()

    private boolean validatePhone(String phone) {
        String regexStr = "^05\\d{8}$";
        if (phone.length() < 10 || phone.length() > 13 || phone.matches(regexStr) == false) {
            return false;
        } else return true;
    }//End validatePhone()

    private boolean validateEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }// end validateEmail()
}//end fragment class