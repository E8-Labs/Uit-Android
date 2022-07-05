package com.antizon.uit_android.company.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.antizon.uit_android.R;
import com.antizon.uit_android.generic.uit_interface.JsonPlaceHolderAPI;
import com.antizon.uit_android.generic_utils.AppConstants;
import com.antizon.uit_android.generic_utils.ImagePickerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public abstract class BaseActivity extends AppCompatActivity implements VolleyRequest.VolleyResponseListener {

    private static final String TAG = BaseActivity.class.getSimpleName();
    Gson gson;
    VolleyRequest volleyRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void hideWindowsKeyboard() {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void hideSoftKeyboard(Context context, EditText editText) {

        editText.clearFocus();
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void hideSoftKeyboard(Context context, AppCompatEditText editText) {

        editText.clearFocus();
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public void showDialog_Without_Listener(final String title, final String message, final String buttonText) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            AlertDialog.Builder adbb = new AlertDialog.Builder(BaseActivity.this);
            adbb.setIcon(R.drawable.app_icon);
            adbb.setTitle(title);
            if (message != null)
                adbb.setMessage(message);
            adbb.setPositiveButton(buttonText, null);
            adbb.show();
        });
    }

    public void showDialog_With_Listener(final String title, final String message, final String positiveBtnText, final String negativeBtnText, final DialogInterface.OnClickListener listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {

                AlertDialog.Builder adb = new AlertDialog.Builder(BaseActivity.this);
                adb.setIcon(R.drawable.app_icon);
                adb.setTitle(title);
                adb.setMessage(message);
                if (positiveBtnText != null && !positiveBtnText.equals("")) {
                    adb.setPositiveButton(positiveBtnText, listener);
                }
                if (negativeBtnText != null && !negativeBtnText.equals("")) {
                    adb.setNegativeButton(negativeBtnText, listener);
                }
                adb.show();
            }
        });
    }

    public void stopKeyBoardPopUp() {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void extractToFullScreen() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        Log.d(TAG, "photoSelection: getEncoded64ImageStringFromBitmap: ");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    public void sendServerRequestGET(String data, String accessToken) {
        // This is to initialze gson object.
        // We will use this gson to get Json object in child classes

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        volleyRequest = new VolleyRequest();
        volleyRequest.mVolleyResponse = BaseActivity.this;
        data = data.replaceAll(" ", "+");
        volleyRequest.getRequest(BaseActivity.this, data, false, accessToken);
        Log.d(TAG, "sendServerRequest: complete url:" + data);
    }


    public void sendServerRequestPOST(String url, HashMap<String, String> hashMapParams) {
        // This is to initialze gson object.
        // We will use this gson to get Json object in child classes

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        volleyRequest = new VolleyRequest();
        volleyRequest.mVolleyResponse = BaseActivity.this;
        url = url.replaceAll(" ", "+");
        volleyRequest.postRequest(BaseActivity.this, url, hashMapParams, false);
        Log.d(TAG, "sendServerRequest: complete url:" + url);
    }

    public void sendServerRequestPOST(String url, HashMap<String, String> hashMapParams, String accessToken) {
        // This is to initialze gson object.
        // We will use this gson to get Json object in child classes

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        volleyRequest = new VolleyRequest();
        volleyRequest.mVolleyResponse = BaseActivity.this;
        url = url.replaceAll(" ", "+");
        volleyRequest.postRequest(BaseActivity.this, url, hashMapParams, false, accessToken);
        Log.d(TAG, "sendServerRequest: complete url:" + url);
    }

    @Override
    public void requestStarted() {

    }

    @Override
    public void onResponseReceived(String response, String urlCalled) {

    }

    @Override
    public void requestEndedWithError(VolleyError error) {

    }


    public void pickImage(Activity activity) {

        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions(activity);
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog(activity);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showImagePickerOptions(Context context) {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent(context);
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent(context);
            }
        });
    }

    private void launchCameraIntent(Context context) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityIfNeeded(intent, AppConstants.REQUEST_CODE_For_IMAGE);
    }

    private void launchGalleryIntent(Context context) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityIfNeeded(intent, AppConstants.REQUEST_CODE_For_IMAGE);
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityIfNeeded(intent, 101);
    }

    public void loadProfile(Context context, String url, ImageView imageView) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .circleCrop())
                .into(imageView);
        imageView.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }


    public static JsonPlaceHolderAPI getApi(){

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        //Set your desired log level
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder mOkHttpClient=new OkHttpClient.Builder();
        mOkHttpClient.connectTimeout(2, TimeUnit.MINUTES).writeTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES);
        Retrofit.Builder mBuilder=new Retrofit.Builder().baseUrl(AppConstants.BASEURL).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit mRetrofit=mBuilder.client(mOkHttpClient.addInterceptor(httpLoggingInterceptor).build()).build();
        //Create a very simple rest adapter which points the github api endpoints
        JsonPlaceHolderAPI mClient=mRetrofit.create(JsonPlaceHolderAPI.class);
        return mClient;
    }

    public void showImageUsingGlide(Activity activity, ImageView imageView, String imageUrl) {
        Glide.with(activity)
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.uit_app_icon_for_background))
                .into(imageView);
    }

    public void showImageUsingGlide(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().circleCrop().placeholder(R.drawable.uit_app_icon_for_background))
                .into(imageView);
    }

}