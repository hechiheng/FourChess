package com.example.fourchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ChessPanel1 extends View {

	private int panelMargin;// 棋盘边缘
	private int panelWidth; // 棋盘宽度
	private int cellWidth; // 棋格宽度
	private int chessRadius; // 棋子半径

	private Paint paint;

	public ChessPanel1(Context context) {
		super(context);

	}

	public ChessPanel1(Context context, AttributeSet attrs) {
		super(context, attrs);

		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		float density = dm.density;// 屏幕密度（像素比例：0.75, 1.0, 1.5, 2.0）
		int densityDPI = dm.densityDpi;// 屏幕密度（每寸像素：120, 160, 240, 320）
		// Log.d("aaa", density + "," + densityDPI);

		panelMargin = (int) density * 20;

		paint = new Paint();
		paint.setAntiAlias(true); // 设置画笔是否使用抗锯齿
		paint.setDither(true); // 设置画笔是否防抖动
		paint.setStrokeWidth(10);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBroad(canvas);
		// drawChess(canvas);
		drawChessByBitmap(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获得正方形的边长
		int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
		setMeasuredDimension(length, length);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		panelWidth = w;
		cellWidth = (panelWidth - panelMargin * 2) / 3;
		chessRadius = cellWidth / 6;
	}

	private void drawBroad(Canvas canvas) {
		paint.setColor(Color.WHITE); // 给画笔设置颜色
		paint.setStyle(Paint.Style.STROKE); // 设置画笔样式，这里使用描边

		canvas.drawLine(panelMargin, panelMargin, panelMargin + cellWidth * 3,
				panelMargin, paint);// 第一行
		canvas.drawLine(panelMargin, panelMargin + cellWidth, panelMargin
				+ cellWidth * 3, panelMargin + cellWidth, paint);// 第二行
		canvas.drawLine(panelMargin, panelMargin + cellWidth * 2, panelMargin
				+ cellWidth * 3, panelMargin + cellWidth * 2, paint);// 第三行
		canvas.drawLine(panelMargin, panelMargin + cellWidth * 3, panelMargin
				+ cellWidth * 3, panelMargin + cellWidth * 3, paint);// 第四行
		canvas.drawLine(panelMargin, panelMargin, panelMargin, panelMargin
				+ cellWidth * 3, paint);// 第一列
		canvas.drawLine(panelMargin + cellWidth, panelMargin, panelMargin
				+ cellWidth, panelMargin + cellWidth * 3, paint);// 第二列
		canvas.drawLine(panelMargin + cellWidth * 2, panelMargin, panelMargin
				+ cellWidth * 2, panelMargin + cellWidth * 3, paint);// 第三列
		canvas.drawLine(panelMargin + cellWidth * 3, panelMargin, panelMargin
				+ cellWidth * 3, panelMargin + cellWidth * 3, paint);// 第四列
	}

	private void drawChess(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		// 黑棋
		paint.setColor(Color.BLACK);
		canvas.drawCircle(panelMargin, panelMargin, chessRadius, paint);
		canvas.drawCircle(panelMargin + cellWidth, panelMargin, chessRadius,
				paint);
		canvas.drawCircle(panelMargin + cellWidth * 2, panelMargin,
				chessRadius, paint);
		canvas.drawCircle(panelMargin + cellWidth * 3, panelMargin,
				chessRadius, paint);

		// 白棋
		paint.setColor(Color.GRAY);
		canvas.drawCircle(panelMargin, panelMargin + cellWidth * 3,
				chessRadius, paint);
		canvas.drawCircle(panelMargin + cellWidth, panelMargin + cellWidth * 3,
				chessRadius, paint);
		canvas.drawCircle(panelMargin + cellWidth * 2, panelMargin + cellWidth
				* 3, chessRadius, paint);
		canvas.drawCircle(panelMargin + cellWidth * 3, panelMargin + cellWidth
				* 3, chessRadius, paint);

	}

	private void drawChessByBitmap(Canvas canvas) {
		Matrix matrix = new Matrix();
		matrix.postScale(0.8f, 0.8f);

		// 黑棋
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.black);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		canvas.drawBitmap(bitmap, panelMargin - bitmap.getWidth() / 2,
				panelMargin - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmap, panelMargin + cellWidth - bitmap.getWidth()
				/ 2, panelMargin - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmap,
				panelMargin + cellWidth * 2 - bitmap.getWidth() / 2,
				panelMargin - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmap,
				panelMargin + cellWidth * 3 - bitmap.getWidth() / 2,
				panelMargin - bitmap.getWidth() / 2, paint);

		// 白棋
		Bitmap bitmapWhite = BitmapFactory.decodeResource(getResources(),
				R.drawable.white);
		bitmapWhite = Bitmap.createBitmap(bitmapWhite, 0, 0,
				bitmapWhite.getWidth(), bitmapWhite.getHeight(), matrix, true);
		canvas.drawBitmap(bitmapWhite, panelMargin - bitmap.getWidth() / 2,
				panelMargin + cellWidth * 3 - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmapWhite,
				panelMargin + cellWidth - bitmap.getWidth() / 2, panelMargin
						+ cellWidth * 3 - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmapWhite,
				panelMargin + cellWidth * 2 - bitmap.getWidth() / 2,
				panelMargin + cellWidth * 3 - bitmap.getWidth() / 2, paint);
		canvas.drawBitmap(bitmapWhite,
				panelMargin + cellWidth * 3 - bitmap.getWidth() / 2,
				panelMargin + cellWidth * 3 - bitmap.getWidth() / 2, paint);
	}
}
