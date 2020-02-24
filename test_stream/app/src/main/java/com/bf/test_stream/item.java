package com.bf.test_stream;

public class item {
	String albumname;
	String title;
	String filesrc;
	String arbumart;
	String artist;


	public item(String _albumname, String _title, String _filesrc, String _arbumart, String _artist) {
		albumname = _albumname;
		title = _title;
		filesrc = _filesrc;
		arbumart = _arbumart;
		artist = _artist;
	}



	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setfilesrc(String filesrc) {
		this.filesrc = filesrc;
	}

	public void setArbumart(String arbumart) {
		this.arbumart = arbumart;
	}

	public void setalbumname(String albumname) {
		this.albumname = albumname;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getfilesrc() {
		return filesrc;
	}

	public String getArbumart() {
		return arbumart;
	}

	public String getalbumname() {
		return albumname;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}
}
