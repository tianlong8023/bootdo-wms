package com.bootdo.miniApp.service;

import com.bootdo.miniApp.domain.AppUserLoginLogDO;

import java.util.List;
import java.util.Map;

/**
 * 小程序用户登录日志表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:38
 */
public interface AppUserLoginLogService {

    AppUserLoginLogDO get(Integer id);

    AppUserLoginLogDO getByBid(String bid);

    List<AppUserLoginLogDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(AppUserLoginLogDO appUserLoginLog);

    int update(AppUserLoginLogDO appUserLoginLog);

    int remove(Integer id);

    int batchRemove(Integer[] ids);
}
