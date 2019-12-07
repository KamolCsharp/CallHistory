package com.example.call;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;
import android.widget.Toast;

import com.example.call.Data.DatabaseHandler;
import com.example.call.Model.Contact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

    private TextView lab_id, lab_name, lab_number, lab_type, lab_date, lab_duration;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lab_id = findViewById(R.id.label_id);
        lab_name = findViewById(R.id.label_name);
        lab_number = findViewById(R.id.label_number);
        lab_type = findViewById(R.id.label_type);
        lab_date = findViewById(R.id.label_date);
        lab_duration = findViewById(R.id.label_duration);
//        DatabaseHandler db = new DatabaseHandler(this);
//        List<Contact> contacts = db.getAllCallHistory();
//        Toast.makeText(this,contacts.size(),Toast.LENGTH_LONG).show();
//        for (Contact contact : contacts) {
//            lab_date.setText("\n"+contact.getCallDate());
//            lab_duration.setText("\n"+contact.getDuration());
//            lab_name.setText("\n"+contact.getName());
//            lab_number.setText("\n"+contact.getPhone_number());
//            lab_type.setText("\n"+contact.getType());
//        }
        ConnectDatabase();

    }

    public void ConnectDatabase() {
        if ((ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);


            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, 1);

            }
        } else {
            Toast.makeText(this, "Xatolik yuz berdi !!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        lab_id = findViewById(R.id.label_id);
                        lab_name = findViewById(R.id.label_name);
                        lab_number = findViewById(R.id.label_number);
                        lab_type = findViewById(R.id.label_type);
                        lab_date = findViewById(R.id.label_date);
                        lab_duration = findViewById(R.id.label_duration);
                        // label = findViewById(R.id.label);
                         GetCallDetails(lab_id, lab_name, lab_number, lab_type, lab_date, lab_duration);
                        // label.setText(GetCallDetails());
                        //                        Intent email=new Intent(Intent.ACTION_SEND);
//                        email.putExtra(Intent.EXTRA_EMAIL,new String[]{"kamolmuattar@gmail.com"});
//                        email.putExtra(Intent.EXTRA_SUBJECT,"Yashirin Xabar");
//                        email.putExtra(Intent.EXTRA_TEXT,label.getText());
//                        email.setType("message/rfc822");
//                        startActivity(Intent.createChooser(email,"Choose an Email client:"));

                    } else {
                        Toast.makeText(this, "No Perimission Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    private void GetCallDetails(TextView lab_id, TextView lab_name, TextView lab_number, TextView lab_type, TextView lab_date, TextView lab_duration) {
        StringBuffer sb_id = new StringBuffer();
        StringBuffer sb_name = new StringBuffer();
        StringBuffer sb_number = new StringBuffer();
        StringBuffer sb_type = new StringBuffer();
        StringBuffer sb_date = new StringBuffer();
        StringBuffer sb_duration = new StringBuffer();
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int i = 0, dircode = 0, min = 0, sec = 0, m;
        //////////////////////////////////////////////////
        DatabaseHandler db = new DatabaseHandler(this);

        //////////////////////////////////////////////////
        String id, phNumber, callType, calldate, contname, datastring, callDuration, dir, calld;
        Date calldayTime, calldurationtime;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
        while (managedCursor.moveToNext()) {
            phNumber = managedCursor.getString(number);
            callType = managedCursor.getString(type);
            calldate = managedCursor.getString(date);
            callDuration = managedCursor.getString(duration);
            contname = managedCursor.getString(name);
            calldayTime = new Date(Long.valueOf(calldate));
            datastring = formatter.format(calldayTime);
            dir = null;
            dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "chiquvchi";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "kiruvchi";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "javobsiz";
                    break;
            }
            i++;
            m = Integer.parseInt(callDuration);
            min = (m % 3600) / 60;
            sec = m % 60;
            callDuration = String.valueOf(min);
            sb_id.append("\n" + i);
            sb_name.append("\n" + contname);
            sb_number.append("\n" + phNumber);
            sb_type.append("\n" + dir);
            sb_date.append("\n" + datastring);
            db.addCallHistory(new Contact(contname, phNumber, dir, calldayTime, m));
            dir = String.valueOf(sec);
            sb_duration.append("\n" + (callDuration.length() == 1 ? "0" + min : min) + ":" + (dir.length() == 1 ? "0" + sec : sec));

        }
        lab_id.setText(sb_id);
        lab_date.setText(sb_date);
        lab_duration.setText(sb_duration);
        lab_name.setText(sb_name);
        lab_number.setText(sb_number);
        lab_type.setText(sb_type);
        managedCursor.close();
    }
}
