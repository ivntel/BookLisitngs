 package com.example.ivnte.booklisitngs;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

 public class MainActivity extends AppCompatActivity {
     ListView bookList;
     public TextView noResults;
     private static String urlString;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         Button but = (Button) findViewById(R.id.search_button);
         noResults = (TextView) findViewById(R.id.no_results);

         but.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 EditText searchText = (EditText) findViewById(R.id.search_text);
                 String inputQuery = searchText.getText().toString();
                 inputQuery = inputQuery.replace(" ", "+");
                 /*String APIKEY = "&key=AIzaSyA1h9z38G2ZmIfvDgKmhh-t9a07zfVlYRY";
                 urlString = "https://www.googleapis.com/books/v1/volumes?q=" + inputQuery + "&orderBy=newest";
                 Log.v("URL:", urlString);
                 new ProcessJSON().execute(urlString);*/
             }
         });
     }
 }
