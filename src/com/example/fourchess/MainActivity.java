package com.example.fourchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.fourchess.ChessPanel.OnChessPanelListener;

public class MainActivity extends Activity {

	private TextView blackText, whiteText;
	private ChessPanel chessPanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		blackText = (TextView) findViewById(R.id.blackText);
		whiteText = (TextView) findViewById(R.id.whiteText);
		blackText.setVisibility(View.INVISIBLE);
		whiteText.setVisibility(View.INVISIBLE);

		chessPanel = (ChessPanel) findViewById(R.id.chessPanel);
		chessPanel.setOnChessPanelListener(new OnChessPanelListener() {
			@Override
			public void onHandTypeChange(int handChessType) {
				if (handChessType == 0) {
					blackText.setVisibility(View.INVISIBLE);
					whiteText.setVisibility(View.INVISIBLE);
				} else if (handChessType == 1) {
					whiteText.setVisibility(View.VISIBLE);
					whiteText.setText("轮到白棋走了");
					blackText.setVisibility(View.INVISIBLE);
				} else if (handChessType == 2) {
					blackText.setVisibility(View.VISIBLE);
					whiteText.setText("轮到黑棋走了");
					whiteText.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onGameOver(int handChessType) {
				if (handChessType == 1) {
					whiteText.setVisibility(View.VISIBLE);
					whiteText.setText("恭喜白棋赢了！");
					blackText.setVisibility(View.INVISIBLE);
					showMessage("游戏结束", "恭喜白棋赢了！");
				} else {
					blackText.setVisibility(View.VISIBLE);
					blackText.setText("恭喜黑棋赢了！");
					whiteText.setVisibility(View.INVISIBLE);
					showMessage("游戏结束", "恭喜黑棋赢了！");
				}
			}
		});

	}

	/**
	 * 显示信息框
	 * 
	 * @param title
	 * @param message
	 */
	private void showMessage(String title, String message) {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton("重新开始",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						chessPanel.restart();
					}
				});
		builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 返回按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setMessage("是否退出游戏？");
			builder.setTitle("提示");
			builder.setPositiveButton("重新开始",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							chessPanel.restart();
						}
					});
			builder.setNegativeButton("退出",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							System.exit(0);
						}
					});
			builder.create().show();
		}
		return true;
	}
}
