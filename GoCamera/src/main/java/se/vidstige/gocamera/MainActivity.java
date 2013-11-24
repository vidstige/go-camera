package se.vidstige.gocamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity implements SensorEventListener {

    private Camera _camera;
    private CameraPreview _preview;
    private Sensor _gravitySensor;
    private SensorManager _sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _preview = (CameraPreview) findViewById(R.id.camera_preview);

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

    private float dot(float[] a, float[] b)
    {
        return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
    }

    private float[] copy(float[] a)
    {
        float b[] = new float[3];
        b[0] = a[0];
        b[1] = a[1];
        b[2] = a[2];
        return b;
    }

    private void normalize(float[] a)
    {
        double l = Math.sqrt(dot(a, a));
        a[0] /= l;
        a[1] /= l;
        a[2] /= l;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float up[] = { 0, 0 , 1f};
        float gravity[] = copy(sensorEvent.values);
        normalize(gravity);
        Log.d("Angle", "a=" + Math.acos(dot(gravity, up)));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
