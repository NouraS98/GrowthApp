package sa.ksu.swe444;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        handleDefaultAppLocale();

    }//end onCreate()

    private String getSystemLocalLanguage() {
        Locale locale;
        //checking versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getSystem().getConfiguration().getLocales().get(0);
        }//end if
        else {
            locale = getResources().getSystem().getConfiguration().locale;
        }//end else
        return locale.getLanguage();

    }//end getSystemLocalLanguage()


    public void showProgressDialog(boolean isCancebale) {

        try {
            if (progressDialog == null || !progressDialog.isShowing()) {

                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.Pleas_wait));
                progressDialog.setCancelable(isCancebale);
                progressDialog.show();

            }//end if

        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch

    }//end showProgressDialog

    public void hideProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }//end inner if
            }//end bigger if
        }//end try
        catch (Exception e) {
            e.printStackTrace();
        }//end catch
    }//end hideProgressDialog

    public void showDialog(String msg) {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setMessage(msg);

        alertDialog.setButton((androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE), getString(R.string.okBtn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();

            }//end on click

        });//end setButton

        alertDialog.show();

    }//end showDialog
}//END CLASS