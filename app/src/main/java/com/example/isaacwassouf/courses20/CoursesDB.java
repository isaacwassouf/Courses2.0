package com.example.isaacwassouf.courses20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by isaacwassouf on 10/4/17.
 */

public class CoursesDB {

    Courses courses;
    Context context;
    public CoursesDB(Context context){
        courses= new Courses(context);
        this.context=context;
    }


    private boolean CourseExist(String coursename){
        boolean res=false;
        SQLiteDatabase db= courses.getReadableDatabase();
        String[] col= new String[] {courses.CourseName};
        Cursor cursor = db.query(courses.TB2name,col,null,null,null,null,null);
        int index = cursor.getColumnIndex(courses.CourseName);

        if (cursor.moveToFirst()){
            do {
                String course= cursor.getString(index);
                if (coursename.equals(course)){
                    res= true;
                    break;
                }
            }while (cursor.moveToNext());
        }
        return res;
    }

    private boolean checkCursorEmpty(Cursor cursor){
        if(cursor.moveToNext()){
            return false;
        }
        else{
            return true;
        }
    }

    public String retrieveCourse(int pos){
        SQLiteDatabase db= courses.getReadableDatabase();
        String[] str= new String[] {courses.CourseName};
        Cursor cursor = db.query(courses.TB2name,str,null,null,null,null,null);
        int i=0;
        String cou="";
        if (cursor.moveToFirst()){
            while(i<=pos){
                cou= cursor.getString(cursor.getColumnIndex(courses.CourseName));
                cursor.moveToNext();
                i++;
            }
        }
        return cou;
    }

    public Cursor showCourses(){
        try {
            SQLiteDatabase db = courses.getReadableDatabase();
            String[] col = new String[]{courses.ID ,courses.CourseName}; //MUST INCLUDE THE ID COLUMN
            Cursor cursor = db.query(courses.TB2name, col, null, null, null, null, null);
            if (checkCursorEmpty(cursor)){
                Toast.makeText(context, "No Courses Yet", Toast.LENGTH_SHORT).show();
                return null;
            }
            else{
                return cursor;
            }
        }
        catch (SQLException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public Cursor showLectures(String courseName){

        if (courseName.isEmpty()){
            Toast.makeText(context, "courseName is Empty", Toast.LENGTH_SHORT).show();
            return null;
        }

        else {


            try {
                SQLiteDatabase db = courses.getReadableDatabase();
                String[] col = new String[]{courses.ID, courses.lectureName, courses.checked};
                String selection = Courses.CourseName + " =?";
                String[] selectionArgs = new String[]{courseName};
                Cursor cursor = db.query(courses.TB1name, col, selection, selectionArgs, null, null, null);
                return cursor;

            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                return null;
            }
        }

    }
    
    public void insertCourse(String courseName){

        if (courseName.isEmpty()){
            Toast.makeText(context, "you entered a blank name", Toast.LENGTH_LONG).show();
            return;
        }
        else if (CourseExist(courseName)) {
            Toast.makeText(context, "you have a course in the same name", Toast.LENGTH_SHORT).show();
            return;
        }

        else {

            try {
                SQLiteDatabase db = courses.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(courses.CourseName, courseName);
                contentValues.put(courses.lecNum, 0);
                db.insert(courses.TB2name, null, contentValues);
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    public void insertLecture(String lectureName, String courseName){

        if(lectureName.isEmpty()){
            Toast.makeText(context, "you enterd a blank lecture name", Toast.LENGTH_SHORT).show();
            return;
        }

        else {
            try {
                SQLiteDatabase db = courses.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(courses.lectureName, lectureName);
                contentValues.put(courses.CourseName, courseName);
                contentValues.put(courses.checked, 0);
                db.insert(courses.TB1name, null, contentValues);

            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateLectureToZero(int id){
        try{
            SQLiteDatabase db = courses.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(courses.checked,0);
            String[] whereargs = new String[] {String.valueOf(id)};
            db.update(courses.TB1name,contentValues,"_id=?",whereargs);

        }catch(SQLException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void updateLectureToOne(int id){
        try{
            SQLiteDatabase db = courses.getReadableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(courses.checked,1);
            String[] whereargs = new String[] {String.valueOf(id)};
            db.update(courses.TB1name,contentValues,"_id=?",whereargs);

        }catch(SQLException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteLecture(int id){
        try{
            SQLiteDatabase db = courses.getReadableDatabase();
            String [] whereargs = new String[] {String.valueOf(id)};
            db.delete(courses.TB1name,"_id=?",whereargs);

        }catch(SQLException e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCourse(String courseName){
        try{
            SQLiteDatabase db= courses.getWritableDatabase();
            String[] whereargs = new String[] {courseName};
            db.delete(courses.TB1name,courses.CourseName+"=?",whereargs);
            db.delete(courses.TB2name,courses.CourseName+"=?",whereargs);

        }catch(SQLException e){
            Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    
    static class Courses extends SQLiteOpenHelper{


        private final static String DBname="CoursesDB";
        private final static String TB1name="Lecture";
        private final static String TB2name="Course";
        private final static String lectureName="lecture_name";
        private final static String CourseName="course_name";
        private final static String checked="is_checked";
        private final static String lecNum="LecturesNum";
        private final static String ID="_id";
        private final static int versionNum=1;

        private final static String TB1Create="CREATE TABLE IF NOT EXISTS " + TB1name + "("
                +ID+" INTEGER  PRIMARY KEY, " + lectureName + " TEXT," + CourseName + " TEXT,"
                + checked + " INTEGER DEFAULT 0 "+");";

        private final static String TB2Create="CREATE TABLE IF NOT EXISTS " + TB2name + "("
                +ID+" INTEGER  PRIMARY KEY, " + CourseName + " TEXT," + lecNum + " INTEGER  DEFAULT 0" + ");";

        private  Context context;

        public Courses(Context context) {
            super(context,DBname, null,versionNum);
            this.context= context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try{
                sqLiteDatabase.execSQL(TB1Create);
                sqLiteDatabase.execSQL(TB2Create);
            }
            catch (SQLException e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            try{
                sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+ TB1name);
                sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+ TB2name);
                onCreate(sqLiteDatabase);
            }
            catch (SQLException e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
