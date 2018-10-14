package com.bootdo.miniApp.service;

import com.bootdo.miniApp.domain.AppUserDO;

/**
 * <br>
 * <b>功能：</b> 小程序公共 AppCommonService <br>
 * <b>作者：</b> sky.liu <br>
 * <b>日期：</b> 2018/10/14 15:09 <br>
 * <b>版权所有：</b> COPYRIGHT © 2018 联寓智能.沪ICP备 16013189号-1. <br>
 */
public interface AppCommonService {

    /**
     * 小程序登录
     *
     * @param code 用户code值
     * @return
     */
    AppUserDO login(String code);

}
