package api;

import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

public class  TextToSpeechAPIOnline {
    public static void getTextToSpeech(String text, String language) {
        try {
            VoiceProvider tts = new VoiceProvider("b71ac35ba2a140d29a88ade10512d5e9");
            VoiceParameters params = new VoiceParameters(text, language.equals("English") ? Languages.English_UnitedStates : Languages.Vietnamese);
            params.setCodec(AudioCodec.WAV);
            params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
            params.setBase64(false);
            params.setSSML(false);
            params.setRate(0);
            byte[] voice = tts.speech(params);
            AudioManager.startPlaying(voice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
