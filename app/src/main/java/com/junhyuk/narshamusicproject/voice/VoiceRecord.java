package com.junhyuk.narshamusicproject.voice;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.junhyuk.narshamusicproject.dialog.CustomDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VoiceRecord {
    public static interface VoiceRecordResult{
        public void getVoide(String msg);
    }
//    private Context context;
//    private File recordFile;
//    public static String RECORD_NAME = "recorded.pcm";
//    public static String TAG = "VoiceRecord";
//    private int mAudioSource = MediaRecorder.AudioSource.MIC;
//    private int mSampleRate = 16000;
//    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO;
//    private int mAudioFormat = AudioFormat.ENCODING_PCM_8BIT;
//    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);
//    public Thread mRecordThread = null;
//    public AudioTrack mAudioTrack = null;
//    public boolean isRecording = false;
//
//    public AudioRecord mAudioRecord = null;
//    public VoiceRecord(Context context) {
//        this.context = context;
//        //recordFile =  new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/", RECORD_NAME);
//        recordFile = new File("/storage/emulated/0/Music/"+RECORD_NAME);
//        Log.d(TAG, "저장할 파일 명 : " + recordFile.getAbsoluteFile());
//        //setRecordThread();
//    }
//    public void recordAudio() {
//        setRecordThread();
//        isRecording = true;
//
//        if(mAudioRecord == null) {
//            //AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,16000, 16, 1, 2, 16000.0F, false);
//            mAudioRecord =  new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
//            mAudioRecord.startRecording();
//        }
//        mRecordThread.start();
//
//    }
//    private void setRecordThread(){
//        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
//        mAudioRecord.startRecording();
//
//        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
//        mRecordThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] readData = new byte[mBufferSize];
//
//                FileOutputStream fos = null;
//                try {
//                    //Log.d(TAG, "오픈할 파일 명 : " + recordFile.getAbsoluteFile());
//                   // fos = new FileOutputStream(recordFile);
//                    fos = context.openFileOutput(recordFile.getName(),Context.MODE_PRIVATE);
//
//
//                } catch(FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                while(isRecording) {
//                    int ret = mAudioRecord.read(readData, 0, mBufferSize);
//                   // Log.d(TAG, "read bytes is " + ret);
//
//                    try {
//                        if(fos!=null) {
//                            Log.d(TAG, "read bytes is " + ret);
//                            fos.write(readData, 0, mBufferSize);
//                        }else{
//                            Log.d(TAG, "write false");
//                        }
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
//                }
//
//                mAudioRecord.stop();
//                mAudioRecord.release();
//                mAudioRecord = null;
//
//                try {
//
//                    fos.close();
//                    File f = context.getFileStreamPath(recordFile.getName());
//                    Log.d(TAG, "fos path : " + f.getAbsoluteFile());
//                    Log.d(TAG, "file save : " + f.length());
//                    EtriVoiceApi.voiceToString(context.getFileStreamPath(recordFile.getName()) );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//    public void stopRecord(){
//        isRecording = false;
//    }
    Context context;
    String TAG="VoiceRecord";//LOG타이틀
    //음성 인식용
    Intent SttIntent;
    SpeechRecognizer mRecognizer;
    CustomDialog customDialog;

    public VoiceRecord(Context context, CustomDialog customDialog) {
        this.context = context;
        this.customDialog = customDialog;
    }

    public void recordAudio() {
        SttIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,this.context.getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");//한국어 사용
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(this.context);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(SttIntent);
    }
    private RecognitionListener listener=new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            log("onReadyForSpeech..........."+"\r\n");

        }

        @Override
        public void onBeginningOfSpeech() {
            Toast.makeText(context,"지금부터 말을 해주세요...........", Toast.LENGTH_LONG).show();
            log("지금부터 말을 해주세요..........."+"\r\n");
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            log("onBufferReceived..........."+"\r\n");
        }

        @Override
        public void onEndOfSpeech() {
            log("onEndOfSpeech..........."+"\r\n");
        }

        @Override
        public void onError(int i) {
            Toast.makeText(context,"천천히 다시 말해 주세요...........", Toast.LENGTH_LONG).show();
            log("천천히 다시 말해 주세요..........."+"\r\n");
        }

        @Override
        public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult =results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            //txtInMsg.setText(rs[0]+"\r\n"+txtInMsg.getText());
            log("success end");
            Toast.makeText(context, rs[0], Toast.LENGTH_LONG).show();
            log(rs[0]+"\r\n");
            //FuncVoiceOrderCheck(rs[0]);
            customDialog.getVoide(rs[0]);
            mRecognizer.startListening(SttIntent);

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            log("onPartialResults..........."+"\r\n");
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            log("onEvent..........."+"\r\n");
        }
    };
    private void log(String data){
        Log.d(TAG,data);
    }


}
