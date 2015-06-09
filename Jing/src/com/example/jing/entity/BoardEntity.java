package com.example.jing.entity;

public class BoardEntity {
	private String url;
	private String msg;

	public BoardEntity() {
		super();
	}

	public BoardEntity(String url, String msg) {
		super();
		this.url = url;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "BoardEntity [url=" + url + ", msg=" + msg + "]";
	}


}
