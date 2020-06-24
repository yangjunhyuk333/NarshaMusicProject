package com.junhyuk.narshamusicproject.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.junhyuk.narshamusicproject.R;
import com.junhyuk.narshamusicproject.voice.VoiceRecord;

public class CustomDialog implements VoiceRecord.VoiceRecordResult {
    private Context context;
    private static VoiceRecord record;
    Dialog dialog;

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
                Toast.makeText(context, "녹음 완료", Toast.LENGTH_SHORT).show();
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
        Log.d("VoiceRecord","msg : "+msg);
        dialog.dismiss();
    }
}
