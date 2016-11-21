package socket.network.kiuno.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private EditText txtServerIP,txtPort;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
            goHeadTransfer();
    }

    private void initialize(){
        txtServerIP = (EditText)findViewById(R.id.txtServerIP);
        txtPort = (EditText)findViewById(R.id.txtPort);
    }

    public void goHeadPicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {}

            if (photoFile != null) {
                try {
                    Uri photoURI = Uri.fromFile(photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }catch(Exception e){
                    e.printStackTrace();
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    public void goHeadTransfer(){
        try {
            String ip = txtServerIP.getText().toString();
            int port = Integer.parseInt(txtPort.getText().toString());
            TransferFiles tf = new TransferFiles(ip,port);
            tf.start(mCurrentPhotoPath);
        }catch (Exception e){}
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void clearData(View view) {
        txtServerIP.setText("");
        txtPort.setText("");
    }
}
