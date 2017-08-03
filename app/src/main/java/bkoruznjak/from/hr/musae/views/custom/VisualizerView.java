package bkoruznjak.from.hr.musae.views.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by bkoruznjak on 08/05/2017.
 */

public class VisualizerView extends View {

    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Paint mOddPaint = new Paint();
    private Paint mEvenPaint = new Paint();
    private int mDivisions = 30;
    private float mCenterX;
    private float mCenterY;
    private RadialGradient magicGradient;
    private boolean centerGrowing;
    private int centerRadius = 120;

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

    public static float getXCoordinateForKnownAngleAndDistance(float knownX, float distance, double angle) {
        double angleCos = Math.cos(angle);
        float unknownX = (float) (distance * angleCos);
        unknownX += knownX;
        return unknownX;
    }

    public static float getYCoordinateForKnownAngleAndDistance(float knownY, float distance, double angle) {
        double angleSin = Math.sin(angle);
        float unknownY = (float) (distance * angleSin);
        unknownY += knownY;
        return unknownY;
    }

    private void init() {
        mBytes = null;

        mOddPaint.setStrokeWidth(30f);
        mOddPaint.setStrokeCap(Paint.Cap.ROUND);
        mOddPaint.setAntiAlias(true);

        mEvenPaint.setStrokeWidth(20f);
        mEvenPaint.setStrokeCap(Paint.Cap.ROUND);
        mEvenPaint.setAntiAlias(true);
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        int colors[] = {Color.parseColor("#A65D9E"), Color.parseColor("#E282A1"), Color.parseColor("#F6AE9B"), Color.parseColor("#F8BD9D"), Color.parseColor("#FBCA9F")};
        magicGradient = new RadialGradient(mCenterX, mCenterY, mCenterX, colors, null, Shader.TileMode.REPEAT);
        mOddPaint.setShader(magicGradient);
        mEvenPaint.setShader(magicGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCenterX == 0) {
            return;
        }

        if (mCenterY == 0) {
            return;
        }

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

            float distance = mPoints[i * 4 + 1] - mPoints[i * 4 + 3];
            float angleOffset = 360.0f / mDivisions;
            double angle = (i * angleOffset) * Math.PI / 180;
            if (i % 2 == 0) {
                canvas.drawLine(mCenterX, mCenterY, getXCoordinateForKnownAngleAndDistance(mCenterX, distance, angle), getYCoordinateForKnownAngleAndDistance(mCenterY, distance, angle), mEvenPaint);
            } else {
                canvas.drawLine(mCenterX, mCenterY, getXCoordinateForKnownAngleAndDistance(mCenterX, distance, angle), getYCoordinateForKnownAngleAndDistance(mCenterY, distance, angle), mOddPaint);
            }

        }

        if (centerRadius <= 110) {
            centerGrowing = true;
        } else if (centerRadius >= 135) {
            centerGrowing = false;
        }

        if (centerGrowing) {
            centerRadius++;
        } else {
            centerRadius--;
        }

        canvas.drawCircle(mCenterX, mCenterY, centerRadius, mEvenPaint);
    }

}

