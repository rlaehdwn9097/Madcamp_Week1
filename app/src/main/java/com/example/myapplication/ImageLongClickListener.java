package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class ImageLongClickListener implements View.OnLongClickListener {

    Context context;
    int position;
    public ImageLongClickListener(Context context,int position){
        this.context = context;
        this.position = position;
    }
    public boolean onLongClick(View v) {

        DBHelper dbHelper;
        dbHelper = new DBHelper(context,"test2.db",null,1);
        deleteDialog(dbHelper);
        //dbHelper.delete(String.valueOf(position));

        return false;
    }
    private void deleteDialog(DBHelper dbHelper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("삭제 확인"); builder.setMessage("삭제하시겠습니까?");
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //예 눌렀을때의 이벤트 처리
                dbHelper.delete(String.valueOf(position));
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //아니오 눌렀을때의 이벤트 처리
            }
        });
        builder.show();

    }

}
