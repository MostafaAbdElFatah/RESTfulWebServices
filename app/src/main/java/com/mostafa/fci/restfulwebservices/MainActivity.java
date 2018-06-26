package com.mostafa.fci.restfulwebservices;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    ArrayList<Flower> flowersList = new ArrayList<>();
    FlowerAdapter flowerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.flowersList);
        progressBar = findViewById(R.id.loading);

        flowerAdapter = new FlowerAdapter(getApplicationContext()
                , R.layout.child_row ,flowersList);
        listView.setAdapter(flowerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_XMLTask) {
            if (isOnLine()) {
                /**
                 * not secure path no have Authentication
                 * */

                new MyTask().execute(URLs.mainURL + URLs.feedURL + URLs.xmlFlowersURL, "XML");

                /**
                 *  secure path  have Authentication
                 * */
                //new MyTask().execute(URLs.mainURL + URLs.secureURL + URLs.xmlFlowersURL, "XML"
                 //       ,"feeduser","feedpassword");

            }else
                Toast.makeText(MainActivity.this,"Network isn't Available"
                        ,Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_JSONTask) {
            if (isOnLine()) {
                /**
                 * not secure path no have Authentication
                 * */

                new MyTask().execute(URLs.mainURL + URLs.feedURL + URLs.jsonFlowersURL, "JSON");

                /**
                 *  secure path  have Authentication
                 * */
                //new MyTask().execute(URLs.mainURL + URLs.secureURL + URLs.jsonFlowersURL, "JSON"
                  //      ,"feeduser","feedpassword");

            }else
                Toast.makeText(MainActivity.this,"Network isn't Available"
                        ,Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_GETParametrsTask) {
            if (isOnLine()) {
                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setUri(URLs.mainURL + URLs.restfulURL);
                requestPackage.setMethod("GET");
                requestPackage.setParam("param1","value1");
                requestPackage.setParam("param2","value2");
                requestPackage.setParam("param3","value3");
                requestPackage.setParam("param4","value4");
                new GETPOSTParamsTask().execute(requestPackage);

            }else
                Toast.makeText(MainActivity.this,"Network isn't Available"
                        ,Toast.LENGTH_SHORT).show();
            return true;
        }else if (id == R.id.action_POSTParametrsTask) {
            if (isOnLine()) {
                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setUri(URLs.mainURL + URLs.restfulURL);
                requestPackage.setMethod("POST");
                requestPackage.setParam("param1","value1");
                requestPackage.setParam("param2","value2");
                requestPackage.setParam("param3","value3");
                requestPackage.setParam("param4","value4");
                new GETPOSTParamsTask().execute(requestPackage);

            }else
                Toast.makeText(MainActivity.this,"Network isn't Available"
                        ,Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateDisplay(){
        //flowerAdapter.notifyDataSetChanged();
        flowerAdapter = new FlowerAdapter(getApplicationContext()
                , R.layout.child_row ,flowersList);
        listView.setAdapter(flowerAdapter);
    }

    protected boolean isOnLine(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting() )
            return true;
        else
            return false;

    }

    private  class MyTask extends AsyncTask<String , String , List<Flower>  > {

        String type = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Flower>  doInBackground(String... params) {
            String content = null;
            if (params.length > 2) {
                //content = URLManager.getDatabyHttpURLConnection(params[0], params[2], params[3]);
                content = URLManager.getDatabyOKHttpClient(params[0],params[2],params[3]);
            }else{
                //content = URLManager.getDatabyHttpURLConnection(params[0]);
                content = URLManager.getDatabyOKHttpClient(params[0]);
            }

            List<Flower> flowersList = null;
            if (params[1].equals("XML")) {
                flowersList = XMLParser.parse(content);
                type = params[1];
            }else {
                flowersList = JSONParser.parse(content);
                JSONParser.moshiParsing(content);
                type = params[1];
            }

            /** loading image with the date
            for (Flower flower :flowersList){
                String imageURL = URLs.mainURL + URLs.photosFlowersURL + flower.getPhoto();
                try {
                    InputStream inputStream = (InputStream) new URL(imageURL).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    flower.setBitmap(bitmap);
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }*/

            return flowersList;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Flower> list ) {
            super.onPostExecute(flowersList);
            if (list != null) {
                flowersList = (ArrayList<Flower>) list;
                updateDisplay();
            }else
                Toast.makeText(MainActivity.this,"There is Some Problem"
                        ,Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    private class GETPOSTParamsTask extends AsyncTask<RequestPackage,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(RequestPackage... params) {
            String content = URLManager.getDatabyHttpURLConnection(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

}
