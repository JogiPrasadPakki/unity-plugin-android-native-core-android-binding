package com.jogiprasadpakki.unity.androidnativecore;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

/**
 * Created by Jogi Prasad Pakki on 15-May-18.
 * No one allowed to use or modify this script.
 * If you any questions mail me to jogiprasadpakki@gmail.com
 * Copy right 2018 All reserved by Jogi Prasad Pakki.
 */

public class NotificationManager {

    private android.app.NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    public NotificationManager(){
        notificationManager =(android.app.NotificationManager)UnityPlayer.currentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void channel(String id, String name, String description, int importence, int LockScreenVisibility, boolean enableLights, String LightColor, boolean enableVibration, boolean enableBadge){
        android.app.NotificationChannel channel = new android.app.NotificationChannel(id,name,importence);
        channel.setDescription(description);
        channel.enableLights(enableLights);
        channel.setLightColor(Color.parseColor(LightColor));
        channel.setShowBadge(enableBadge);
        channel.setLockscreenVisibility(LockScreenVisibility);
        channel.enableVibration(enableVibration);
        notificationManager.createNotificationChannel(channel);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteNotificationChanel(String chanel){
        notificationManager.deleteNotificationChannel(chanel);
    }

    public void NotificationBuilder(String chanel){
        PendingIntent pendingIntent = PendingIntent.getActivity(UnityPlayer.currentActivity,0,new Intent(UnityPlayer.currentActivity.getApplicationContext(), UnityPlayerActivity.class),0);
        notificationBuilder = new NotificationCompat.Builder(UnityPlayer.currentActivity,chanel);
        notificationBuilder.setContentIntent(pendingIntent);

    }
    public void NotificationAutoCansel(boolean cansel){
        notificationBuilder.setAutoCancel(cansel);
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
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(Core.GetImage(data)));
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
            notificationManager.notify(id,notificationBuilder.build());
        }
    }
}
