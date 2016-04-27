package com.acbelter.android1.homework2.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.acbelter.android1.homework2.Api;
import com.acbelter.android1.homework2.MainApplication;
import com.acbelter.android1.homework2.R;
import com.acbelter.android1.homework2.StreamUtils;
import com.acbelter.android1.homework2.TechItem;
import com.acbelter.android1.homework2.TechParser;
import com.acbelter.android1.homework2.db.DbHelper;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements DataLoadingListener {
    private static final long DEFAULT_SPLASH_LENGTH = 2000L;
    private LoadDataTask mLoadDataTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLoadDataTask == null) {
            mLoadDataTask = new LoadDataTask(this);
            mLoadDataTask.execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelDataLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelDataLoading();
    }

    private void cancelDataLoading() {
        if (mLoadDataTask != null) {
            mLoadDataTask.cancel(true);
            mLoadDataTask = null;
        }
    }

    @Override
    public void onDataLoaded() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDataLoadingFailed(int errorResId) {
        Toast.makeText(getApplicationContext(), errorResId, Toast.LENGTH_LONG).show();
    }


    private static class LoadDataTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<DataLoadingListener> mListenerWeakRef;
        private int mErrorResId = R.string.error_load;

        LoadDataTask(DataLoadingListener listener) {
            mListenerWeakRef = new WeakReference<>(listener);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DbHelper dbHelper = MainApplication.getDbHelper();
            if (attemptToLoadDataFromDb(dbHelper)) {
                return true;
            }

            HttpURLConnection conn = null;
            try {
                URL url = new URL(Api.DATA_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream in = new BufferedInputStream(conn.getInputStream());
                String data = StreamUtils.readStream(in);
                in.close();

                List<TechItem> items = TechParser.parse(data);
                dbHelper.deleteTechnologies();
                dbHelper.insertTechnologies(items);

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                if (attemptToLoadDataFromDb(dbHelper)) {
                    return true;
                }
                mErrorResId = R.string.error_read;
            } catch (JSONException e) {
                e.printStackTrace();
                if (attemptToLoadDataFromDb(dbHelper)) {
                    return true;
                }
                mErrorResId = R.string.error_parse;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return false;
        }

        private boolean attemptToLoadDataFromDb(DbHelper dbHelper) {
            if (dbHelper.hasTechnologies()) {
                try {
                    Thread.sleep(DEFAULT_SPLASH_LENGTH);
                } catch (InterruptedException e) {
                    // Ignore
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!isCancelled()) {
                DataLoadingListener listener = mListenerWeakRef.get();
                if (listener != null) {
                    if (result) {
                        listener.onDataLoaded();
                    } else {
                        listener.onDataLoadingFailed(mErrorResId);
                    }
                }
            }
        }
    }
}
