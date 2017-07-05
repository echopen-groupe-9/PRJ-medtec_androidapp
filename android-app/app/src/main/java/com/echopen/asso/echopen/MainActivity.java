package com.echopen.asso.echopen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingConnectionType;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;
import com.echopen.asso.echopen.model.EchoImage.EchoCharImage;
import com.echopen.asso.echopen.ui.RenderingContextController;
import com.echopen.asso.echopen.utils.Ln;
import com.echopen.asso.echopen.utils.Timer;

import org.w3c.dom.Text;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

/**
 * MainActivity class handles the main screen of the app.
 * Tools are called in the following order :
 * - initSwipeViews() handles the gesture tricks via GestureDetector class
 * - initViewComponents() mainly sets the clickable elements
 * - initActionController() and setupContainer() : in order to separate concerns, View parts are handled by the initActionController()
 * method which calls the MainActionController class that deals with MainActivity Views,
 * especially handles the display of the main screen picture
 * These two methods should be refactored into one
 */

public class MainActivity extends Activity implements EchographyImageVisualisationContract.View, View.OnClickListener {


    ImageView echo_image;
    EchographyImageStreamingService serviceEcho;

    private static final String TAG = "MyActivity";

    /**
     * This method calls all the UI methods and then gives hand to  UDPToBitmapDisplayer class.
     * UDPToBitmapDisplayer listens to UDP data, processes them with the help of ScanConversion,
     * and then displays them.
     * Also, this method uses the Config singleton class that provides device-specific constants
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RenderingContextController rdController = new RenderingContextController();
        serviceEcho = new EchographyImageStreamingService(rdController);
        final EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(serviceEcho, this);

        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode("192.168.10.1", REDPITAYA_PORT);
        serviceEcho.connect(mode, this);
        presenter.start();

        final Button btn_capture = (Button) findViewById(R.id.btn_capture);
        final Button btn_save = (Button) findViewById(R.id.btn_save);
        final TextView txtview = (TextView) findViewById(R.id.txt_view_main);

        txtview.setOnClickListener(MainActivity.this);
        btn_save.setVisibility(View.INVISIBLE);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            //Freeze picture & hide take button
            public void onClick(View v) {
                serviceEcho.deleteObservers();
                btn_capture.setVisibility(View.INVISIBLE);
                btn_save.setVisibility(View.VISIBLE);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "Picture Saved" ,Toast.LENGTH_SHORT );
                toast.show();
                presenter.start();
                btn_capture.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * Following the doc https://developer.android.com/intl/ko/training/basics/intents/result.html,
     * onActivityResult is “Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.”,
     * See more here : https://stackoverflow.com/questions/20114485/use-onactivityresult-android
     *
     * @param requestCode, integer argument that identifies your request
     * @param resultCode,  to get its values, check RESULT_CANCELED, RESULT_OK here https://developer.android.com/reference/android/app/Activity.html#RESULT_OK
     * @param data,        Intent instance
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void refreshImage(final Bitmap iBitmap) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView echoImage = (ImageView) findViewById(R.id.echo_view);
                    echoImage.setImageBitmap(iBitmap);
                    Timer.logResult("Display Bitmap");
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {
        presenter.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_capture:
                serviceEcho.deleteObservers();
                Button btn_capture = (Button) findViewById(R.id.btn_capture);
                Button btn_save = (Button) findViewById(R.id.btn_save);
                btn_capture.setVisibility(View.INVISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                break;
            case R.id.btn:
                Log.d("Log","Lo d");
        }
    }

    public void test (EchographyImageStreamingService service){

    }
}