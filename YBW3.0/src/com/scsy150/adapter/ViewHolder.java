package com.scsy150.adapter;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private Set<Integer> mSeletedImg = new HashSet<>();
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.setmPosition(position);
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertview,
			ViewGroup parent, int layoutId, int position) {
		if (convertview == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertview.getTag();
			holder.setmPosition(position);
			return holder;
		}

	}

	/**
	 * ͨ��viewId��ȡ�ؼ�
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;

	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * ��ʡadapter��settext����
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * ����ͼƬ
	 */
	public ViewHolder setImageResource(int viewId, int resId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(resId);
		return this;
	}

	/**
	 * ����ͼƬ
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {

		ImageView iView = getView(viewId);
		iView.setImageBitmap(bitmap);
		return this;
	}

	/**
	 * 
	 * @param id
	 * @param onClickListener
	 * @return
	 */
	public ViewHolder setFragLayoutClick(int id, OnClickListener onClickListener) {

		FrameLayout frameLayout = getView(id);
		frameLayout.setOnClickListener(onClickListener);

		return this;
	}

	/**
	 * click
	 * 
	 * @return
	 */
	public ViewHolder setImgOnClick(int viewId, OnClickListener onClickListener) {
		ImageView imageView = getView(viewId);
		imageView.setOnClickListener(onClickListener);
		return this;

	}

	/**
	 * 长按
	 * 
	 * @return
	 */
	public ViewHolder setImaOnLongClick(int pos, int viewId,
			OnLongClickListener onLongClickListener) {

		ImageView imageView = getView(viewId);
		
		imageView.setOnLongClickListener(onLongClickListener);
		imageView.setTag(pos);

		return this;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}

}
