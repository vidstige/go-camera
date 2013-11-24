package se.vidstige.gocamera;

import android.content.Context;
import android.hardware.Camera;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder _holder;
    private Camera _camera;
    private String TAG = "CameraPreview";

    public CameraPreview(Context context, AttributeSet attributes) {
        super(context, attributes);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        _holder = getHolder();
        _holder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        _holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera camera)
    {
        _camera = camera;
        requestLayout();
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (_camera == null) return;

        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            _camera.setPreviewDisplay(holder);
            _camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (_holder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        if (_camera == null) return;

        try {
            _camera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            _camera.setPreviewDisplay(_holder);
            _camera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
