package com.junhyuk.narshamusicproject.voice;

import android.util.Log;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EtriVoiceApi {
    public static String res = "";
    public static String voiceToString(final File voiceFile){
        res="";
        //EtriApi thread = new EtriApi(voiceFile.getAbsolutePath());
        EtriApi thread = new EtriApi("/storage/emulated/0/Music/record.wav");
        try{
            thread.start();
            thread.join(10000);
        }catch (Exception e){

        }
        Log.d("VoiceRecord","[body res] " + res);



        return res;
    }
    static class EtriApi extends Thread{
        private String path="";
        public EtriApi(String path){
            this.path = path;
        }
        @Override
        public void run() {
            String openApiURL = "http://aiopen.etri.re.kr:8000/WiseASR/Recognition";
            String accessKey = "1c8d733e-e922-4d5e-b4ef-e2cf9e1d8570"; // 발급받은 API Key
            String languageCode = "korean"; // 언어 코드
            //String audioFilePath = "C:\\audio\\KORF.pcm"; // 녹음된 음성 파일 경로
            String audioFilePath = path;//"//C:\\audio\\record.wav";
            String audioContents = null;

            Gson gson = new Gson();

            Map<String, Object> request = new HashMap<>();
            Map<String, String> argument = new HashMap<>();
            Log.d("VoiceRecord","[audioFilePath] " + audioFilePath);
            try {
                Path path = Paths.get(audioFilePath);
                byte[] audioBytes = Files.readAllBytes(path);
                audioContents = Base64.getEncoder().encodeToString(audioBytes);
                Log.d("VoiceRecord","[send] " + audioContents);
                System.out.println("send : "+audioContents);
            } catch (IOException e) {
                e.printStackTrace();
            }

            argument.put("language_code", languageCode);
            argument.put("audio", audioContents);

            request.put("access_key", accessKey);
            request.put("argument", argument);

            URL url;
            Integer responseCode = null;
            String responBody = null;
            try {
                url = new URL(openApiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.write(gson.toJson(request).getBytes("UTF-8"));
                wr.flush();
                wr.close();

                responseCode = con.getResponseCode();
                InputStream is = con.getInputStream();
                byte[] buffer = new byte[is.available()];
                int byteRead = is.read(buffer);
                responBody = new String(buffer);
                Log.d("VoiceRecord","[byteRead] " + byteRead);

                System.out.println("[responseCode] " + responseCode);
                System.out.println("[responBody]");
                System.out.println(responBody);
                Log.d("VoiceRecord","[responseCode] " + responseCode);
                Log.d("VoiceRecord","[body] " + responBody);
                res = responBody;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
