package com.junhyuk.narshamusicproject;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CustomDialog {
    private Context context;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void show(){

        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_item);

        dialog.show();

        Button button = (Button)dialog.findViewById(R.id.cancel_bnt);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "다이얼로그 취소", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
    }
}
