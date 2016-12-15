package vulan.com.chatapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import vulan.com.chatapp.R;

public class PhotoUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonChoose;
    private StorageReference mStorageReference;
    private static final int GALLERY_INTENT=2,CAMERA_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
       // mStorageReference= FirebaseStorage.getInstance().getReference();
        mStorageReference=FirebaseStorage.getInstance().getReferenceFromUrl("gs://chatapp-a87a2.appspot.com");
        findView();
    }

    private void findView(){
        mButtonChoose= (Button) findViewById(R.id.button_choose_photo);
        mButtonChoose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_CODE&&resultCode==RESULT_OK){
            Uri uri=data.getData();
            StorageReference filePath=mStorageReference.child("Photos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(PhotoUploadActivity.this,"Done",Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
