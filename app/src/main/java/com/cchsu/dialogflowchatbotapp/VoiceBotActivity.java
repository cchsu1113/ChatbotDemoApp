package com.cchsu.dialogflowchatbotapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIService;
import ai.api.android.AIConfiguration;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class VoiceBotActivity extends AppCompatActivity implements AIListener{

    private Button process = null;
    private TextView resultMessage = null;
    private AIService aiService = null;
    private AIConfiguration config = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_bot);

        process = (Button)findViewById(R.id.btn_process);
        resultMessage = (TextView)findViewById(R.id.tv_voice_result);
        config = new AIConfiguration(MainActivity.CLIENT_ACCESS_TOKEN, AIConfiguration.SupportedLanguages.ChineseTaiwan, AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);
    }

    public void onProcess(View v) {
        aiService.startListening();
    }

    public void onResult(final AIResponse response) {
        Result result = response.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        resultMessage.setText("Query:" + result.getResolvedQuery() +
                "\nResponse: " + result.getFulfillment().getSpeech());
    }

    @Override
    public void onError(final AIError error) {
        resultMessage.setText(error.toString());
    }

    @Override
    public void onListeningStarted() {}

    @Override
    public void onListeningCanceled() {}

    @Override
    public void onListeningFinished() {}

    @Override
    public void onAudioLevel(final float level) {}

    public void onGoBack(View v) {
        finish();
    }
}
