package com.lakeel.altla.sample.speachrecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public final class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SpeechRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRecognizer();
    }

    @Override
    protected void onPause() {
        stopRecognizer();
        super.onPause();
    }

    public void startRecognizer() {
        if (recognizer == null) {
            if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Speach recognition not available.", Toast.LENGTH_LONG).show();
                Log.e(TAG, "Speach recognition not available.");
                finish();
            }

            recognizer = SpeechRecognizer.createSpeechRecognizer(this);
            recognizer.setRecognitionListener(new RecognitionListenerImpl());
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        recognizer.startListening(intent);
    }

    public void stopRecognizer() {
        if (recognizer != null) {
            recognizer.destroy();
        }
        recognizer = null;
    }

    public void restartRecognizer() {
        stopRecognizer();
        startRecognizer();
    }

    private final class RecognitionListenerImpl implements RecognitionListener {

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech()");
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived(byte[])");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech()");
        }

        public void onError(int error) {
            Log.d(TAG, "onError(int)");

            String reason;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    reason = "ERROR_AUDIO";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    reason = "ERROR_CLIENT";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    reason = "ERROR_INSUFFICIENT_PERMISSIONS";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    reason = "ERROR_NETWORK";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    reason = "ERROR_NETWORK_TIMEOUT";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    reason = "ERROR_NO_MATCH";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    reason = "ERROR_RECOGNIZER_BUSY";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    reason = "ERROR_SERVER";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    reason = "ERROR_SPEECH_TIMEOUT";
                    break;
                default:
                    reason = "Unknown";
                    break;
            }

            Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
            restartRecognizer();
        }

        public void onEvent(int eventType, Bundle params) {
        }

        public void onPartialResults(Bundle partialResults) {
        }

        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "Please talk.", Toast.LENGTH_SHORT).show();
        }

        public void onResults(Bundle results) {
            List<String> strings = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (strings != null) {
                StringBuilder builder = new StringBuilder();
                for (String string : strings) {
                    builder.append(string).append(System.lineSeparator());
                }
                if (builder.length() != 0) {
                    builder.setLength(builder.length() - 1);
                }

                Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_LONG).show();
            }

            restartRecognizer();
        }

        public void onRmsChanged(float rmsdB) {
        }
    }
}
