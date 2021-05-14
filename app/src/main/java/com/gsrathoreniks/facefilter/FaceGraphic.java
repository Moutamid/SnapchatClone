package com.gsrathoreniks.facefilter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.android.gms.vision.face.Face;
import com.gsrathoreniks.facefilter.camera.GraphicOverlay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Graphic instance for rendering face position, orientation, and landmarks within an associated
 * graphic overlay view.
 */
class FaceGraphic extends GraphicOverlay.Graphic {
    private static final String TAG = "FaceGraphicClass";

    private static final float FACE_POSITION_RADIUS = 10.0f;
    private static final float ID_TEXT_SIZE = 40.0f;
    private static final float ID_Y_OFFSET = 50.0f;
    private static final float ID_X_OFFSET = -50.0f;
    private static final float GENERIC_POS_OFFSET = 20.0f;
    private static final float GENERIC_NEG_OFFSET = -20.0f;
    private static final float BOX_STROKE_WIDTH = 5.0f;

    private static final int MASK[] = {
            R.drawable.transparent,
            R.drawable.hat,
            R.drawable.hat2,
            R.drawable.mask,
            R.drawable.mask2,
            R.drawable.mask3,
            R.drawable.glasses2,
            R.drawable.glasses3,
            R.drawable.glasses4,
            R.drawable.glasses5,
            R.drawable.cat2,
            R.drawable.dog,
            R.drawable.snap
    };

//    private static final int MASK[] = {
//            R.drawable.transparent,
//            R.drawable.hair,
//            R.drawable.op,
//            R.drawable.snap,
//            R.drawable.glasses2,
//            R.drawable.glasses3,
//            R.drawable.glasses4,
//            R.drawable.glasses5,
//            R.drawable.mask,
//            R.drawable.mask2,
//            R.drawable.mask3,
//            R.drawable.dog,
//            R.drawable.cat2
//    };

    /*
    private static final int COLOR_CHOICES[] = {
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.RED,
            Color.WHITE,
            Color.YELLOW
    };
    private static int mCurrentColorIndex = 0;
    private Paint mFacePositionPaint;
    private Paint mIdPaint;
    private Paint mBoxPaint;*/

    private volatile Face mFace;
    private int mFaceId;
    private float mFaceHappiness;
    private Bitmap bitmap;
    private Bitmap op;

//    FaceGraphic(GraphicOverlay overlay, String path2, String name2) {
//        super(overlay);
//
//        File f = new File(path2, name2);
//        try {
//            Bitmap bitmap3 = BitmapFactory.decodeStream(new FileInputStream(f));
//            op = bitmap3;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, "FaceGraphic: Constructor triggered!");
//    }

    //    FaceGraphic(GraphicOverlay overlay, Bitmap bitmap1) {
//        super(overlay);
//        /*
//        mCurrentColorIndex = (mCurrentColorIndex + 1) % COLOR_CHOICES.length;
//        final int selectedColor = COLOR_CHOICES[mCurrentColorIndex];
//
//        mFacePositionPaint = new Paint();
//        mFacePositionPaint.setColor(selectedColor);
//
//        mIdPaint = new Paint();
//        mIdPaint.setColor(selectedColor);
//        mIdPaint.setTextSize(ID_TEXT_SIZE);
//
//        mBoxPaint = new Paint();
//        mBoxPaint.setColor(selectedColor);
//        mBoxPaint.setStyle(Paint.Style.STROKE);
//        mBoxPaint.setStrokeWidth(BOX_STROKE_WIDTH);
//        */
//
////        if (bitmap == op)
////            return;
//
//
//
//        op = bitmap1;
//
//        Log.d(TAG, "FaceGraphic: Constructor triggered!");
//    }
//    FaceGraphic(GraphicOverlay overlay,int c) {
//        super(overlay);
//        bitmap = BitmapFactory.decodeResource(getOverlay().getContext().getResources(),MASK[c]);
//        op = bitmap;
//    }
    FaceGraphic(GraphicOverlay overlay, int currentImageByteValue) {
        super(overlay);
        bitmap = BitmapFactory.decodeResource(getOverlay().getContext().getResources(), currentImageByteValue);
        op = bitmap;
    }

    void setId(int id) {
        mFaceId = id;
        Log.d(TAG, "setId: triggered");
    }

    Bitmap getBitmap(Face face, Bitmap bmp){
        return Bitmap.createScaledBitmap(op, (int) scaleX(face.getWidth()),
                (int) scaleY(((bitmap.getHeight() * face.getWidth()) / bitmap.getWidth())), false);
    }

    /**
     * Updates the face instance from the detection of the most recent frame.  Invalidates the
     * relevant portions of the overlay to trigger a redraw.
     */
    void updateFace(Face face, int currentImageByteValue1) {
        mFace = face;
        bitmap = BitmapFactory.decodeResource(getOverlay().getContext().getResources(), currentImageByteValue1);
        op = bitmap;
        op = Bitmap.createScaledBitmap(op, (int) scaleX(face.getWidth()),
                (int) scaleY(((bitmap.getHeight() * face.getWidth()) / bitmap.getWidth())), false);
        postInvalidate();
    }
//    void updateFace(Face face, String path, String name) {
//        Log.d(TAG, "updateFace: triggered");
//
//        try {
//            File f = new File(path, name);
//            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
//
//            mFace = face;
//            op = bitmap;
//            op = Bitmap.createScaledBitmap(op, (int) scaleX(face.getWidth()),
//                    (int) scaleY(((bitmap.getHeight() * face.getWidth()) / bitmap.getWidth())), false);
//
//            postInvalidate();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
//    void updateFace(Face face, Bitmap bitmap) {
////        if (bitmap == op)
////            return;
//
//        Log.d(TAG, "updateFace: triggered");
//
//        mFace = face;
//        op = bitmap;
//        op = Bitmap.createScaledBitmap(op, (int) scaleX(face.getWidth()),
//                (int) scaleY(((bitmap.getHeight() * face.getWidth()) / bitmap.getWidth())), false);
//        postInvalidate();
//
//    }
//    void updateFace(Face face, int c) {
//            mFace = face;
//            bitmap = BitmapFactory.decodeResource(getOverlay().getContext().getResources(), MASK[c]);
//            op = bitmap;
//            op = Bitmap.createScaledBitmap(op, (int) scaleX(face.getWidth()),
//                    (int) scaleY(((bitmap.getHeight() * face.getWidth()) / bitmap.getWidth())), false);
//            postInvalidate();
//        }

    /**
     * Draws the face annotations for position on the supplied canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (op == null)
            return;

        Face face = mFace;
        if (face == null) return;
        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);
        /*
        canvas.drawCircle(x, y, FACE_POSITION_RADIUS, mFacePositionPaint);
        canvas.drawText("id: " + mFaceId, x + ID_X_OFFSET, y + ID_Y_OFFSET, mIdPaint);
        canvas.drawText("happiness: " + String.format("%.2f", face.getIsSmilingProbability()), x - ID_X_OFFSET, y - ID_Y_OFFSET, mIdPaint);
        canvas.drawText("right eye: " + String.format("%.2f", face.getIsRightEyeOpenProbability()), x + ID_X_OFFSET * 2, y + ID_Y_OFFSET * 2, mIdPaint);
        canvas.drawText("left eye: " + String.format("%.2f", face.getIsLeftEyeOpenProbability()), x - ID_X_OFFSET*2, y - ID_Y_OFFSET*2, mIdPaint);
        */
        float xOffset = scaleX(face.getWidth() / 2.0f);
        float yOffset = scaleY(face.getHeight() / 2.0f);
        float left = x - xOffset;
        float top = y - yOffset;
        //float right = x + xOffset;
        //float bottom = y + yOffset;
        //canvas.drawRect(left, top, right, bottom, mBoxPaint);
        canvas.drawBitmap(op, left, top, new Paint());

        Log.d(TAG, "draw: triggered");
    }
    /*
    private float getNoseAndMouthDistance(PointF nose, PointF mouth) {
        return (float) Math.hypot(mouth.x - nose.x, mouth.y - nose.y);
    }*/
}
