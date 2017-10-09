package com.example.isaacwassouf.courses20;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CoursesDB Db;
    ListView listView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Db= new CoursesDB(this);
        listView = (ListView) findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str= Db.retrieveCourse(i);
                Bundle bundle = new Bundle();
                bundle.putString("class",str);
                Intent intent = new Intent( MainActivity.this,Main2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }


        @Override
      protected void onResume(){
            super.onResume();
            populateList2();
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void addCourse(MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.alert_layout,null);
        builder.setView(view);
        final EditText editText = view.findViewById(R.id.editText);
        builder.setMessage("Enter a Course Name");
        builder.setTitle("Add new Course");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String course= editText.getText().toString();
                Db.insertCourse(course);
                populateList2();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }




    //POPULATE A LISTVIEW WITH A SIMPLECURSORADAPTER

   /* public void populateList(){
        Cursor cursor = Db.showCourses();
        String[] str= new String[] {"course_name"};
        int[] id= new int[]{R.id.textView};
        SimpleCursorAdapter simpleCursorAdapter =
                new SimpleCursorAdapter(this,R.layout.first_listview_row,cursor,str,id, 0);
        listView.setAdapter(simpleCursorAdapter);

    } */

    public void populateList2(){
        Cursor cursor= Db.showCourses();
        customAdapter= new CustomAdapter(this,cursor); // PROBLEM IS HERE!!
        listView.setAdapter(customAdapter);
    }


    class CustomAdapter extends CursorAdapter{


        public CustomAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return LayoutInflater.from(context).inflate(R.layout.first_listview_row,viewGroup,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(R.id.textView);
            String txt= cursor.getString(cursor.getColumnIndexOrThrow("course_name"));
            textView.setText(txt);
        }
    }


}

