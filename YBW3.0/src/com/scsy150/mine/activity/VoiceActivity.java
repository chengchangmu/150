package com.scsy150.mine.activity;

import java.io.File;
import java.io.IOException;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.common.ContentActivity;
import com.scsy150.util.ResourcesUtil;
import com.scsy150.util.SoundMeter;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;

public class VoiceActivity extends BaseActivity implements OnClickListener {

	@ViewInject(R.id.record)
	private ImageView mRecord;

	@ViewInject(R.id.voice)
	private TextView txtName;// 显示声音文件

	@ViewInject(R.id.timedown)
	private Chronometer timedown;// 显示倒计时

	private int flag = 1;
	private Handler mHandler = new Handler();
	private long startVoiceT, endVoiceT;
	private String voiceName;
	private SoundMeter mSensor;
	private static final int POLL_INTERVAL = 29 * 60;
	// 保存使用状态的文件
	private long timeLeftInS = 0;
	private MediaPlayer player;

	private void soundUse(String fileName) {
		// 判断sd卡上是否有声音文件，有的话就显示名称并播放
		final String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/scsy150/" + voiceName;
		File file = new File(path);
		if (file.exists()) {
			String soundName = file.getName();
			txtName.setText(soundName);
			// 点击声音文件播放声音
			txtName.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						player = new MediaPlayer();
						try {
							player.setDataSource(path);
							player.prepare();
							player.start();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (event.getAction() == MotionEvent.ACTION_UP) {
					}
					return true;
				}
			});

		}
	}

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			stop();
		}
	};
	private Runnable mPollTask = new Runnable() {
		public void run() {
			mHandler.postDelayed(mPollTask, POLL_INTERVAL);
		}
	};

	private void start(String name) {
		mSensor.start(name);
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
	}

	/**
	 * 初始化计时器，计时器是通过widget.Chronometer来实现的
	 * 
	 * @param total
	 *            一共多少秒
	 */
	private void initTimer(long total) {
		this.timeLeftInS = total;
		timedown.setOnChronometerTickListener(new OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if (timeLeftInS <= 0) {
					Toast.makeText(VoiceActivity.this, "录音时间到",
							Toast.LENGTH_SHORT).show();
					timedown.stop();
					// 录音停止
					stop();
					return;
				}
				timeLeftInS--;
				refreshTimeLeft();
			}
		});
	}

	private void refreshTimeLeft() {
		this.timedown.setText("录音时间剩余：" + timeLeftInS);
		// TODO 格式化字符串
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (player != null) {
			player.stop();
			player.release();
		}
	}

	@Override
	public void addViewIntoContent() {
		View view = View.inflate(this, R.layout.mine_voice, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mCurrentActivity);
		}
		setLeftVisibility(View.VISIBLE);
		setTitle(ResourcesUtil.getString(R.string.user_option3));
		init();

	}

	private void init() {

		voiceName = "scsy150.mp3";
		mSensor = new SoundMeter();
		// 触摸录音按钮触发事件
		mRecord.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int[] location = new int[2];
					int btn_rc_Y = location[1];
					int btn_rc_X = location[0];
					if (flag == 1) {
						if (!Environment.getExternalStorageDirectory().exists()) {
							Toast.makeText(VoiceActivity.this, "No SDCard",
									Toast.LENGTH_LONG).show();
							return false;
						}
						System.out.println("2");
						System.out.println(event.getY() + "..." + btn_rc_Y
								+ "...." + event.getX() + "...." + btn_rc_X);
						mHandler.postDelayed(new Runnable() {
							public void run() {
							}
						}, 1);
						startVoiceT = SystemClock.currentThreadTimeMillis();
						start(voiceName);
						// 设置录音时间
						timedown.setVisibility(View.VISIBLE);
						initTimer(30);
						timedown.start();
						flag = 2;
					}
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					timedown.stop();
					if (flag == 2) {
						stop();
						flag = 1;
						soundUse(voiceName);

						// File file = new
						// File(android.os.Environment.getExternalStorageDirectory()+"/hq_100/"+
						// voiceName);
						// if (file.exists()) {
						// file.delete();
						// }
					} else {
						stop();
						endVoiceT = SystemClock.currentThreadTimeMillis();
						flag = 1;
						int time = (int) ((endVoiceT - startVoiceT) / 1000);
						System.out.println(time);
					}
				}
				return false;
			}
		});
		soundUse(voiceName);
	}

	@Override
	@OnClick({ R.id.voice, R.id.record })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.voice:
			soundUse(voiceName);
			break;
		case R.id.record:
			break;
		}
	}
}
