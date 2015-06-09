package com.example.jing.entity;

public class FileItem {
    public  final static int DIR_TYPE = 0;
    public final static int FILE_TYPE = 1;

    private int mFileType;
    private String mFileName;
    private String mFileUri;
    
    public FileItem() {
		super();
	}

    public FileItem(int mFileType, String mFileName, String mFileUri) {
		super();
		this.mFileType = mFileType;
		this.mFileName = mFileName;
		this.mFileUri = mFileUri;
	}

	public int getFileType() {
        return mFileType;
    }
    
    public void setFileType(int type) {
        mFileType = type;
    }

	public String getFileName() {
		return mFileName;
	}

	public void setFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	public String getFileUri() {
		return mFileUri;
	}

	public void setFileUri(String mFileUri) {
		this.mFileUri = mFileUri;
	}

	@Override
	public String toString() {
		return "FileItem [mFileType=" + mFileType + ", mFileName=" + mFileName
				+ ", mFileUri=" + mFileUri + "]";
	}

}
