package com.example.memoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter{

        private final static String DB_NAME = "memo.db";
        private final static String TABLE_NAME = "memos";
        private final static int DB_VERSION = 1;

        public final static String MEMO_ID = "_id";
        public final static String MEMO_TITLE = "title";
        public final static String MEMO_PLACE = "place";
        public final static String MEMO_TEXT = "text";

        private SQLiteDatabase db = null;
        private DBHelper dbHelper = null;
        protected Context context;

        public DBAdapter(Context context) {
            this.context = context;
            dbHelper = new DBHelper(this.context);
        }

        public DBAdapter openDB() {
            db = dbHelper.getWritableDatabase();
            return this;
        }

        public DBAdapter readDB() {
            db = dbHelper.getReadableDatabase();
            return this;
        }

        public void closeDB() {
            db.close();
            db = null;
        }

        public void saveDB(String Title, String Place, String Text) {

            db.beginTransaction();

            try {
                ContentValues values = new ContentValues();
                values.put(MEMO_TITLE, Title);
                values.put(MEMO_PLACE, Place);
                values.put(MEMO_TEXT, Text);

                db.insert(TABLE_NAME, null, values);

                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        public Cursor getDB(String[] columns) {

            return db.query(TABLE_NAME, columns, null, null, null, null, null);
        }

        public Cursor searchDB(String[] columns, String column, String[] name) {
            return db.query(TABLE_NAME, columns, column + " like ?", name, null, null, null);
        }

        public void allDelete() {

            db.beginTransaction();
            try {

                db.delete(TABLE_NAME, null, null);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        public void selectDelete(String position) {

            db.beginTransaction();
            try {
                db.delete(TABLE_NAME, MEMO_ID + "=?", new String[]{position});
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        private static class DBHelper extends SQLiteOpenHelper {

            public DBHelper(Context context) {

                super(context, DB_NAME, null, DB_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {

                //テーブルを作成するSQL文の定義 ※スペースに気を付ける
                String createTbl = "CREATE TABLE " + TABLE_NAME + " ("
                        + MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + MEMO_TITLE + " TEXT NOT NULL,"
                        + MEMO_PLACE + " TEXT NOT NULL,"
                        + MEMO_TEXT + " TEXT NOT NULL"
                        + ");";

                db.execSQL(createTbl);
            }


            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                db.execSQL("DROP TABLE IF EXISTS memos");

                onCreate(db);
            }
        }


}