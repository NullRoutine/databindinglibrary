package com.twq.databindinghelper.util;


import com.twq.databindinghelper.application.DataBindingApplication;

/**
 * SPUtils 工具辅助类
 */

public class SPUtilHelpr {


	//国家区号
	public static final String COUNTRYCODE = "countrycode";

	//设备Id
	private static final String DEVICE_ID = "deviceid";

	//是否第一次进入APP
	private static final String ISFIRSTIN="isfirstin";

	//是否第一次显示首页提示
	private static final String ISFIRSTIN2="isfirstin3";

	//是否修改了服务城市数据
	private static final String ISCHANGECITY="ischangecity";

    //权限等级,=1时 保存targetId
	private static final String TARGETID="targetid";



	/**
	 * 设置国家区号
	 * @param s
	 */
	public static void saveCountryCode(String s)
	{
		SPUtils.put(DataBindingApplication.getInstance(),COUNTRYCODE,"+"+s);
	}

	/**
	 * 获取国家区号
	 * @return
	 */
	public static String getCountryCode()
	{
		return SPUtils.getString(DataBindingApplication.getInstance(),COUNTRYCODE, "+86");
	}

	/**
	 * 获取设备ID
	 * @return
	 */
	public static String getDeviceId(){

	return 	SPUtils.getString(DataBindingApplication.getInstance(),DEVICE_ID,null);

	}
	/**
	 * 获取设备ID
	 * @return
	 */
	public static void saveDeviceId(String id){

		SPUtils.put(DataBindingApplication.getInstance(),DEVICE_ID,id);

	}

	/**
	 * 是否第一次进入
	 * @return
	 */
	public static Boolean IsfirstIn() {
		return 	SPUtils.getBoolean(DataBindingApplication.getInstance(), ISFIRSTIN, true);
	}

	/**
	 * 是否第一次进入 用于等级弹框提醒
	 * @return
	 */
	public static Boolean IsfirstInTips() {
		return 	SPUtils.getBoolean(DataBindingApplication.getInstance(), ISFIRSTIN2, true);
	}



	/**
	 * 改变是否第一次进入状态
	 * @return
	 */
	public static void SavefirstIn() {
		SPUtils.put(DataBindingApplication.getInstance(), ISFIRSTIN, false);
	}

	/**
	 * 改变是否第一次进入状态 用于等级弹框提醒
	 * @return
	 */
	public static void SavefirstInTips() {
		SPUtils.put(DataBindingApplication.getInstance(), ISFIRSTIN2, false);
	}

	/**
	 * 是否修改过城市
	 * @return
	 */
	public static Boolean IsChangeCity() {
		return 	SPUtils.getBoolean(DataBindingApplication.getInstance(), ISCHANGECITY, false);
	}


	/**
	 * 改变是否修改过城市
	 * @return
	 */
	public static void SetIsChangeCity(boolean isChange) {
		SPUtils.put(DataBindingApplication.getInstance(), ISCHANGECITY, isChange);
	}

	/**
	 * 保存targetId
	 * @param s
	 */
	public static void savtargetId(String s)
	{
		SPUtils.put(DataBindingApplication.getInstance(),TARGETID,s);
	}

	/**
	 * 获取targetId
	 * @return
	 */
	public static String gettargetId()
	{
		return SPUtils.getString(DataBindingApplication.getInstance(),TARGETID, "0");
	}


}
