package com.example.lab5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import es.dmoral.toasty.Toasty;

public class BaseActivity extends AppCompatActivity {

    public void startNewActivity(Class target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessegeSuccess(String message1) {
        Toasty.success(this, message1, Toast.LENGTH_SHORT).show();
    }

    public void showMessegeWarning(String message2) {
        Toasty.warning(this, message2, Toast.LENGTH_SHORT).show();
    }

    public void showMessegeError(String message3) {
        Toasty.error(this, message3, Toast.LENGTH_SHORT).show();
    }

    public Bitmap convertDrawabletoBitmap(int drawable) {
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), drawable);
        return icon;
    }

    public byte[] setByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 5, bos);
        return bos.toByteArray();
    }

    public Bitmap ByteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }
}
