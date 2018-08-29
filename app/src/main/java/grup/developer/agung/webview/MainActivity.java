package grup.developer.agung.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private WebView webView;
    private TextView errorMessage;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        errorMessage = (TextView)findViewById(R.id.error_message);
        refreshButton = (Button)findViewById(R.id.refresh_button);
        webView = (WebView) findViewById(R.id.webview);

        setWebView();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected()){
                    webView.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                    refreshButton.setVisibility(View.VISIBLE);
                    errorMessage.setText("Failed to load Url, Check your internet connection than Refresh");
                }else {
                    setWebView();
                }



            }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void setWebView(){
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // Tiga baris di bawah ini agar laman yang dimuat dapat
        // melakukan zoom.
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("https://www.codepolitan.com");

        if (isNetworkConnected()){
            errorMessage.setVisibility(View.GONE);
            refreshButton.setVisibility(View.GONE);

            webView.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);
                    if (progress == 100) {
                        progressBar.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        webView.setVisibility(View.GONE);
                    }
                }
            });

            webView.setWebViewClient(new WebViewClient(){

                @Override public void onReceivedError(WebView view, WebResourceRequest request,
                                                      WebResourceError error) {
                    super.onReceivedError(view, request, error);
                    // Do something
                    webView.setVisibility(View.GONE);
                    errorMessage.setVisibility(View.VISIBLE);
                    refreshButton.setVisibility(View.VISIBLE);
                    errorMessage.setText("Failed to load Url, Check your internet connection than Refresh");

                }
            });

        }else {
            webView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    public class MyWebViewClient extends WebViewClient{
        boolean timeout;

        public MyWebViewClient() {
            timeout = true;
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            timeout = false;
        }
    }
}
