package com.jogiprasadpakki.unity.androidnativecore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;

import com.jogiprasadpakki.unity.androidnativecore.callbacks.callBacks;
import com.unity3d.player.UnityPlayer;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Jogi Prasad Pakki on 15-May-18.
 * No one allowed to use or modify this script.
 * If you any questions mail me to jogiprasadpakki@gmail.com
 * Copy right 2018 All reserved by Jogi Prasad Pakki.
 */

public class Dialogs {
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertBuilder;

    private callBacks.onClickListener onClickListener;

    //Alert Dialog
    public void AlertBuild(int Theme,callBacks.onClickListener onClickListener){
        this.onClickListener = onClickListener;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
            switch (Theme){

                case 1:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,android.R.style.Theme_Material_Dialog_Alert);
                    break;
                case 2:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,android.R.style.Theme_Material_Dialog_Alert);
                    break;
                case 3:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,android.R.style.Theme_Material_Light_Dialog_Alert);
                    break;
                case 4:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,android.R.style.Theme_DeviceDefault_Dialog_Alert);
                    break;
                case 5:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    break;
            }
        }else{
            switch (Theme){
                case 1:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,0x00000001);
                    break;
                case 2:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,AlertDialog.THEME_HOLO_DARK);
                    break;
                case 3:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,AlertDialog.THEME_HOLO_LIGHT);
                    break;
                case 4:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    break;
                case 5:
                    alertBuilder = new AlertDialog.Builder(UnityPlayer.currentActivity,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    break;
            }
        }

    }
    public void AlertSetTitle(String tit){
        alertBuilder.setTitle(tit);
    }
    public void AlertSetIcon(String id){
        Resources resources = UnityPlayer.currentActivity.getResources();
        int app_icon = resources.getIdentifier(id, "drawable", UnityPlayer.currentActivity.getPackageName());
        alertBuilder.setIcon(app_icon);
    }
    public void AlertSetMsg(String msg){
        alertBuilder.setMessage(msg);
    }
    public void AlertSetPositiveButtion(String txt, final String ClickId){
        alertBuilder.setPositiveButton(txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onClick(ClickId);
            }
        });
    }
    public void AlertSetNegtiveButtion(String txt, final String ClickId){
        alertBuilder.setNegativeButton(txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onClick(ClickId);
            }
        });
    }
    public void AlertSetCanselable(boolean canselabel){
        alertBuilder.setCancelable(canselabel);
    }
    public void AlertSetNeutralButtion(String txt, final String ClickId){
        alertBuilder.setNeutralButton(txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onClickListener.onClick(ClickId);
            }
        });
    }
    public void ShowAlert(){
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
    public  void dismissAlert() {
       alertDialog.dismiss();
   }

}
