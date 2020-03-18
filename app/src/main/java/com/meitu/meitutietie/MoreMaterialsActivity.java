package com.meitu.meitutietie;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoreMaterialsActivity extends AppCompatActivity {

    private final String Url = "http://tietie.sucai.meitu.com/json/android/allPacks.json";

    private RecyclerView mRecyclerView;
    private ArrayList<StickerTheme> themeList;
    private ArrayList<StickerTheme> header;
    private ImageButton returnButton;
    private ImageButton myThemeButton;
    private ThemeAdapter mAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        themeList = new ArrayList<>();
        header = new ArrayList<>();

        // 通过网络下载文件数据
        RetrieveData retrieveData = new RetrieveData(this);
        retrieveData.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_materials);

        // 隐藏上方状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        returnButton = findViewById(R.id.moreMaterialReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myThemeButton = findViewById(R.id.my_theme_btn);
        myThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreMaterialsActivity.this, MyTheme.class);
                startActivity(intent);
            }
        });

    }


    class RetrieveData extends AsyncTask<String, String, String> {

        Activity mActivity;

        public RetrieveData(Activity activity) {
            mActivity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject jsonObject = getJsonOjectFromUrl(Url);
                JSONArray arr = jsonObject.getJSONArray("allThemePacks");
                for (int i = 0; i < arr.length(); i++) {
                    StickerTheme stickerTheme = new StickerTheme();
                    JSONObject obj = arr.getJSONObject(i);
                    stickerTheme.setThumbnailUrl(obj.getString("thumbnailUrl"));
                    stickerTheme.setTopThumbnailUrl(obj.getString("topThumbnailUrl"));
                    stickerTheme.setPreviewUrl(obj.getString("previewUrl"));
                    stickerTheme.setCount(Integer.valueOf(obj.getString("count")));
                    stickerTheme.setZipUrl(obj.getString("zipUrl"));
                    stickerTheme.setZipSize(obj.getInt("zipSize"));
                    String unicode_name = obj.getString("name");
                    String name = AssetsHelper.decode(unicode_name);
                    int id = Integer.valueOf(obj.getString("id"));
                    stickerTheme.setId(id);
                    stickerTheme.setName(name);
                    boolean isFree = obj.getInt("isPurchase") == 0;
                    stickerTheme.setFree(isFree);

                    // 查看是否已经下载
                    boolean isDownloaded = DBHelper.queryDownload(id, MoreMaterialsActivity.this);
                    stickerTheme.setDownloaded(isDownloaded);

                    if (name.equals("圣诞特辑") || name.equals("实力卖萌")) {
                        header.add(stickerTheme);
                    }
                    themeList.add(stickerTheme);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAdapter = new ThemeAdapter(mActivity);
            mAdapter.setItemList(themeList);
            mAdapter.setHeaderList(header);
            mRecyclerView = findViewById(R.id.more_material_recycler_view);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);

            mAdapter.setRecyclerView(mRecyclerView);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                    ((LinearLayoutManager) layoutManager).getOrientation());
            mRecyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    /**
     * 通过url获取Json文件
     *
     * @param urlString
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static JSONObject getJsonOjectFromUrl(String urlString) throws IOException, JSONException {

        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + '\n');
        }
        br.close();

        urlConnection.disconnect();

        String jsonString = sb.toString();
        return new JSONObject(jsonString);
    }

}
