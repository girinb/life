package com.bf.test_stream;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
	String TAG = "log";
	String uri = "http://girinb.gonetis.com:20000/Music/index.xml";

	MediaPlayer mediaPlayer;
	ArrayList<item> songdb = new ArrayList<>();

	int position = 0;
	boolean isPaused = false;   // To prevent false resume.
	MyThread t_seeker;
	TextView t_tvuitext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MyThread2 th2 = new MyThread2();
		th2.start();


		// Play

		FloatingActionButton button = findViewById(R.id.fab);
		t_tvuitext = findViewById(R.id.tv_uitext);

//		t_seeker = new MyThread();
//		t_seeker.start();

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
				try {
					mediaPlayer = new MediaPlayer();
//					mediaPlayer.setDataSource("http://girinb.gonetis.com:20000/Music/%5b2019.08.11%5d%20%eb%b2%a4%20-%20%ed%98%b8%ed%85%94%20%eb%8d%b8%eb%a3%a8%eb%82%98%20OST%20Part.9%20%5b%ec%8b%b1%ea%b8%80%5d%20%5bFLAC%5d/01.%20%eb%82%b4%20%eb%aa%a9%ec%86%8c%eb%a6%ac%20%eb%93%a4%eb%a6%ac%eb%8b%88.flac");  // or your audio file url.
					mediaPlayer.setDataSource("http://girinb.gonetis.com:20000/Music/%eb%b0%95%ed%9a%a8%ec%8b%a0/01.%20Home.mp3");
					mediaPlayer.prepareAsync();
					mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							if (mediaPlayer != null) {
								mediaPlayer.start();
								handler.sendEmptyMessage(1);

							}
						}
					});

					isPaused = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				Toast.makeText(getApplicationContext(), "Media player started", Toast.LENGTH_SHORT).show();
			}
		});

		// Stop
		FloatingActionButton button2 = findViewById(R.id.fab2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer != null) {
					mediaPlayer.stop();
					isPaused = false;
					Toast.makeText(getApplicationContext(), "Media player stopped", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Pause
		FloatingActionButton button3 = findViewById(R.id.fab3);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					position = mediaPlayer.getCurrentPosition();
					mediaPlayer.pause();
					isPaused = true;
					Toast.makeText(getApplicationContext(), "Media player paused at " + position / 1000 + " sec", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Resume
		FloatingActionButton button4 = findViewById(R.id.fab4);
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer != null && !mediaPlayer.isPlaying() && isPaused == true) {
					mediaPlayer.start();
					mediaPlayer.seekTo(position);
					isPaused = false;
					Toast.makeText(getApplicationContext(), "Media player resumed at " + position / 1000 + " sec", Toast.LENGTH_SHORT).show();
				}
			}
		});
		FloatingActionButton button5 = findViewById(R.id.fab5);

		button5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mediaPlayer != null) {
//					mediaPlayer.seekTo(10000);
					try {
						mediaPlayer = new MediaPlayer();
						mediaPlayer.reset();
						mediaPlayer.setDataSource("http://girinb.gonetis.com:20000/Music/%5b2019.08.11%5d%20%eb%b2%a4%20-%20%ed%98%b8%ed%85%94%20%eb%8d%b8%eb%a3%a8%eb%82%98%20OST%20Part.9%20%5b%ec%8b%b1%ea%b8%80%5d%20%5bFLAC%5d/01.%20%eb%82%b4%20%eb%aa%a9%ec%86%8c%eb%a6%ac%20%eb%93%a4%eb%a6%ac%eb%8b%88.flac");
						mediaPlayer.prepareAsync();

						mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
							@Override
							public void onPrepared(MediaPlayer mp) {
								if (mediaPlayer != null) {
									mediaPlayer.start();
									handler.sendEmptyMessage(1);

								}
							}
						});
					} catch (Exception e) {
					}
				}
			}
		});

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mediaPlayer != null) {
			mediaPlayer.release();
		}
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case 1:
					t_tvuitext.setText(DateFormat.format("mm:ss", mediaPlayer.getDuration()));
					break;
				case 2:
					t_tvuitext.setText(DateFormat.format("mm:ss", mediaPlayer.getCurrentPosition()) + "/" + DateFormat.format("mm:ss", mediaPlayer.getDuration()));
					break;


			}
//			switch (msg.what) {

//				case SEND_INFORMATION:
//					textView.setText(Integer.toString(msg.arg1) + msg.obj);
//					break;
//				case SEND_STOP:
//					thread.stopThread();
//					textView.setText("Thread 가 중지됨.");
//					break;
//				default:
//					break;
//			}
		}
	};

	class MyThread extends Thread {
		@Override
		public void run() { // 쓰레드가 시작되면 콜백되는 메서드

			while (true) {
				if (mediaPlayer != null) {
					handler.sendEmptyMessage(2);
				}
			}
		}
	}

	class MyThread2 extends Thread {
		@Override
		public void run() { // 쓰레드가 시작되면 콜백되는 메서드
//
			try {
				URL url = new URL(uri);
				URLConnection con = url.openConnection();
				InputStream inputStream = con.getInputStream();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

				DocumentBuilder builder = factory.newDocumentBuilder();

				Document doc = builder.parse(inputStream);

				Element order = doc.getDocumentElement();

//				doc.normalizeDocument();

				NodeList songList = doc.getElementsByTagName("song");
				NodeList titleList = doc.getElementsByTagName("title");
				NodeList albumnameList = doc.getElementsByTagName("albumname");
				NodeList arbumartList = doc.getElementsByTagName("arbumart");
				NodeList filesrcList = doc.getElementsByTagName("filesrc");
				NodeList artistList = doc.getElementsByTagName("artist");


				songdb.clear();
				for (int i = 0; i < songList.getLength(); i++) {
					String title = titleList.item(i).getFirstChild().getNodeValue();
					String albumname = albumnameList.item(i).getFirstChild().getNodeValue();
					String filesrc = filesrcList.item(i).getFirstChild().getNodeValue();
					String arbumart= arbumartList.item(i).getFirstChild().getNodeValue();
					String artist = artistList.item(i).getFirstChild().getNodeValue();
					songdb.add(new item(albumname,title,filesrc,arbumart,artist));
//					Log.w(TAG, i + " " + title + " " + filesrc + " " + albumname + " " + artist+" "+arbumart);
				}
			} catch (Exception e) {
				Log.w(TAG, "run: 에러" + e.toString());
			}
			for (int i =0; songdb.size() > i;i++)
			{
				Log.w(TAG, songdb.get(i).albumname +songdb.get(i).artist);
			}

//			//XmlPullParser
//			try {
//				URL url = new URL(uri);
////
//
//				InputStream inputStream = url.openStream();
//				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//				BufferedReader bufferReader = new BufferedReader(inputStreamReader);
//
//				StringBuffer stringBuffer = new StringBuffer();
//				String line = null;
//
//				try {
//					XmlPullParserFactory factory = XmlPullParserFactory.newInstance(); // XmlPullParserFactory 객체 생성
//					XmlPullParser parser = factory.newPullParser();                    // XmlPullParser 객체 생성
//					parser.setInput(bufferReader); //XmlPullParser에 Reader객체 넣기
//
//					int eventType = parser.getEventType(); //tag의 이벤트? (시작, 끝 등등)구분하기 위함
//
//					while (eventType != parser.END_DOCUMENT) {//xml파일의 끝을 읽기까지 반복
//
//						switch (eventType) {
//							case XmlPullParser.START_TAG:
//								String startTag = parser.getName();
//								if (startTag.equals("student")) {
////									student = new Student();
//									Log.w(TAG, "student: " + parser.getName());
//								}
//								if (startTag.equals("name")) {
//									Log.w(TAG, "name: " + parser.nextText());
//
//								}
//								if (startTag.equals("age")) {
//									Log.w(TAG, "age: " + parser.nextText());
//
//								}
//								if (startTag.equals("address")) {
//									Log.w(TAG, "address: " + parser.nextText());
//
////									student.setAddress(parser.nextText());
//								}
//								break;
//							case XmlPullParser.END_TAG:
//								String endTag = parser.getName();
//								if (endTag.equals("student")) {
////									arrayList.add(student);
//								}
//								break;
//						}
//
//						eventType = parser.next(); //parser가 다음을 가르키게 하기
//					}
//				} catch (XmlPullParserException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} finally {
//					try {
//						if (bufferReader != null)
//							bufferReader.close();
//						if (inputStreamReader != null)
//							inputStreamReader.close();
//						if (inputStream != null)
//							inputStream.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			} catch (Exception e) {
//				Log.w(TAG, "에러: " + e.toString());
//			}
		}
	}

//	private InputStream getInputStream(String para_url) {
//		while (true) {
//			try {
//				URL url = new URL(para_url);
//				URLConnection con = url.openConnection();
//				InputStream is = con.getInputStream();
//				return is;
//			} catch (Exception e) {
//				Log.d("mytag", e.getMessage());
//			}
//		}
//	}

//	public List<String> getPosts(String screen_name) {
//		List<String> stringList = new ArrayList<String>();
//
//		try {
//
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			factory.setNamespaceAware(true);
//			XmlPullParser xpp = factory.newPullParser();
//			InputStream stream = getInputStream("http://girinb.gonetis.com:20000/Music/index.xml");
//			xpp.setInput(stream, "UTF-8");
//			int eventType = xpp.getEventType();
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//				switch (eventType) {
//					case XmlPullParser.START_DOCUMENT:
//						//Log.d("mytag","Start document");
//						break;
//					case XmlPullParser.END_DOCUMENT:
//						//Log.d("mytag","End document");
//						break;
//					case XmlPullParser.START_TAG:
//						//Log.d("mytag","Start tag "+xpp.getName());
//						if (xpp.getName().equals("body")) {
//							eventType = xpp.next();
//							stringList.add(xpp.getText());
//						}
//						break;
//					case XmlPullParser.END_TAG:
//						//Log.d("mytag","End tag "+xpp.getName());
//						break;
//					case XmlPullParser.TEXT:
//						//Log.d("mytag","Text "+xpp.getText());
//						break;
//				}
//				eventType = xpp.next();
//			}
//
//			return stringList;
//		} catch (Exception e) {
//			Log.d("mytag", e.getMessage());
//			return null;
//		}
//	}

//	class DownloadThread extends Thread {
//		String ServerUrl;
//		String LocalPath;
//
//		DownloadThread(String serverPath, String localPath) {
//			ServerUrl = serverPath;
//			LocalPath = localPath;
//		}
//
//		@Override
//		public void run() {
//			URL imgurl;
//			int Read;
//			try {
//				imgurl = new URL(ServerUrl);
//				HttpURLConnection conn = (HttpURLConnection) imgurl
//						.openConnection();
//				int len = conn.getContentLength();
//				byte[] tmpByte = new byte[len];
//				InputStream is = conn.getInputStream();
//				File file = new File(LocalPath);
//				FileOutputStream fos = new FileOutputStream(file);
//				for (; ; ) {
//					Read = is.read(tmpByte);
//					if (Read <= 0) {
//						break;
//					}
//					fos.write(tmpByte, 0, Read);
//				}
//				is.close();
//				fos.close();
//				conn.disconnect();
//
//			} catch (MalformedURLException e) {
//				Log.e("ERROR1", e.getMessage());
//			} catch (IOException e) {
//				Log.e("ERROR2", e.getMessage());
//				e.printStackTrace();
//			}
//		}
//	}


}