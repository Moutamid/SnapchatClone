package com.gsrathoreniks.facefilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private View view;

    private RecyclerView namesRecyclerView;

    private ArrayList<String> nameList = new ArrayList<>();
    private Context context = getActivity();

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Extra Coding Here

//"https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/330px-Image_created_with_a_mobile_phone.png",
        // show The Image in a ImageView
//        new DownloadImageTask(imageView)
//                .execute("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/330px-Image_created_with_a_mobile_phone.png");

        nameList.add("Admin Default Filters");

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {

        RecyclerViewAdapterChaptersVideoLectures adapterGradeSelection = new RecyclerViewAdapterChaptersVideoLectures();

        namesRecyclerView = view.findViewById(R.id.recyclerview_home);
        namesRecyclerView.setAdapter(adapterGradeSelection);
        namesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        namesRecyclerView.setHasFixedSize(true);
        namesRecyclerView.setNestedScrollingEnabled(false);

        Log.d("HomeActivity", "initRecyclerView: completed");
    }

    private class RecyclerViewAdapterChaptersVideoLectures
            extends RecyclerView.Adapter<RecyclerViewAdapterChaptersVideoLectures
            .ViewHolderChaptersVideoLectures> {

        @NonNull
        @Override
        public ViewHolderChaptersVideoLectures onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_items, parent, false);
            return new ViewHolderChaptersVideoLectures(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderChaptersVideoLectures holder, final int position) {


//            holder.chapterNumber.setText(mChapterNmbrVideoLectures.get(position));
//            holder.chapterName.setText(mChapterNameVideoLectures.get(position));

            holder.viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                }
            });

            Log.d("HomeActivity", "onBindViewHolder: completed at position " + String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return nameList.size();
        }

        public class ViewHolderChaptersVideoLectures extends RecyclerView.ViewHolder {

            ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
            TextView nameTextView;
            Button viewBtn;

            public ViewHolderChaptersVideoLectures(@NonNull View itemView) {
                super(itemView);

                imageView1 = itemView.findViewById(R.id.image1);
                imageView2 = itemView.findViewById(R.id.image2);
                imageView3 = itemView.findViewById(R.id.image3);
                imageView4 = itemView.findViewById(R.id.image4);
                imageView5 = itemView.findViewById(R.id.image5);
                nameTextView = itemView.findViewById(R.id.name_textview_filterItem);
                viewBtn = itemView.findViewById(R.id.viewBtn);
            }
        }

    }

}
