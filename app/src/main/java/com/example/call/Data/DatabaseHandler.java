package com.example.call.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.call.Model.Contact;
import com.example.call.Util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTAGER PRIMARY KEY NOT NULL, " +
                Util.KEY_NAME + " TEXT, " + Util.KEY_PHONE_NUMBER + " TEXT, "
                + Util.KEY_TYPE + " TEXT, " + Util.KEY_DATE + " DATE, " +
                Util.KEY_DURATION + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);

    }

    public void addCallHistory(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhone_number());
        values.put(Util.KEY_TYPE, contact.getType());
        values.put(Util.KEY_DATE, Date.parse(String.valueOf(contact.getCallDate())));
        values.put(Util.KEY_DURATION, contact.getDuration());
        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    public Contact getCallHistory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.KEY_ID,
                        Util.KEY_NAME, Util.KEY_PHONE_NUMBER, Util.KEY_TYPE, Util.KEY_DATE, Util.KEY_DURATION},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),
                cursor.getString(3), new Date(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)));
        return contact;
    }

    public List<Contact> getAllCallHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Contact> contactlist = new ArrayList<Contact>();
        String selectall = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectall, null);
        if (cursor.moveToNext()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhone_number(cursor.getString(2));
                contact.setType(cursor.getString(3));
                contact.setCallDate(new Date(cursor.getString(4)));
                contact.setDuration(Integer.parseInt(cursor.getString(5)));
                contactlist.add(contact);
            } while (cursor.moveToNext());
        }
        return contactlist;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.getName());
        values.put(Util.KEY_PHONE_NUMBER, contact.getPhone_number());
        values.put(Util.KEY_TYPE, contact.getType());
        values.put(Util.KEY_DATE, Date.parse(String.valueOf(contact.getCallDate())));
        values.put(Util.KEY_DURATION, contact.getDuration());
        return db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteCall(Contact contact) {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }
}
