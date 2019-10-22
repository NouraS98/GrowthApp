package sa.ksu.swe444.ui;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;


import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sa.ksu.swe444.R;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {

    private static final int INTENT_CAMERA =401;
    private static final int INTENT_GALLERY =301;
    ImageView image;
    View root;
    Boolean isSelectImage;
    private File imageFile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

         root = inflater.inflate(R.layout.fragment_setting, container, false);
        LinearLayout camerall = root.findViewById(R.id.cameralayout);
        image = root.findViewById(R.id.signupImg);

        camerall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCameraChoser();
            }
        });
        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_GALLERY || requestCode == INTENT_CAMERA) {
                try{
                    final Uri imageURI = imageReturnedIntent.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageURI);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(selectedImage);
                    isSelectImage=true;
                    persistImage(selectedImage);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Uri selectedImage = imageReturnedIntent.getData();
                image.setImageURI(selectedImage);
            }// END INNER IF
        }// END OUTER IF



//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    image.setImageURI(selectedImage);
//                }


    }// end onActivityResult
    private void persistImage(Bitmap bitmap){
        File filesDir = getActivity().getFilesDir();
        String namee="";
//        if(name.getText() !=null)
//            namee = name.getText().toString();
        imageFile = new File(filesDir,namee+".png");
        OutputStream os;
        try{
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG , 100 ,os);
            os.flush();
            os.close();

        }catch (Exception E){
            Log.e(getClass().getSimpleName(),"Error writing bitmap",E);
        }
    }
    private void openCameraChoser(){
        if (!isPermission()) {
            requestPermissions();
            return;
        }
        showPhotoOptionDialog();
    }
    private boolean isPermission(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }
    private void requestPermissions(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},INTENT_CAMERA);
    }
    private  void showPhotoOptionDialog(){
        final CharSequence[] items ={"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i].equals("Camera")){
                    cameraIntent();
                } else if(items[i].equals("Gallery")){
                    galleryIntent();
                }
            }


        });
        builder.show();

    }
    private void galleryIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent,INTENT_GALLERY);

    }
    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,INTENT_CAMERA);
    }
}
