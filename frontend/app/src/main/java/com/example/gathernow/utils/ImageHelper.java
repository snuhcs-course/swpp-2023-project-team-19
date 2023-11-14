package com.example.gathernow.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {
    public static Bitmap getCorrectlyRotatedImg(InputStream inputStream) {
        // Clone the input stream because inputStream can only be read once
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream inputStreamClone;
        InputStream inputStreamClone2;
        try {
            inputStream.transferTo(baos);
            inputStreamClone = new ByteArrayInputStream(baos.toByteArray());
            inputStreamClone2 = new ByteArrayInputStream(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
//            Log.d("SignUpActivity Testing", "IO Exception when cloning the input stream");
            return null;
        }

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(inputStreamClone);
        } catch (IOException e) {
            e.printStackTrace();
//            Log.d("SignUpActivity Testing", "IO Exception when reading exif");
        }
        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degrees = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degrees = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degrees = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degrees = 270;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(degrees);
            Bitmap selectedImgBitmap = BitmapFactory.decodeStream(inputStreamClone2);
            return Bitmap.createBitmap(selectedImgBitmap, 0, 0, selectedImgBitmap.getWidth(), selectedImgBitmap.getHeight(), matrix, true);

        }
        return BitmapFactory.decodeStream(inputStream);
    }

    // Return the path of the saved image
    public static String handleImagePicker(Context context, Uri uri, String feature) {
        if (uri != null) {
            // Load picture from uri
            InputStream inputStream;
            File outputFile = new File(context.getFilesDir(), feature + ".jpg");
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                Bitmap selectedImgBitmap = ImageHelper.getCorrectlyRotatedImg(inputStream);

                if (selectedImgBitmap != null) {
                    // Compress bitmap
                    selectedImgBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                    outputStream.close();
                    Log.d("ImageHelper Testing", "Profile picture saved to: " + outputFile.getPath());
                    return outputFile.getPath();
//                    avatarFilePath.postValue(outputFile.getPath());
                }

            } catch (IOException e) {
                Log.e("ImageHelper Testing", "Error");
                e.printStackTrace();
            }
        }
        return null;
    }
}
