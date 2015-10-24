package com.scsy150.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scsy150.R;
import com.scsy150.util.ResourcesUtil;

/*
 * Copyright (C) 2015 四川硅谷科技重庆分公司
 * 版权所有
 *
 * 功能描述：move from 2.4 trunk,通用dialog
 * 作者：硅谷科技
 * 创建时间：2015-08-24
 *
 * 修改人：
 * 修改描述：
 * 修改日期
 */

public class YBWDialog extends Dialog implements OnItemClickListener {
	/**
	 * 普通文字对话框
	 */
	protected final int STYLE_WORD_TYPE = 100;
	/**
	 * 输入对话框
	 */
	protected final int STYLE_INPUT_TYPE = 101;
	/**
	 * 列表选项对话框
	 */
	protected final int STYLE_LIST_TYPE = 102;
	/**
	 * 单输入框
	 */
	public static final int INPUT_SINGAL = 103;
	/**
	 * 两个输入框
	 */
	public static final int INPUT_DOUBLE = 104;

	// 列表框类型的子类型
	public static final int LIST_SUB_STYLE_NORMAL = 200;
	// 靠底部，并有取消的选项
	public static final int LIST_SUB_STYLE_AGLIN_BOTTOM = 201;

	private int currentStyle = STYLE_WORD_TYPE;
	private int subStyle = LIST_SUB_STYLE_NORMAL;

	private LinearLayout mContainer;
	private LinearLayout mContainerWithTitle;
	private ScrollView mMsgContainerWithTitle;
	private Spanned message;
	private Spanned title;
	private Context mContext;
	private TextView mTitle;
	private ScrollView mMsgContainerNoTitle;
	private TextView mMessageNoTitle;
	private TextView mMessageWithTitle;
	private EditText mFirstInput;
	private EditText mSecondInput;
	private ListView mListView;
	private Button btnOK;
	private Button btnCancel;
	private String[] listTexts;
	private int[] listImages;
	private String firstText;
	private String secondText;
	private int firstInputType;
	private int secondInputType;
	private int firstHint;
	private int secondHint;
	private int firstMax = -1;
	private int secondMax = -1;
	private DialogItemOnClick listItemClickListener;
	private DialogOnClickListener positiveListener;
	private DialogOnClickListener negativeListener;
	private int positiveText;
	private int negativeText;
	private View btnContent;
	private TextView mBottomCancel;
	private int currentInputType = INPUT_SINGAL;
	private boolean mIsFirstSingle = true;
	private boolean mIsSecondSingle = true;
	private int mMsgGravity = Gravity.CENTER;

	public interface DialogOnClickListener {
		/**
		 * 若一个输入框，则输入内容为firstText
		 * 
		 * @param firstText
		 * @param secondText
		 */
		public void onClick(String firstText, String secondText);
	}

	public interface DialogItemOnClick {
		public void onItemClick(int position, String text);
	}

	/**
	 * 普通文字对话框
	 */
	public YBWDialog(Context context, int msg, int title) {
		super(context, R.style.common_dialog);
		if (msg <= 0) {
			try {
				throw (new Throwable("message is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_WORD_TYPE, title, msg, 0, null);
		}
	}

	/**
	 * 普通文字对话框
	 */
	public YBWDialog(Context context, Spanned msg, int title) {
		super(context, R.style.common_dialog);
		if (TextUtils.isEmpty(msg)) {
			try {
				throw (new Throwable("message is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_WORD_TYPE, title, msg, 0, null);
		}
	}

	/**
	 * 普通文字对话框
	 */
	public YBWDialog(Context context, CharSequence msg, int title) {
		super(context, R.style.common_dialog);
		if (TextUtils.isEmpty(msg)) {
			try {
				throw (new Throwable("message is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_WORD_TYPE, title, new SpannedString(msg), 0,
					null);
		}
	}

	/**
	 * 输入对话框
	 */
	public YBWDialog(Context context, int titleId) {
		super(context, R.style.common_dialog);
		init(context, STYLE_INPUT_TYPE, titleId, 0, 0, null);
	}

	/**
	 * 输入对话框
	 */
	public YBWDialog(Context context, Spanned title) {
		super(context, R.style.common_dialog);
		init(context, STYLE_INPUT_TYPE, title, 0, 0, null);
	}

	/**
	 * 输入对话框
	 */
	public YBWDialog(Context context, String title) {
		super(context, R.style.common_dialog);
		init(context, STYLE_INPUT_TYPE, title, 0, 0, null);
	}
	
	

	/**
	 * 列表选项对话框
	 */
	public YBWDialog(Context context, int title, int txtsArrayId, int[] images) {
		super(context, R.style.common_dialog);
		if (txtsArrayId <= 0) {
			try {
				throw (new Throwable("list item text is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_LIST_TYPE, title, 0, txtsArrayId, images);
		}
	}

	/**
	 * 列表选项对话框
	 */
	public YBWDialog(Context context, int title, String[] txtsArray,
			int[] images) {
		super(context, R.style.common_dialog);
		if (txtsArray == null) {
			try {
				throw (new Throwable("list item text is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_LIST_TYPE, title, 0, txtsArray, images);
		}
	}

	/**
	 * 列表选项对话框
	 */
	public YBWDialog(Context context, int title, int txtsArrayId,
			int[] images, int subType) {
		super(context, R.style.common_dialog);
		this.subStyle = subType;
		if (txtsArrayId <= 0) {
			try {
				throw (new Throwable("list item text is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_LIST_TYPE, title, 0, txtsArrayId, images);
		}
	}

	/**
	 * 列表选项对话框
	 */
	public YBWDialog(Context context, int title, String[] txtsArray,
			int[] images, int subType) {
		super(context, R.style.common_dialog);
		this.subStyle = subType;
		if (txtsArray == null) {
			try {
				throw (new Throwable("list item text is null"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			init(context, STYLE_LIST_TYPE, title, 0, txtsArray, images);
		}
	}

	private void init(Context context, int styleId, int titleId, int msgId,
			String[] txtsArray, int[] images) {
		mContext = context;
		currentStyle = styleId;
		listImages = images;
		listTexts = txtsArray;
		if (msgId != 0) {
			message = new SpannedString(mContext.getString(msgId));
		}
		if (titleId != 0) {
			this.title = new SpannedString(mContext.getString(titleId));
		}
	}

	private void init(Context context, int style, Spanned title, int msg,
			int txtsArrayId, int[] images) {
		mContext = context;
		currentStyle = style;
		listImages = images;
		this.title = title;
		if (msg != 0) {
			message = new SpannedString(mContext.getString(msg));
		}
		if (txtsArrayId != 0) {
			listTexts = mContext.getResources().getStringArray(txtsArrayId);
		}
	}

	private void init(Context context, int style, String title, int msg,
			int txtsArrayId, int[] images) {
		mContext = context;
		currentStyle = style;
		listImages = images;
		if (!TextUtils.isEmpty(title)) {
			this.title = new SpannedString(title);
		}
		if (msg != 0) {
			message = new SpannedString(mContext.getString(msg));
		}
		if (txtsArrayId != 0) {
			listTexts = mContext.getResources().getStringArray(txtsArrayId);
		}
	}

	private void init(Context context, int style, int title, Spanned msg,
			int txtsArrayId, int[] images) {
		mContext = context;
		currentStyle = style;
		message = msg;
		if (title > 0) {
			this.title = new SpannedString(mContext.getString(title));
		}
		listImages = images;
		if (txtsArrayId != 0) {
			listTexts = mContext.getResources().getStringArray(txtsArrayId);
		}
	}

	private void init(Context context, int styleId, int titleId, int msgId,
			int txtsArrayId, int[] images) {
		mContext = context;
		currentStyle = styleId;
		listImages = images;
		if (txtsArrayId > 0) {
			listTexts = mContext.getResources().getStringArray(txtsArrayId);
		}
		if (msgId != 0) {
			message = new SpannedString(mContext.getString(msgId));
		}
		if (txtsArrayId != 0) {
			listTexts = mContext.getResources().getStringArray(txtsArrayId);
		}
		if (titleId != 0) {
			this.title = new SpannedString(mContext.getString(titleId));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_common_dialog);
		/**
		 * 设置对话框属性
		 */
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams params = dialogWindow.getAttributes();
		int width = dialogWindow.getWindowManager().getDefaultDisplay()
				.getWidth();

		if (LIST_SUB_STYLE_AGLIN_BOTTOM == subStyle) {
			dialogWindow.setGravity(Gravity.BOTTOM);
		} else {
			width -= 2 * getContext().getResources().getDimensionPixelSize(
					R.dimen.px30);
			dialogWindow.setGravity(Gravity.CENTER);
		}
		dialogWindow.setLayout(width, LayoutParams.WRAP_CONTENT);
		params.dimAmount = 0.5f;
		dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		dialogWindow.setAttributes(params);

		initView();
		setInfo();
	}

	protected void setInfo() {
		setTitleStatus();
		setBtnStatus();
		switch (currentStyle) {
		case STYLE_WORD_TYPE:
			if (!TextUtils.isEmpty(title)) {
				// mMessageWithTitle.setGravity(mMsgGravity);
				mMessageWithTitle.setText(message);
				mMessageWithTitle.setVisibility(View.VISIBLE);
				mMsgContainerWithTitle.setVisibility(View.VISIBLE);
			} else {
				// mMessageNoTitle.setGravity(mMsgGravity);
				mMessageNoTitle.setText(message);
				mMessageNoTitle.setVisibility(View.VISIBLE);
				mMsgContainerNoTitle.setVisibility(View.VISIBLE);
			}

			break;
		case STYLE_INPUT_TYPE:
			setInputStatus();
			break;
		case STYLE_LIST_TYPE:
			mListView.setAdapter(new DialogListAdapter(mContext, this,
					listTexts, listImages,
					subStyle != LIST_SUB_STYLE_AGLIN_BOTTOM));
			mListView.setOnItemClickListener(this);
			mListView.setVisibility(View.VISIBLE);
			if (subStyle == LIST_SUB_STYLE_AGLIN_BOTTOM) {
				mContainer.setBackgroundDrawable(new BitmapDrawable());
				mContainer.setBackgroundColor(ResourcesUtil
						.getColor(R.color.common_grey_bg_color));
				mBottomCancel.setVisibility(View.VISIBLE);
				mBottomCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						cancel();
					}

				});

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, getContext().getResources()
						.getDimensionPixelOffset(R.dimen.px30), 0, 0);

				mListView.setLayoutParams(lp);
				setCanceledOnTouchOutside(true);
			}
			break;
		default:
			break;
		}
	}

	protected void setTitleStatus() {
		if (!TextUtils.isEmpty(title)) {
			mTitle.setText(title);
			mContainerWithTitle.setVisibility(View.VISIBLE);
			if (currentStyle != STYLE_WORD_TYPE) {
				mMsgContainerWithTitle.setVisibility(View.GONE);
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContainerWithTitle
						.getLayoutParams();
				lp.height = LayoutParams.WRAP_CONTENT;
				mContainerWithTitle.setLayoutParams(lp);
			}
		} else if (currentStyle == STYLE_WORD_TYPE) {
			mMsgContainerNoTitle.setVisibility(View.VISIBLE);
		}
	}

	protected void setInputStatus() {
		if (currentInputType == INPUT_SINGAL) {
			mFirstInput.setVisibility(View.VISIBLE);
			mSecondInput.setVisibility(View.GONE);
		} else {
			mFirstInput.setVisibility(View.VISIBLE);
			mSecondInput.setVisibility(View.VISIBLE);
		}
		if (!TextUtils.isEmpty(firstText)) {
			mFirstInput.setText(firstText);
			Selection.selectAll(mFirstInput.getEditableText());
		}
		if (!TextUtils.isEmpty(secondText)) {
			mSecondInput.setText(secondText);
			Selection.selectAll(mSecondInput.getEditableText());
		}
		if (firstMax != -1) {
			mFirstInput
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							firstMax) });
		}
		if (secondMax != -1) {
			mSecondInput
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							secondMax) });
		}
		if (firstInputType > 0) {
			mFirstInput.setInputType(firstInputType);
		}
		if (secondInputType > 0) {
			mSecondInput.setInputType(secondInputType);
		}
		if (firstHint > 0) {
			mFirstInput.setHint(firstHint);
		}
		if (secondHint > 0) {
			mSecondInput.setHint(secondHint);
		}
		mFirstInput.requestFocus();
		mFirstInput.setVisibility(View.VISIBLE);
		if (currentInputType == INPUT_DOUBLE) {
			mSecondInput.setVisibility(View.VISIBLE);
		}
		mFirstInput.setSingleLine(mIsFirstSingle);
		mSecondInput.setSingleLine(mIsSecondSingle);
	}

	protected void setBtnStatus() {
		if (positiveText > 0 && positiveListener != null) {
			btnOK.setText(positiveText);
			btnOK.setOnClickListener(new android.view.View.OnClickListener() {

				@Override
				public void onClick(View v) {
					positiveListener.onClick(mFirstInput.getText().toString()
							.trim(), mSecondInput.getText().toString().trim());
				}
			});
			btnOK.setVisibility(View.VISIBLE);
		}
		if (negativeText > 0 && negativeListener != null) {
			btnCancel.setText(negativeText);
			btnCancel
					.setOnClickListener(new android.view.View.OnClickListener() {

						@Override
						public void onClick(View v) {
							negativeListener.onClick(mFirstInput.getText()
									.toString().trim(), mSecondInput.getText()
									.toString().trim());
						}
					});
			btnCancel.setVisibility(View.VISIBLE);
		}
		if ((positiveText > 0 && positiveListener != null)
				&& (negativeText > 0 && negativeListener != null)) {
			btnContent.setVisibility(View.VISIBLE);
		} else if ((positiveText > 0 && positiveListener != null)
				|| (negativeText > 0 && negativeListener != null)) {
			btnContent.setVisibility(View.VISIBLE);
			btnOK.setBackgroundResource(R.drawable.common_white_btn_cornor_selector);
			btnCancel
					.setBackgroundResource(R.drawable.common_white_btn_cornor_selector);
		} else {
			btnContent.setVisibility(View.GONE);
		}
	}

	public void setPositiveButton(int textId,
			final DialogOnClickListener listener) {
		if (textId <= 0 || listener == null) {
			try {
				throw (new Throwable(
						"please set button text and OnClickListener"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			positiveText = textId;
			positiveListener = listener;
		}
	}

	public void setNegativeButton(int textId,
			final DialogOnClickListener listener) {
		if (textId <= 0 || listener == null) {
			try {
				throw (new Throwable(
						"please set button text and OnClickListener"));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} else {
			negativeText = textId;
			negativeListener = listener;
		}
	}

	public void setItemClickListener(DialogItemOnClick listener) {
		listItemClickListener = listener;
	}

	public void setFirstInputText(String text) {
		firstText = text;
	}

	public void setSecondInputText(String text) {
		secondText = text;
	}

	/**
	 * 
	 * @param type
	 *            InputType.TYPE....
	 */
	public void setFirstInputType(int type) {
		firstInputType = type;
	}

	public void setFirstInputMaxLength(int length) {
		firstMax = length;
	}

	/**
	 * 
	 * @param type
	 *            InputType.TYPE....
	 */
	public void setSecondInputType(int type) {
		secondInputType = type;
	}

	public void setSecondInputMaxLength(int length) {
		secondMax = length;
	}

	/**
	 * 输入框个数，最多支持2个输入框
	 * 
	 * @param num
	 */
	public void setInputNum(int inputType) {
		currentInputType = inputType;
	}

	public void initView() {
		mContainer = (LinearLayout) findViewById(R.id.container);
		mContainerWithTitle = (LinearLayout) findViewById(R.id.dialog_with_title);
		mMsgContainerWithTitle = (ScrollView) findViewById(R.id.msg_content);
		mMsgContainerNoTitle = (ScrollView) findViewById(R.id.msg_content_no_title);
		mMessageWithTitle = (TextView) findViewById(R.id.dialog_msg);
		mMessageNoTitle = (TextView) findViewById(R.id.dialog_msg_no_title);
		mFirstInput = (EditText) findViewById(R.id.dialog_input_first);
		mSecondInput = (EditText) findViewById(R.id.dialog_input_second);
		mListView = (ListView) findViewById(R.id.dialog_list);
		btnCancel = (Button) findViewById(R.id.dialog_cancel);
		btnOK = (Button) findViewById(R.id.dialog_ok);
		mTitle = (TextView) findViewById(R.id.dialog_title);
		btnContent = findViewById(R.id.dialog_btn_content);
		mBottomCancel = (TextView) findViewById(R.id.image_chooser_dialog_cancel);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		cancel();
		listItemClickListener.onItemClick(position, listTexts[position]);
	}

	public void setFirstHint(int hint) {
		firstHint = hint;
	}

	public void setFirstSingle(boolean flag) {
		mIsFirstSingle = flag;
	}

	public void setSecondHint(int hint) {
		secondHint = hint;
	}

	public void setSecondSingle(boolean flag) {
		mIsSecondSingle = flag;
	}

	public void setMsgAlign(int gravity) {
		mMsgGravity = mMsgGravity | gravity;
	}
}
