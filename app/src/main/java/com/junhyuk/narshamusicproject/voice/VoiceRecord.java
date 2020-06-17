package com.junhyuk.narshamusicproject.voice;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoiceRecord {
    private Context context;
    private File recordFile;
    public static String RECORD_NAME = "recorded.mp4";
    public static String TAG = "VoiceRecord";
    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    private int mSampleRate = 16000;
    private int mChannelCount = AudioFormat.CHANNEL_IN_STEREO;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelCount, mAudioFormat);
    public Thread mRecordThread = null;
    public AudioTrack mAudioTrack = null;
    public boolean isRecording = false;

    public AudioRecord mAudioRecord = null;
    public VoiceRecord(Context context) {
        this.context = context;
        recordFile =  new File(Environment.getExternalStorageDirectory(), RECORD_NAME);
        Log.d(TAG, "저장할 파일 명 : " + recordFile.getAbsoluteFile());
        setRecordThread();
    }
    public void recordAudio() {


    }
    private void setRecordThread(){
        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelCount, mAudioFormat, mBufferSize);
        mAudioRecord.startRecording();

        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, mChannelCount, mAudioFormat, mBufferSize, AudioTrack.MODE_STREAM);
        mRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] readData = new byte[mBufferSize];

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(recordFile);
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }

                while(isRecording) {
                    int ret = mAudioRecord.read(readData, 0, mBufferSize);
                    Log.d(TAG, "read bytes is " + ret);

                    try {
                        fos.write(readData, 0, mBufferSize);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void stopRecord(){
        isRecording = false;
    }
}
