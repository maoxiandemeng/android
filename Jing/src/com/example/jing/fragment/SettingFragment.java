package com.example.jing.fragment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.jing.R;
import com.example.jing.adapter.FileAdapter;
import com.example.jing.base.BaseFragment;
import com.example.jing.entity.FileItem;
import com.example.jing.thread.TimerHandler;
import com.example.jing.thread.TimerHandler.onTimerListener;
import com.example.jing.view.MyListView;
import com.example.jing.view.MyViewPage;
import com.example.jing.view.MyViewPage.OnItemChangeListener;
import com.example.jing.view.MyViewPage.OnItemSelectListener;

public class SettingFragment extends BaseFragment implements OnItemClickListener{
	private MyViewPage mViewPage;
	private MyListView mFileListView;
	private FileAdapter mFileAdapter;
	private ArrayList<FileItem> mFileList;
	private int i = 0;
	private TimerHandler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMainView = inflater.inflate(R.layout.fragment_setting, null);
		return mMainView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewPage = (MyViewPage) view.findViewById(R.id.viewpage);
		mFileListView = (MyListView) view.findViewById(R.id.file_list);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Integer[] idStr = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < idStr.length; i++) {
			ids.add(idStr[i]);
		}
		mViewPage.setDrawable(ids);
		mViewPage.show();
		
		mViewPage.setOnItemChangeListener(new OnItemChangeListener() {
			
			@Override
			public void onPosition(int position, int totalCount) {
				Log.i("info", "positionChange: "+position);
			}
		});
		mViewPage.setOnItemSelectListener(new OnItemSelectListener() {
			
			@Override
			public void onItemSelect(View view, int position) {
				Log.i("info", "positionSelect: "+position);
			}
		});
		
		handler = new TimerHandler(new onTimerListener() {
			
			@Override
			public void setOnTimer() {
				Log.i("info", "i: " + i);
				i++;
			}
		}, 1000);
		handler.sendEmptyMessage(0);
		
		mFileAdapter = new FileAdapter(getActivity(), null);
		mFileListView.setAdapter(mFileAdapter);
		mFileListView.setOnItemClickListener(this);
		refreshList(Environment.getExternalStorageDirectory());
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {
			mViewPage.removeMessage();
			handler.removeMessages(0);
		}else {
			mViewPage.sendMessage();
			handler.sendEmptyMessage(0);
		}
	}
	
	private ArrayList<FileItem> getFile(File currentDir){
		ArrayList<FileItem> fileList = new ArrayList<FileItem>();
		File[] list = currentDir.listFiles();
        String parentPath = currentDir.getParent();
        if (TextUtils.isEmpty(parentPath)) {
            parentPath = currentDir.getAbsolutePath();
        }
        FileItem parent = new FileItem(FileItem.DIR_TYPE, "返回上级", parentPath);
        fileList.add(parent);
        if (list != null && list.length != 0) {
            for (File file : list) {
                FileItem item = new FileItem();
                item.setFileName(file.getName());
                item.setFileUri(file.getAbsolutePath());
                if (file.isDirectory()) {
                    item.setFileType(FileItem.DIR_TYPE);
                } else {
                    item.setFileType(FileItem.FILE_TYPE);
                }
                fileList.add(item);
            }
        }
		return fileList;
	}
	
	private void refreshList(File file) {
        mFileList = getFile(file);
        mFileAdapter.setList(mFileList);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FileItem item = (FileItem) parent.getAdapter().getItem(position);
		if (FileItem.DIR_TYPE == item.getFileType()) {
			refreshList(new File(item.getFileUri()));
		}else {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			File file = new File(item.getFileUri());
			intent.setDataAndType(Uri.fromFile(file), "image/*");
			startActivity(intent);
		}
	}
	
	/**
	 * 创建一个文件过滤器
	 * @return
	 */
	private FileFilter createFileFilter() {
        final HashMap<String, String> supportTypeMap = new HashMap<String, String>();
        supportTypeMap.put("rmvb", "rmvb");
        supportTypeMap.put("mp4", "mp4");
        supportTypeMap.put("3gp", "3gp");
        supportTypeMap.put("mov", "mov");
        supportTypeMap.put("mkv", "mkv");
        supportTypeMap.put("ts", "ts");
        supportTypeMap.put("flv", "flv");
        supportTypeMap.put("asf", "asf");
        supportTypeMap.put("wmv", "wmv");
        supportTypeMap.put("rm", "rm");
        supportTypeMap.put("avi", "avi");
        supportTypeMap.put("f4v", "f4v");
        supportTypeMap.put("m3u8", "m3u8");
        supportTypeMap.put("ac3", "ac3");
        supportTypeMap.put("mpg", "mpg");
        supportTypeMap.put("vob", "vob");
        supportTypeMap.put("swf", "swf");

        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }

                String path = pathname.getName().toLowerCase();
                if (path != null) {
                    int index = path.lastIndexOf('.');
                    if (index != -1) {
                        String suffix = path.substring(index + 1);
                        if (supportTypeMap.get(suffix) != null) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
        return filter;
    }
	
}
