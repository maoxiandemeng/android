package com.example.jing.entity;

import android.graphics.Bitmap;

public class VideoInfo{
	private int id;
	private String sizeText;
	private String title;
	private String data;
	private String displayName;
	private String mimeType;
	private Bitmap bitmap;
	
	public VideoInfo() {
		super();
	}

	public VideoInfo(int id, String sizeText, String title, String data,
			String displayName, String mimeType, Bitmap bitmap) {
		super();
		this.id = id;
		this.sizeText = sizeText;
		this.title = title;
		this.data = data;
		this.displayName = displayName;
		this.mimeType = mimeType;
		this.bitmap = bitmap;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSizeText() {
		return sizeText;
	}

	public void setSizeText(String sizeText) {
		this.sizeText = sizeText;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public String toString() {
		return "VideoInfo [id=" + id + ", sizeText=" + sizeText + ", title="
				+ title + ", data=" + data + ", displayName=" + displayName
				+ ", mimeType=" + mimeType + ", bitmap=" + bitmap + "]";
	}
	
}
