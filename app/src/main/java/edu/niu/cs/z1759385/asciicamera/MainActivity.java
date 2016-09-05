package edu.niu.cs.z1759385.asciicamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Bitmap capturedPhoto;
    private TextView asciiImage;
    private Button asciiBtn,cameraBtn;
    private ProgressBar asciiPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        asciiImage = (TextView)findViewById(R.id.artTextView);
        asciiBtn = (Button)findViewById(R.id.asciiButton);
        asciiBtn.setEnabled(false);
        cameraBtn = (Button)findViewById(R.id.cameraButton);

        asciiPB = (ProgressBar)findViewById(R.id.asciiProgressBar);
        asciiPB.setAlpha(0);
    }

    public void toCamera(View view)
    {
        asciiImage.setText("");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView photoPreview = (ImageView)findViewById(R.id.cameraImageView);

        if(requestCode == CAPTURE_IMAGE_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Bundle extras = data.getExtras();
                capturedPhoto = (Bitmap)extras.get("data");
                photoPreview.setImageBitmap(capturedPhoto);
                asciiBtn.setEnabled(true);
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Result Cancelled", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"image capture failed",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void toAscii(View view)
    {
        new PerformAsyncTask().execute();
    }

    //inner AsyncTask class
    private class PerformAsyncTask extends AsyncTask<Void, Void, Void>
    {
        String asciiArtWork;
        @Override
        protected Void doInBackground(Void... params) {
            int thumbnailWidth = capturedPhoto.getWidth(),
                    thumbnailHeight = capturedPhoto.getHeight(),
                    scaleWidth = 2,
                    scaleHeight = 2;
            for(int y = 0;y<thumbnailHeight/scaleHeight;y++)
            {
                for(int x =0 ;x<thumbnailWidth/scaleWidth;x++)
                {
                    int pixel = capturedPhoto.getPixel(x*scaleWidth,y*scaleHeight);
                    int redVal = Color.red(pixel),
                            greenVal = Color.green(pixel),
                            blueVal = Color.blue(pixel);
                    int grayVal = (redVal+greenVal+blueVal)/3;
                    if( grayVal < 35 )
                        asciiArtWork += "MM";
                    else if( grayVal <= 52 )
                        asciiArtWork += "$$";
                    else if( grayVal <= 69 )
                        asciiArtWork += "##";
                    else if( grayVal <= 86 )
                        asciiArtWork += "%%";
                    else if( grayVal <= 103 )
                        asciiArtWork += "**";
                    else if( grayVal <= 120 )
                        asciiArtWork += "++";
                    else if( grayVal <= 137 )
                        asciiArtWork += "vV";
                    else if( grayVal <= 154 )
                        asciiArtWork += "-;";
                    else if( grayVal <= 171 )
                        asciiArtWork += "--";
                    else if( grayVal <= 188 )
                        asciiArtWork += ";;";
                    else if( grayVal <= 205 )
                        asciiArtWork += "::";
                    else if( grayVal <= 222 )
                        asciiArtWork += "..";
                    else
                        asciiArtWork += "  ";
                }
                asciiArtWork += "\n";
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asciiBtn.setEnabled(false);
            cameraBtn.setEnabled(false);
            //make progressbar visible
            asciiPB.setAlpha(1);
            asciiArtWork = "\n";
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cameraBtn.setEnabled(true);
            asciiBtn.setEnabled(false);
            asciiPB.setAlpha(0);
            asciiImage.setText(asciiArtWork);
        }
    }
}
