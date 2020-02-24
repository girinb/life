package com.bf.test_mp3;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;


public class playing_page extends AppCompatActivity {
	private final static int LOADER_ID = 0x001;
	int count = 0;
	ImageButton mBtnPlayPause;
	SeekBar sb;
	MyThread t_seeker;
	TextView t_tv_nowtime;
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			t_tv_nowtime.setText(DateFormat.format("mm:ss", msg.what));
		}
	};
	private ImageView mImgAlbumArt;
	private TextView mTxtTitle;
	private RemoteViews mRemoteViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playing_page);
		t_seeker = new MyThread();
		mImgAlbumArt = findViewById(R.id.img_albumart);
		mTxtTitle = findViewById(R.id.txt_title);
		mBtnPlayPause = findViewById(R.id.btn_play_pause);
		sb = findViewById(R.id.seekBar);
		t_tv_nowtime = findViewById(R.id.tv_nowtime);
		sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
//				AudioService.getmp();
				AudioService.getmp().seekTo(seekBar.getProgress());
				AudioService.getmp().setLooping(true);
				AudioService.getmp().start();

				if (!t_seeker.isAlive()) {
					t_seeker = new MyThread();
					t_seeker.start();
				}
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				AudioService.getmp().pause();
				if (!t_seeker.isAlive()) {
					t_seeker = new MyThread();
					t_seeker.start();
				}
			}

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				handler.sendEmptyMessage(seekBar.getProgress());
				if (seekBar.getMax() == progress) {
					AudioService.getmp().stop();
				}
			}
		});

		View.OnClickListener control_menu = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.btn_rewind:
						// 이전곡으로 이동
						AudioApplication.getInstance().getServiceInterface().rewind();
						updateUI();
						break;
					case R.id.btn_play_pause:
						// 재생 또는 일시정지
						AudioApplication.getInstance().getServiceInterface().togglePlay();
						updateUI();
						break;
					case R.id.btn_forward:
						// 다음곡으로 이동
						AudioApplication.getInstance().getServiceInterface().forward();
						updateUI();
						break;
					default:
						break;
				}
			}
		};


		findViewById(R.id.btn_rewind).setOnClickListener(control_menu);
		findViewById(R.id.btn_forward).setOnClickListener(control_menu);

		mBtnPlayPause.setOnClickListener(control_menu);


		sb.setMax(AudioService.getmp().getDuration());
		t_seeker.start();
		updateUI();

	}

	private void updateUI() {
		AudioAdapter.AudioItem audioItem = AudioApplication.getInstance().getServiceInterface().getAudioItem();
		if (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
			mBtnPlayPause.setImageResource(R.drawable.pause);
			TextView t_tv_album = findViewById(R.id.tv_plating_album);
			t_tv_album.setText("앨범: " + audioItem.mAlbum);
			TextView t_tv_songer = findViewById(R.id.tv_songer);
			t_tv_songer.setText("아티스트: " + audioItem.mArtist);
			TextView t_tv_title = findViewById(R.id.tv_playing_title);
			t_tv_title.setText("타이틀: " + audioItem.mTitle);
			TextView t_tv_fulltime = findViewById(R.id.tv_fulltime);
			t_tv_fulltime.setText(DateFormat.format("mm:ss", audioItem.mDuration));
			if (!t_seeker.isAlive()) {
				t_seeker = new MyThread();
				t_seeker.start();
			}
		} else {
			mBtnPlayPause.setImageResource(R.drawable.play);
		}

		if (audioItem != null) {
			Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), audioItem.mAlbumId);
			Picasso.with(getApplicationContext()).load(albumArtUri).error(R.drawable.empty_albumart).into(mImgAlbumArt);
//			mTxtTitle.setText(audioItem.mTitle);
		} else {
//			mImgAlbumArt.setImageResource(R.drawable.empty_albumart);
//			mTxtTitle.setText("재생중인 음악이 없습니다.");
		}


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		t_seeker.interrupt();
	}

	class MyThread extends Thread {
		@Override
		public void run() { // 쓰레드가 시작되면 콜백되는 메서드
			while (AudioApplication.getInstance().getServiceInterface().isPlaying()) {
				sb.setProgress(AudioService.getmp().getCurrentPosition());
				handler.sendEmptyMessage(AudioService.getmp().getCurrentPosition());
			}
		}
	}
}
