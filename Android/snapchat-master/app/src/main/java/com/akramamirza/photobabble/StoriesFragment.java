package com.akramamirza.photobabble;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoriesFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<MyStory> myStoryList=new ArrayList<MyStory>();
    ListView list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

     String[] description;
    private OnFragmentInteractionListener mListener;

   // public ArrayList mypost= new
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoriesFragment newInstance(String param1, String param2) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public StoriesFragment() {
        // Required empty public constructor
    }
    private void populateListView() {
        //String[] string=new String[]{"hel","a","e"};
       // ArrayAdapter<String > adapter= new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,string);
        //MyListAdapter();
   //      list=(ListView)rootView.findViewById(R.id.listView);
//        list.setAdapter(adapter);
    }


    public void taketoactivity(View view)
    {
        Intent intent = new Intent("com.akramamirza.photobabble.FeedActivity");
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
     //   populateStories();
    //    populateListView();

        //   new AsyncHttpTask().execute("http://192.168.100.88/envitter/index.php/getcatname");

    }

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_stories, container, false);

        return rootView;
    }

 /*   @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
          //  populateStories();

            Log.d("hell", "" + myStoryList.get(1).description);
            //populateListView();

        }
        else {
        }
    }
*/
/*
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        populateStories();
        populateListView();

    }
*/
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            // TODO: mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void parseResult(String result) throws JSONException {
        try{

            Log.d("Bigjson","String="+result);
            JSONObject response = new JSONObject(result);
            Log.d("response","response="+response);
            JSONArray posts = response.optJSONArray("hotels");
            Log.d("posts","posts="+posts);
            description =new String[posts.length()];
         //   ImageList=new String[posts.length()];
        //    hotelid=new int[posts.length()];
        //    places=new String[posts.length()];
            for(int i=0; i< posts.length();i++ ){
                JSONObject post = posts.getJSONObject(i);
                Log.d("Jsonobject","Jsonobject="+post);

                //String title = post.optString("name").toString();

              //  description[i] = parseProperly(""+post,"name");
                //String istring=parseProperly(""+post,"thumb");
                //ImageList[i]=removeSlash(istring);
              //  ImageList[i]=parseProperly("" + post, "icon");
             //   hotelid[i]=Integer.parseInt(parseProperly("" + post, "h_id"));
            //    places[i]=parseProperly(""+post,"address");
                //Log.d("Names","Name="+ImageList[i]);
            }
        }catch (JSONException e){
            Log.d("Exception", "Error in Json parsing");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

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
                urlConnection.setRequestProperty("Accept", "application/json");

                /* for Get request */
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    Log.d("sherkick","hh"+response);
                   // parseResult(response);
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

          //  populateStories();
//            //onListViewClick();
            /* Download complete. Lets update UI */
            /*if(result == 1){
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, blogTitles);
                ListView listView=(ListView)findViewById(R.id.list);
                listView.setAdapter(arrayAdapter);
            }else{
                Log.e("Failed", "Failed to fetch data!");
            }*/

           /* spinner.setVisibility(View.GONE);
            int n=places.length;
            for(int i=0;i<n;i++)
            {
                MenuItem item = placemenu.add(Menu.NONE,1,Menu.NONE,places[i]);
            }
           */ //MenuItem item = placemenu.add(Menu.NONE,1,Menu.NONE,R.string.exitOption);
            /*profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);
            profilePictureView.setProfileId(userId);*/
        }
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
