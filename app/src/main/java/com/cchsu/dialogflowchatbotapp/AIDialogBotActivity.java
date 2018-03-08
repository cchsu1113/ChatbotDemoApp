package com.cchsu.dialogflowchatbotapp;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;
import ai.api.ui.AIDialog;

public class AIDialogBotActivity extends AppCompatActivity implements AIDialog.AIDialogListener {

    private TextView resultTextView;
    private AIDialog aiDialog;
    private AIConfiguration config = null;
    private TextToSpeech mTextToSpeech = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidialog_bot);

        resultTextView = (TextView)findViewById(R.id.resultTextView);
        config = new AIConfiguration(MainActivity.CLIENT_ACCESS_TOKEN, AIConfiguration.SupportedLanguages.ChineseTaiwan, AIConfiguration.RecognitionEngine.System);

        aiDialog = new AIDialog(this, config);
        aiDialog.setResultsListener(this);
    }

    public void buttonListenOnClick(final View view) {
        aiDialog.showAndListen();
    }

    @Override
    public void onResult(final AIResponse response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Result result = response.getResult();
                final String speech = result.getFulfillment().getSpeech();
                resultTextView.setText("Query:" + result.getResolvedQuery() + "\nResponse: " + speech);
                // text to sppech

                //TextToSpeech text2speech = new TextToSpeech(this, this);
                //TTS.speak(speech); //語音合成會把程式當掉，有空再處理
            }
        });
    }

    @Override
    public void onError(final AIError error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        if (aiDialog != null) {
            aiDialog.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (aiDialog != null) {
            aiDialog.resume();
        }
        super.onResume();
    }

    @Override
    public void OnInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            int result = mTextToSpeech.setLanguage(Locale.TAIWAN);//设置识别语音为中文
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                    || result == TextToSpeech.ERROR) {
                Toast.makeText(this, "数据丢失或语言不支持", Toast.LENGTH_SHORT).show();
            }
            if (result == TextToSpeech.LANG_AVAILABLE) {
                Toast.makeText(this, "支持该语言", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "初始化成功", Toast.LENGTH_SHORT).show();
        }
}
