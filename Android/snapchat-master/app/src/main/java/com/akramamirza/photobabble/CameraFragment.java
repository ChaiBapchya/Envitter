package com.akramamirza.photobabble;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CameraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends android.support.v4.app.Fragment {

    public static Bitmap bitmap;
    private FrameLayout cameraFrame;
    private CameraTextureView cameraTextureView;
    private int currentCameraId;
    private int numOfCameras;
    private int frontCameraId = Integer.MAX_VALUE;
    private int backCameraId = Integer.MAX_VALUE;
    private boolean isFacingBack = true;
    public final int MEDIA_TYPE_IMAGE = 1;
    public final int MEDIA_TYPE_VIDEO = 2;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String UPLOAD_URL ="http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        Log.d("abc","in camera");
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        numOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < numOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = i;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = i;
            }
        }

    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        cameraFrame = (FrameLayout) rootView.findViewById(R.id.cameraFrame);
        cameraTextureView = new CameraTextureView(MainApplication.getAppContext(), getCameraInstance(backCameraId));
        cameraFrame.addView(cameraTextureView);

//        final Button switchCameraButton = (Button) rootView.findViewById(R.id.switchCameraButton);
        final Button captureButton = (Button) rootView.findViewById(R.id.captureButton);
        final Button crossButton = (Button) rootView.findViewById(R.id.crossButton);
        final Button sendButton = (Button) rootView.findViewById(R.id.sendButton);

        // set the width and height of the button to the height of the screen divided by 14
       /* int switchCameraButtonWidthHeight = getResources().getDisplayMetrics().heightPixels / 14;
        ViewGroup.LayoutParams switchCameraButtonLayoutParams = switchCameraButton.getLayoutParams();
        switchCameraButtonLayoutParams.height = switchCameraButtonWidthHeight;
        switchCameraButtonLayoutParams.width = switchCameraButtonWidthHeight;
        switchCameraButton.setLayoutParams(switchCameraButtonLayoutParams);
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent("com.akramamirza.photobabble.FeedActivity");
                startActivity(intent);

            }
        });*/

        int captureButtonWidthHeight = getResources().getDisplayMetrics().heightPixels / 7;
        ViewGroup.LayoutParams captureButtonLayoutParams = captureButton.getLayoutParams();
        captureButtonLayoutParams.height = captureButtonWidthHeight;
        captureButtonLayoutParams.width = captureButtonWidthHeight;
        captureButton.setLayoutParams(captureButtonLayoutParams);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("abd","hello");
                Runnable takePictureRunnable = new Runnable() {
                    @Override
                    public void run() {
                        bitmap = cameraTextureView.getBitmap();
                        getStringImage(bitmap);
                        Log.d("abd",bitmap+"");
                        System.out.print("ayaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+bitmap);
                        cameraTextureView.getCamera().stopPreview();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                        byte[] byteArray = stream.toByteArray();
                        try {
                            saveToInternalStorage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //If the user wants to save the photo on their phone
                        /*File pictureFile = getOutputMediaFile();
                        pictureFile.createNewFile();
                        FileOutputStream fos = new FileOutputStream(pictureFile);
                        fos.write(byteArray);
                        fos.close();*/

                        // do this when the user presses the send button, otherwise keep bitmap offline until he sends
                        /*ParseFile imageFile = new ParseFile("snap.jpg", byteArray);
                        imageFile.saveInBackground();
                        ParseObject snap = new ParseObject("Snap");
                        snap.put("imageFile", imageFile);
                        snap.saveInBackground();*/
                    }
                };
                Thread takePictureThread = new Thread(takePictureRunnable);
                takePictureThread.start();

                captureButton.getStateListAnimator().jumpToCurrentState();
                doAnimation(R.animator.scale_in, crossButton, sendButton);
                getCircleAnimator(captureButton).start();
              //  getCircleAnimator(switchCameraButton).start();



                /*cameraTextureView.getCamera().takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        //cameraTextureView.getCamera().stopPreview();
                    }
                }, null, new Camera.PictureCallback() { //instead of take picture, take a screenshot of the cameraframe
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            File pictureFile = getOutputMediaFile();
                            pictureFile.createNewFile();
                            FileOutputStream fos = new FileOutputStream(pictureFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Log.d("ERROR ", "File not found: " + e.getMessage());
                        } catch (IOException e) {
                            Log.d("ERROR ", "Error accessing file: " + e.getMessage());
                        }
                        camera.stopPreview();
                        captureButton.setVisibility(View.GONE);
                        switchCameraButton.setVisibility(View.GONE);
                        crossButton.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

        int crossButtonWidthHeight = getResources().getDisplayMetrics().heightPixels / 20;
        ViewGroup.LayoutParams crossButtonLayoutParams = crossButton.getLayoutParams();
        crossButtonLayoutParams.height = crossButtonWidthHeight;
        crossButtonLayoutParams.width = crossButtonWidthHeight;
        crossButton.setLayoutParams(crossButtonLayoutParams);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cameraTextureView.getCamera().startPreview();
                    }
                });
                thread.start();

                crossButton.getStateListAnimator().jumpToCurrentState();
                getCircleAnimator(crossButton).start();
                getCircleAnimator(sendButton).start();
               // doAnimation(R.animator.scale_in, switchCameraButton, captureButton);



            }
        });

        int sendButtonWidthHeight = getResources().getDisplayMetrics().heightPixels / 12;
        ViewGroup.LayoutParams sendButtonLayoutParams = sendButton.getLayoutParams();
        sendButtonLayoutParams.height = sendButtonWidthHeight;
        sendButtonLayoutParams.width = sendButtonWidthHeight;
        sendButton.setLayoutParams(sendButtonLayoutParams);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getParentFragment().getActivity().getSupportFragmentManager();

                fm.beginTransaction()
                        .replace(R.id.mainFrame, new SendFragment())
                        .commit();
            }
        });



        return rootView;
    }

    private void uploadImage(){
        //Showing the progress dialog
      //  final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                  //      loading.dismiss();
                        //Showing toast message of the response
             //           Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                  //      loading.dismiss();

                        //Showing toast
                 //       Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
               // String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, "abc");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    private void doAnimation(int animationId, View... myObjects) {

        ArrayList<AnimatorSet> animatorSets = new ArrayList<AnimatorSet>();

        for (View myObject : myObjects) {
            AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(MainApplication.getAppContext(), animationId);
            set.setTarget(myObject);
            animatorSets.add(set);
        }

        for (AnimatorSet animatorSet : animatorSets) {
            animatorSet.start();
        }
    }

    private Animator getCircleAnimator(final View myView) {
        // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = myView.getWidth();

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        anim.setInterpolator(new DecelerateInterpolator(3));

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                myView.setVisibility(View.GONE);
            }
        });
        return anim;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraTextureView.getCamera() == null) {
            cameraTextureView.setCamera(getCameraInstance(backCameraId));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraTextureView.getCamera().release();
        cameraTextureView.setCamera(null);
    }

    public Camera getCameraInstance(int cameraId){
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    void switchCamera() {
        if (numOfCameras < 1) {
            return;
        }
        cameraTextureView.getCamera().stopPreview();
        cameraTextureView.getCamera().release();

        if (isFacingBack) {
            cameraTextureView.setCamera(getCameraInstance(frontCameraId));
        } else {
            cameraTextureView.setCamera(getCameraInstance(backCameraId));
        }

        isFacingBack = !isFacingBack;

    }

    private File getOutputMediaFile() throws IOException {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
                File.separator + "PhotoBabble" + File.separator);
        mediaDirectory.mkdirs();

        File mediaFile = new File(mediaDirectory, "IMG_"+ timeStamp + ".jpg");
        Log.d("A", "DIRECTORY: " + mediaFile.getPath());



/*      implement this when adding video
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
*/

        return mediaFile;
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
            // TODO: mListener = (OnFragmentInteractionListener) activity;
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
