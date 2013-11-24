package se.vidstige.gocamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements SensorEventListener {

    private Camera _camera;
    private CameraPreview _preview;
    private GoBanOverlay _gobanOverlay;
    private Sensor _gravitySensor;
    private SensorManager _sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _preview = (CameraPreview) findViewById(R.id.camera_preview);

        _gobanOverlay = (GoBanOverlay) findViewById(R.id.goban_overlay);

        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        _gravitySensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (_gravitySensor == null) throw new RuntimeException("FU!!");
    }

    @Override
    protected void onResume()
    {
        _camera = getCameraInstance();
        _preview.setCamera(_camera);

        if (!_sensorManager.registerListener(this, _gravitySensor, SensorManager.SENSOR_DELAY_NORMAL))
        {
            throw new RuntimeException("error registering");
        }

        super.onResume();
    }

    @Override
    protected void onPause()
    {
        _sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        _gobanOverlay.setGravity(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
