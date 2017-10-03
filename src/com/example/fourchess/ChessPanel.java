package com.example.fourchess;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class ChessPanel extends View {

	private int panelMargin;// 棋盘边缘
	private int panelWidth; // 棋盘宽度
	private int cellWidth; // 棋格宽度
	private Chess[] chesss; // 棋子集合
	private Point[][] points;// 棋盘点坐标

	private Paint paint;

	private boolean isLoad;

	private int chessType;// 棋子类型 0未执棋、1执白棋、2执黑棋
	private int handChessType;// 执棋类型 0未执棋、1执白棋、2执黑棋
	private int idx_chess_selected = -1;// 当前选中的棋子下标
	private int whiteChessNum, blackChessNum;// 白棋子、黑棋子的剩余数量

	public interface OnChessPanelListener {
		void onHandTypeChange(int handChessType);

		void onGameOver(int handChessType);

	}

	private OnChessPanelListener chessPanelListener;

	public void setOnChessPanelListener(OnChessPanelListener chessPanelListener) {
		this.chessPanelListener = chessPanelListener;
	}

	public ChessPanel(Context context) {
		super(context);

	}

	public ChessPanel(Context context, AttributeSet attrs) {
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
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBroad(canvas);
		drawChess(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 获得正方形的边长
		int length = Math.min(getMeasuredHeight(), getMeasuredWidth());
		panelWidth = length;
		cellWidth = (panelWidth - panelMargin * 2) / 3;
		if (!isLoad) {
			initChessIndex();
		}
		isLoad = true;

		setMeasuredDimension(length, length);
	}

	private void initChessIndex() {
		chessType = 0;
		handChessType = 0;
		whiteChessNum = 4;
		blackChessNum = 4;
		if (chesss == null) {
			chesss = new Chess[8];// 8个棋子
		}
		for (int i = 0; i < chesss.length; i++) {
			if (i < 4) {
				Chess chess = new Chess(this, false);// 黑棋
				chess.setX(panelMargin + cellWidth * i - chess.getWidth() / 2);
				chess.setY(panelMargin - chess.getWidth() / 2);
				chesss[i] = chess;
			} else {
				Chess chess = new Chess(this, true);// 白棋
				chess.setX(panelMargin + cellWidth * (i - 4) - chess.getWidth()
						/ 2);
				chess.setY(panelMargin + cellWidth * 3 - chess.getWidth() / 2);
				chesss[i] = chess;
			}
		}
		if (points == null) {
			points = new Point[4][4];// 16个点
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				Point point = new Point(i, j);
				point.setChess(null);
				if (i == 0) {
					point.setX(panelMargin + cellWidth * j);
					point.setY(panelMargin);
					chesss[j].setIdx_point(i, j);
					point.setChess(chesss[j]);
				} else if (i == 1) {
					point.setX(panelMargin + cellWidth * j);
					point.setY(panelMargin + cellWidth);
				} else if (i == 2) {
					point.setX(panelMargin + cellWidth * j);
					point.setY(panelMargin + cellWidth * 2);
				} else {
					point.setX(panelMargin + cellWidth * j);
					point.setY(panelMargin + cellWidth * 3);
					chesss[j + 4].setIdx_point(i, j);
					point.setChess(chesss[j + 4]);
				}
				points[i][j] = point;
			}
		}
	}

	private void drawBroad(Canvas canvas) {
		paint.setColor(Color.WHITE); // 给画笔设置颜色
		paint.setStyle(Paint.Style.STROKE); // 设置画笔样式，这里使用描边
		paint.setStrokeWidth(5);

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
		for (int i = 0; i < chesss.length; i++) {
			Chess chess = chesss[i];
			if (!chess.isEatoff()) {
				chess.drawChess(canvas, paint);
			}
		}
	}

	/**
	 * 检测吃掉棋子
	 * 
	 * @return
	 */
	private List<Chess> checkEatoff(Chess chess) {
		int i = chess.getI_point();
		int j = chess.getJ_point();
		List<Chess> list = new ArrayList<Chess>();

		/**
		 * 横向检测
		 */
		// 0112/0221 0代表没有棋子，1代表白棋子，2代表黑棋子
		if (points[i][0].hasnotChess()
				&& ((chessType == 1 && points[i][1].isWhiteChess()
						&& points[i][2].isWhiteChess() && points[i][3]
							.isBlackChess()) || (chessType == 2
						&& points[i][1].isBlackChess()
						&& points[i][2].isBlackChess() && points[i][3]
							.isWhiteChess()))) {
			list.add(points[i][3].getChess());
		}
		// 0211/0122
		else if (points[i][0].hasnotChess()
				&& ((chessType == 1 && points[i][1].isBlackChess()
						&& points[i][2].isWhiteChess() && points[i][3]
							.isWhiteChess()) || (chessType == 2
						&& points[i][1].isWhiteChess()
						&& points[i][2].isBlackChess() && points[i][3]
							.isBlackChess()))) {
			list.add(points[i][1].getChess());
		}
		// 2110/1220
		else if (((chessType == 1 && points[i][0].isBlackChess()
				&& points[i][1].isWhiteChess() && points[i][2].isWhiteChess()) || (chessType == 2
				&& points[i][0].isWhiteChess() && points[i][1].isBlackChess() && points[i][2]
					.isBlackChess())) && points[i][3].hasnotChess()) {
			list.add(points[i][0].getChess());
		}
		// 1120/2210
		else if (((chessType == 1 && points[i][0].isWhiteChess()
				&& points[i][1].isWhiteChess() && points[i][2].isBlackChess()) || (chessType == 2
				&& points[i][0].isBlackChess() && points[i][1].isBlackChess() && points[i][2]
					.isWhiteChess())) && points[i][3].hasnotChess()) {
			list.add(points[i][2].getChess());
		}

		/**
		 * 纵向检测
		 */
		// 0112/0221 0代表没有棋子，1代表白棋子，2代表黑棋子
		if (points[0][j].hasnotChess()
				&& ((chessType == 1 && points[1][j].isWhiteChess()
						&& points[2][j].isWhiteChess() && points[3][j]
							.isBlackChess()) || (chessType == 2
						&& points[1][j].isBlackChess()
						&& points[2][j].isBlackChess() && points[3][j]
							.isWhiteChess()))) {
			list.add(points[3][j].getChess());
		}
		// 0211/0122
		else if (points[0][j].hasnotChess()
				&& ((chessType == 1 && points[1][j].isBlackChess()
						&& points[2][j].isWhiteChess() && points[3][j]
							.isWhiteChess()) || (chessType == 2
						&& points[1][j].isWhiteChess()
						&& points[2][j].isBlackChess() && points[3][j]
							.isBlackChess()))) {
			list.add(points[1][j].getChess());
		}
		// 2110/1220
		else if (((chessType == 1 && points[0][j].isBlackChess()
				&& points[1][j].isWhiteChess() && points[2][j].isWhiteChess()) || (chessType == 2
				&& points[0][j].isWhiteChess() && points[1][j].isBlackChess() && points[2][j]
					.isBlackChess())) && points[3][j].hasnotChess()) {
			list.add(points[0][j].getChess());
		}
		// 1120/2210
		else if (((chessType == 1 && points[0][j].isWhiteChess()
				&& points[1][j].isWhiteChess() && points[2][j].isBlackChess()) || (chessType == 2
				&& points[0][j].isBlackChess() && points[1][j].isBlackChess() && points[2][j]
					.isWhiteChess())) && points[3][j].hasnotChess()) {
			list.add(points[2][j].getChess());
		}

		return list;
	}

	/**
	 * 检测结果
	 */
	private boolean checkOver() {
		if (whiteChessNum <= 1) {
			chessPanelListener.onGameOver(2);
			return true;
		} else if (blackChessNum <= 1) {
			chessPanelListener.onGameOver(1);
			return true;
		}
		return false;
	}

	/**
	 * 重新开始游戏
	 */
	public void restart() {
		initChessIndex();
		invalidate();
		chessPanelListener.onHandTypeChange(0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		boolean isClickChess = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (checkOver()) {
				return true;
			}
			for (int i = 0; i < chesss.length; i++) {
				if (chesss[i].isClickChess(x, y)) {
					isClickChess = true;
					if (handChessType == 2 && chesss[i].isWhite()) {
						Toast.makeText(this.getContext(), "轮到黑棋移动",
								Toast.LENGTH_LONG).show();
						return true;
					} else if (handChessType == 1 && !chesss[i].isWhite()) {
						Toast.makeText(this.getContext(), "轮到白棋移动",
								Toast.LENGTH_LONG).show();
						return true;
					}
					if (idx_chess_selected >= 0) {
						chesss[idx_chess_selected].setSelected(false);
					}
					idx_chess_selected = i;
					chesss[idx_chess_selected].setSelected(true);

					if (chesss[idx_chess_selected].isWhite()) {
						chessType = 1;
					} else {
						chessType = 2;
					}
				}
			}
			if (!isClickChess) {
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						if (points[i][j].isClickPoint(x, y)) {
							if (idx_chess_selected >= 0) {
								float _x = points[i][j].getX()
										- points[chesss[idx_chess_selected]
												.getI_point()][chesss[idx_chess_selected]
												.getJ_point()].getX();
								float _y = points[i][j].getY()
										- points[chesss[idx_chess_selected]
												.getI_point()][chesss[idx_chess_selected]
												.getJ_point()].getY();
								if ((Math.abs(_x) <= cellWidth && _y == 0)
										|| (_x == 0 || Math.abs(_y) <= cellWidth)) {
									chesss[idx_chess_selected]
											.setX(points[i][j].getX()
													- chesss[idx_chess_selected]
															.getWidth() / 2);
									chesss[idx_chess_selected]
											.setY(points[i][j].getY()
													- chesss[idx_chess_selected]
															.getWidth() / 2);
									chesss[idx_chess_selected]
											.setSelected(false);
									points[i][j]
											.setChess(chesss[idx_chess_selected]);
									points[chesss[idx_chess_selected]
											.getI_point()][chesss[idx_chess_selected]
											.getJ_point()].setChess(null);
									chesss[idx_chess_selected].setIdx_point(i,
											j);

									List<Chess> list = checkEatoff(chesss[idx_chess_selected]);
									for (Chess chess : list) {
										chess.setEatoff(true);
										points[chess.getI_point()][chess
												.getJ_point()].setChess(null);
										if (chess.isWhite()) {
											whiteChessNum--;
										} else {
											blackChessNum--;
										}
									}

									if (chesss[idx_chess_selected].isWhite()) {
										handChessType = 2;
									} else {
										handChessType = 1;
									}
									chessPanelListener
											.onHandTypeChange(handChessType);
									idx_chess_selected = -1;

								}
							}

						}
					}
				}
			}

			invalidate();

			if (checkOver()) {
				return true;
			}
		}

		return true;
	}

}
