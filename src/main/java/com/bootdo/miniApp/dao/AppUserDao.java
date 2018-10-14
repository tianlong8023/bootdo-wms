package com.bootdo.miniApp.dao;

import com.bootdo.miniApp.domain.AppUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 小程序用户表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:29
 */
@Mapper
public interface AppUserDao {

    AppUserDO get(Integer id);

    AppUserDO getByBid(String bid);

    List<AppUserDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(AppUserDO appUser);

    int update(AppUserDO appUser);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

}
