package com.gsrathoreniks.facefilter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dezlum.codelabs.getjson.GetJson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private ViewPagerFragmentAdapter adapter;
    private ViewPager viewPager;

    private Context context = HomeActivity.this;

//    protected class yourDataTask extends AsyncTask<Void, Void, JSONObject> {
//        @Override
//        protected JSONObject doInBackground(Void... params) {
//
//            String str = "https://jsonplaceholder.typicode.com/todos/1";
//            URLConnection urlConn = null;
//            BufferedReader bufferedReader = null;
//            try {
//                URL url = new URL(str);
//                urlConn = url.openConnection();
//                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//
//                final StringBuffer stringBuffer = new StringBuffer();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line);
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, stringBuffer.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                return new JSONObject(stringBuffer.toString());
//            } catch (Exception ex) {
//                Log.e("App", "yourDataTask", ex);
//                return null;
//            } finally {
//                if (bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject response) {
//            if (response != null)
//                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "completed!", Toast.LENGTH_SHORT).show();
////            if(response != null)
////            {
////                try {
////                    Log.e("App", "Success: " + response.getString("yourJsonElement") );
////                } catch (JSONException ex) {
////                    Log.e("App", "Failure", ex);
////                }
////            }
//        }
//    }
//
//    //    private class GetChapterNames extends AsyncTask<Void, Void, Void> {
////
////        @Override
////        protected Void doInBackground(Void... arg0) {
////            HttpHandler sh = new HttpHandler();
////
////            // Making a request to url and getting response
////            String jsonStr = sh.makeServiceCall("https://jsonplaceholder.typicode.com/todos/1");
//////            String jsonStr = sh.makeServiceCall("https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/filters.json");
////
////            Log.e("", "Response from url: " + jsonStr);
////
////            if (jsonStr != null) {
////                try {
////                    JSONObject jsonObj = new JSONObject(jsonStr);
////
////                    Iterator<String> iter = jsonObj.keys();
////                    while (iter.hasNext()) {
////                        String key = iter.next();
////                        try {
////                            final String value = jsonObj.get(key).toString();
////                            runOnUiThread(new Runnable() {
////                                @Override
////                                public void run() {
////                                    Toast.makeText(HomeActivity.this,
////                                            value, Toast.LENGTH_SHORT).show();
////                                }
////                            });
//////                            Object value = jsonObj.get(key);
////                        } catch (final JSONException e) {
////                            // Something went wrong!
////                            runOnUiThread(new Runnable() {
////                                @Override
////                                public void run() {
////                                    Toast.makeText(HomeActivity.this,
////                                            "Json parsing error: " + e.getMessage(),
////                                            Toast.LENGTH_LONG)
////                                            .show();
////                                }
////                            });
////                        }
////                    }
////
//////                    JSONObject subjectObject = jsonObj.getJSONObject(
//////                            utils.getStoredString(getActivity(), USER_CLASS)
//////                    ).getJSONObject(
//////                            utils.getStoredString(getActivity(), SUBJECT_SELECTED)
//////                    );
//////
//////                    JSONArray chaptersArray = subjectObject.getJSONArray("chapter-names");
//////
//////                    // Looping through all chapter names
//////                    for (int i = 0; i < chaptersArray.length(); i++) {
//////
//////                        mChapterNameVideoLectures.add(chaptersArray.getString(i));
//////
//////                    }
//////
//////                    // Looping through all chapter numbers
//////                    for (int i = 1; i <= mChapterNameVideoLectures.size(); i++) {
//////
//////                        String num = "Chapter: " + i;
//////
//////                        mChapterNmbrVideoLectures.add(num);
//////
//////                    }
////
//////                    // Getting JSON Array node
//////                    JSONArray contacts = jsonObj.getJSONArray("contacts");
//////
//////                    // looping through All Contacts
//////                    for (int i = 0; i < contacts.length(); i++) {
//////                        JSONObject c = contacts.getJSONObject(i);
//////
//////                        String id = c.getString("id");
//////                        String name = c.getString("name");
//////                        String email = c.getString("email");
//////                        String address = c.getString("address");
//////
//////                    }
////
////                } catch (final JSONException e) {
////                    Log.e(TAG, "Json parsing error: " + e.getMessage());
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            Toast.makeText(HomeActivity.this,
////                                    "Json parsing error: " + e.getMessage(),
////                                    Toast.LENGTH_LONG)
////                                    .show();
////                        }
////                    });
////
////                    e.printStackTrace();
////                }
////            } else {
////                Log.e(TAG, "Couldn't get json from server.");
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Toast.makeText(HomeActivity.this,
////                                "Couldn't get json from server. Check LogCat for possible errors!",
////                                Toast.LENGTH_LONG)
////                                .show();
////                    }
////                });
////
////            }
////
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(Void result) {
////            super.onPostExecute(result);
////
//////            if (!isCancelled())
//////                initRecyclerView();
////            Toast.makeText(HomeActivity.this, "Completed!", Toast.LENGTH_SHORT).show();
////
////        }
////
////    }
//    private class GetChapterNames extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
////            try {
////                JsonObject jsonObj = new GetJson().AsJSONObject("https://mysnapchatapp-cb445-default-rtdb.firebaseio.com/filters.json");
////                Iterator<String> iter = jsonObj.keys();
////                while (iter.hasNext()) {
////                    String key = iter.next();
////                    try {
////                        String value = jsonObj.get(key).toString();
//////                            Object value = jsonObj.get(key);
////                    } catch (final JSONException e) {
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                Toast.makeText(HomeActivity.this, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
////                            }
////                        });
////                    }
////                }
//
////                Toast.makeText(context, jsonObject2.toString(), Toast.LENGTH_LONG).show();
//            //            String date = jsonObject2.get("time").getAsString();
////            Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
//
////            } catch (ExecutionException e) {
////                e.printStackTrace();
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//
////            JSONObject jsonObj = new JSONObject(jsonStr);
//
////                    JSONObject subjectObject = jsonObj.getJSONObject(
////                            utils.getStoredString(getActivity(), USER_CLASS)
////                    ).getJSONObject(
////                            utils.getStoredString(getActivity(), SUBJECT_SELECTED)
////                    );
////
////                    JSONArray chaptersArray = subjectObject.getJSONArray("chapter-names");
////
////                    // Looping through all chapter names
////                    for (int i = 0; i < chaptersArray.length(); i++) {
////
////                        mChapterNameVideoLectures.add(chaptersArray.getString(i));
////
////                    }
////
////                    // Looping through all chapter numbers
////                    for (int i = 1; i <= mChapterNameVideoLectures.size(); i++) {
////
////                        String num = "Chapter: " + i;
////
////                        mChapterNmbrVideoLectures.add(num);
////
////                    }
//
////                    // Getting JSON Array node
////                    JSONArray contacts = jsonObj.getJSONArray("contacts");
////
////                    // looping through All Contacts
////                    for (int i = 0; i < contacts.length(); i++) {
////                        JSONObject c = contacts.getJSONObject(i);
////
////                        String id = c.getString("id");
////                        String name = c.getString("name");
////                        String email = c.getString("email");
////                        String address = c.getString("address");
////
////                    }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//
////            if (!isCancelled())
////                initRecyclerView();
//            Toast.makeText(HomeActivity.this, "Completed!", Toast.LENGTH_SHORT).show();
//
//        }
//
//
////        private static class HttpHandler {
////
////            private String TAG = "HttpHandler";
////
////            public HttpHandler() {
////            }
////
////            public String makeServiceCall(String reqUrl) {
////                String response = null;
////                try {
////                    URL url = new URL(reqUrl);
////                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
////                    conn.setRequestMethod("GET");
////                    // read the response
////                    InputStream in = new BufferedInputStream(conn.getInputStream());
////
////                    response = convertStreamToString(in);
////                } catch (MalformedURLException e) {
////                    Log.e(TAG, "MalformedURLException: " + e.getMessage());
////                } catch (ProtocolException e) {
////                    Log.e(TAG, "ProtocolException: " + e.getMessage());
////                } catch (IOException e) {
////                    Log.e(TAG, "IOException: " + e.getMessage());
////                } catch (Exception e) {
////                    Log.e(TAG, "Exception: " + e.getMessage());
////                }
////                return response;
////            }
////
////            private String convertStreamToString(InputStream is) {
////                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
////                StringBuilder sb = new StringBuilder();
////
////                String line;
////                try {
////
////                    while ((line = reader.readLine()) != null) {
////
////                        sb.append(line).append('\n');
////
////                    }
////
////                } catch (IOException e) {
////
////                    e.printStackTrace();
////
////                } finally {
////
////                    try {
////
////                        is.close();
////
////                    } catch (IOException e) {
////
////                        e.printStackTrace();
////                    }
////                }
////
////                return sb.toString();
////            }
////        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        UniversalImageLoade universalImageLoader = new UniversalImageLoader(mContext);

//        GetChapterNames getChapterNames = new GetChapterNames();
//        getChapterNames.execute();
//        yourDataTask yourDataTask = new yourDataTask();
//        yourDataTask.execute();

//        Toast.makeText(context, String.valueOf(new GetJson().isConnected(context)), Toast.LENGTH_SHORT).show();

        if (!new Utils().getStoredBoolean(HomeActivity.this, "isLoggedIn")) {

            finish();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }

//        viewPager = findViewById(R.id.view_pager_homeactivity);
        findViewById(R.id.middleBtn_home).setOnClickListener(middleBtnClickListener());
//        findViewById(R.id.homeBtn_home).setOnClickListener(homeBtnClickListener());
//        findViewById(R.id.personalBtn_home).setOnClickListener(personalBtnClickListener());
//        adapter = new ViewPagerFragmentAdapter(getSupportFragmentManager());

        // Setting up the view Pager
//        setupViewPager(viewPager);

        findViewById(R.id.camera_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, FaceFilterActivity.class));
            }
        });
        findViewById(R.id.developer_info_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DeveloperInfoActivity.class));

            }
        });
        findViewById(R.id.saved_photos_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SavedPhotosActivity.class));
            }
        });

        setProfileView();
        setLogoutView();

//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() == null) {
//            finish();
//            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
////            return;
//        }


    }

    private View.OnClickListener personalBtnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView homeImage = findViewById(R.id.homeBtn_home);
                ImageView personalImage = findViewById(R.id.personalBtn_home);


                homeImage.setImageResource(R.drawable.ic_outline_home_24);
                personalImage.setImageResource(R.drawable.ic_baseline_person_24);
//                ImageView homeImageUnselected = findViewById(R.id.homeBtn_home_unselected);
//                ImageView personalImageUnselected = findViewById(R.id.personalBtn_home_unclicked);

//                ImageView middleImage = findViewById(R.id.middleBtn_home);

//                personalImage.setVisibility(View.VISIBLE);
//                personalImageUnselected.setVisibility(View.GONE);
//                homeImage.setVisibility(View.GONE);
//                homeImageUnselected.setVisibility(View.VISIBLE);


//                middleImage.setBackgroundResource(R.drawable.ic_baseline_add_24);

                viewPager.setCurrentItem(1);

            }
        };
    }

    private View.OnClickListener homeBtnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView homeImage = findViewById(R.id.homeBtn_home);
                ImageView personalImage = findViewById(R.id.personalBtn_home);

                homeImage.setImageResource(R.drawable.ic_baseline_home_24);
                personalImage.setImageResource(R.drawable.ic_outline_person_24);

//                ImageView homeImageUnselected = findViewById(R.id.homeBtn_home_unselected);
//                ImageView personalImageUnselected = findViewById(R.id.personalBtn_home_unclicked);

//                ImageView middleImage = findViewById(R.id.middleBtn_home);

//                personalImage.setVisibility(View.GONE);
//                personalImageUnselected.setVisibility(View.VISIBLE);
//                homeImage.setVisibility(View.VISIBLE);
//                homeImageUnselected.setVisibility(View.GONE);

//                middleImage.setBackgroundResource(R.drawable.ic_baseline_camera_alt_24);

                viewPager.setCurrentItem(0);

            }
        };
    }

    private View.OnClickListener middleBtnClickListener() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, FaceFilterActivity.class));

//                if (viewPager.getCurrentItem() == 0)
//                    Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(context, FaceFilterActivity.class));

//                else Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
//                else Add filters from gallery


            }
        };
    }

//    private void setupViewPager(ViewPager viewPager) {
//
//        // Adding Fragments to Adapter
//        adapter.addFragment(new HomeFragment());
////        adapter.addFragment(new PersonalFragment());
//        viewPager.setOffscreenPageLimit(1);
////        viewPager.setOffscreenPageLimit(2);
//        viewPager.setAdapter(adapter);
//
//        Log.d(TAG, "setupViewPager: adapter attached");
//
////        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            @Override
////            public void onPageScrolled(int position, float v, int i1) {
////
////                switch (position) {
////                    case 0:
////
////                        break;
////                    case 1:
////
////                        break;
////                }
////
////            }
////
////            @Override
////            public void onPageSelected(int position) {
////
////
////            }
////
////            @Override
////            public void onPageScrollStateChanged(int i) {
////
////            }
////        });
//

//    }

    private void setProfileView() {
        ImageView imageView = findViewById(R.id.profile_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));

            }
        });
    }

    private void setLogoutView() {
        ImageView imageView = findViewById(R.id.logout_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Utils().showDialog(HomeActivity.this,
                        "You sure?",
                        "Do you really want to logout?",
                        "Yes",
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                new Utils().removeSharedPref(HomeActivity.this);

                                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                dialogInterface.dismiss();

                                finish();
                                startActivity(intent);

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }, true);

            }
        });
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 0) super.onBackPressed();

        else viewPager.setCurrentItem(0);

    }

    public static class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        public ViewPagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}