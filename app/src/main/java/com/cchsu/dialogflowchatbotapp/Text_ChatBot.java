package com.cchsu.dialogflowchatbotapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

public class Text_ChatBot extends AppCompatActivity {

    private EditText input = null;
    private TextView usersay = null;
    private TextView responseMsg = null;

    private ai.api.AIDataService dataService = null;
    private AIConfiguration config = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__chatbot);

        input = (EditText)findViewById(R.id.txt_inputmsg);
        usersay = (TextView)findViewById(R.id.tv_botsaying);
        responseMsg = (TextView)findViewById(R.id.tv_botresponse);

        config = new AIConfiguration(MainActivity.CLIENT_ACCESS_TOKEN, AIConfiguration.SupportedLanguages.ChineseTaiwan, AIConfiguration.RecognitionEngine.System);
        dataService = new ai.api.AIDataService(config);
    }

    private Runnable serviceThread = new Runnable() {
        @Override
        public void run() {
            try {
                String aiQryMessage = input.getText().toString();
                AIRequest request = new AIRequest(aiQryMessage);
                AIResponse response = dataService.request(request);
                if (response.getStatus().getCode() == 200) {
                    input.setText("");
                    usersay.setText(aiQryMessage);
                    responseMsg.setText(response.getResult().getFulfillment().getSpeech());
                } else {
                    responseMsg.setText(response.getStatus().getErrorDetails());
                }
            } catch (Exception e) {
                responseMsg.setText(e.toString());
            }
        }
    };

    public void sendClick(View v) {
        Thread thread = new Thread(serviceThread);
        thread.start();
    }

    public void onGoBack(View v) {
        finish();
    }
}
