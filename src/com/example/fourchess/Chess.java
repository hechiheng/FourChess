package com.example.fourchess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class Chess {

	private boolean white;// 是否白棋子
	private Bitmap bitmap;// 棋子位图
	private float x, y;// 棋子坐标
	private float left, top, right, bottom;// 点击区域
	private boolean selected;// 是否选中
	private int i_point;// 棋盘点下标
	private int j_point;// 棋盘点下标
	private boolean eatoff;// 是否被吃掉
	private float dist = 30;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		setClickArea();
	}

	public boolean isWhite() {
		return white;
	}

	public void setWhite(boolean white) {
		this.white = white;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getI_point() {
		return i_point;
	}

	public void setI_point(int i_point) {
		this.i_point = i_point;
	}

	public int getJ_point() {
		return j_point;
	}

	public void setJ_point(int j_point) {
		this.j_point = j_point;
	}

	public void setIdx_point(int i, int j) {
		this.i_point = i;
		this.j_point = j;
	}

	public boolean isEatoff() {
		return eatoff;
	}

	public void setEatoff(boolean eatoff) {
		this.eatoff = eatoff;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public Chess(View view, boolean isWhite) {
		white = isWhite;
		if (white) {
			bitmap = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.white);
		} else {
			bitmap = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.black);
		}
		Matrix matrix = new Matrix();
		matrix.postScale(0.8f, 0.8f);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
	}

	public void drawChess(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bitmap, x, y, paint);
		if (selected) {
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(x + bitmap.getWidth() / 2, y + bitmap.getWidth()
					/ 2, 10, paint);
		}
	}

	private void setClickArea() {
		left = x - dist;
		top = y - dist;
		right = x + getWidth() + dist;
		bottom = y + getWidth() + dist;
	}

	/**
	 * 是否点击棋子
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickChess(float x, float y) {
		return x > left && x < right && y > top && y < bottom;
	}

}
