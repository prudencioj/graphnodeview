/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prudencio.graphnodeview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

public class GraphView extends View {
    private static final int INVALID_POINTER_ID = -1;
    public Bitmap defaultPhoto;
    private ForceDirectedGraph graph;
    private Paint mCirclePaint;
    private float mPosX;
    private float mPosY;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;
    private int mNodeSelected;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    public GraphView(Context context) {
        this(context, null, 0);
    }

    public GraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mNodeSelected = -1;
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setTextAlign(Paint.Align.CENTER);


        graph = new ForceDirectedGraph();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        graph.forcedBasedDrawing();

        canvas.save();
        canvas.translate(mPosX, mPosY);
        canvas.scale(mScaleFactor, mScaleFactor);

        setBackgroundColor(Color.WHITE);


        for (int k = 0; k < graph.getGraph().size(); k++) {
            Node node = graph.getGraph().get(k);

            mCirclePaint.setColor(Color.GRAY);
            ArrayList<Node> adjacents = node.getAdjacentNodes();
            for (int r = 0; r < adjacents.size(); r++) {
                Node destNode = adjacents.get(r);
                canvas.drawLine((float) node.getPosX(), (float) node.getPosY(), (float) destNode.getPosX(), (float) destNode.getPosY(), mCirclePaint);
            }
        }

        for (int i = 0; i < graph.getGraph().size(); i++) {
            Node node = graph.getGraph().get(i);
            mCirclePaint.setColor(Color.RED);

            canvas.drawBitmap(defaultPhoto, (float) node.getPosX() - defaultPhoto.getWidth() / 2.0F, (float) node.getPosY() - defaultPhoto.getHeight() / 2.0F, mCirclePaint);

            mCirclePaint.setColor(Color.BLACK);
            mCirclePaint.setTextSize(20);
            canvas.drawText(node.getName(), (float) node.getPosX(), (float) node.getPosY() + 30 + 20, mCirclePaint);
        }

        canvas.restore();

        this.invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);


        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                for (int i = 0; i < graph.getGraph().size(); i++) {
                    Node node = graph.getGraph().get(i);
                    if (inCircle(x, y, mPosX + (float) node.getPosX() * mScaleFactor, mPosY + (float) node.getPosY() * mScaleFactor, 50 * mScaleFactor)) {
                        mNodeSelected = i;
                        node.setDragged(true);
                        break;
                    }
                }


                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);


                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    if (mNodeSelected > -1) {
                        Node node = graph.getGraph().get(mNodeSelected);
                        node.setPosX(node.getPosX() + dx);
                        node.setPosY(node.getPosY() + dy);
                    } else {
                        mPosX += dx;
                        mPosY += dy;
                    }

                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                if (mNodeSelected != -1) {
                    Node node = graph.getGraph().get(mNodeSelected);
                    node.setDragged(false);
                }

                mNodeSelected = -1;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                if (mNodeSelected != -1) {
                    Node node = graph.getGraph().get(mNodeSelected);
                    node.setDragged(false);
                }
                mNodeSelected = -1;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                if (mNodeSelected != -1) {
                    Node node = graph.getGraph().get(mNodeSelected);
                    node.setDragged(false);
                }

                mNodeSelected = -1;
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }


                break;
            }
        }

        return true;
    }

    private boolean inCircle(float x, float y, float circleCenterX, float circleCenterY, float circleRadius) {
        double dx = Math.pow(x - circleCenterX, 2);
        double dy = Math.pow(y - circleCenterY, 2);

        if ((dx + dy) < Math.pow(circleRadius, 2)) {
            return true;
        } else {
            return false;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }


}
