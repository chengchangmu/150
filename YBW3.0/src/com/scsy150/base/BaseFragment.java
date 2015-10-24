package com.scsy150.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.scsy150.R;
import com.scsy150.dialog.ProgressDialog;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	public String TAG = getClass().getSimpleName();
	protected LinearLayout mContainer;
	protected LinearLayout mFragmentProgress;
	protected LinearLayout mFragmentFailed;
	// 加载框是否可以取消
	private boolean isCancelable = false;
	protected List<String> mRequestList;
	private ProgressDialog mProgressDialog;

	// Fragment的Activity
	protected FragmentActivity mActivity;
	public RequestQueue mQueue;

	public abstract void addView();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequestList = new ArrayList<String>();
		mActivity = getActivity();// 获取所依赖的Activity
		if (mQueue == null) {
			mQueue = Volley.newRequestQueue(mActivity);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == container) {
			return null;
		}

		View view = inflater.inflate(R.layout.fragment_base, null);
		mContainer = (LinearLayout) view
				.findViewById(R.id.base_fragment_container);
		mFragmentProgress = (LinearLayout) view
				.findViewById(R.id.base_fragment_progress);
		mFragmentProgress.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		mFragmentFailed = (LinearLayout) view
				.findViewById(R.id.base_fragment_failed);
		addView();
		return view;
	}

	public boolean getProgressCancelable() {
		return isCancelable;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mRequestList.clear();
	}

	/**
	 * 以view的形式显示加载提示
	 * 
	 * @param handler
	 *            数据访问的handler
	 * @param cancelable
	 *            是否可以取消请求
	 */
	public void showProgress(String handlerId, boolean cancelable) {
		mRequestList.add(handlerId);
		isCancelable = cancelable;
		mFragmentFailed.setVisibility(View.GONE);
		mFragmentProgress.getBackground().setAlpha(100);
		mFragmentProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * 关闭view形式的加载提示
	 */
	public void closeProgress() {
		isCancelable = false;
		mFragmentFailed.setVisibility(View.GONE);
		mFragmentProgress.setVisibility(View.GONE);
	}

	/**
	 * 显示view形式的加载失败提示
	 * 
	 * @param listener
	 *            数据加载失败的点击事件
	 */
	public void showFaiedView(OnClickListener listener) {
		isCancelable = false;
		mFragmentProgress.setVisibility(View.GONE);
		mFragmentFailed.setVisibility(View.VISIBLE);
		mFragmentFailed.setOnClickListener(listener);
	}

	/**
	 * 显示对话框形式的加载提示
	 * 
	 * @param handler
	 *            数据访问的handler
	 * @param cancelable
	 *            是否可以取消请求
	 */
	public void showProgressDialog(final String handlerId, boolean cancelable) {
		mRequestList.add(handlerId);
		isCancelable = cancelable;
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getActivity());
		}
		mProgressDialog.setCancelable(isCancelable);
		mProgressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isCancelable = false;
			}
		});
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				isCancelable = false;
			}
		});
		mProgressDialog.show();
	}

	/**
	 * 取消加载对话框
	 */
	public void closeProgressDialog() {
		isCancelable = false;
		if (mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		fragmentClick(v);
	}

	// 提供给子类实现的点击接口
	public abstract void fragmentClick(View v);
}
