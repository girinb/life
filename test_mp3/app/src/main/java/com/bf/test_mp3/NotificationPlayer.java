package com.bf.test_mp3;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;

public class NotificationPlayer extends Application {


	private final static int NOTIFICATION_PLAYER_ID = 0x342;
	private AudioService mService;
	private NotificationManager mNotificationManager;
	private NotificationManagerBuilder mNotificationManagerBuilder;
	private boolean isForeground;

	public NotificationPlayer(AudioService service) {
		mService = service;
	}


	public void updateNotificationPlayer() {
		cancel();
		mNotificationManagerBuilder = new NotificationManagerBuilder();
		mNotificationManagerBuilder.execute();
	}

	public void removeNotificationPlayer() {
		cancel();
//		isForeground = false;
	}


	private void cancel() {
		if (mNotificationManagerBuilder != null) {
			mNotificationManagerBuilder.cancel(true);
			mNotificationManagerBuilder = null;
		}
		if (mNotificationManager != null) {
			mNotificationManager.cancel(NOTIFICATION_PLAYER_ID);
		}
	}


//	private void startMyOwnForeground() {
//		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		NotificationCompat.Builder builder;
//		Intent mainActivity = new Intent(this, MainActivity.class);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		String title = "이건 타이틀?";
//		String message = "이건 메시지 내용";
//// 오레오 버전 이상은 Channel 생성
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//			NotificationChannel mChannel = new NotificationChannel("ebookPush", "ebookPush", NotificationManager.IMPORTANCE_HIGH);
//			notificationManager.createNotificationChannel(mChannel);
//			builder = new NotificationCompat.Builder(this, mChannel.getId());
//		} else {
//			builder = new NotificationCompat.Builder(this);
//		}
//		builder.setAutoCancel(true)
//				.setSmallIcon((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? R.drawable.rewind : R.drawable.forward)
//				.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
//				.setTicker(title)
//				.setContentTitle(title)
//				.setContentText(message)
//				.setPriority(NotificationCompat.PRIORITY_MAX); //최대로 펼침
//		builder.setContentIntent(pendingIntent);
//		builder.setDefaults(Notification.DEFAULT_VIBRATE);
//		builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//
//		notificationManager.notify(1234, builder.build());
//	}

	private class NotificationManagerBuilder extends AsyncTask<Void, Void, Notification> {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
			}
		};
		private RemoteViews mRemoteViews;
		private NotificationCompat.Builder mNotificationBuilder;
		private PendingIntent mMainPendingIntent;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


			Intent mainActivity = new Intent(mService, MainActivity.class);
			mMainPendingIntent = PendingIntent.getActivity(mService, 0, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
			mRemoteViews = createRemoteView(R.layout.notification_player);
			mNotificationBuilder = new NotificationCompat.Builder(mService, "음악채널이다냥");
			mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
					.setOngoing(true)
					.setContentIntent(mMainPendingIntent)
					.setPriority(Notification.PRIORITY_MAX)
					.setContent(mRemoteViews);

			mNotificationManager =
					(NotificationManager) MainActivity.getMyApplication().getSystemService(Context.NOTIFICATION_SERVICE);


			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				NotificationChannel channel = new NotificationChannel("음악채널이다냥",
						"음악 채널이다냥",
						NotificationManager.IMPORTANCE_HIGH);
				mNotificationManager.createNotificationChannel(channel);
			}
//			if (!isForeground) {
//				isForeground = true;
			mNotificationManager.notify(NOTIFICATION_PLAYER_ID, mNotificationBuilder.build());

//			}
		}

		@Override
		protected Notification doInBackground(Void... params) {
			mNotificationBuilder.setContent(mRemoteViews);
			mNotificationBuilder.setContentIntent(mMainPendingIntent);
			mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
			Notification notification = mNotificationBuilder.build();
			updateRemoteView(mRemoteViews, notification);
			return notification;
		}

		@Override
		protected void onPostExecute(Notification notification) {
			super.onPostExecute(notification);
			try {
				mNotificationManager.notify(NOTIFICATION_PLAYER_ID, notification);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private RemoteViews createRemoteView(int layoutId) {
			RemoteViews remoteView = new RemoteViews(mService.getPackageName(), layoutId);
			Intent actionTogglePlay = new Intent(CommandActions.TOGGLE_PLAY);
			Intent actionForward = new Intent(CommandActions.FORWARD);
			Intent actionRewind = new Intent(CommandActions.REWIND);
			Intent actionClose = new Intent(CommandActions.CLOSE);
			PendingIntent togglePlay = PendingIntent.getService(mService, 0, actionTogglePlay, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent forward = PendingIntent.getService(mService, 0, actionForward, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent rewind = PendingIntent.getService(mService, 0, actionRewind, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent close = PendingIntent.getService(mService, 0, actionClose, PendingIntent.FLAG_UPDATE_CURRENT);

			remoteView.setOnClickPendingIntent(R.id.btn_player_pause, togglePlay);
			remoteView.setOnClickPendingIntent(R.id.btn_player_forward, forward);
			remoteView.setOnClickPendingIntent(R.id.btn_player_rewind, rewind);
			remoteView.setOnClickPendingIntent(R.id.btn_player_close, close);
			return remoteView;
		}

		private void updateRemoteView(RemoteViews remoteViews, Notification notification) {
			if (mService.isPlaying()) {
				remoteViews.setImageViewResource(R.id.btn_player_pause, R.drawable.pause);
			} else {
				remoteViews.setImageViewResource(R.id.btn_player_pause, R.drawable.play);
			}

			String title = mService.getAudioItem().mTitle;
			remoteViews.setTextViewText(R.id.txt_title, title);
			Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), mService.getAudioItem().mAlbumId);
			try {
				Picasso.with(mService).load(albumArtUri).into(remoteViews, R.id.img_albumart, NOTIFICATION_PLAYER_ID, notification);
			} catch (Exception e) {
				remoteViews.setImageViewResource(R.id.img_albumart, R.drawable.empty_albumart);
			}


		}

	}
}