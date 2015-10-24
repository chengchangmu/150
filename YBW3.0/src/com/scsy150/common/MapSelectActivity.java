package com.scsy150.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.scsy150.R;
import com.scsy150.base.BaseActivity;
import com.scsy150.consts.SystemConsts;
import com.scsy150.dialog.YBWDialog;
import com.scsy150.dialog.YBWDialog.DialogOnClickListener;
import com.scsy150.util.ToastUtil;
import com.scsy150.util.view.OnClick;
import com.scsy150.util.view.ViewInject;
import com.scsy150.util.view.ViewUtils;


public class MapSelectActivity extends BaseActivity implements OnClickListener {
	/**
	 * 既能输入框输入搜索，也能点击定位
	 */
	public static final int MAP_SELECTED_SEARCH = 109;
	/**
	 * 仅仅支持点击地图选点
	 */
	public static final int MAP_ONLY_SELECTED = 110;
	/**
	 * 仅根据传入经纬度查找点，并显示
	 */
	public static final int MAP_NOLY_VIEW = 111;
	public static final String MAP_DATA_KEY = "mapDataKey";
	public static final String MAP_TYPE = "mapViewType";
	private MapController mMapController;
	private LocationClient mLocClient;
	private MKSearch mSearch;
	private MyLocationOverlay myLocationOverlay = null;
	private BMapManager mBMapManager = null;
	private OverlayItem itempoint;
	private MyOverlay mOverlay;
	private boolean isFirst = true;
	private AreaBean mArea;
	private int mType;
	@ViewInject(R.id.map_select_map_view)
	private MapView mMapView;
	@ViewInject(R.id.map_head_addr)
	private TextView mTvAddr;
	@ViewInject(R.id.map_select_write)
	private Button mWriteSearch;
	private TextView mBtnSubmit;

	@Override
	public void addViewIntoContent() {
		mBMapManager = new BMapManager(getApplicationContext());
		mBMapManager.init(SystemConsts.MAP_KEY, new MyGeneralListener());
		View view = View.inflate(this, R.layout.activity_map_select, null);
		mBaseContent.addView(view);
		ViewUtils.inject(this);
		setLeftVisibility(View.VISIBLE);
		setRightTxt(R.string.save);
		initView();
		initData();
	}

	public void initView() {
		setTitle(R.string.map_mark);
		mBtnSubmit = mTvRight;
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().enableClick(true);
		mMapView.showScaleControl(true);
		mMapController = mMapView.getController();

		mWriteSearch.setOnClickListener(this);
		mSearch = new MKSearch();
		mBMapManager.start();
		mSearch.init(mBMapManager, mSearchListener);

		mOverlay = new MyOverlay(getResources()
				.getDrawable(R.drawable.map_mark), mMapView);
		mMapView.getOverlays().add(mOverlay);
	}

	public void initData() {
		mArea = new AreaBean();
		mArea.LocationType = SystemConsts.locationType;
		Intent intent = getIntent();
		if (intent != null) {
			AreaBean bean = (AreaBean) intent
					.getSerializableExtra(MAP_DATA_KEY);
			mType = intent.getIntExtra(MAP_TYPE, MAP_NOLY_VIEW);
			switch (mType) {
			case MAP_SELECTED_SEARCH:
				showWriteDialog();
				break;
			case MAP_ONLY_SELECTED:
				mWriteSearch.setVisibility(View.GONE);
				break;
			case MAP_NOLY_VIEW:
				mWriteSearch.setVisibility(View.GONE);
				mMapView.getOverlays().clear();
				break;
			default:
				break;
			}
			if (bean == null || bean.Lng <= 0 || bean.Lat <= 0) {
				Location();
			} else {
				if (bean != null) {
					mArea = bean;
				}
				mTvAddr.setText(mArea.DetailAddress);
				Overlay();
			}
		}
	}

	private void Overlay() {
		GeoPoint point = new GeoPoint((int) (mArea.Lat * 1E6),
				(int) (mArea.Lng * 1E6));
		OverlayItem itempoint = new OverlayItem(point, "", "");
		itempoint.setMarker(getResources().getDrawable(R.drawable.map_mark));
		mOverlay.addItem(itempoint);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
		mMapController.setCenter(point);
	}

	private void Location() {
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(new MyLocationListenner());
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(false);
		// option.setCoorType("bd09ll");
		// option.setScanSpan(1000);
		// option.setAddrType("all");
		// String packName = PhoneUtil.getPackageName(this);
		// option.setProdName(packName);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll"); // 设置坐标类型为bd09ll
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
		mLocClient.start();
		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.setData(new LocationData());
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		mMapView.refresh();
		myLocationOverlay.setMarker(getResources().getDrawable(
				R.drawable.map_mark));

	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				ToastUtil.showToast(MapSelectActivity.this,
						R.string.network_connection_error, Toast.LENGTH_LONG);
				return;
			}
			int type = location.getLocType();
			if (type == 61 || type == 65 || type == 66 || type == 161
					|| type == 68 && TextUtils.isEmpty(location.getAddrStr())) {
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				LocationData data = new LocationData();
				data.latitude = lat;
				data.longitude = lng;
				GeoPoint point = new GeoPoint((int) (lat * 1e6),
						(int) (lng * 1e6));
				data.accuracy = location.getRadius();
				data.direction = location.getDirection();
				myLocationOverlay.setData(data);
				mArea.Lat = lat;
				mArea.Lng = lng;
				mArea.DetailAddress = location.getAddrStr();
				mTvAddr.setText(mArea.DetailAddress);
				mMapView.refresh();
				if (isFirst) {
					mMapController.animateTo(point);

					mArea.Lat = location.getLatitude();
					mArea.Lng = location.getLongitude();
					isFirst = false;
				}
				myLocationOverlay.setMarker(getResources().getDrawable(
						R.drawable.map_mark));
				mLocClient.stop();
				mSearch.reverseGeocode(point);
				mBtnSubmit.setClickable(true);
			} else {
				mLocClient.requestLocation();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mBMapManager.stop();
		if (mLocClient != null) {
			mLocClient.stop();
		}
		mMapView.destroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	@OnClick({ R.id.left_view, R.id.right_view, R.id.map_select_write })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view:
			finish();
			break;
		case R.id.right_view:
			Intent intent = new Intent();
			mArea.PutRegionalType = 1;
			intent.putExtra(MAP_DATA_KEY, mArea);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.map_select_write:
			showWriteDialog();
			break;
		}
	}

	public void showWriteDialog() {
		final YBWDialog mWriteDiloag = new YBWDialog(this,
				R.string.input_address);
		mWriteDiloag.setFirstHint(R.string.map_dialog_city_eg);
		mWriteDiloag.setSecondHint(R.string.map_dialog_addr_eg);
		mWriteDiloag.setInputNum(YBWDialog.INPUT_DOUBLE);
		mWriteDiloag.setPositiveButton(R.string.sure,
				new DialogOnClickListener() {

					@Override
					public void onClick(String firstText, String secondText) {
						if (TextUtils.isEmpty(firstText)) {
							ToastUtil.showToast(MapSelectActivity.this,
									R.string.input_city_name,
									Toast.LENGTH_SHORT);
						} else if (TextUtils.isEmpty(secondText)) {
							ToastUtil.showToast(MapSelectActivity.this,
									R.string.input_addr_detail,
									Toast.LENGTH_SHORT);
							return;
						} else {
							mBtnSubmit.setClickable(false);
							mSearch.geocode(secondText, firstText);
							showProgressDialog(null, true);
							mWriteDiloag.dismiss();
						}
					}
				});
		mWriteDiloag.setNegativeButton(R.string.cancel,
				new DialogOnClickListener() {

					@Override
					public void onClick(String firstText, String secondText) {
						mWriteDiloag.dismiss();
					}
				});
		mWriteDiloag.show();
	}

	class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			closeProgressDialog();
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				ToastUtil.showToast(MapSelectActivity.this,
						R.string.network_connection_error, Toast.LENGTH_LONG);
			} else {
				ToastUtil.showToast(MapSelectActivity.this,
						R.string.network_package_error, Toast.LENGTH_LONG);
			}
		}

		@Override
		public void onGetPermissionState(int iError) {
			closeProgressDialog();
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				ToastUtil.showToast(MapSelectActivity.this,
						R.string.baidu_map_access_error, Toast.LENGTH_LONG);
			}
		}
	}

	private MKSearchListener mSearchListener = new MKSearchListener() {

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

		}

		@Override
		public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
				int arg2) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {

		}

		@Override
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			if (result == null || iError != 0) {
				closeProgressDialog();
				mBtnSubmit.setClickable(false);
				ToastUtil.showToast(MapSelectActivity.this,
						R.string.input_search_error, Toast.LENGTH_LONG);
			} else {
				switch (result.type) {
				case MKAddrInfo.MK_GEOCODE:
					setItemPoint(result);
					break;
				case MKAddrInfo.MK_REVERSEGEOCODE:
					setItemPoint(result);
					break;
				default:
					break;
				}
			}
		}
	};

	public void setItemPoint(MKAddrInfo result) {
		closeProgressDialog();
		GeoPoint point = result.geoPt;
		if (point == null) {
			mBtnSubmit.setClickable(false);
			return;
		}
		mArea.Lat = point.getLatitudeE6() / 1E6;
		mArea.Lng = point.getLongitudeE6() / 1E6;
		mArea.DetailAddress = result.strAddr;
		mTvAddr.setText(mArea.DetailAddress);
		itempoint = new OverlayItem(point, "", mArea.DetailAddress);
		itempoint.setMarker(getResources().getDrawable(R.drawable.map_mark));
		itempoint.setTitle(result.strAddr);
		mOverlay.removeAll();
		mOverlay.addItem(itempoint);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(mOverlay);
		mMapView.refresh();
		mMapController.animateTo(point);
		mBtnSubmit.setClickable(true);
	}

	@SuppressWarnings("rawtypes")
	public class MyOverlay extends ItemizedOverlay {

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		@Override
		public boolean onTap(int index) {
			return false;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mMapView) {
			if (mType == MAP_NOLY_VIEW) {
				return true;
			}
			showProgressDialog(null, true);
			mBtnSubmit.setClickable(false);
			mArea.Lng = pt.getLongitudeE6() / 1E6;
			mArea.Lat = pt.getLatitudeE6() / 1E6;
			GeoPoint point = new GeoPoint((pt.getLatitudeE6()),
					(pt.getLongitudeE6()));
			mSearch.reverseGeocode(point);
			clearOverlay();
			itempoint = new OverlayItem(point, "", mArea.DetailAddress);
			itempoint
					.setMarker(getResources().getDrawable(R.drawable.map_mark));
			mOverlay.addItem(itempoint);
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(mOverlay);
			mMapView.refresh();
			return true;
		}

	}

	public void clearOverlay() {
		mOverlay.removeAll();
		mMapView.refresh();
	}

}
