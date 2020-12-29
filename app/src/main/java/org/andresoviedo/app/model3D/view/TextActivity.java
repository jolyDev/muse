package org.andresoviedo.app.model3D.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.nmmu.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;

public class TextActivity extends Activity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text2);

        textView = findViewById(R.id.textView);

        Bundle b = getIntent().getExtras();
        String url = b.getString("url");
        textView.setMovementMethod(new ScrollingMovementMethod());

        new Loader(url).execute();
    }

    public class Loader extends AsyncTask<Void,Void,Void>{

        String text = "";
        String url;

        private final ProgressDialog dialog;

        public Loader(String url){
            this.url = url;
            this.dialog = new ProgressDialog(TextActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(this.url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null){
                    line = bufferedReader.readLine();
                    text = text + line + "\r\n";
                }
                text = text.replace("null","");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Loading...");
            this.dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            textView.setText(text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}