package com.example.demoapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.demoapplication.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private int[] getScreenSIze(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;

        int[] size={w,h};
        return size;
    }

    //Open the survey link in a dialog with WebView
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://surveys.hotjar.com/450faf48-3842-431b-8c48-8605f52b2116";
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.webview, null);
                WebView wv = (WebView) dialogView.findViewById(R.id.web);
                WebSettings webSettings = wv.getSettings();
                webSettings.setJavaScriptEnabled(true);
                //webSettings.setSafeBrowsingEnabled(true);
                webSettings.setAllowContentAccess(false);
                webSettings.setDomStorageEnabled(true);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
                webSettings.setAppCacheEnabled(false);
                wv.clearCache(true);
                wv.clearHistory();
                wv.clearFormData();
                wv.clearMatches();
                wv.clearSslPreferences();
                // Delete all the data to reset the survey
                WebStorage ws = WebStorage.getInstance();
                ws.deleteAllData();

                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookies(value -> {Toast.makeText(getContext(),"Remove Cookies",Toast.LENGTH_LONG).show();});
                cookieManager.removeSessionCookies(value -> {Toast.makeText(getContext(),"Remove Session",Toast.LENGTH_LONG).show();});
                cookieManager.flush();
                int[] screenSize= getScreenSIze(); //This is the method above remember
                int width=screenSize[0];
                int height=screenSize[1];

                int webViewWidth=(int)(0.8*width); //Casting to make them int
                int webViewHeight=(int)(0.7*height);

                wv.setMinimumHeight(webViewHeight);
                wv.setMinimumWidth(webViewWidth);

                //wv.setWebViewClient(new WebViewClient());
                wv.loadUrl(url);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest wrs) {
                        view.loadUrl(url);

                        return true;
                    }
                });

                dialogBuilder.setView(dialogView);
                Dialog popUpDialog = dialogBuilder.create();
                popUpDialog.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(popUpDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                popUpDialog.getWindow().setAttributes(lp);
            }
        });

//Open the browser with the survey link
        binding.buttonFirst2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://surveys.hotjar.com/450faf48-3842-431b-8c48-8605f52b2116";
                /*Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
                // Test opening private tab directly
                Intent intent = new Intent("org.chromium.chrome.browser.incognito.OPEN_PRIVATE_TAB");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}