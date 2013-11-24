package se.vidstige.gocamera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

    private Camera _camera;
    private CameraPreview _preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _preview = (CameraPreview) findViewById(R.id.camera_preview);
    }

    @Override
    protected void onResume()
    {
        _camera = getCameraInstance();
        _preview.setCamera(_camera);

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        releaseCameraAndPreview();
        super.onPause();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void releaseCameraAndPreview() {
        _preview.setCamera(null);

        if (_camera != null) {
            _camera.release();
            _camera = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //switch (item.getItemId()) {
        //    case R.id.action_settings:
        //        return true;
        //}
        return super.onOptionsItemSelected(item);
    }
}
