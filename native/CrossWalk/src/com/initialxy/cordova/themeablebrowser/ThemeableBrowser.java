/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.initialxy.cordova.themeablebrowser;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Browser;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;
import android.webkit.DownloadListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult.Status;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginManager;
import org.apache.cordova.PluginResult;
import org.apache.cordova.Whitelist;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.file.FileUtils;
import org.apache.cordova.filetransfer.FileTransfer;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ClipDrawable;
import android.widget.ProgressBar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint("SetJavaScriptEnabled")
public class ThemeableBrowser extends CordovaPlugin {

    private static final String NULL = "null";
    protected static final String LOG_TAG = "ThemeableBrowser";
    private static final String SELF = "_self";
    private static final String SYSTEM = "_system";
    // private static final String BLANK = "_blank";
    private static final String EXIT_EVENT = "exit";
    private static final String LOAD_START_EVENT = "loadstart";
    private static final String LOAD_STOP_EVENT = "loadstop";
    private static final String LOAD_ERROR_EVENT = "loaderror";

    private static final String ALIGN_LEFT = "left";
    private static final String ALIGN_RIGHT = "right";

    private static final int TOOLBAR_DEF_HEIGHT = 44;
    private static final int DISABLED_ALPHA = 127;  // 50% AKA 127/255.

    private static final String EVT_ERR = "ThemeableBrowserError";
    private static final String EVT_WRN = "ThemeableBrowserWarning";
    private static final String ERR_CRITICAL = "critical";
    private static final String ERR_LOADFAIL = "loadfail";
    private static final String WRN_UNEXPECTED = "unexpected";
    private static final String WRN_UNDEFINED = "undefined";
    private WebViewClient client;
	private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private int FCR = 1;
    private CordovaPlugin cordovacorcor;
    // private CordovaInterface cordova;
    private ThemeableBrowserDialog dialog;
    private WebView inAppWebView;
    private EditText edittext;
    private CallbackContext callbackContext;
    private Options features;
    private static final String TAG = "No";

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments, wrapped with some Cordova helpers.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */

@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;
            //Check if response is positive
            if(resultCode== cordova.getActivity().RESULT_OK){
                if(requestCode == FCR){
                    if(null == mUMA){
                        return;
                    }
                    if(intent == null){
                        //Capture Photo if no image available
                        if(mCM != null){
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    }else{
                        String dataString = intent.getDataString();
                        if(dataString != null){
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        }else{
            if(requestCode == FCR){
                if(null == mUM) return;
                Uri result = intent == null || resultCode != cordova.getActivity().RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState){
    	final CordovaWebView thatWebView = this.webView;
    	final ProgressBar progressbar = new ProgressBar(cordova.getActivity(), null, android.R.attr.progressBarStyleHorizontal);
        assert inAppWebView != null;
        inAppWebView.setWebViewClient(client);
        WebSettings settings = inAppWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(features.zoom);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setPluginState(android.webkit.WebSettings.PluginState.ON);
        inAppWebView.setWebChromeClient(new InAppChromeClient(thatWebView, progressbar){
                	//For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                cordova.setActivityResultCallback(cordovacorcor);
                cordova.getActivity().startActivityForResult(Intent.createChooser(i,"File Chooser"), FCR);
            }
            // // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
            public void openFileChooser(ValueCallback uploadMsg, String acceptType){
                mUM = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                cordova.setActivityResultCallback(cordovacorcor);
                cordova.getActivity().startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FCR);
            }
            //For Android 4.1+
            // public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
            //     mUM = uploadMsg;
            //     Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            //     i.addCategory(Intent.CATEGORY_OPENABLE);
            //     i.setType("image/*");
            //     cordova.getActivity().startActivityForResult(Intent.createChooser(i, "File Chooser"), cordova.getActivity().FCR);
            // }
            //For Android 5.0+
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams){
                if(mUMA != null){
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(cordova.getActivity().getPackageManager()) != null){
                    File photoFile = null;
                    try{
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    }catch(IOException ex){
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if(photoFile != null){
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    }else{
                        takePictureIntent = null;
                    }
                }
                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");
                Intent[] intentArray;
                if(takePictureIntent != null){
                    intentArray = new Intent[]{takePictureIntent};
                }else{
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                cordova.setActivityResultCallback(cordovacorcor);
                cordova.getActivity().startActivityForResult(chooserIntent, FCR);
                return true;
            }
        });
	}


    public boolean execute(String action, CordovaArgs args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("open")) {
            this.callbackContext = callbackContext;
            final String url = args.getString(0);
            String t = args.optString(1);
            if (t == null || t.equals("") || t.equals(NULL)) {
                t = SELF;
            }
            final String target = t;
            final Options features = parseFeature(args.optString(2));

            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String result = "";
                    // SELF
                    if (SELF.equals(target)) {
                        /* This code exists for compatibility between 3.x and 4.x versions of Cordova.
                         * Previously the Config class had a static method, isUrlWhitelisted(). That
                         * responsibility has been moved to the plugins, with an aggregating method in
                         * PluginManager.
                         */
                        Boolean shouldAllowNavigation = null;
                        if (url.startsWith("javascript:")) {
                            shouldAllowNavigation = true;
                        }
                        if (shouldAllowNavigation == null) {
                            shouldAllowNavigation = new Whitelist().isUrlWhiteListed(url);
                        }
                        if (shouldAllowNavigation == null) {
                            try {
                                Method gpm = webView.getClass().getMethod("getPluginManager");
                                PluginManager pm = (PluginManager)gpm.invoke(webView);
                                Method san = pm.getClass().getMethod("shouldAllowNavigation", String.class);
                                shouldAllowNavigation = (Boolean)san.invoke(pm, url);
                            } catch (NoSuchMethodException e) {
                            } catch (IllegalAccessException e) {
                            } catch (InvocationTargetException e) {
                            }
                        }
                        // load in webview
                        if (Boolean.TRUE.equals(shouldAllowNavigation)) {
                            webView.loadUrl(url);
                        }
                        //Load the dialer
                        else if (url.startsWith(WebView.SCHEME_TEL))
                        {
                            try {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse(url));
                                cordova.getActivity().startActivity(intent);
                            } catch (android.content.ActivityNotFoundException e) {
                                emitError(ERR_CRITICAL,
                                        String.format("Error dialing %s: %s", url, e.toString()));
                            }
                        }
                        // load in ThemeableBrowser
                        else {
                            result = showWebPage(url, features);
                        }
                    }
                    // SYSTEM
                    else if (SYSTEM.equals(target)) {
                        result = openExternal(url);
                    }
                    // BLANK - or anything else
                    else {
                        result = showWebPage(url, features);
                    }

                    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, result);
                    pluginResult.setKeepCallback(true);
                    callbackContext.sendPluginResult(pluginResult);
                }
            });
        }
        else if (action.equals("close")) {
            closeDialog();
        }
        else if (action.equals("injectScriptCode")) {
            String jsWrapper = null;
            if (args.getBoolean(1)) {
                jsWrapper = String.format("prompt(JSON.stringify([eval(%%s)]), 'gap-iab://%s')", callbackContext.getCallbackId());
            }
            injectDeferredObject(args.getString(0), jsWrapper);
        }
        else if (action.equals("injectScriptFile")) {
            String jsWrapper;
            if (args.getBoolean(1)) {
                jsWrapper = String.format("(function(d) { var c = d.createElement('script'); c.src = %%s; c.onload = function() { prompt('', 'gap-iab://%s'); }; d.body.appendChild(c); })(document)", callbackContext.getCallbackId());
            } else {
                jsWrapper = "(function(d) { var c = d.createElement('script'); c.src = %s; d.body.appendChild(c); })(document)";
            }
            injectDeferredObject(args.getString(0), jsWrapper);
        }
        else if (action.equals("injectStyleCode")) {
            String jsWrapper;
            if (args.getBoolean(1)) {
                jsWrapper = String.format("(function(d) { var c = d.createElement('style'); c.innerHTML = %%s; d.body.appendChild(c); prompt('', 'gap-iab://%s');})(document)", callbackContext.getCallbackId());
            } else {
                jsWrapper = "(function(d) { var c = d.createElement('style'); c.innerHTML = %s; d.body.appendChild(c); })(document)";
            }
            injectDeferredObject(args.getString(0), jsWrapper);
        }
        else if (action.equals("injectStyleFile")) {
            String jsWrapper;
            if (args.getBoolean(1)) {
                jsWrapper = String.format("(function(d) { var c = d.createElement('link'); c.rel='stylesheet'; c.type='text/css'; c.href = %%s; d.head.appendChild(c); prompt('', 'gap-iab://%s');})(document)", callbackContext.getCallbackId());
            } else {
                jsWrapper = "(function(d) { var c = d.createElement('link'); c.rel='stylesheet'; c.type='text/css'; c.href = %s; d.head.appendChild(c); })(document)";
            }
            injectDeferredObject(args.getString(0), jsWrapper);
        }
        else if (action.equals("show")) {
            this.cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                }
            });
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
            pluginResult.setKeepCallback(true);
            this.callbackContext.sendPluginResult(pluginResult);
        }
        else if (action.equals("reload")) {
            if (inAppWebView != null) {
                this.cordova.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        inAppWebView.reload();
                    }
                });
            }
        }
        else {
            return false;
        }
        return true;
    }

    /**
     * Called when the view navigates.
     */
    @Override
    public void onReset() {
        closeDialog();
    }

    /**
     * Called by AccelBroker when listener is to be shut down.
     * Stop listener.
     */
    public void onDestroy() {
        closeDialog();
    }

    /**
     * Inject an object (script or style) into the ThemeableBrowser WebView.
     *
     * This is a helper method for the inject{Script|Style}{Code|File} API calls, which
     * provides a consistent method for injecting JavaScript code into the document.
     *
     * If a wrapper string is supplied, then the source string will be JSON-encoded (adding
     * quotes) and wrapped using string formatting. (The wrapper string should have a single
     * '%s' marker)
     *
     * @param source      The source object (filename or script/style text) to inject into
     *                    the document.
     * @param jsWrapper   A JavaScript string to wrap the source string in, so that the object
     *                    is properly injected, or null if the source string is JavaScript text
     *                    which should be executed directly.
     */
    private void injectDeferredObject(String source, String jsWrapper) {
        String scriptToInject;
        if (jsWrapper != null) {
            org.json.JSONArray jsonEsc = new org.json.JSONArray();
            jsonEsc.put(source);
            String jsonRepr = jsonEsc.toString();
            String jsonSourceString = jsonRepr.substring(1, jsonRepr.length()-1);
            scriptToInject = String.format(jsWrapper, jsonSourceString);
        } else {
            scriptToInject = source;
        }
        final String finalScriptToInject = scriptToInject;
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if (inAppWebView != null) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                        // This action will have the side-effect of blurring the currently focused
                        // element
                        inAppWebView.loadUrl("javascript:" + finalScriptToInject);
                    } else {
                        inAppWebView.evaluateJavascript(finalScriptToInject, null);
                    }
                }
            }
        });
    }

    /**
     * Put the list of features into a hash map
     *
     * @param optString
     * @return
     */
    private Options parseFeature(String optString) {
        Options result = null;
        if (optString != null && !optString.isEmpty()) {
            try {
                result = ThemeableBrowserUnmarshaller.JSONToObj(
                        optString, Options.class);
            } catch (Exception e) {
                emitError(ERR_CRITICAL,
                        String.format("Invalid JSON @s", e.toString()));
            }
        } else {
            emitWarning(WRN_UNDEFINED,
                    "No config was given, defaults will be used, "
                    + "which is quite boring.");
        }

        if (result == null) {
            result = new Options();
        }

        // Always show location, this property is overwritten.
        result.location = true;

        return result;
    }

    /**
     * Display a new browser with the specified URL.
     *
     * @param url
     * @return
     */
    public String openExternal(String url) {
        try {
            Intent intent = null;
            intent = new Intent(Intent.ACTION_VIEW);
            // Omitting the MIME type for file: URLs causes "No Activity found to handle Intent".
            // Adding the MIME type to http: URLs causes them to not be handled by the downloader.
            Uri uri = Uri.parse(url);
            if ("file".equals(uri.getScheme())) {
                intent.setDataAndType(uri, webView.getResourceApi().getMimeType(uri));
            } else {
                intent.setData(uri);
            }
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, cordova.getActivity().getPackageName());
            this.cordova.getActivity().startActivity(intent);
            return "";
        } catch (android.content.ActivityNotFoundException e) {
            Log.d(LOG_TAG, "ThemeableBrowser: Error loading url "+url+":"+ e.toString());
            return e.toString();
        }
    }

    /**
     * Closes the dialog
     */
    public void closeDialog() {
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // The JS protects against multiple calls, so this should happen only when
                // closeDialog() is called by other native code.
                if (inAppWebView == null) {
                    emitWarning(WRN_UNEXPECTED, "Close called but already closed.");
                    return;
                }

                inAppWebView.setWebViewClient(new WebViewClient() {
                    // NB: wait for about:blank before dismissing
                    public void onPageFinished(WebView view, String url) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        // Clean up.
                        dialog = null;
                        inAppWebView = null;
                        edittext = null;
                        callbackContext = null;
                    }
                });

                // NB: From SDK 19: "If you call methods on WebView from any
                // thread other than your app's UI thread, it can cause
                // unexpected results."
                // http://developer.android.com/guide/webapps/migrating.html#Threads
                inAppWebView.loadUrl("about:blank");

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("type", EXIT_EVENT);
                    sendUpdate(obj, false);
                } catch (JSONException ex) {
                }
            }
        });
    }

    private void emitButtonEvent(Event event, String url) {
        emitButtonEvent(event, url, null);
    }

    private void emitButtonEvent(Event event, String url, Integer index) {
        if (event != null && event.event != null) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("type", event.event);
                obj.put("url", url);
                if (index != null) {
                    obj.put("index", index.intValue());
                }
                sendUpdate(obj, true);
            } catch (JSONException e) {
                // Ignore, should never happen.
            }
        } else {
            emitWarning(WRN_UNDEFINED,
                    "Button clicked, but event property undefined. "
                    + "No event will be raised.");
        }
    }

    private void emitError(String code, String message) {
        emitLog(EVT_ERR, code, message);
    }

    private void emitWarning(String code, String message) {
        emitLog(EVT_WRN, code, message);
    }

    private void emitLog(String type, String code, String message) {
        if (type != null) {
            try {
                JSONObject obj = new JSONObject();
                obj.put("type", type);
                obj.put("code", code);
                obj.put("message", message);
                sendUpdate(obj, true);
            } catch (JSONException e) {
                // Ignore, should never happen.
            }
        }
    }

    /**
     * Checks to see if it is possible to go back one page in history, then does so.
     */
    public void goBack() {
        if (this.inAppWebView != null && this.inAppWebView.canGoBack()) {
            this.inAppWebView.goBack();
        }
    }

    /**
     * Can the web browser go back?
     * @return boolean
     */
    public boolean canGoBack() {
        return this.inAppWebView != null && this.inAppWebView.canGoBack();
    }

    /**
     * Checks to see if it is possible to go forward one page in history, then does so.
     */
    private void goForward() {
        if (this.inAppWebView != null && this.inAppWebView.canGoForward()) {
            this.inAppWebView.goForward();
        }
    }

    /**
     * Navigate to the new page
     *
     * @param url to load
     */
    private void navigate(String url) {
        InputMethodManager imm = (InputMethodManager)this.cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

        if (!url.startsWith("http") && !url.startsWith("file:")) {
            this.inAppWebView.loadUrl("http://" + url);
        } else {
            this.inAppWebView.loadUrl(url);
        }
        this.inAppWebView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                            String contentDisposition, String mimetype,
                            long contentLength) {
                      Intent i = new Intent(Intent.ACTION_VIEW);
                      i.setData(Uri.parse(url));
                      cordova.getActivity().startActivity(i);
                    }
                });
        this.inAppWebView.requestFocus();
    }

    private ThemeableBrowser getThemeableBrowser() {
        return this;
    }

    /**
     * Display a new browser with the specified URL.
     *
     * @param url
     * @param features
     * @return
     */
    public String showWebPage(final String url, final Options features) {
        final CordovaWebView thatWebView = this.webView;

        // Create dialog in new thread
        Runnable runnable = new Runnable() {
            @SuppressLint("NewApi")
            public void run() {
                // Let's create the main dialog
                dialog = new ThemeableBrowserDialog(cordova.getActivity(),
                        android.R.style.Theme_Black_NoTitleBar,
                        features.hardwareback);
                if (!features.disableAnimation) {
                    dialog.getWindow().getAttributes().windowAnimations
                            = android.R.style.Animation_Dialog;
                }
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setThemeableBrowser(getThemeableBrowser());

                // Main container layout
                ViewGroup main = null;

                if (features.fullscreen) {
                    main = new FrameLayout(cordova.getActivity());
                } else {
                    main = new LinearLayout(cordova.getActivity());
                    ((LinearLayout) main).setOrientation(LinearLayout.VERTICAL);
                }

                // Toolbar layout
                Toolbar toolbarDef = features.toolbar;
                FrameLayout toolbar = new FrameLayout(cordova.getActivity());
                toolbar.setBackgroundColor(hexStringToColor(
                        toolbarDef != null && toolbarDef.color != null
                                ? toolbarDef.color : "#ffffffff"));
                toolbar.setLayoutParams(new ViewGroup.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dpToPixels(toolbarDef != null
                                ? toolbarDef.height : TOOLBAR_DEF_HEIGHT)));

                if (toolbarDef != null
                        && (toolbarDef.image != null || toolbarDef.wwwImage != null)) {
                    try {
                        Drawable background = getImage(toolbarDef.image
                                , toolbarDef.wwwImage, toolbarDef.wwwImageDensity);
                        setBackground(toolbar, background);
                    } catch (Resources.NotFoundException e) {
                        emitError(ERR_LOADFAIL,
                                String.format("Image for toolbar, %s, failed to load",
                                        toolbarDef.image));
                    } catch (IOException ioe) {
                        emitError(ERR_LOADFAIL,
                                String.format("Image for toolbar, %s, failed to load",
                                        toolbarDef.wwwImage));
                    }
                }

                // Left Button Container layout
                LinearLayout leftButtonContainer = new LinearLayout(cordova.getActivity());
                FrameLayout.LayoutParams leftButtonContainerParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                leftButtonContainerParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                leftButtonContainer.setLayoutParams(leftButtonContainerParams);
                leftButtonContainer.setVerticalGravity(Gravity.CENTER_VERTICAL);

                // Right Button Container layout
                LinearLayout rightButtonContainer = new LinearLayout(cordova.getActivity());
                FrameLayout.LayoutParams rightButtonContainerParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                rightButtonContainerParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                rightButtonContainer.setLayoutParams(rightButtonContainerParams);
                rightButtonContainer.setVerticalGravity(Gravity.CENTER_VERTICAL);

                // Edit Text Box
                edittext = new EditText(cordova.getActivity());
                RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                textLayoutParams.addRule(RelativeLayout.RIGHT_OF, 1);
                textLayoutParams.addRule(RelativeLayout.LEFT_OF, 5);
                edittext.setLayoutParams(textLayoutParams);
                edittext.setSingleLine(true);
                edittext.setText(url);
                edittext.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                edittext.setImeOptions(EditorInfo.IME_ACTION_GO);
                edittext.setInputType(InputType.TYPE_NULL); // Will not except input... Makes the text NON-EDITABLE
                edittext.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            navigate(edittext.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });

                // Back button
                final Button back = createButton(
                    features.backButton,
                    "back button",
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            emitButtonEvent(
                                    features.backButton,
                                    inAppWebView.getUrl());

                            if (features.backButtonCanClose && !canGoBack()) {
                                closeDialog();
                            } else {
                                goBack();
                            }
                        }
                    }
                );

                if (back != null) {
                    back.setEnabled(features.backButtonCanClose);
                }

                // Forward button
                final Button forward = createButton(
                    features.forwardButton,
                    "forward button",
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            emitButtonEvent(
                                    features.forwardButton,
                                    inAppWebView.getUrl());

                            goForward();
                        }
                    }
                );

                if (back != null) {
                    back.setEnabled(false);
                }


                // Close/Done button
                Button close = createButton(
                    features.closeButton,
                    "close button",
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            emitButtonEvent(
                                    features.closeButton,
                                    inAppWebView.getUrl());
                            closeDialog();
                        }
                    }
                );

                // Menu button
                Spinner menu = features.menu != null
                        ? new MenuSpinner(cordova.getActivity()) : null;
                if (menu != null) {
                    menu.setLayoutParams(new LinearLayout.LayoutParams(
                            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    menu.setContentDescription("menu button");
                    setButtonImages(menu, features.menu, DISABLED_ALPHA);

                    // We are not allowed to use onClickListener for Spinner, so we will use
                    // onTouchListener as a fallback.
                    menu.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (event.getAction() == MotionEvent.ACTION_UP) {
                                emitButtonEvent(
                                        features.menu,
                                        inAppWebView.getUrl());
                            }
                            return false;
                        }
                    });

                    if (features.menu.items != null) {
                        HideSelectedAdapter<EventLabel> adapter
                                = new HideSelectedAdapter<EventLabel>(
                                cordova.getActivity(),
                                android.R.layout.simple_spinner_item,
                                features.menu.items);
                        adapter.setDropDownViewResource(
                                android.R.layout.simple_spinner_dropdown_item);
                        menu.setAdapter(adapter);
                        menu.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(
                                            AdapterView<?> adapterView,
                                            View view, int i, long l) {
                                        if (inAppWebView != null
                                                && i < features.menu.items.length) {
                                            emitButtonEvent(
                                                    features.menu.items[i],
                                                    inAppWebView.getUrl(), i);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(
                                            AdapterView<?> adapterView) {
                                    }
                                }
                        );
                    }
                }

                // Title
                final TextView title = features.title != null
                        ? new TextView(cordova.getActivity()) : null;
                if (title != null) {
                    FrameLayout.LayoutParams titleParams
                            = new FrameLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    titleParams.gravity = Gravity.CENTER;
                    title.setLayoutParams(titleParams);
                    title.setSingleLine();
                    title.setEllipsize(TextUtils.TruncateAt.END);
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(hexStringToColor(
                            features.title.color != null
                                    ? features.title.color : "#000000ff"));
                    if (features.title.staticText != null) {
                        title.setText(features.title.staticText);
                    }
                }
                final ProgressBar progressbar = new ProgressBar(cordova.getActivity(), null, android.R.attr.progressBarStyleHorizontal);
                FrameLayout.LayoutParams progressbarLayout = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 6);
                //progressbarLayout.
                progressbar.setLayoutParams(progressbarLayout);
                if (features.browserProgress != null){
                    Integer progressColor=Color.BLUE;
                    if ( features.browserProgress.progressColor != null
                            && features.browserProgress.progressColor.length() > 0) {
                        progressColor = Color.parseColor(features.browserProgress.progressColor);
                    }
                    ClipDrawable progressDrawable = new ClipDrawable(new ColorDrawable(progressColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
                    progressbar.setProgressDrawable(progressDrawable);
                    Integer progressBgColor = Color.GRAY;
                    if ( features.browserProgress.progressBgColor != null
                            && features.browserProgress.progressBgColor.length() > 0) {
                        progressBgColor = Color.parseColor(features.browserProgress.progressBgColor);
                    }
                    progressbar.setBackgroundColor(progressBgColor);
                }
                // WebView
                inAppWebView = new WebView(cordova.getActivity());
                final ViewGroup.LayoutParams inAppWebViewParams = features.fullscreen
                        ? new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                        : new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
                if (!features.fullscreen) {
                    ((LinearLayout.LayoutParams) inAppWebViewParams).weight = 1;
                }
                inAppWebView.setLayoutParams(inAppWebViewParams);
                inAppWebView.setWebChromeClient(new InAppChromeClient(thatWebView, progressbar));
                WebViewClient client = new ThemeableBrowserClient(thatWebView, new PageLoadListener() {
                    @Override
                    public void onPageFinished(String url, boolean canGoBack, boolean canGoForward) {
                        if (inAppWebView != null
                                && title != null && features.title != null
                                && features.title.staticText == null
                                && features.title.showPageTitle) {
                            title.setText(inAppWebView.getTitle());
                        }

                        if (back != null) {
                            back.setEnabled(canGoBack || features.backButtonCanClose);
                        }

                        if (forward != null) {
                            forward.setEnabled(canGoForward);
                        }
                    }
                });
                inAppWebView.setWebViewClient(client);
                WebSettings settings = inAppWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setJavaScriptCanOpenWindowsAutomatically(true);
                settings.setBuiltInZoomControls(features.zoom);
                settings.setDisplayZoomControls(false);
                settings.setAllowFileAccess(true);
                settings.setPluginState(android.webkit.WebSettings.PluginState.ON);

                //Toggle whether this is enabled or not!
                Bundle appSettings = cordova.getActivity().getIntent().getExtras();
                boolean enableDatabase = appSettings == null || appSettings.getBoolean("ThemeableBrowserStorageEnabled", true);
                if (enableDatabase) {
                    String databasePath = cordova.getActivity().getApplicationContext().getDir("themeableBrowserDB", Context.MODE_PRIVATE).getPath();
                    settings.setDatabasePath(databasePath);
                    settings.setDatabaseEnabled(true);
                }
                settings.setDomStorageEnabled(true);

                if (features.clearcache) {
                    CookieManager.getInstance().removeAllCookie();
                } else if (features.clearsessioncache) {
                    CookieManager.getInstance().removeSessionCookie();
                }

                inAppWebView.loadUrl(url);
                inAppWebView.setDownloadListener(new DownloadListener() {
                    public void onDownloadStart(String url, String userAgent,
                            String contentDisposition, String mimetype,
                            long contentLength) {
                      Intent i = new Intent(Intent.ACTION_VIEW);
                      i.setData(Uri.parse(url));
                      cordova.getActivity().startActivity(i);
                    }
                });
                inAppWebView.setId(Integer.valueOf(6));
                inAppWebView.getSettings().setLoadWithOverviewMode(true);
                inAppWebView.getSettings().setUseWideViewPort(true);
                inAppWebView.requestFocus();
                inAppWebView.requestFocusFromTouch();

                // Add buttons to either leftButtonsContainer or
                // rightButtonsContainer according to user's alignment
                // configuration.
                int leftContainerWidth = 0;
                int rightContainerWidth = 0;

                if (features.customButtons != null) {
                    for (int i = 0; i < features.customButtons.length; i++) {
                        final BrowserButton buttonProps = features.customButtons[i];
                        final int index = i;
                        Button button = createButton(
                            buttonProps,
                            String.format("custom button at %d", i),
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (inAppWebView != null) {
                                        emitButtonEvent(buttonProps,
                                                inAppWebView.getUrl(), index);
                                    }
                                }
                            }
                        );

                        if (ALIGN_RIGHT.equals(buttonProps.align)) {
                            rightButtonContainer.addView(button);
                            rightContainerWidth
                                    += button.getLayoutParams().width;
                        } else {
                            leftButtonContainer.addView(button, 0);
                            leftContainerWidth
                                    += button.getLayoutParams().width;
                        }
                    }
                }

                // Back and forward buttons must be added with special ordering logic such
                // that back button is always on the left of forward button if both buttons
                // are on the same side.
                if (forward != null && features.forwardButton != null
                        && !ALIGN_RIGHT.equals(features.forwardButton.align)) {
                    leftButtonContainer.addView(forward, 0);
                    leftContainerWidth
                            += forward.getLayoutParams().width;
                }

                if (back != null && features.backButton != null
                        && ALIGN_RIGHT.equals(features.backButton.align)) {
                    rightButtonContainer.addView(back);
                    rightContainerWidth
                            += back.getLayoutParams().width;
                }

                if (back != null && features.backButton != null
                        && !ALIGN_RIGHT.equals(features.backButton.align)) {
                    leftButtonContainer.addView(back, 0);
                    leftContainerWidth
                            += back.getLayoutParams().width;
                }

                if (forward != null && features.forwardButton != null
                        && ALIGN_RIGHT.equals(features.forwardButton.align)) {
                    rightButtonContainer.addView(forward);
                    rightContainerWidth
                            += forward.getLayoutParams().width;
                }

                if (menu != null) {
                    if (features.menu != null
                            && ALIGN_RIGHT.equals(features.menu.align)) {
                        rightButtonContainer.addView(menu);
                        rightContainerWidth
                                += menu.getLayoutParams().width;
                    } else {
                        leftButtonContainer.addView(menu, 0);
                        leftContainerWidth
                                += menu.getLayoutParams().width;
                    }
                }

                if (close != null) {
                    if (features.closeButton != null
                            && ALIGN_RIGHT.equals(features.closeButton.align)) {
                        rightButtonContainer.addView(close);
                        rightContainerWidth
                                += close.getLayoutParams().width;
                    } else {
                        leftButtonContainer.addView(close, 0);
                        leftContainerWidth
                                += close.getLayoutParams().width;
                    }
                }

                // Add the views to our toolbar
                toolbar.addView(leftButtonContainer);
                // Don't show address bar.
                // toolbar.addView(edittext);
                toolbar.addView(rightButtonContainer);

                if (title != null) {
                    int titleMargin = Math.max(
                            leftContainerWidth, rightContainerWidth);

                    FrameLayout.LayoutParams titleParams
                            = (FrameLayout.LayoutParams) title.getLayoutParams();
                    titleParams.setMargins(titleMargin, 0, titleMargin, 0);
                    toolbar.addView(title);
                }

                if (features.fullscreen) {
                    // If full screen mode, we have to add inAppWebView before adding toolbar.
                    main.addView(inAppWebView);
                }

                // Don't add the toolbar if its been disabled
                if (features.location) {
                    // Add our toolbar to our main view/layout
                    main.addView(toolbar);
                    if (features.browserProgress!=null&&features.browserProgress.showProgress){
                       main.addView(progressbar);
                   }
                }

                if (!features.fullscreen) {
                    // If not full screen, we add inAppWebView after adding toolbar.
                    main.addView(inAppWebView);
                }

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                dialog.setContentView(main);
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                // the goal of openhidden is to load the url and not display it
                // Show() needs to be called to cause the URL to be loaded
                if(features.hidden) {
                    dialog.hide();
                }
            }
        };
        this.cordova.getActivity().runOnUiThread(runnable);
        return "";
    }

    /**
     * Convert our DIP units to Pixels
     *
     * @return int
     */
    private int dpToPixels(int dipValue) {
        int value = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                (float) dipValue,
                cordova.getActivity().getResources().getDisplayMetrics()
        );

        return value;
    }

    private int hexStringToColor(String hex) {
        int result = 0;

        if (hex != null && !hex.isEmpty()) {
            if (hex.charAt(0) == '#') {
                hex = hex.substring(1);
            }

            // No alpha, that's fine, we will just attach ff.
            if (hex.length() < 8) {
                hex += "ff";
            }

            result = (int) Long.parseLong(hex, 16);

            // Almost done, but Android color code is in form of ARGB instead of
            // RGBA, so we gotta shift it a bit.
            int alpha = (result & 0xff) << 24;
            result = result >> 8 & 0xffffff | alpha;
        }

        return result;
    }

    /**
    * This is a rather unintuitive helper method to load images. The reason why this method exists
    * is because due to some service limitations, one may not be able to add images to native
    * resource bundle. So this method offers a way to load image from www contents instead.
    * However loading from native resource bundle is already preferred over loading from www. So
    * if name is given, then it simply loads from resource bundle and the other two parameters are
    * ignored. If name is not given, then altPath is assumed to be a file path _under_ www and
    * altDensity is the desired density of the given image file, because without native resource
    * bundle, we can't tell what density the image is supposed to be so it needs to be given
    * explicitly.
    */
    private Drawable getImage(String name, String altPath, double altDensity) throws IOException {
        Drawable result = null;
        Resources activityRes = cordova.getActivity().getResources();

        if (name != null) {
            int id = activityRes.getIdentifier(name, "drawable",
                    cordova.getActivity().getPackageName());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                result = activityRes.getDrawable(id);
            } else {
                result = activityRes.getDrawable(id, cordova.getActivity().getTheme());
            }
        } else if (altPath != null) {
            File file = new File("www", altPath);
            InputStream is = null;
            try {
                is = cordova.getActivity().getAssets().open(file.getPath());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bitmap.setDensity((int) (DisplayMetrics.DENSITY_MEDIUM * altDensity));
                result = new BitmapDrawable(activityRes, bitmap);
            } finally {
                // Make sure we close this input stream to prevent resource leak.
                try {
                    is.close();
                } catch (Exception e) {}
            }
        }
        return result;
    }

    private void setButtonImages(View view, BrowserButton buttonProps, int disabledAlpha) {
        Drawable normalDrawable = null;
        Drawable disabledDrawable = null;
        Drawable pressedDrawable = null;

        CharSequence description = view.getContentDescription();

        if (buttonProps.image != null || buttonProps.wwwImage != null) {
            try {
                normalDrawable = getImage(buttonProps.image, buttonProps.wwwImage,
                        buttonProps.wwwImageDensity);
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.width = normalDrawable.getIntrinsicWidth();
                params.height = normalDrawable.getIntrinsicHeight();
            } catch (Resources.NotFoundException e) {
                emitError(ERR_LOADFAIL,
                        String.format("Image for %s, %s, failed to load",
                                description, buttonProps.image));
            } catch (IOException ioe) {
                emitError(ERR_LOADFAIL,
                        String.format("Image for %s, %s, failed to load",
                                description, buttonProps.wwwImage));
            }
        } else {
            emitWarning(WRN_UNDEFINED,
                    String.format("Image for %s is not defined. Button will not be shown",
                            description));
        }

        if (buttonProps.imagePressed != null || buttonProps.wwwImagePressed != null) {
            try {
                pressedDrawable = getImage(buttonProps.imagePressed, buttonProps.wwwImagePressed,
                        buttonProps.wwwImageDensity);
            } catch (Resources.NotFoundException e) {
                emitError(ERR_LOADFAIL,
                        String.format("Pressed image for %s, %s, failed to load",
                                description, buttonProps.imagePressed));
            } catch (IOException e) {
                emitError(ERR_LOADFAIL,
                        String.format("Pressed image for %s, %s, failed to load",
                                description, buttonProps.wwwImagePressed));
            }
        } else {
            emitWarning(WRN_UNDEFINED,
                    String.format("Pressed image for %s is not defined.",
                            description));
        }

        if (normalDrawable != null) {
            // Create the disabled state drawable by fading the normal state
            // drawable. Drawable.setAlpha() stopped working above Android 4.4
            // so we gotta bring out some bitmap magic. Credit goes to:
            // http://stackoverflow.com/a/7477572
            Bitmap enabledBitmap = ((BitmapDrawable) normalDrawable).getBitmap();
            Bitmap disabledBitmap = Bitmap.createBitmap(
                    normalDrawable.getIntrinsicWidth(),
                    normalDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(disabledBitmap);

            Paint paint = new Paint();
            paint.setAlpha(disabledAlpha);
            canvas.drawBitmap(enabledBitmap, 0, 0, paint);

            Resources activityRes = cordova.getActivity().getResources();
            disabledDrawable = new BitmapDrawable(activityRes, disabledBitmap);
        }

        StateListDrawable states = new StateListDrawable();
        if (pressedDrawable != null) {
            states.addState(
                new int[] {
                    android.R.attr.state_pressed
                },
                pressedDrawable
            );
        }
        if (normalDrawable != null) {
            states.addState(
                new int[] {
                    android.R.attr.state_enabled
                },
                normalDrawable
            );
        }
        if (disabledDrawable != null) {
            states.addState(
                new int[] {},
                disabledDrawable
            );
        }

        setBackground(view, states);
    }

    private void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    private Button createButton(BrowserButton buttonProps, String description,
            View.OnClickListener listener) {
        Button result = null;
        if (buttonProps != null) {
            result = new Button(cordova.getActivity());
            result.setContentDescription(description);
            result.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            setButtonImages(result, buttonProps, DISABLED_ALPHA);
            if (listener != null) {
                result.setOnClickListener(listener);
            }
        } else {
            emitWarning(WRN_UNDEFINED,
                    String.format("%s is not defined. Button will not be shown.",
                            description));
        }
        return result;
    }

    /**
     * Create a new plugin success result and send it back to JavaScript
     *
     * @param obj a JSONObject contain event payload information
     */
    private void sendUpdate(JSONObject obj, boolean keepCallback) {
        sendUpdate(obj, keepCallback, PluginResult.Status.OK);
    }

    /**
     * Create a new plugin result and send it back to JavaScript
     *
     * @param obj a JSONObject contain event payload information
     * @param status the status code to return to the JavaScript environment
     */
    private void sendUpdate(JSONObject obj, boolean keepCallback, PluginResult.Status status) {
        if (callbackContext != null) {
            PluginResult result = new PluginResult(status, obj);
            result.setKeepCallback(keepCallback);
            callbackContext.sendPluginResult(result);
            if (!keepCallback) {
                callbackContext = null;
            }
        }
    }

    public static interface PageLoadListener {
        public void onPageFinished(String url, boolean canGoBack,
                boolean canGoForward);
    }

    /**
     * The webview client receives notifications about appView
     */
    public class ThemeableBrowserClient extends WebViewClient {
        PageLoadListener callback;
        CordovaWebView webView;

        /**
         * Constructor.
         *
         * @param webView
         * @param callback
         */
        public ThemeableBrowserClient(CordovaWebView webView,
                PageLoadListener callback) {
            this.webView = webView;
            this.callback = callback;
        }

        /**
         * Override the URL that should be loaded
         *
         * This handles a small subset of all the URIs that would be encountered.
         *
         * @param webView
         * @param url
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.startsWith(WebView.SCHEME_TEL)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    cordova.getActivity().startActivity(intent);
                    return true;
                } catch (android.content.ActivityNotFoundException e) {
                    Log.e(LOG_TAG, "Error dialing " + url + ": " + e.toString());
                }
            } else if (url.startsWith("geo:") || url.startsWith(WebView.SCHEME_MAILTO) || url.startsWith("market:")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    cordova.getActivity().startActivity(intent);
                    return true;
                } catch (android.content.ActivityNotFoundException e) {
                    Log.e(LOG_TAG, "Error with " + url + ": " + e.toString());
                }
            }
            // If sms:5551212?body=This is the message
            else if (url.startsWith("sms:")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    // Get address
                    String address = null;
                    int parmIndex = url.indexOf('?');
                    if (parmIndex == -1) {
                        address = url.substring(4);
                    } else {
                        address = url.substring(4, parmIndex);

                        // If body, then set sms body
                        Uri uri = Uri.parse(url);
                        String query = uri.getQuery();
                        if (query != null) {
                            if (query.startsWith("body=")) {
                                intent.putExtra("sms_body", query.substring(5));
                            }
                        }
                    }
                    intent.setData(Uri.parse("sms:" + address));
                    intent.putExtra("address", address);
                    intent.setType("vnd.android-dir/mms-sms");
                    cordova.getActivity().startActivity(intent);
                    return true;
                } catch (android.content.ActivityNotFoundException e) {
                    Log.e(LOG_TAG, "Error sending sms " + url + ":" + e.toString());
                }
            }
            return false;
        }


        /*
         * onPageStarted fires the LOAD_START_EVENT
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            String newloc = "";
            if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:")) {
                newloc = url;
            }
            else
            {
                // Assume that everything is HTTP at this point, because if we don't specify,
                // it really should be.  Complain loudly about this!!!
                Log.e(LOG_TAG, "Possible Uncaught/Unknown URI");
                newloc = "https://duckduckgo.com?q=" + url;
            }

            // Update the UI if we haven't already
            if (!newloc.equals(edittext.getText().toString())) {
                edittext.setText(newloc);
            }

            try {
                JSONObject obj = new JSONObject();
                obj.put("type", LOAD_START_EVENT);
                obj.put("url", newloc);
                sendUpdate(obj, true);
            } catch (JSONException ex) {
                Log.e(LOG_TAG, "URI passed in has caused a JSON error.");
            }
        }

        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            try {
                JSONObject obj = new JSONObject();
                obj.put("type", LOAD_STOP_EVENT);
                obj.put("url", url);

                sendUpdate(obj, true);

                if (this.callback != null) {
                    this.callback.onPageFinished(url, view.canGoBack(),
                            view.canGoForward());
                }
            } catch (JSONException ex) {
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            Log.e(String.valueOf(errorCode), description);
            if (errorCode < 1000000000) {
                String summary = "<html>" +
                        "<head>" +
                        "<title>Error!</title>" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, user-scalable=no\">" +
                        "<style>"+
                        "html, body {" +
                        "    margin: 0;" +
                        "    padding: 0;" +
                        "    width: 100%;" +
                        "    height: 100%;" +
                        "    display: table;" +
                        "}"+
                        ".container {" +
                        "    display: table-cell;" +
                        "    text-align: center;" +
                        "    vertical-align: middle;" +
                        "}" +
                        ".content {" +
                        "margin-top: -100px;"+
                        "    display: inline-block;" +
                        "    text-align: left;" +
                        "}"+
                        "</style>"+
                        "</head>" +
                        "<body style='background: white;'>" +
                        "<div class='container'><div class='content'>"+
                        "<center>" +
                        "<img style=\"height: 30%; max-width: 100%; \" src=\"data:image/png;base64,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\" />" +
                        "<h2 style='color: slategrey;'>" +
                        "Unable To Load.<b></h2><h3 style='color: grey'>Could Not Connect To The Server." +
                        "</h3>" +
                        "<h4 style='color: #545454'>"+description+"</h4></b>"+
                        "</center>" +
                        "</div></div>"+
                        "</body>" +
                        "</html>";
                view.loadData(summary, "text/html", null);
                return;
            }
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    /**
     * Like Spinner but will always trigger onItemSelected even if a selected
     * item is selected, and always ignore default selection.
     */
    public class MenuSpinner extends Spinner {
        private OnItemSelectedListener listener;

        public MenuSpinner(Context context) {
            super(context);
        }

        @Override
        public void setSelection(int position) {
            super.setSelection(position);

            if (listener != null) {
                listener.onItemSelected(null, this, position, 0);
            }
        }

        @Override
        public void setOnItemSelectedListener(OnItemSelectedListener listener) {
            this.listener = listener;
        }
    }

    /**
     * Extension of ArrayAdapter. The only difference is that it hides the
     * selected text that's shown inside spinner.
     * @param <T>
     */
    private static class HideSelectedAdapter<T> extends ArrayAdapter {

        public HideSelectedAdapter(Context context, int resource, T[] objects) {
            super(context, resource, objects);
        }

        public View getView (int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            v.setVisibility(View.GONE);
            return v;
        }
    }


    /**
     * A class to hold parsed option properties.
     */
    private static class Options {
        public boolean location = true;
        public boolean hidden = false;
        public boolean clearcache = false;
        public boolean clearsessioncache = false;
        public boolean zoom = true;
        public boolean hardwareback = true;

        public Toolbar toolbar;
        public Title title;
        public BrowserButton backButton;
        public BrowserButton forwardButton;
        public BrowserButton closeButton;
        public BrowserMenu menu;
        public BrowserButton[] customButtons;
        public boolean backButtonCanClose;
        public boolean disableAnimation;
        public boolean fullscreen;
        public BrowserProgress browserProgress;
    }

    private static class Event {
        public String event;
    }

    private static class EventLabel extends Event {
        public String label;

        public String toString() {
            return label;
        }
    }

    private static class BrowserButton extends Event {
        public String image;
        public String wwwImage;
        public String imagePressed;
        public String wwwImagePressed;
        public double wwwImageDensity = 1;
        public String align = ALIGN_LEFT;
    }

    private static class BrowserMenu extends BrowserButton {
        public EventLabel[] items;
    }
    
    private static class BrowserProgress {
        public boolean showProgress;
        public String progressBgColor;
        public String progressColor;
    }

    private static class Toolbar {
        public int height = TOOLBAR_DEF_HEIGHT;
        public String color;
        public String image;
        public String wwwImage;
        public double wwwImageDensity = 1;
    }

    private static class Title {
        public String color;
        public String staticText;
        public boolean showPageTitle;
    }

    private File createImageFile() throws IOException{
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

}
