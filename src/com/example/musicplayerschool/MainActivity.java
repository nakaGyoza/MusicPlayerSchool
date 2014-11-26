package com.example.musicplayerschool;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	final int requestCode = 10000;

	MediaPlayer mediaPlayer;
	TextView title;
	TextView current_time;
	TextView whole_time;
	SeekBar seekBar;
	Uri uri;

	Timer timer;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		/*
		// 音楽ファイル選択してみよう
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("audio/*");
		startActivity(intent);
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		title = (TextView) findViewById(R.id.title);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		current_time = (TextView) findViewById(R.id.current_time);
		whole_time = (TextView) findViewById(R.id.whole_time);
		title.setText("sample");
		/*// 曲の再生時間を取得
		int duration = mediaPlayer.getDuration();
		duration /= 1000;
		int minute = duration / 60;
		int second = duration % 60;
		String m = String.format(Locale.JAPAN, "%02d", minute);
		String s = String.format(Locale.JAPAN, "%02d", second);
		whole_time.setText(m + ":" + s);
		seekBar.setMax(duration);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress() * 1000;
				mediaPlayer.seekTo(progress);
				mediaPlayer.start();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mediaPlayer.pause();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		*/

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == this.requestCode) {
			if (resultCode == 10000) {
				startActivityForResult(data, requestCode);
				uri = data.getData();
				mediaPlayer = MediaPlayer.create(this, uri);
				Toast.makeText(this, "OK was clicked.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Cancel was clicked.", Toast.LENGTH_LONG).show();
			}
		}

	}
	
	public void chooseNum(View v,Intent intent){
				// 音楽ファイル選択してみよう
				intent.setType("audio/*");
				startActivity(intent);
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 曲の再生時間を取得
				int duration = mediaPlayer.getDuration();
				duration /= 1000;
				int minute = duration / 60;
				int second = duration % 60;
				String m = String.format(Locale.JAPAN, "%02d", minute);
				String s = String.format(Locale.JAPAN, "%02d", second);
				whole_time.setText(m + ":" + s);
				seekBar.setMax(duration);
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						int progress = seekBar.getProgress() * 1000;
						mediaPlayer.seekTo(progress);
						mediaPlayer.start();
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						mediaPlayer.pause();
					}

					@Override
					public void onProgressChanged(SeekBar seekBar, int progress,
							boolean fromUser) {

					}
				});
		
	}

	public void start(View v) {
		mediaPlayer.start();
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					int duration = mediaPlayer.getCurrentPosition() / 1000;
					int minute = duration / 60;
					int second = duration % 60;

					final String m = String
							.format(Locale.JAPAN, "%02d", minute);
					final String s = String
							.format(Locale.JAPAN, "%02d", second);
					handler.post(new Runnable() {
						@Override
						public void run() {
							current_time.setText(m + ":" + s);
							seekBar.setProgress(mediaPlayer
									.getCurrentPosition() / 1000);
						}
					});
				}
			}, 0, 1000);
		}
	}

	public void pause(View v) {
		mediaPlayer.pause();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void stop(View v) {
		mediaPlayer.stop();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mediaPlayer.release();
	}

}
