package com.antizon.uit_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.antizon.uit_android.R;
import com.antizon.uit_android.utilities.Utilities;
import com.github.barteksc.pdfviewer.PDFView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PdfViewerActivity extends AppCompatActivity {


    RelativeLayout btnBack;
    PDFView pdfView;
    TextView text_heading;
    String pdfUrl, headerText;
    AVLoadingIndicatorView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Utilities.setWhiteBars(PdfViewerActivity.this);
        pdfUrl = getIntent().getStringExtra("pdfUrl");
        headerText = getIntent().getStringExtra("headerText");

        pdfView = findViewById(R.id.pdfView);
        loading = findViewById(R.id.loading);
        text_heading = findViewById(R.id.text_heading);
        btnBack = findViewById(R.id.btnBack);

        text_heading.setText(headerText);
        new RetrievePdfFromUrl().execute(pdfUrl);

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    // create an async task class for loading pdf file from URL.
    public class RetrievePdfFromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).onLoad(nbPages -> loading.setVisibility(View.GONE)).load();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_bottom);
    }
}