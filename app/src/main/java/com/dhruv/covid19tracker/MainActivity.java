package com.dhruv.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView mylist;

    ArrayAdapter<String> arrayAdapter;

    String[] fetch={"" ,"","","","","","","","","","","","","","","","","","","","","","","","","" ,"","","","","","","","","","","",""} ;
    int i=0;
    SQLiteDatabase CaseDB;
    customAdapter custom;

    public void refresh(View view)
{arrayAdapter.notifyDataSetChanged();



}


////////////////////// function to scrap data from web and put it in sql database
    public void webscrape(){
        CaseDB.execSQL("DELETE FROM rawdata");

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url="https://www.mohfw.gov.in/";
                String sql = "INSERT INTO rawData (name,confirmed,cured,death) VALUES (?,?,?,?)";
                SQLiteStatement statement= CaseDB.compileStatement(sql);

                try{

                    final Document document = Jsoup.connect(url).get();


                    for (Element row : document.select(
                            "div.data-table.table-responsive tr")){

                        final String Sno =
                                row.select("td:nth-of-type(1)").text();

                        final String Name =
                                row.select("td:nth-of-type(2)").text();

                        final String Con =
                                row.select("td:nth-of-type(3)").text();

                        final String Cured =
                                row.select("td:nth-of-type(4)").text();

                        final String Death =
                                row.select("td:nth-of-type(5)").text();

                        statement.bindString(1,Name);
                        statement.bindString(2,Con);
                        statement.bindString(3,Cured);
                        statement.bindString(4,Death);
                        statement.execute();
                        fetch[i]=(Sno + " " + Name + " " + Con + " " + Cured + " " + Death);
                        i++;}


                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "NOT WORKING", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

           // getdata();

    }
// function to access data from database and put it in custom list
  public void getdata() {
      //CaseDB.execSQL("DELETE FROM rawdata");
      //CaseDB.execSQL("INSERT INTO rawData (name,confirmed,cured,death) VALUES (0,0,0,0)");
      Cursor c = CaseDB.rawQuery("SELECT * FROM rawData", null);

      int nameindex = c.getColumnIndex("name");
      int confirmedindex = c.getColumnIndex("confirmed");
      int curedindex = c.getColumnIndex("cured");
      int deathindex = c.getColumnIndex("death");

      custom  = new customAdapter(this);


      int cSize = c.getCount();
      if (c.moveToFirst() && c != null) {
          for (int i = 0; i < cSize; i++) {
              custom.list.add(new singleRow(c.getString(nameindex),c.getString(confirmedindex),c.getString(curedindex),c.getString(deathindex)));
              c.moveToNext();
          }
      }
      mylist.setAdapter(new customAdapter(this));

      custom.notifyDataSetChanged();
      //getting log of array size and cursor
          Log.d("SIZE", String.valueOf(custom.list.size()) + " " + cSize);

  }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mylist= findViewById(R.id.mylist);
        CaseDB = this.openOrCreateDatabase("RawData",MODE_PRIVATE,null);
        CaseDB.execSQL("CREATE TABLE IF NOT EXISTS rawData (id INTEGER PRIMARY KEY,name VARCHAR, confirmed VARCHAR,cured VARCHAR,death VARCHAR)");
        arrayAdapter=  new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fetch);
            mylist.setAdapter(arrayAdapter);
        webscrape();
        //getdata();
       // custom.notifyDataSetChanged();




    }
    // defining class for custom adapter and custom layout
    class singleRow{

        String name,confirmed,cured,death;


        singleRow(String name,String confirmed,String cured,String death)
        {
            this.name = name;
            this.confirmed=confirmed;
            this.cured= cured;
            this.death=death;

        }


    }
    class customAdapter extends BaseAdapter{

        ArrayList<singleRow> list;
        Context context;


        customAdapter(Context cc)
        {context=cc;
            list= new ArrayList<singleRow>();
                list.add(new singleRow("State","Cases","Recovered","Death"));


        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.singlerowl_layout,parent,false);

            TextView name = (TextView)row.findViewById(R.id.name);
            TextView confimed = (TextView)row.findViewById(R.id.confirmed);
            TextView cured = (TextView)row.findViewById(R.id.cured);
            TextView death = (TextView)row.findViewById(R.id.death);
            singleRow temp = list.get(position);

            name.setText(temp.name);
            confimed.setText(temp.confirmed);
            cured.setText(temp.cured);
            death.setText(temp.death);

            return row;
        }
    }



    }//main class ends here



