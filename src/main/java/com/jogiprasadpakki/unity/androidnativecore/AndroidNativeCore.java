package com.jogiprasadpakki.unity.androidnativecore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jogiprasadpakki.unity.androidnativecore.callbacks.callBacks;
import com.unity3d.player.UnityPlayer;

import java.io.File;

/**
 * Created by Jogi Prasad Pakki on 15-May-18.
 * No one allowed to use or modify this script.
 * If you any questions mail me to jogiprasadpakki@gmail.com
 * Copy right 2018 All reserved by Jogi Prasad Pakki.
 */

public class AndroidNativeCore extends Activity {

    private final int CameraImageRequest = 1001;
    private final int GallerImageRequest = 1002;

    private File file;
    private Uri fileUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = getIntent().getExtras().getInt("id");
        switch (id){
            case 8:
                Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                Resources resources = UnityPlayer.currentActivity.getResources();
                int app_name = resources.getIdentifier("app_name", "string", UnityPlayer.currentActivity.getPackageName());
                file = new File(Environment.getExternalStorageDirectory(),getString(app_name).replace("\\s+","")+"_"+String.valueOf(System.currentTimeMillis() + ".jpg"));
                fileUri = Uri.fromFile(file);
                imageIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
                startActivityForResult(imageIntent, CameraImageRequest);
                break;
            case 9:
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GallerImageRequest);
                break;
        }

    }

    public String getImagePath(Uri uri){
        String path = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        if(getContentResolver() != null){
            Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
            if(cursor != null){
                cursor.moveToFirst();
                int index = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(index);
                cursor.close();
            }
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CameraImageRequest:
                if (resultCode == Activity.RESULT_OK){
                   UnityPlayer.UnitySendMessage("AndroidNativeCore","onImagePick",file.getAbsolutePath());
                   finish();
                }else if(resultCode == Activity.RESULT_CANCELED){
                    UnityPlayer.UnitySendMessage("AndroidNativeCore","onCansel","");
                    finish();
                }
                break;
            case GallerImageRequest:
                if(resultCode == Activity.RESULT_OK){
                    Uri uri = data.getData();
                    UnityPlayer.UnitySendMessage("AndroidNativeCore","onImagePick",getImagePath(uri));
                    finish();
                }else if(resultCode == Activity.RESULT_CANCELED) {
                    UnityPlayer.UnitySendMessage("AndroidNativeCore","onCansel","");
                    finish();
                }
                break;
        }

    }
}


