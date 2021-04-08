package com.jogiprasadpakki.unity.androidnativecore;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.hardware.Camera;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.jogiprasadpakki.unity.androidnativecore.callbacks.callBacks;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by Jogi Prasad Pakki on 15-May-18.
 * No one allowed to use or modify this script.
 * If you any questions mail me to jogiprasadpakki@gmail.com
 * Copy right 2018 All reserved by Jogi Prasad Pakki.
 */

public  class Core {

    private NotificationCompat.Builder notificationBuilder;
    private Camera camera;

    //Os details
    public String GetVersionName() {
        return Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
    }

    //device Details
    public boolean isFlashAvailable() {
        return UnityPlayer.currentActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    //Vibration
    @SuppressLint("MissingPermission")
    public void Vibrate(long milliseconds) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator)UnityPlayer.currentActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator)UnityPlayer.currentActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(milliseconds);
        }
    }
    @SuppressLint("MissingPermission")
    public void VibrateWaveForm(long[] milliseconds, int repet) {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) UnityPlayer.currentActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createWaveform(milliseconds, repet));
        } else {
            ((Vibrator) UnityPlayer.currentActivity.getSystemService(VIBRATOR_SERVICE)).vibrate(milliseconds, repet);
        }
    }
    @SuppressLint("MissingPermission")
    public void VibrateCansel(){
        ((Vibrator) UnityPlayer.currentActivity.getSystemService(VIBRATOR_SERVICE)).cancel();
    }

    public void FlashOn() {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
    }
    public void FlashOff() {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            camera.release();
    }

    //Notification
    public void NotificationBuilder(String chanel){
            PendingIntent pendingIntent = PendingIntent.getActivity(UnityPlayer.currentActivity,0,new Intent(UnityPlayer.currentActivity.getApplicationContext(), UnityPlayerActivity.class),0);
            notificationBuilder = new NotificationCompat.Builder(UnityPlayer.currentActivity,chanel);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setAutoCancel(true);

    }
    public void NotificationSetIcon(String id){
        Resources resources = UnityPlayer.currentActivity.getResources();
        int app_icon = resources.getIdentifier(id, "drawable", UnityPlayer.currentActivity.getPackageName());
        notificationBuilder.setSmallIcon(app_icon);
    }
    public void NotificationSetDefaultSound(){
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
    }
    public void NotificationSetSound(String identifier){

        Resources resources =  UnityPlayer.currentActivity.getResources();
        int sound_id = resources.getIdentifier(identifier, "raw", UnityPlayer.currentActivity.getPackageName());
        Log.d("ANC", String.valueOf(sound_id));
        Uri soundUri = Uri.parse("android.resource://"+UnityPlayer.currentActivity.getPackageName()+"/"+sound_id);
        notificationBuilder.setSound(soundUri);

    }
    public void NotificationsetGroup(String groupId,boolean gropSummery){
        notificationBuilder.setGroup(groupId);
        notificationBuilder.setGroupSummary(gropSummery);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void crateNotificationChanel(String id, String name, String description, int importence, int LockScreenVisibility, boolean enableLights, String LightColor, boolean enableVibration, boolean enableBadge){
            NotificationManager notificationManager = (NotificationManager)UnityPlayer.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            android.app.NotificationChannel channel = new android.app.NotificationChannel(id,name,importence);
            channel.setDescription(description);
            channel.enableLights(enableLights);
            channel.setLightColor(Color.parseColor(LightColor));
            channel.setShowBadge(enableBadge);
            channel.setLockscreenVisibility(LockScreenVisibility);
            channel.enableVibration(enableVibration);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChanel(String chanel){
        NotificationManager notificationManager = (NotificationManager)UnityPlayer.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.deleteNotificationChannel(chanel);
    }
    public void NotificationSetTitle(String tit){
        notificationBuilder.setContentTitle(tit);
    }
    public void NotificationSetPriority(int priority){
        notificationBuilder.setPriority(priority);
    }
    public void NotificationSetMsg(String msg){
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
    }
    public void NotificationBigMsg(String msg){
        notificationBuilder.setContentText(msg);
    }
    public void NotificationSetLargeIcon(byte[] data){
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(GetImage(data)));
    }
    public void ShowNotification(final int id, final String imgUrl){
        if(!imgUrl.equals("null")){
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... strings) {
                    InputStream in;
                    try {
                        String link = strings[0];
                        Log.d("ANC",link);
                        URL url = new URL(link);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        in = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(in);
                        in.close();
                        return myBitmap;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                    NotificationManagerCompat notify = NotificationManagerCompat.from(UnityPlayer.currentActivity);
                    notify.notify(id,notificationBuilder.build());
                }
            }.execute(imgUrl);
        }else {
            NotificationManagerCompat notify = NotificationManagerCompat.from(UnityPlayer.currentActivity);
            notify.notify(id,notificationBuilder.build());
        }
    }

    //Pickers
    public void openImagePicker(boolean isCamera){
        Intent intent = new Intent(UnityPlayer.currentActivity,AndroidNativeCore.class);
        if(isCamera){
            intent.putExtra("id",8);
            UnityPlayer.currentActivity.startActivity(intent);
        }else {
            intent.putExtra("id",9);
            UnityPlayer.currentActivity.startActivity(intent);
        }
    }

    //OpenSettings
    public void LunchApp(String packageId){
            Intent intent = UnityPlayer.currentActivity.getPackageManager().getLaunchIntentForPackage(packageId);
            UnityPlayer.currentActivity.startActivity(intent);
    }
    public boolean isApplicationInstalled(String packageId){
            PackageManager packageManager = UnityPlayer.currentActivity.getPackageManager();
            try {
                packageManager.getPackageInfo(packageId,PackageManager.GET_ACTIVITIES);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return false;
    }

    //GetImage From Unity
    public static Bitmap GetImage(byte[] data){
        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        return bitmap;
    }
    public String saveBitmap(byte[] data){
        Resources resources = UnityPlayer.currentActivity.getResources();
        int app_name = resources.getIdentifier("app_name", "string", UnityPlayer.currentActivity.getPackageName());
        String bitmapPath = MediaStore.Images.Media.insertImage(UnityPlayer.currentActivity.getContentResolver(), GetImage(data),UnityPlayer.currentActivity.getString(app_name)+"_"+ String.valueOf(System.currentTimeMillis()), null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        return String.valueOf(bitmapUri);
    }
}