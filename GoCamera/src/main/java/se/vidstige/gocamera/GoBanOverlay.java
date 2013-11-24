package se.vidstige.gocamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GoBanOverlay extends View {
    private final Paint paint;
    private List<float[]> _vertices = new ArrayList<float[]>();
    private List<float[]> _transformed = new ArrayList<float[]>();

    public GoBanOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.RED);

        _vertices.add(vector(-1, -1, 0));
        _vertices.add(vector( 1, -1, 0));
        _vertices.add(vector( 1, -1, 0));
        _vertices.add(vector( 1,  1, 0));

        _transformed.add(vector(-1, -1, 0));
        _transformed.add(vector( 1, -1, 0));
        _transformed.add(vector( 1, -1, 0));
        _transformed.add(vector( 1,  1, 0));
    }

    private float[] vector(float x, float y, float z)
    {
        float[] v = new float[4];
        v[0] = x;
        v[1] = y;
        v[2] = z;
        v[3] = 1;
        return v;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawLine(0,0, canvas.getWidth(), canvas.getHeight(), paint);
        //canvas.drawLine(canvas.getWidth(),0, 0, canvas.getHeight(), paint);

//        float cx = canvas.getWidth()/2;
//        float cy = canvas.getWidth()/2;
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

    public void setGravity(float[] gravity_values) {
        float up[] = { 0, 0 , 1f};
        float gravity[] = copy(gravity_values);
        normalize(gravity);

        float ax = 0, az = 0;
        float ay = (float)Math.acos(dot(up, gravity));

        float[] rotation = new float[16];
        Matrix.setRotateEulerM(rotation, 0, ax, ay, az);

        for (int i=0; i<_vertices.size(); i++)
        {
            Matrix.multiplyMV(_transformed.get(i), 0, rotation, 0, _vertices.get(i), 0);
        }

        this.invalidate();
    }
}
