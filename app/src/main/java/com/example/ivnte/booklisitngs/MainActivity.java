 package com.example.ivnte.booklisitngs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

 public class MainActivity extends AppCompatActivity {
     ListView bookList;
     public TextView noResults;
     public TextView titles;
     public TextView authors;
     private static String booksURL;
     public ProgressBar progressBar;
     //JSON Key names
     private static final String KEY_TITLE = "title";
     private static final String KEY_AUTHORS = "authors";

     ArrayList<HashMap<String, String>> booksdata = new ArrayList<HashMap<String, String>>();

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         isNetworkAvailable();

         Button but = (Button) findViewById(R.id.search_button);
         noResults = (TextView) findViewById(R.id.no_results);
         titles = (TextView) findViewById(R.id.titles_row);
         authors = (TextView) findViewById(R.id.authors_row);
         progressBar = (ProgressBar) findViewById(R.id.progress_bar);

         but.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 EditText searchText = (EditText) findViewById(R.id.search_text);
                 String inputSearch = searchText.getText().toString();
                 inputSearch = inputSearch.replace(" ", "+");
                 String APIKEY = "&key=AIzaSyA1h9z38G2ZmIfvDgKmhh-t9a07zfVlYRY";
                 booksURL = "https://www.googleapis.com/books/v1/volumes?q=" + inputSearch + "&orderBy=newest";
                 Log.v("URL:", booksURL);
                 new ProcessJSON().execute(booksURL);
             }
         });
     }

     private boolean isNetworkAvailable() {
         ConnectivityManager connectivityManager
                 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         return activeNetworkInfo != null && activeNetworkInfo.isConnected();
     }

     private class ProcessJSON extends AsyncTask<String, Void, String> {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             progressBar.setVisibility(View.VISIBLE);
         }

         protected String doInBackground(String... strings) {
             String bookData = null;
             String booksURL = strings[0];

             HTTPDataHandler hh = new HTTPDataHandler();
             bookData = hh.GetHTTPData(booksURL);

             // Return the data from specified url
             return bookData;
         }

         protected void onPostExecute(String stream) {
             bookList = (ListView) findViewById(R.id.books_list);
             if (stream != null) {
                 try {
                     // Get the full HTTP Data as JSONObject
                     JSONObject reader = new JSONObject(stream);
                     int totalItems = reader.getInt("totalItems");
                     if (totalItems == 0) {
                         bookList.setVisibility(View.INVISIBLE);
                         progressBar.setVisibility(View.GONE);
                         Toast.makeText(MainActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                     } else {
                         noResults.setVisibility(View.GONE);
                         progressBar.setVisibility(View.GONE);
                         titles.setVisibility(View.VISIBLE);
                         authors.setVisibility(View.VISIBLE);
                         // Get the JSONArray weather
                         JSONArray bookArray = reader.getJSONArray("items");
                         // Get first object array JSONObject
                         for (int i = 0; i < bookArray.length(); i++) {

                             JSONObject BookObject = bookArray.getJSONObject(i);
                             String title, authors;
                             JSONObject BookDetails = BookObject.getJSONObject("volumeInfo");
                             if (BookDetails.has("authors")) {
                                 authors = (BookDetails.getString("authors"));
                                 authors = authors.replace("[", "");
                                 authors = authors.replace("]", "");
                             } else {
                                 authors = "";

                             }
                             title = BookDetails.getString("title");

                             Log.v(KEY_TITLE, title);
                             Log.v(KEY_AUTHORS, authors);


                             // Adding value HashMap key => value

                             HashMap<String, String> map = new HashMap<String, String>();
                             map.put(KEY_TITLE, title);
                             map.put(KEY_AUTHORS, authors);

                             booksdata.add(map);

                             ListAdapter adapter = new SimpleAdapter(MainActivity.this,
                                     booksdata,
                                     R.layout.listview_layout,
                                     new String[]{KEY_TITLE, KEY_AUTHORS},
                                     new int[]{R.id.title, R.id.authors}
                             );
                             bookList.setAdapter(adapter);
                         }

                     }
                     // process other data as this way..............
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             } // if statement
         } // onPostExecute()
     } // ProcessJSON class
 } // Activity
