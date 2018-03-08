package com.cchsu.dialogflowchatbotapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

/**
 * Created by cchsu on 2018/1/10.
 */

public class TTS {

    private static TextToSpeech textToSpeech = null;

    public static void init(final Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {

                }
            });
        }
    }

    public static void speak(final String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "test");
        //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
