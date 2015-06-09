package com.example.jing.base;

import java.util.ArrayList;

import com.example.jing.entity.FileItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseObjectAdapter<T> extends BaseAdapter {
	protected ArrayList<T> list = new ArrayList<T>();
	protected Context context;
	protected LayoutInflater inflater;
	
	protected BaseObjectAdapter(Context context, ArrayList<T> list){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if (list == null) {
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
	/**
	 * ���ü���
	 * @param objects
	 */
	public void setList(ArrayList<T> objects) {
        list = objects;
        this.notifyDataSetChanged();
    }
	
	/**
	 * ���һ������
	 * @param objects
	 */
	public void addAll(ArrayList<T> objects){
		list.addAll(objects);
		notifyDataSetChanged();
	}
	
	/**
	 * ���һ������
	 * @param object
	 */
	public void addItem(T object){
		list.add(object);
		notifyDataSetChanged();
	}
	
	/**
	 * �Ƴ�һ������λ��
	 * @param position
	 */
	public void removeItem(int position) {
		list.remove(position);
		notifyDataSetChanged();
	}
	
	/**
	 * �����������
	 */
	public void clearAll(){
		list.clear();
		notifyDataSetChanged();
	}

}
