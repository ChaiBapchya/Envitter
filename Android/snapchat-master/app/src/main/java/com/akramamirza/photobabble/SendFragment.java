package com.akramamirza.photobabble;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONArray;
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


public class SendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rootView;
    Spinner spinner;
    String cat_global;
    int cat_global_int;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public  Bitmap bitmap1;
    private OnFragmentInteractionListener mListener;
    public JSONArray jsonArray=null;
public String global_desc;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendFragment newInstance(String param1, String param2) {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SendFragment() {
        // Required empty public constructor
        bitmap1=CameraFragment.bitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        bitmap1=CameraFragment.bitmap;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
View v = inflater.inflate(R.layout.fragment_send, container, false);
        Spinner spinner=(Spinner)v.findViewById(R.id.spinner1);
        spinner.setPrompt("Select category");
        BitmapDrawable ob = new BitmapDrawable(getResources(), CameraFragment.bitmap);
         ImageView img=(ImageView)v.findViewById(R.id.imageView);
       /*     img.setImageBitmap(CameraFragment.bitmap);
        Log.d("abc", "image set");
       */
     //   Log.d("abc", "" + bitmap1);
        img.setImageBitmap(bitmap1);

        //editText.getText("");

      /*  GPSTracker gpsTracker=new GPSTracker(getActivity());

        TextView lati=(TextView)v.findViewById(R.id.lat);
        TextView longi=(TextView)v.findViewById(R.id.longi);

        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            lati.setText(stringLatitude);

            String stringLongitude = String.valueOf(gpsTracker.longitude);
            longi.setText(stringLongitude);
        }else{
            gpsTracker.showSettingsAlert();

        }


        lati.setText(gpsTracker.latitude+"latitude");
        longi.setText(gpsTracker.longitude+"longitude");*/
        //Back pressed Logic for fragment
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                        return true;
                    }
                }
                return false;
            }
        });

        EditText etext=(EditText)v.findViewById(R.id.Description);
        String des=etext.toString();
        return inflater.inflate(R.layout.fragment_send, container, false);


    }



   /* public void onmain(View view)
    {
        Intent myIntent = new Intent(getActivity(), PostActivity.class);
        getActivity().startActivity(myIntent);

    }
*/
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
    private void parseResult(String result) {
        try{

            Log.d("json","String="+result);


        }catch (Exception e){
            Log.d("Exception", "Error in Json parsing");
        }
    }

    public class insertDescription extends AsyncTask<String, Void, Integer> {

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



                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap1.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                String encodedString = Base64.encodeToString(byte_arr, 0);

                String json = "{'lat':'19.186079','longi':'72.847142','desc':'"+global_desc+"','catid':'"+cat_global_int+"',img:"+URLEncoder.encode(encodedString)+"}";
                JSONObject jsonObject= new JSONObject(json);
                urlConnection.setRequestMethod("POST");

                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(jsonObject);
                out.close();
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

        }
    }
@Override
    public void onViewCreated(View view,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = view;
    spinner=(Spinner)rootView.findViewById(R.id.spinner1);
        spinner.setPrompt("Select category");
        BitmapDrawable ob = new BitmapDrawable(getResources(), CameraFragment.bitmap);
        ImageView img=(ImageView)rootView.findViewById(R.id.imageView);


       /*     img.setImageBitmap(CameraFragment.bitmap);
        Log.d("abc", "image set");
       */

        Log.d("abc", "" + global_desc);
        img.setImageBitmap(bitmap1);


    final Button sendButton = (Button) view.findViewById(R.id.btnSubmit);
    ViewGroup.LayoutParams switchCameraButtonLayoutParams = sendButton.getLayoutParams();
   // switchCameraButtonLayoutParams.height = sendButton;
   // switchCameraButtonLayoutParams.width = switchCameraButtonWidthHeight;
    sendButton.setLayoutParams(switchCameraButtonLayoutParams);
    sendButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            EditText editText=(EditText)rootView.findViewById(R.id.Description);
            global_desc=editText.getText().toString();

           cat_global= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
            switch(cat_global)
            {
                case "Garbage":
                    cat_global_int=1;
                    break;
                case "Pollution":
                    cat_global_int=2;
                    break;
                case "Amimal Rescue":
                    cat_global_int=3;
                    break;
                case "Deforestation":
                    cat_global_int=4;
                    break;
                case "Quality of Air":
                    cat_global_int=5;
                    break;
                case "Appreciation":
                    cat_global_int=6;
            }
            String url="http://192.168.100.88/envitter/index.php/androidController/insertDescriptionController";
            new insertDescription().execute(url);


            Log.d("aya","lele");
            Intent myIntent = new Intent(getActivity(), PostActivity.class);
            getActivity().startActivity(myIntent);

        }
    });


 /*   GPSTracker gpsTracker=new GPSTracker(getActivity());

    TextView lati=(TextView)rootView.findViewById(R.id.lat);
    TextView longi=(TextView)rootView.findViewById(R.id.longi);

    if (gpsTracker.getIsGPSTrackingEnabled()) {
        Log.d("tag",""+gpsTracker.latitude);
        String stringLatitude = String.valueOf(gpsTracker.latitude);
        Log.d("tag",""+gpsTracker.longitude);
        lati.setText(stringLatitude);

        String stringLongitude = String.valueOf(gpsTracker.longitude);
        longi.setText(stringLongitude);
    }else{
        gpsTracker.showSettingsAlert();

    }


    lati.setText(gpsTracker.latitude + "latitude");
    longi.setText(gpsTracker.longitude + "longitude");*/

    // return inflater.inflate(R.layout.fragment_send, container, false);
    }



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
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
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


}
