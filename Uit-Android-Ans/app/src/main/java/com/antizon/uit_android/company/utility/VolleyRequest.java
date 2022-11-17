package com.antizon.uit_android.company.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antizon.uit_android.generic_utils.SessionManagement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    public VolleyResponseListener mVolleyResponse;
    private static String TAG = "VolleyRequest";

    /*---------------------------------------------GET REQUEST---------------------------------------------------------*/
    public void getRequest(Context context, final String url, boolean withProgressDialog, String accessToken) {

//        if(withProgressDialog)
//            CustomProgressDialog.showProgressDialog(context);

        if (mVolleyResponse != null) {
            mVolleyResponse.requestStarted();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String urlEncoded = null;
        try {
//            urlEncoded = StaticVariables.Base_URL + URLEncoder.encode(url, "utf-8");
            urlEncoded = URLEncoder.encode(url, "utf-8");
            final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
            urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String finalUrlEncoded = urlEncoded;
        StringRequest sr = new StringRequest(Request.Method.GET, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:" + response);
                if (mVolleyResponse != null) {
                    mVolleyResponse.onResponseReceived(response, finalUrlEncoded);
                }
//                CustomProgressDialog.cancelProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mVolleyResponse != null) {
                    mVolleyResponse.requestEndedWithError(error);
                }
                Log.d(TAG, "onErrorResponse: error:" + error);
//                CustomProgressDialog.cancelProgressDialog();

            }
        }) {



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.d(TAG, "getHeaders: accessToken: " + accessToken);
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(sr);
    }

    /*---------------------------------------------POST REQUEST-------------------------------------------------------*/

    public void postRequest(Context context, final String url, final HashMap<String, String> parameters,
                            boolean withProgressDialog) {

//        if(withProgressDialog)
//            CustomProgressDialog.showProgressDialog(context);
        if (mVolleyResponse != null) {
            mVolleyResponse.requestStarted();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String urlEncoded = null;
        try {
//            urlEncoded = StaticVariables.Base_URL + URLEncoder.encode(url, "utf-8");
            urlEncoded = URLEncoder.encode(url, "utf-8");
            final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
            urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String finalUrlEncoded = urlEncoded;
        StringRequest sr = new StringRequest(Request.Method.POST, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:" + response);
                if (mVolleyResponse != null) {
                    mVolleyResponse.onResponseReceived(response, finalUrlEncoded);
                }
//                CustomProgressDialog.cancelProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mVolleyResponse != null) {
                    mVolleyResponse.requestEndedWithError(error);
                }
                Log.d(TAG, "onErrorResponse: error:" + error);
//                CustomProgressDialog.cancelProgressDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManagement sessionManagement = new SessionManagement(context);
                String accessToken = sessionManagement.getUserDetails().get("token");
//                params.put("Authorization", "Bearer " + accessToken);
                params.put("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC96b3Jyb2FwcC50ZWNoXC91aXRcL2FwaVwvbG9naW4iLCJpYXQiOjE2NDU3OTg1ODcsImV4cCI6MTY3NjkwMjU4NywibmJmIjoxNjQ1Nzk4NTg3LCJqdGkiOiJnZFJOVkJPb1lpOUdwcjJaIiwic3ViIjo1NSwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.4Faf58wc_G6xdhHaqThDTWn3trLzXpkOtpSOH0oTUuA");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<String, String>();
//                String accessToken = sessionManagement.getUserDetails().get("token");
//                Log.d(TAG, "getHeaders: accessToken: " + accessToken);
//                headers.put("Authorization", "Bearer " + accessToken);
//                return headers;
//            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }

    public void postRequest(Context context, final String url, final HashMap<String, String> parameters,
                            boolean withProgressDialog, String accessToken) {

//        if(withProgressDialog)
//            CustomProgressDialog.showProgressDialog(context);
        if (mVolleyResponse != null) {
            mVolleyResponse.requestStarted();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String urlEncoded = null;
        try {
//            urlEncoded = StaticVariables.Base_URL + URLEncoder.encode(url, "utf-8");
            urlEncoded = URLEncoder.encode(url, "utf-8");
            final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
            urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String finalUrlEncoded = urlEncoded;
        StringRequest sr = new StringRequest(Request.Method.POST, urlEncoded, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse:" + response);
                if (mVolleyResponse != null) {
                    mVolleyResponse.onResponseReceived(response, finalUrlEncoded);
                }
//                CustomProgressDialog.cancelProgressDialog();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mVolleyResponse != null) {
                    mVolleyResponse.requestEndedWithError(error);
                }
                Log.d(TAG, "onErrorResponse: error:" + error);
//                CustomProgressDialog.cancelProgressDialog();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.d(TAG, "getHeaders: accessToken: " + accessToken);
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        sr.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(sr);
    }


    public interface VolleyResponseListener {
        void requestStarted();

        void onResponseReceived(String response, String urlCalled);

        void requestEndedWithError(VolleyError error);

    }
}
