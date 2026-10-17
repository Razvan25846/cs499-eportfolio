package com.example.warehouseinventory;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class SmsActivity extends AppCompatActivity {

    private static final int REQ_SMS = 3001;

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_sms);

        tvStatus = findViewById(R.id.tvPermissionStatus);
        MaterialButton btn = findViewById(R.id.btnRequestSmsPermission);

        btn.setOnClickListener(v -> requestSmsPermission());
        refreshStatus();
    }

    private void refreshStatus() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
        tvStatus.setText(granted ? "Granted" : "Not granted");
    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "SMS permission already granted.", Toast.LENGTH_SHORT).show();
            refreshStatus();
            return;
        }

        ActivityCompat.requestPermissions(
                this,
                new String[]{ Manifest.permission.SEND_SMS },
                REQ_SMS
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_SMS) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Toast.makeText(this, granted ? "SMS enabled." : "SMS denied. App will still work.", Toast.LENGTH_SHORT).show();
            refreshStatus();
        }
    }
}