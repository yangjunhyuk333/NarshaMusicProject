package com.junhyuk.narshamusicproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.voice.VoiceRecord;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDialog implements VoiceRecord.VoiceRecordResult {
    private Context context;
    private static VoiceRecord record;
    Dialog dialog;
    public static final String TAG = "CustomDialog";

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void show(){

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_item);

        dialog.show();

        Button button = dialog.findViewById(R.id.cancel_bnt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "녹음 완료", Toast.LENGTH_SHORT).show();
                if(record != null) {
                    //record.stopRecord();
                }
                dialog.dismiss();
            }
        });
        recordStart();
    }
    private void recordStart(){
        if(record == null){
            record = new VoiceRecord(context,this);
        }
        record.recordAudio();
    }

    @Override
    public void getVoide(String msg) {
        Log.d(TAG,"msg : "+msg);
        dialog.dismiss();
        new S2WTask().execute("");
    }

    public void stringToWord(String msg) {

    }
    static public class Morpheme {
        final String text;
        final String type;
        Integer count;
        public Morpheme (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }
    static public class NameEntity {
        final String text;
        final String type;
        Integer count;
        public NameEntity (String text, String type, Integer count) {
            this.text = text;
            this.type = type;
            this.count = count;
        }
    }

    class S2WTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU";
            String accessKey = "1c8d733e-e922-4d5e-b4ef-e2cf9e1d8570\n";   // 발급받은 API Key
            String analysisCode = "ko-KR";        // 언어 분석 코드
            String text = "가을아침 노래 틀어줘";//VoiceRecord.rs;           // 분석할 텍스트 데이터

            Gson gson = new Gson();

            Map<String, Object> request = new HashMap<>();
            Map<String, String> argument = new HashMap<>();

            argument.put("analysis_code", analysisCode);
            argument.put("text", text);

            request.put("access_key", accessKey);
            request.put("argument", argument);

            URL url;
            Integer responseCode = null;
            String responBodyJson = null;
            Map<String, Object> responeBody = null;

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
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer();

                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                responBodyJson = sb.toString();
                Log.d(TAG,"[responseCode] " + responBodyJson);
                // http 요청 오류 시 처리
                if ( responseCode != 200 ) {
                    // 오류 내용 출력
                    Log.d(TAG,"[error] " + responBodyJson);
                    return null;
                }

                responeBody = gson.fromJson(responBodyJson, Map.class);
                Integer result = ((Double) responeBody.get("result")).intValue();
                Map<String, Object> returnObject;
                List<Map> sentences;

                // 분석 요청 오류 시 처리
                if ( result != 0 ) {

                    // 오류 내용 출력
                    Log.d(TAG,"[error] " + responeBody.get("result"));
                    return null;
                }

                // 분석 결과 활용
                returnObject = (Map<String, Object>) responeBody.get("return_object");
                sentences = (List<Map>) returnObject.get("sentence");

                Map<String, Morpheme> morphemesMap = new HashMap<String, Morpheme>();
                Map<String, NameEntity> nameEntitiesMap = new HashMap<String, NameEntity>();
                List<Morpheme> morphemes = null;
                List<NameEntity> nameEntities = null;

                for( Map<String, Object> sentence : sentences ) {
                    // 형태소 분석기 결과 수집 및 정렬
                    List<Map<String, Object>> morphologicalAnalysisResult = (List<Map<String, Object>>) sentence.get("morp");
                    for( Map<String, Object> morphemeInfo : morphologicalAnalysisResult ) {
                        String lemma = (String) morphemeInfo.get("lemma");
                        Morpheme morpheme = morphemesMap.get(lemma);
                        if ( morpheme == null ) {
                            morpheme = new Morpheme(lemma, (String) morphemeInfo.get("type"), 1);
                            morphemesMap.put(lemma, morpheme);
                        } else {
                            morpheme.count = morpheme.count + 1;
                        }
                    }

                    // 개체명 분석 결과 수집 및 정렬
                    List<Map<String, Object>> nameEntityRecognitionResult = (List<Map<String, Object>>) sentence.get("NE");
                    for( Map<String, Object> nameEntityInfo : nameEntityRecognitionResult ) {
                        String name = (String) nameEntityInfo.get("text");
                        NameEntity nameEntity = nameEntitiesMap.get(name);
                        if ( nameEntity == null ) {
                            nameEntity = new NameEntity(name, (String) nameEntityInfo.get("type"), 1);
                            nameEntitiesMap.put(name, nameEntity);
                        } else {
                            nameEntity.count = nameEntity.count + 1;
                        }
                    }
                }

                if ( 0 < morphemesMap.size() ) {
                    morphemes = new ArrayList<Morpheme>(morphemesMap.values());
                    morphemes.sort( (morpheme1, morpheme2) -> {
                        return morpheme2.count - morpheme1.count;
                    });
                }

                if ( 0 < nameEntitiesMap.size() ) {
                    nameEntities = new ArrayList<NameEntity>(nameEntitiesMap.values());
                    nameEntities.sort( (nameEntity1, nameEntity2) -> {
                        return nameEntity2.count - nameEntity1.count;
                    });
                }

                // 형태소들 중 명사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
                morphemes
                        .stream()
                        .filter(morpheme -> {
                            return morpheme.type.equals("NNG") ||
                                    morpheme.type.equals("NNP") ||
                                    morpheme.type.equals("NNB");
                        })
                        .limit(5)
                        .forEach(morpheme -> {
                            Log.d(TAG,"[명사] " + morpheme.text + " ("+morpheme.count+")" );
                        });

                // 형태소들 중 동사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
                Log.d(TAG,"");
                morphemes
                        .stream()
                        .filter(morpheme -> {
                            return morpheme.type.equals("VV");
                        })
                        .limit(5)
                        .forEach(morpheme -> {
                            Log.d(TAG,"[동사] " + morpheme.text + " ("+morpheme.count+")" );
                        });

                // 인식된 개채명들 많이 노출된 순으로 출력 ( 최대 5개 )
                Log.d(TAG,"");
                nameEntities
                        .stream()
                        .limit(5)
                        .forEach(nameEntity -> {
                            Log.d(TAG,"[개체명] " + nameEntity.text + " ("+nameEntity.count+")" );
                        });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
