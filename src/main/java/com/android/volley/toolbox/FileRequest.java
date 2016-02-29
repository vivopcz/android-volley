package com.android.volley.toolbox;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.File;

/**
 * Created by Administrator on 2016/2/27.
 */
public class FileRequest extends Request<File> {
    /** Socket timeout in milliseconds for image requests */
    private static final int IMAGE_TIMEOUT_MS = 1000;

    /** Default number of retries for image requests */
    private static final int IMAGE_MAX_RETRIES = 2;

    /** Default backoff multiplier for image requests */
    private static final float IMAGE_BACKOFF_MULT = 2f;

    private final Response.Listener<File> mListener;
    private  File file;

    public FileRequest(String url, Response.Listener<File> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(
                new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        mListener = listener;
    }

    public FileRequest(String url, File file, Response.Listener<File> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        setRetryPolicy(
                new DefaultRetryPolicy(IMAGE_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        mListener = listener;
        this.file = file;
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected Response<File> parseNetworkResponse(NetworkResponse response) {
        if (response.file == null) {
            return Response.error(new ParseError(response));
        } else {
            return Response.success(response.file, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(File response) {
        if (mListener != null ){
            mListener.onResponse(response);
        }
    }

    @Override
    public Priority getPriority() {
        return Priority.LOW;
    }
}
