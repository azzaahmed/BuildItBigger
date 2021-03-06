package com.app.azza.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;

import com.app.azza.androidjokedisplay.JokeActivity;
import com.app.azza.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by azza ahmed on 4/17/2017.
 */

    class EndpointsAsyncTask extends AsyncTask<Pair<Context, ProgressBar>, Void, String> {
        private static MyApi myApiService = null;
        private Context context;
       // ProgressDialog progress;
       private ProgressBar spinner;


    @Override
        protected String doInBackground(Pair<Context, ProgressBar>... params) {

            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                    //local Wlan IP not public IP
                     .setRootUrl("http://192.168.1.3:8080/_ah/api/")
                     //.setRootUrl("https://android-app-backend.appspot.com/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });

                // end options for devappserver

                myApiService = builder.build();
            }

            context = params[0].first;
             spinner = params[0].second;


            try {
            //  return myApiService.
            return myApiService.getJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
         //   progress.dismiss();
            spinner.setVisibility(View.GONE);
            Intent intent = new Intent(context, JokeActivity.class);

            intent.putExtra(JokeActivity.JOKE_KEY, result);
            context.startActivity(intent);
        }
    }

