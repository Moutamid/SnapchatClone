package com.gsrathoreniks.facefilter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.gsrathoreniks.facefilter.camera.CameraSourcePreview;
import com.gsrathoreniks.facefilter.camera.GraphicOverlay;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.gsrathoreniks.facefilter.camera.GraphicOverlay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import static android.view.View.GONE;

public class FaceFilterActivity extends AppCompatActivity {//implements FaceFilterActivity.MyRecyclerViewAdapter.ItemClickListener
    TextGraphic mTextGraphic;

    private final Thread observer = new Thread("observer") {

        {
            setDaemon(true);
        }

        public void run() {

            while (!isInterrupted()) {
                /*
                TextGraphic mTextGraphic = new TextGraphic(mGraphicOverlay);
                mGraphicOverlay.add(mTextGraphic);*/
                //mTextGraphic.updateText(2);
            }

        }

        ;
    };

    private static final String TAG = "FaceTracker";
    private ArrayList<String> filterLinksList = new ArrayList<>();

    private ArrayList<Integer> filterImagesList = new ArrayList<>();
    private int currentImageValue = R.drawable.transparent;
    private Bitmap graphicBitmap;

    private static final String ANIMALS_LINK = "https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/animal.json";
    private static final String GLASSES_LINK = "https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/glasses.json";
    private static final String HAT_LINK = "https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/hat.json";
    private static final String MASKS_LINK = "https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/masks.json";
    private static final String XYZ_LINK = "https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/xyz.json";

    private void initAnimalsList() {
        filterImagesList.clear();

        filterImagesList.add(R.drawable.cat2);
        filterImagesList.add(R.drawable.dog);
        filterImagesList.add(R.drawable.snap);

        initRecyclerview();
    }

    private void initGlassesList() {
        filterImagesList.clear();

        filterImagesList.add(R.drawable.glasses4);
        filterImagesList.add(R.drawable.glasses3);
        filterImagesList.add(R.drawable.glasses2);
        filterImagesList.add(R.drawable.glasses5);

        initRecyclerview();
    }

    private void initHatList() {
        filterImagesList.clear();

        filterImagesList.add(R.drawable.hat);
        filterImagesList.add(R.drawable.hat2);
        filterImagesList.add(R.drawable.eyeglasses);

        initRecyclerview();
    }

    private void initMasksList() {
        filterImagesList.clear();

        filterImagesList.add(R.drawable.mask);
        filterImagesList.add(R.drawable.mask2);
        filterImagesList.add(R.drawable.mask3);

        initRecyclerview();
    }

    private void initXyzList() {
        filterImagesList.clear();

        filterImagesList.add(R.drawable.eyeglasses);

//        filterImagesList.add(R.drawable.hat2);
//        filterImagesList.add(R.drawable.hair);
//        filterImagesList.add(R.drawable.snap);
//        filterImagesList.add(R.drawable.glasses2);

        initRecyclerview();
    }

    private Bitmap bitmapSRC;
    private Bitmap bitmapPREV;

    private CameraSource mCameraSource = null;
    private int typeFace = 0;
    private int typeFlash = 0;
    private String imageNamePath = "profile.jpg";
    private String path1;
    private boolean flashmode = false;
    private Camera camera;

    private static final int MASK1[] = {
            R.id.no_filter,
            R.id.hair,
            R.id.op,
            R.id.snap,
            R.id.glasses2,
            R.id.glasses3,
            R.id.glasses4,
            R.id.glasses5,
            R.id.mask,
            R.id.mask2,
            R.id.mask3,
            R.id.dog,
            R.id.cat2
    };

    private static final int MASK[] = {
            R.drawable.transparent,
            R.drawable.hat,
            R.drawable.hat2,
            R.drawable.mask,
            R.drawable.mask2,
            R.drawable.mask3,
            R.drawable.glasses2,
            R.drawable.glasses3,
            R.drawable.glasses4,
            R.drawable.glasses5,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.snap
    };

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private ImageButton emptyImageButton, hatImageButton,
            maskImageButton, glassesImageButton, animalImageButton, xyzImageButton;

    private LinearLayout emptyLinearLayout, hatLinearLayout,
            maskLinearLayout, glassesLinearLayout, animalLinearLayout;

    private LinearLayout currentLinearLayout;
    private ImageButton currentImageButton;

    private ImageView hatImageView, hat2ImageView, maskImageView, mask2ImageView,
            mask3ImageView,
            glasses2ImageView, glasses3ImageView, glasses4ImageView, glasses5ImageView,
            cat2ImageView, dogImageView, snapImageView;

    private ImageView currentImageView;
    private View rootView;

    private LinearLayout filterItemsLinearLayout;
    private ImageLoader imageLoader;

    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_face_filter);
        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        initViews();

        bitmapSRC = BitmapFactory.decodeResource(getResources(), currentImageValue);
//        bitmapSRC = BitmapFactory.decodeResource(getResources(), MASK[0]);
        bitmapPREV = bitmapSRC;

        saveToInternalStorage1(bitmapSRC);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(FaceFilterActivity.this).build();
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();

//        getLinksOfFilters(MASKS_LINK);

        //mTextGraphic = new TextGraphic(mGraphicOverlay);
        //mGraphicOverlay.add(mTextGraphic);

        ImageButton face = (ImageButton) findViewById(R.id.face);
        face.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (findViewById(R.id.scrollView).getVisibility() == GONE) {
                if (filterItemsLinearLayout.getVisibility() == GONE) {
                    filterItemsLinearLayout.setVisibility(View.VISIBLE);
//                    findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face_select);
                } else {
//                    findViewById(R.id.scrollView).setVisibility(GONE);
                    filterItemsLinearLayout.setVisibility(GONE);
                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face);
                }
            }
        });

        ImageButton no_filter = (ImageButton) findViewById(R.id.no_filter);
        no_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 0;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton hair = (ImageButton) findViewById(R.id.hair);
        hair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 1;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton op = (ImageButton) findViewById(R.id.op);
        op.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 2;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton snap = (ImageButton) findViewById(R.id.snap);
        snap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 3;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses2 = (ImageButton) findViewById(R.id.glasses2);
        glasses2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 4;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses3 = (ImageButton) findViewById(R.id.glasses3);
        glasses3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 5;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses4 = (ImageButton) findViewById(R.id.glasses4);
        glasses4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 6;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton glasses5 = (ImageButton) findViewById(R.id.glasses5);
        glasses5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 7;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask = (ImageButton) findViewById(R.id.mask);
        mask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 8;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask2 = (ImageButton) findViewById(R.id.mask2);
        mask2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton mask3 = (ImageButton) findViewById(R.id.mask3);
        mask3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 10;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton dog = (ImageButton) findViewById(R.id.dog);
        dog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 11;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton cat2 = (ImageButton) findViewById(R.id.cat2);
        cat2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 12;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
            }
        });

        ImageButton button = (ImageButton) findViewById(R.id.change);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mCameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_BACK) {

                    mCameraSource.release();
                    mCameraSource = null;
                    createCameraSource(CameraSource.CAMERA_FACING_FRONT);
                    startCameraSource();

                } else {
                    mCameraSource.release();
                    mCameraSource = null;
                    createCameraSource(CameraSource.CAMERA_FACING_BACK);
                    startCameraSource();
                }
                Toast.makeText(FaceFilterActivity.this, "Changed!", Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton flash = (ImageButton) findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int blue;
                if (flashmode == false) {
                    flashmode = true;
                    blue = 0;
                } else {
                    flashmode = false;
                    blue = 255;
                }
                flash.setColorFilter(Color.argb(255, 255, 255, blue));
            }
        });

        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                showImageDialog(getScreenShot());
//                showImageDialog(mPreview.getSurfaceBitmap());
                //                mPreview.stop();
//                showImageDialog(loadBitmapFromView(mPreview));
//                showImageDialog(getScreenShot(mPreview));
                takeImage();
//                onPause();
            }
        });


        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(CameraSource.CAMERA_FACING_FRONT);
        } else {
            requestCameraPermission();
        }
    }

    private void initRecyclerview() {
//        Boolean isConnected = new GetJson().isConnected(FaceFilterActivity.this);

        // set up the RecyclerView
        MyRecyclerViewAdapter adapter;
        RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
        int numberOfColumns = 3;
        int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
        adapter = new MyRecyclerViewAdapter(this);
//        adapter = new MyRecyclerViewAdapter(this, data);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    private Bitmap mergedBitmap(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, 0, 0, null);
//        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        bmp1.recycle();
        bmp2.recycle();
        return bmOverlay;
    }


    private void downloadImage(int position) {
//        ImageView imageView = findViewById(R.id.change);
        downloadImageTask downloadImageTask = new downloadImageTask(position);
        downloadImageTask.execute();
    }

    private void displayImage(ImageView imageView, String link) {
        imageLoader.displayImage(
                link,
                imageView);
    }

    private class downloadImageTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        int position;

        public downloadImageTask(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FaceFilterActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmapSRC = imageLoader.loadImageSync(filterLinksList.get(position));
            bitmapPREV = bitmapSRC;
            saveToInternalStorage1(bitmapSRC);
            //"https://firebasestorage.googleapis.com/v0/b/mysnapchatapp-cb445.appspot.com/o/filters%2FImage.png?alt=media&token=c94fbd47-e2a3-44a4-8f36-e0de9b15c083"
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

//    @Override
//    public void onItemClick(View view, int position) {
//
//    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        //        private String[] mData;
        private LayoutInflater mInflater;
//        private ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context) {//, String[] data
            this.mInflater = LayoutInflater.from(context);
//            this.mData = data;
        }

        // inflates the cell layout from xml when needed
        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
            return new ViewHolder(view);
        }
        //            holder.myTextView.setText(filterLinksList.get(position));
//            holder.myTextView.setText(mData[position]);

        ImageView previousImageView = null;

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int p) {
            final int position = holder.getAdapterPosition();

            holder.holder_imageView.setImageResource(filterImagesList.get(position));

            holder.holder_imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    currentImageValue = filterImagesList.get(position);

                    if (previousImageView == null) {
                        holder.holder_imageView.setBackgroundResource(R.drawable.bg_filter_items_selected);
                        previousImageView = holder.holder_imageView;

                    } else {

                        previousImageView.setBackgroundResource(0);
                        previousImageView = holder.holder_imageView;
                        previousImageView.setBackgroundResource(R.drawable.bg_filter_items_selected);
                    }

                    // HIDE ITEMS LAYOUT ON CLICK
                    filterItemsLinearLayout.setVisibility(GONE);
                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face);
                }
            });

//            displayImage(holder.holder_imageView, filterLinksList.get(position));
//            holder.holder_imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    downloadImage(position);
//
//                    if (previousImageView == null) {
//                        holder.holder_imageView.setBackgroundResource(R.drawable.bg_filter_items_selected);
//                        previousImageView = holder.holder_imageView;
//
//                    } else {
//
//                        previousImageView.setBackgroundResource(0);
//                        previousImageView = holder.holder_imageView;
//                        previousImageView.setBackgroundResource(R.drawable.bg_filter_items_selected);
//                    }
//
//                    // HIDE ITEMS LAYOUT ON CLICK
//                    filterItemsLinearLayout.setVisibility(GONE);
//                    ((ImageButton) findViewById(R.id.face)).setImageResource(R.drawable.face);
//                }
//            });

        }

        // total number of cells
        @Override
        public int getItemCount() {
            return filterImagesList.size();
//            return filterLinksList.size();
//            return mData.length;
        }

        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder {// implements View.OnClickListener
            ImageView holder_imageView;
//            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                holder_imageView = itemView.findViewById(R.id.imageview_recyclerview);
//                itemView.setOnClickListener(this);
            }

//            @Override
//            public void onClick(View view) {
//                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
//            }
        }

        // convenience method for getting data at click position
        String getItem(int id) {
            return filterLinksList.get(id);
//            return mData[id];
        }

        // allows clicks events to be caught
//        void setClickListener(ItemClickListener itemClickListener) {
//            this.mClickListener = itemClickListener;
//        }

        // parent activity will implement this method to respond to click events
//        public interface ItemClickListener {
//            void onItemClick(View view, int position);
//        }
    }

    private class GettingLinksTask extends AsyncTask<Void, Void, Void> {
        String jsonString, error;
        ProgressDialog progressDialog;
        String opted_link;

        public GettingLinksTask(String opted_link) {
            this.opted_link = opted_link;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FaceFilterActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            filterLinksList.clear();
//https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/filters.json
            try {
                jsonString = new GetJson().AsString(opted_link);
                Log.i(TAG, "doInBackground: jsonString:" + jsonString);

            } catch (ExecutionException executionException) {
                Log.i(TAG, "doInBackground: exception occurs");
                executionException.printStackTrace();
                error = executionException.getMessage();
            } catch (InterruptedException interruptedException) {
                Log.i(TAG, "doInBackground: exception occurs");
                interruptedException.printStackTrace();
                error = interruptedException.getMessage();
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (jsonString == null)
                return null;

            try {
                JSONObject object = new JSONObject(jsonString);
                Log.i(TAG, "doInBackground JSONObject object = new JSONObject(jsonString);\n" + object);
                Iterator<String> iter = object.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    Log.i(TAG, "doInBackground String key = iter.next();\n" + key);
                    try {
                        String value = object.get(key).toString();
                        filterLinksList.add(value);
                        Log.i(TAG, "doInBackground: value: " + value);
//                            Object value = jsonObj.get(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        error = "Json parsing error: " + e.getMessage();
                    }
                }

            } catch (JSONException exception) {
                exception.printStackTrace();
                error = "Json parsing error: " + exception.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if (error != null) {
                Toast.makeText(FaceFilterActivity.this, error, Toast.LENGTH_LONG).show();
                return;
            }

            initRecyclerview();
        }
    }

    private void getLinksOfFilters(String link) {
        GettingLinksTask gettingLinksTask = new GettingLinksTask(link);
        gettingLinksTask.execute();
    }

    private Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = FaceFilterActivity.this.getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }

    private Bitmap getScreenShot() {//View view
//        LinearLayout topLayout = findViewById(R.id.topLayout);
//        View screenView = topLayout.getRootView();
        RelativeLayout layout = findViewById(R.id.final_image_layout);
        layout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getDrawingCache());
        layout.setDrawingCacheEnabled(false);
        return bitmap;
    }

    private void initViews() {

        currentImageButton = findViewById(R.id.empty_image_button);
        currentLinearLayout = findViewById(R.id.empty_linear_layout);

        emptyImageButton = findViewById(R.id.empty_image_button);
        hatImageButton = findViewById(R.id.hat_image_button);
        maskImageButton = findViewById(R.id.mask_image_button);
        glassesImageButton = findViewById(R.id.glasses_image_button);
        animalImageButton = findViewById(R.id.animal_image_button);

        xyzImageButton = findViewById(R.id.xyz_image_button);

        emptyLinearLayout = findViewById(R.id.empty_linear_layout);
        hatLinearLayout = findViewById(R.id.hat_linear_layout);
        maskLinearLayout = findViewById(R.id.mask_linear_layout);
        glassesLinearLayout = findViewById(R.id.glasses_linear_layout);
        animalLinearLayout = findViewById(R.id.animal_linear_layout);

        emptyImageButton.setOnClickListener(emptyImageButtonClickListener());
        hatImageButton.setOnClickListener(hatImageButtonClickListener());
        maskImageButton.setOnClickListener(maskImageButtonClickListener());
        glassesImageButton.setOnClickListener(glassesImageButtonClickListener());
        animalImageButton.setOnClickListener(animalImageButtonClickListener());
        xyzImageButton.setOnClickListener(xyzImageButtonClickListener());

        hatImageView = findViewById(R.id.hat_image_view);
        hat2ImageView = findViewById(R.id.hat2_image_view);
        maskImageView = findViewById(R.id.mask_image_view);
        mask2ImageView = findViewById(R.id.mask2_image_view);
        mask3ImageView = findViewById(R.id.mask3_image_view);
        glasses2ImageView = findViewById(R.id.glasses2_image_view);
        glasses3ImageView = findViewById(R.id.glasses3_image_view);
        glasses4ImageView = findViewById(R.id.glasses4_image_view);
        glasses5ImageView = findViewById(R.id.glasses5_image_view);
        cat2ImageView = findViewById(R.id.cat2_image_view);
        dogImageView = findViewById(R.id.dog_image_view);
        snapImageView = findViewById(R.id.snap_image_view);

        currentImageView = findViewById(R.id.hat_image_view);

        hatImageView.setOnClickListener(imageViewClickListener(hatImageView, 1));
        hat2ImageView.setOnClickListener(imageViewClickListener(hat2ImageView, 2));
        maskImageView.setOnClickListener(imageViewClickListener(maskImageView, 3));
        mask2ImageView.setOnClickListener(imageViewClickListener(mask2ImageView, 4));
        mask3ImageView.setOnClickListener(imageViewClickListener(mask3ImageView, 5));
        glasses2ImageView.setOnClickListener(imageViewClickListener(glasses2ImageView, 6));
        glasses3ImageView.setOnClickListener(imageViewClickListener(glasses3ImageView, 7));
        glasses4ImageView.setOnClickListener(imageViewClickListener(glasses4ImageView, 8));
        glasses5ImageView.setOnClickListener(imageViewClickListener(glasses5ImageView, 9));
        cat2ImageView.setOnClickListener(imageViewClickListener(cat2ImageView, 10));
        dogImageView.setOnClickListener(imageViewClickListener(dogImageView, 11));
        snapImageView.setOnClickListener(imageViewClickListener(snapImageView, 12));

        filterItemsLinearLayout = findViewById(R.id.filter_items_linear_layout);
    }

    private View.OnClickListener xyzImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = xyzImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                if (emptyLinearLayout.getVisibility() == View.VISIBLE)
                    emptyLinearLayout.setVisibility(View.GONE);

                initXyzList();

//                getLinksOfFilters(XYZ_LINK);
            }
        };
    }

    private View.OnClickListener imageViewClickListener(final ImageView imageView, final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentImageView != imageView) {

                    currentImageView.setBackgroundResource(0);

                    currentImageView = imageView;

                    currentImageView.setBackgroundResource(R.drawable.bg_filter_items_selected);

//                    downloadImage();
//                    typeFace = index;
                }

            }
        };
    }

    private void toggleButtonAndLayout(ImageButton imageButton, LinearLayout linearLayout) {
        if (currentImageButton != imageButton) {

            currentImageButton.setBackgroundResource(R.drawable.round_background);
            currentLinearLayout.setVisibility(View.GONE);

            currentImageButton = imageButton;
            currentLinearLayout = linearLayout;

            currentImageButton.setBackgroundResource(R.drawable.round_background_select);
            currentLinearLayout.setVisibility(View.VISIBLE);

        }
    }

    private View.OnClickListener emptyImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleButtonAndLayout(emptyImageButton, emptyLinearLayout);

                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = emptyImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
                recyclerView.setVisibility(View.GONE);
                emptyLinearLayout.setVisibility(View.VISIBLE);

                currentImageValue = R.drawable.transparent;
//                currentImageView.setBackgroundResource(0);
//                typeFace = 0;

            }
        };
    }

    private View.OnClickListener hatImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleButtonAndLayout(hatImageButton, hatLinearLayout);

                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = hatImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                if (emptyLinearLayout.getVisibility() == View.VISIBLE)
                    emptyLinearLayout.setVisibility(View.GONE);

                initHatList();

//                getLinksOfFilters(HAT_LINK);

            }
        };
    }

    private View.OnClickListener maskImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleButtonAndLayout(maskImageButton, maskLinearLayout);

                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = maskImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);

                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                if (emptyLinearLayout.getVisibility() == View.VISIBLE)
                    emptyLinearLayout.setVisibility(View.GONE);

                initMasksList();

//                getLinksOfFilters(MASKS_LINK);

            }
        };
    }

    private View.OnClickListener glassesImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleButtonAndLayout(glassesImageButton, glassesLinearLayout);

                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = glassesImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                if (emptyLinearLayout.getVisibility() == View.VISIBLE)
                    emptyLinearLayout.setVisibility(View.GONE);

                initGlassesList();

//                getLinksOfFilters(GLASSES_LINK);

            }
        };
    }

    private View.OnClickListener animalImageButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                toggleButtonAndLayout(animalImageButton, animalLinearLayout);

                currentImageButton.setBackgroundResource(R.drawable.round_background);
                currentImageButton = hatImageButton;
                currentImageButton.setBackgroundResource(R.drawable.round_background_select);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_face_filter);
                if (recyclerView.getVisibility() == View.GONE)
                    recyclerView.setVisibility(View.VISIBLE);

                if (emptyLinearLayout.getVisibility() == View.VISIBLE)
                    emptyLinearLayout.setVisibility(View.GONE);

                initAnimalsList();

//                getLinksOfFilters(ANIMALS_LINK);

            }
        };
    }

    private void showImageDialog(Bitmap bitmapImage, Bitmap loadedBitmap) {
        final Dialog dialog = new Dialog(FaceFilterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_picture_taken);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView imageView = dialog.findViewById(R.id.image_taken_imageview);
        imageView.setImageBitmap(bitmapImage);

        final ImageView imageView2 = dialog.findViewById(R.id.mask_imageview);
//        imageView2.setImageBitmap(graphicBitmap);
        imageView2.setImageBitmap(loadedBitmap);

        dialog.findViewById(R.id.cancelBtn_imagetaken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.saveBtn_imagetaken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE

//                File imageFile;
                RelativeLayout layout = dialog.findViewById(R.id.final_image_layout);
//                Bitmap finalBitmap = getScreenShot();
                Bitmap finalBitmap = loadBitmapFromView(layout);

                String imageName = saveToInternalStorage(finalBitmap);

                Utils utils = new Utils();
                String IMAGES_LIST_NAME = "image_list_names";

                if (utils.getStoredArrayList(FaceFilterActivity.this,
                        IMAGES_LIST_NAME).get(0).equals("Error")) {
                    // ARRAY LIST IS EMPTY!

                    ArrayList<String> listArray = new ArrayList<>();
                    listArray.add(imageName);

                    utils.storeArrayList(FaceFilterActivity.this,
                            IMAGES_LIST_NAME, listArray);
                } else {
                    // ARRAY LIST IS NOT EMPTY

                    ArrayList<String> listArray1 = utils.getStoredArrayList(
                            FaceFilterActivity.this, IMAGES_LIST_NAME);
                    listArray1.add(imageName);

                    utils.storeArrayList(FaceFilterActivity.this,
                            IMAGES_LIST_NAME, listArray1);

                }

//                Toast.makeText(FaceFilterActivity.this,
//                        imageName, Toast.LENGTH_LONG).show();

//                try {
//
//                    String state = Environment.getExternalStorageState();
//                    File folder = null;
//                    if (state.contains(Environment.MEDIA_MOUNTED)) {
//                        folder = new File(Environment
//                                .getExternalStorageDirectory() + "/faceFilter");
//                    } else {
//                        folder = new File(Environment
//                                .getExternalStorageDirectory() + "/faceFilter");
//                    }
//
//                    boolean success = true;
//                    if (!folder.exists()) {
//                        success = folder.mkdirs();
//                    }
//                    if (success) {
//                        java.util.Date date = new java.util.Date();
//                        imageFile = new File(folder.getAbsolutePath()
//                                + File.separator
//                                + new Timestamp(date.getTime()).toString()
//                                + "Image.jpg");
//
//                        imageFile.createNewFile();
//                    } else {
//                        Toast.makeText(getBaseContext(), "Image Not saved",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//
//                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//
//                    // save image into gallery
//                    finalBitmap = resize(finalBitmap, 800, 600);
//                    finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
//
//                    FileOutputStream fout = null;
//
//                    fout = new FileOutputStream(imageFile);
//                    fout.write(ostream.toByteArray());
//                    fout.close();
//
//                    Toast.makeText(FaceFilterActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
//
//                    Toast.makeText(FaceFilterActivity.this, imageFile.toString(), Toast.LENGTH_LONG).show();
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.shareBtn_imagetaken).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // CODE HERE
                RelativeLayout layout = dialog.findViewById(R.id.final_image_layout);
                MarginLayoutParams marginLayoutParams =
                        (MarginLayoutParams) imageView2.getLayoutParams();

                if (mCameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_BACK) {
                    //MARGIN VALUE FOR BACK CAMERA
                    marginLayoutParams.setMargins(55, 55, 55, 55);
                } else {
                    //MARGIN VALUE FOR FRONT CAMERA
                    marginLayoutParams.setMargins(45, 45, 45, 45);
                }

                imageView2.setLayoutParams(marginLayoutParams);

                Bitmap finalBitmap = loadBitmapFromView(layout);
                shareBitmap(finalBitmap);
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(layoutParams);

//        mPreview.getPreviewBitmapImage()

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String name = "profile_" + String.valueOf(new Utils().getRandomNmbr(999))
                + ".jpg";
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new Utils().storeString(FaceFilterActivity.this,
                "images_path",
                directory.getAbsolutePath());

        Toast.makeText(this, "Saved to: " + directory.getAbsolutePath(),
                Toast.LENGTH_LONG).show();

        return name;
    }

    private void saveToInternalStorage1(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDirTemp", Context.MODE_PRIVATE);
        // Create imageDir
        String name = "profile.jpg";
        File mypath = new File(directory, name);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        new Utils().storeString(FaceFilterActivity.this,
//                "temp_image_path",
//                directory.getAbsolutePath());
        path1 = directory.getAbsolutePath();

//        Toast.makeText(this, "Saved to: " + directory.getAbsolutePath(),
//                Toast.LENGTH_LONG).show();

//        return name;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void shareBitmap(@NonNull Bitmap bitmap) {
        //---Save bitmap to external cache directory---//
        //get cache directory
        File cachePath = new File(getExternalCacheDir(), "my_images/");
        cachePath.mkdirs();

        //create png file
        File file = new File(cachePath,
                "Image_" + String.valueOf(new Utils().getRandomNmbr(999))
                        + ".png");
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
//            bitmap = resize(bitmap, 800, 600);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //---Share File---//
        //get file uri
        Uri myImageFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        //create a intent
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri);
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share with"));
    }


    private static Bitmap flippedBitmap(Bitmap source, boolean xFlip, boolean yFlip) {
        Matrix matrix = new Matrix();
        matrix.postScale(xFlip ? -1 : 1, yFlip ? -1 : 1, source.getWidth() / 2f, source.getHeight() / 2f);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void takeImage() {
        try {
            //openCamera(CameraInfo.CAMERA_FACING_BACK);
            //releaseCameraSource();
            //releaseCamera();
            //openCamera(CameraInfo.CAMERA_FACING_BACK);
            //setUpCamera(camera);
            //Thread.sleep(1000);

            mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
                private File imageFile;

                @Override
                public void onPictureTaken(byte[] bytes) {
                    try {
                        // convert byte array into bitmap
                        Bitmap loadedImage = null;
                        Bitmap rotatedBitmap = null;
                        loadedImage = BitmapFactory.decodeByteArray(bytes, 0,
                                bytes.length);
//                        rotateMatrix.postRotate(getWindowManager().getDefaultDisplay().getRotation());

                        // rotate Image
                        Matrix rotateMatrix = new Matrix();

                        int degreesTo = 270;
                        if (mCameraSource.getCameraFacing() == CameraSource.CAMERA_FACING_BACK) {
                            degreesTo = 90;
                        }

                        rotateMatrix.postRotate(degreesTo);//90 degrees for Back, 270 degrees for Front
                        rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                                loadedImage.getWidth(), loadedImage.getHeight(),
                                rotateMatrix, false);

//                      Bitmap maskBitmap;
                        Bitmap loadedBitmap = loadBitmapFromView(mGraphicOverlay);
                        // rotate Image
//                        Matrix rotateBitmapMatrix = new Matrix();
//                        rotateBitmapMatrix.postRotate(270);
//                        maskBitmap = Bitmap.createBitmap(loadedBitmap, 0, 0,
//                                loadedBitmap.getWidth(), loadedBitmap.getHeight(),
//                                rotateBitmapMatrix, false);

//                        maskBitmap = flippedBitmap(loadedBitmap, true, false);

//                        Bitmap filterBm = mergedBitmap(rotatedBitmap, maskBitmap);
//
                        showImageDialog(rotatedBitmap, loadedBitmap);

//                      showImageDialog(loadBitmapFromView(mGraphicOverlay));

                        //                        ContentValues values = new ContentValues();
//
//                        values.put(MediaStore.Images.Media.DATE_TAKEN,
//                                System.currentTimeMillis());
//                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//                        values.put(MediaStore.MediaColumns.DATA,
//                                imageFile.getAbsolutePath());
//
//                        setResult(Activity.RESULT_OK); //add this
//                        finish();
                    } catch (Exception e) {
                        Toast.makeText(FaceFilterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception ex) {
            Toast.makeText(FaceFilterActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }

    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }


    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource(int facingMode) {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.ACCURATE_MODE)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        //new MultiProcessor.Builder<>(new GraphicTextTrackerFactory()).build();

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setFacing(facingMode)
//                .setFacing(CameraSource.CAMERA_FACING_BACK)
//TODO: commented this        .setRequestedFps(30.0f)
                .build();
        //observer.start();
        /*
        TextGraphic mTextGraphic = new TextGraphic(mGraphicOverlay);
        mGraphicOverlay.add(mTextGraphic);
        mTextGraphic.updateText(2);*/
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();

        mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource(CameraSource.CAMERA_FACING_FRONT);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

//    private class GraphicTextTrackerFactory implements MultiProcessor.Factory<String> {
//        @Override
//        public Tracker<String> create(String face) {
//            return new GraphicTextTracker(mGraphicOverlay);
//        }
//    }
//
//    private class GraphicTextTracker extends Tracker<String> {
//        private GraphicOverlay mOverlay;
//        private TextGraphic mTextGraphic;
//
//        GraphicTextTracker(GraphicOverlay overlay) {
//            mOverlay = overlay;
//            mTextGraphic = new TextGraphic(overlay);
//        }
//
//        public void onUpdate() {
//            mOverlay.add(mTextGraphic);
//            mTextGraphic.updateText(3);
//        }
//
//        @Override
//        public void onDone() {
//            mOverlay.remove(mTextGraphic);
//        }
//    }

//==============================================================================================
// Graphic Face Tracker
//==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
//            if (bitmapSRC != bitmapPREV)
//                return;

            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay, currentImageValue);
//            mFaceGraphic = new FaceGraphic(overlay, path1, imageNamePath);
//            mFaceGraphic = new FaceGraphic(overlay, bitmapSRC);
//            mFaceGraphic = new FaceGraphic(overlay, typeFace);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */

        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            Log.d(TAG, "onUpdate123: started");

            //            if (bitmapSRC == bitmapPREV)
//                return;

            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face, currentImageValue);

            graphicBitmap = mFaceGraphic.getBitmap(face, BitmapFactory.decodeResource(getResources(), currentImageValue));

//            mFaceGraphic.updateFace(face, path1, imageNamePath);
//            mFaceGraphic.updateFace(face, bitmapSRC);
//            mFaceGraphic.updateFace(face, typeFace);
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }
}