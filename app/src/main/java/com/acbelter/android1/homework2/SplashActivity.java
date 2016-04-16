package com.acbelter.android1.homework2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements DataLoadingListener {
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

    }

    @Override
    public void onDataLoadingFailed(int errorResId) {
        Toast.makeText(getApplicationContext(), errorResId, Toast.LENGTH_LONG).show();
    }

    protected static String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte[] data = new byte[16384];

        while ((read = is.read(data, 0, data.length)) != -1) {
            outputStream.write(data, 0, read);
        }

        outputStream.flush();
        return outputStream.toString("utf-8");
    }


    private static class LoadDataTask extends AsyncTask<Void, Void, Boolean> {
        private WeakReference<DataLoadingListener> mListenerWeakRef;
        private int mErrorResId = R.string.error_load_data;

        LoadDataTask(DataLoadingListener listener) {
            mListenerWeakRef = new WeakReference<>(listener);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(Api.DATA_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream is = new BufferedInputStream(conn.getInputStream());
                String data = readInputStream(is);
                is.close();

                List<TechnologyItem> items = TechnologyParser.parse(data);

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mErrorResId = R.string.error_connection;
            } catch (IOException e) {
                e.printStackTrace();
                mErrorResId = R.string.error_io;
            } catch (JSONException e) {
                e.printStackTrace();
                mErrorResId = R.string.error_json;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
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
