package com.akramamirza.photobabble;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PostActivity extends Activity {

    private List<MyStory> myStoryList=new ArrayList<MyStory>();

    public String[] desc_array;
    public String[] img_array;
    public ImageLoaderConfiguration config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        config = new ImageLoaderConfiguration.Builder(this).build();
        String url="http://192.168.100.88/envitter/index.php/androidController/getlistController";
        new getinfo().execute(url);


        //   new AsyncHttpTask().execute("http://www.hungrybaba.esy.es/hotelfetch.php");
    }

    public void gotocamera(View view) {
      /*  Intent intent = new Intent("com.akramamirza.photobabble.MainActivity");
        startActivity(intent);
*/
        startActivity(new Intent(this, MainActivity.class));
    }


    private void populateListView() {
        //String[] string = new String[]{"hel", "a", "e"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, string);
         ArrayAdapter<MyStory> adapter=new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    private void populateStories() {

        int size=desc_array.length;
        for(int i=0;i<size;i++)
        {
            myStoryList.add(new MyStory(desc_array[i],img_array[i]));
        }

       /* myStoryList.add(new MyStory("hello"));
        myStoryList.add(new MyStory("hell"));
        myStoryList.add(new MyStory("helo"));
        myStoryList.add(new MyStory("hlo"));*/
    }

    private class MyListAdapter extends ArrayAdapter<MyStory> {

        public MyListAdapter() {
            super(PostActivity.this, R.layout.fragment_stories1,myStoryList);

        }

        @Override
        public View getView(int position,View contentView,ViewGroup parent)
        {
            View itemView=contentView;
            if(itemView==null)
            {
                itemView=getLayoutInflater().inflate(R.layout.fragment_stories1, parent, false);
            }
            MyStory myStory=myStoryList.get(position);
           String description=myStory.getDescription(); //foodname[position];
            TextView textView=(TextView) itemView.findViewById(R.id.text_des);
            textView.setText(description);

            ImageLoader.getInstance().init(config);
            ImageLoader imageLoader=ImageLoader.getInstance();
          //  String plink="http://192.168.100.88/imgupload/uploadedimages58.jpeg";
            String plink= myStory.getUrl();
            ImageView propic=(ImageView)itemView.findViewById(R.id.img_pic);
            imageLoader.displayImage(plink, propic);
            /*ImageLoader.getInstance().init(config);
            ImageLoader imageLoader=ImageLoader.getInstance();
            String plink="http://192.168.100.88/imgupload/uploadedimages43.jpeg";
            ImageView propic=(ImageView)findViewById(R.id.pic);
            imageLoader.displayImage(plink, propic);*/
            return itemView;
            //return  super.getView(position,contentView,parent);
        }
    }

    public class getinfo extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            Integer result = 0;
            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                 /* optional request header */
                urlConnection.setRequestProperty("Content-Type", "application/json");

                /* optional request header */
                //   urlConnection.setRequestProperty("Accept", "application/json");


                //String json = "{'lat':'23','longi':'34','desc':'abcd','catid':'2'}";

                //String postParameters="lat=";
                /* for Get request */



              //  ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
              //  bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, stream);
             //   byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
              //  String encodedString = Base64.encodeToString(byte_arr, 0);

           //     String json = "{'lat':'19.186079','longi':'72.847142','desc':'"+global_desc+"','catid':'"+cat_global_int+"',img:"+ URLEncoder.encode(encodedString)+"}";
              //  JSONObject jsonObject= new JSONObject(json);
                urlConnection.setRequestMethod("GET");

              /*  PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(jsonObject);
                out.close();*/
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    parseResult(response);
                    result = 1; // Successful




                }else{
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("Exception", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            populateStories();
            populateListView();
        }
    }
    private void parseResult(String result) throws JSONException {


            Log.d("json","String="+result);
            JSONObject response = new JSONObject(result);
            Log.d("response","response="+response);
            JSONArray posts = response.optJSONArray("recordslist");
            Log.d("posts","posts="+posts);
            desc_array =new String[posts.length()];
            img_array=new String[posts.length()];

            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.getJSONObject(i);
                Log.d("Jsonobject","Jsonobject="+post);

                //String title = post.optString("name").toString();

                desc_array[i] = parseProperly("" + post, "description");
                //String istring=parseProperly(""+post,"thumb");

                String img1_array=parseProperly("" + post, "image");

                img_array[i]="http://"+removeSlash(img1_array);

                Log.d("Names","Name="+img_array[i]);

            }
    }


    private String parseProperly(String par,String key)
    {
        String result="";
        int i;
        int start=par.indexOf(key+"\":");
        i=start+3+key.length();
        while(par.charAt(i)!='\"')
        {
            result=result+par.charAt(i);
            i++;
        }
        //result=par.matches("name");
        return result;
    }

    private String removeSlash(String input)
    {
        String output="";
        for(int i=0;i<input.length();i++)
        {
            if(input.charAt(i)!='\\') {
                output = output + input.charAt(i);
            }
        }
        return output;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }

            /* Close Stream */
        if(null!=inputStream){
            inputStream.close();
        }
        return result;
    }
}