package bkoruznjak.from.hr.musae.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bkoruznjak on 08/05/2017.
 */

public class VisualizerView extends View {

    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mForePaint = new Paint();
    private int mDivisions = 15;

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBytes = null;
        mForePaint.setStrokeWidth(20f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(Color.rgb(0, 128, 255));
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {
            return;
        }
        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }
        mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < (mBytes.length - 1) / mDivisions; i++) {

            //x0 and x1
            mPoints[i * 4] = i * 4 * mDivisions;
            mPoints[i * 4 + 2] = i * 4 * mDivisions;
            byte rfk = mBytes[mDivisions * i];
            byte ifk = mBytes[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (50 * Math.log10(magnitude));

            //y0 and y1
            mPoints[i * 4 + 1] = mRect.height();
            mPoints[i * 4 + 3] = mRect.height() - (dbValue * 2 - 10);

            //todo use this callback to draw the points you need
            float distance = mPoints[i*4 +1] - mPoints[i * 4 + 3];
        }
        canvas.drawLines(mPoints, mForePaint);
    }

}