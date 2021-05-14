package com.gsrathoreniks.facefilter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SavedPhotosActivity extends AppCompatActivity {
    private static final String TAG = "SavedPhotosActivity";

    private ArrayList<String> imageNamesList = new ArrayList<>();

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_photos);
        Log.d(TAG, "onCreate: started");
        GetImagesNameTask getImagesNameTask = new GetImagesNameTask();
        getImagesNameTask.execute();
    }

    private class GetImagesNameTask extends AsyncTask<Void, Void, Void>{

         ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: started");
            progressDialog = new ProgressDialog(SavedPhotosActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            Log.d(TAG, "onPreExecute: ended");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: started");
            String IMAGES_LIST_NAME = "image_list_names";

            imageNamesList = new Utils().getStoredArrayList(
                    SavedPhotosActivity.this, IMAGES_LIST_NAME);

            for (int i =0;i<=imageNamesList.size()-1;i++){
                Log.d(TAG, "doInBackground listName: "+imageNamesList.get(i));
            }
            Log.d(TAG, "doInBackground: ended");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: started");
            progressDialog.dismiss();

            initRecyclerView();
            Log.d(TAG, "onPostExecute: ended");
        }
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: ");
        conversationRecyclerView = findViewById(R.id.saved_photos_recycler_view);
        adapter = new RecyclerViewAdapterMessages();
        //        LinearLayoutManager layoutManagerUserFriends = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        int numberOfColumns = 3;
        int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 180);
        conversationRecyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));

//        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {

            //        noChatsLayout.setVisibility(View.GONE);
            //        chatsRecyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.noData_textview).setVisibility(View.VISIBLE);

        }

    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_saved_photos, parent, false);
            return new ViewHolderRightMessage(view);
        }

        String path = new Utils().getStoredString(SavedPhotosActivity.this,
                "images_path");

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position) {
            Log.d(TAG, "onBindViewHolder: "+position);
            Log.d(TAG, "onBindViewHolder path: "+path);
            Log.d(TAG, "onBindViewHolder name: "+imageNamesList.get(position));
            try {
                File f = new File(path, imageNamesList.get(position));
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.imageView.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getItemCount() {
            if (imageNamesList == null)
                return 0;
            return imageNamesList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            ImageView imageView;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                imageView = v.findViewById(R.id.imageview_saved_photos);

            }
        }

    }
}