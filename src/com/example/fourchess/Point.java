package com.example.fourchess;

public class Point {
	private int i, j;// 棋盘点下标
	private float x, y;// 棋盘点坐标
	private float dist = 30;
	private float left, top, right, bottom;// 点击区域

	private Chess chess;

	public Point(int i, int j) {
		this.i = i;
		this.j = j;
	}

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

	public Chess getChess() {
		return chess;
	}

	public void setChess(Chess chess) {
		this.chess = chess;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public boolean isHasChess() {
		return chess != null;
	}

	public boolean hasnotChess() {
		return chess == null;
	}

	public boolean isWhiteChess() {
		if (chess != null) {
			return chess.isWhite();
		} else {
			return false;
		}
	}

	public boolean isBlackChess() {
		if (chess != null) {
			return !chess.isWhite();
		} else {
			return false;
		}
	}

	private void setClickArea() {
		left = x - dist;
		top = y - dist;
		right = x + dist;
		bottom = y + dist;
	}

	/**
	 * 是否点击棋盘点
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickPoint(float x, float y) {
		return x > left && x < right && y > top && y < bottom;
	}

}
