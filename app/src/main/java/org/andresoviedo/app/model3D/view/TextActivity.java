package org.andresoviedo.app.model3D.view;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.andresoviedo.dddmodel2.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

        new Loader(url).execute();
    }

    public class Loader extends AsyncTask<Void,Void,Void>{

        String text;
        String url;

        public Loader(String url){
            this.url = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect("https://raw.githubusercontent.com/jolyDev/muse_data_storage/main/test_file.txt").get();
                text = doc.text();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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