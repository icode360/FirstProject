package com.icode.firstproject.common.constant;

/**
 * SP常量配置文件
 */
public interface ISharedPreferences {

    /*************************************SP文件名***************************************/

    /**
     * 应用程序启动管理（多进程）
     */
    @Deprecated
    String SP_USER_START = "sp_user_start";


    /**
     * 默认多进程SP
     */
    String SP_DEFAULT_MULTI_PROCESS = "sp_default_multi_process";


    /***************************************SP内容*****************************************/

    /**
     * 第一次运行及升级的时间
     */
    String APP_FRIST_RUN_AND_UPGRADE_TIME = "app_frist_run_and_upgrade_time";

    String INSTALL_VERSION = "install_version";//首次安装(包括卸载后重新安装)的版本
    /**
     * 是否第一次运行
     */
    String IS_FIRST_RUN = "is_first_run";

    /**
     * 最终的版本号
     */
    String LAST_VERSION_CODE = "last_version_code";
}
