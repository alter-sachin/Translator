package io.github.catcharide.translator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private SpeechRecognizer speechRecognizer;

    public static final int MY_PERMISSION_RECORD_AUDIO = 1;

    private SpeechListener speechListener = new SpeechListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSION_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("onCreate","is SpeechRecognizer Working ? " + SpeechRecognizer.isRecognitionAvailable(this));
        textView = (TextView)findViewById(R.id.textView);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(speechListener);
        Log.d("onCreate","is SpeechRecognizer Working ? " + SpeechRecognizer.isRecognitionAvailable(this));

    }

    public void startRecord(View view) {

        Log.d("startRecord","In here");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        // Checking if speechRecognizer is working.
        speechRecognizer.startListening(intent);
        Log.d("startRecord","is SpeechRecognizer Working ? " + SpeechRecognizer.isRecognitionAvailable(this));
    }

    public void startShare(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, speechListener.getResultString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private class SpeechListener implements RecognitionListener{

        private String resultString;

        public String getResultString() {
            return resultString;
        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("onReadyForSpeech",params.toString());
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("onBeginningOfSpeech","User has started speaking");
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            Log.d("onRmsChanged","New RMS Value : " + rmsdB);
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            Log.d("onBufferReceived","Receving Sound " + buffer.length);
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("onEndOfSpeech","User has stopped speaking");
        }

        @Override
        public void onError(int error) {
            Log.d("onError","Error Occurred Bai Ji " + error);
        }

        @Override
        public void onResults(Bundle results) {
            final ArrayList<String> resultData =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String data = "";
            assert resultData != null;
            for(String result : resultData){
                data += result;
            }
            Log.d("onResults",data);
            resultString += resultData.get(0);
            textView.append(resultData.get(0));
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

            final ArrayList<String> resultData =
                    partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String data = "";
            for(String result : resultData){
                data += result;
            }
            Log.d("onResults",data);
            //textView.append(resultData.get(0));
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}
