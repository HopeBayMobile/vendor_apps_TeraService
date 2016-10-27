package com.hopebaytech.teraservice.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import org.json.JSONObject;

/**
 * @author Vince
 *      Created by Vince on 2016/10/24.
 */

public class ThumbnailApiUtils {
    private final int image = 0;
    private final int video = 1;
    private int retry = 3;
    private int interval = 1000;

    private final String CLASSNAME = getClass().getSimpleName();
    private Context mContext;

    public void createThumbnailImages(Context mContext, JSONObject jsonObj) {
        this.mContext =  mContext;
        try {
            Logs.i(CLASSNAME, "createThumbnailImages", jsonObj.toString());
            Logs.i(CLASSNAME, "createThumbnailImages", "file path: " + jsonObj.getString("file_path"));
            Logs.i(CLASSNAME, "createThumbnailImages", "thumbnail type: " + String.valueOf(jsonObj.getInt("thumbnail_type")));

            int thumbnailType = jsonObj.getInt("thumbnail_type");
            //storage/emulated/0/<file name>
            String filePath = jsonObj.getString("file_path");
            switch (thumbnailType) {
                case image:
                    for (int i = 0; i < retry; i++) {
                        if (getImageThumbnail(filePath) != null) {
                            break;
                        } else {
                            Logs.e(CLASSNAME, "createThumbnailImages",
                                    "Failed to create thumbnail from image, path: " + filePath);
                        }
                        Thread.sleep(interval);
                    }
                    break;

                case video:
                    for (int i = 0; i < retry; i++) {
                        if (getVideoThumbnail(filePath) != null) {
                            break;
                        } else {
                            Logs.e(CLASSNAME, "createThumbnailImages",
                                    "Failed to create thumbnail from video, path: " + filePath);
                        }
                        Thread.sleep(interval);
                    }
                    break;
            }
        } catch (Exception e) {
            Logs.e(CLASSNAME, "createThumbnailImages", e.toString());
        }
    }

    private Bitmap getImageThumbnail(String path) {
        Bitmap thumbnail = null;
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID},
                MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                thumbnail = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            } else {
                Logs.e(CLASSNAME, "getImageThumbnail", "cursor.moveToFirst = false");
            }
            cursor.close();
        } else {
            Logs.e(CLASSNAME, "getImageThumbnail", "cursor = null");
        }
        return thumbnail;
    }

    private Bitmap getVideoThumbnail(String path) {
        Bitmap thumbnail = null;
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID},
                MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(cr, id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
            }
            cursor.close();
        } else {
            Logs.e(CLASSNAME, "getVideoThumbnail", "cursor = null");
        }
        return thumbnail;
    }
}
