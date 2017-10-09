package com.example.isaacwassouf.courses20;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    CoursesDB Db;
    Custom2Adatper custom2Adatper;
    ListView listView;
    String str="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Db= new CoursesDB(this);
        Bundle bundle = getIntent().getExtras();
        str=bundle.getString("class");
        listView= (ListView) findViewById(R.id.listView2);
        populate2list();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void addLecture(MenuItem item) {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Lecture");
        builder.setMessage("Add new lecture name");
        builder.setView(view);
        final EditText editText = view.findViewById(R.id.editText);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String lecName= editText.getText().toString();
                Db.insertLecture(lecName,str);
                populate2list();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




    public void populate2list(){
        Cursor cursor = Db.showLectures(str);
        custom2Adatper= new Custom2Adatper(this,cursor);
        listView.setAdapter(custom2Adatper);
    }


    private class Custom2Adatper extends CursorAdapter{

        public Custom2Adatper(Context context, Cursor cursor) {
            super(context,cursor,0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.second_listview_row,viewGroup,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            TextView textView = view.findViewById(R.id.textView2);
            String lecture= cursor.getString(cursor.getColumnIndexOrThrow("lecture_name"));
            int state=  cursor.getInt(cursor.getColumnIndexOrThrow("is_checked"));

            if (state==0){
                checkBox.setChecked(false);
            }
            else {
                checkBox.setChecked(true);
            }

            textView.setText(lecture);

        }
    }


}
