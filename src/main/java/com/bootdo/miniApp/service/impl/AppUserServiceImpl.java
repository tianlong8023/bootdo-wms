package com.bootdo.miniApp.service.impl;

import com.bootdo.miniApp.dao.AppUserDao;
import com.bootdo.miniApp.domain.AppUserDO;
import com.bootdo.miniApp.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserDao appUserDao;

    @Override
    public AppUserDO get(Integer id) {
        return appUserDao.get(id);
    }

    @Override
    public AppUserDO getByBid(String bid) {
        return appUserDao.getByBid(bid);
    }

    @Override
    public List<AppUserDO> list(Map<String, Object> map) {
        return appUserDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return appUserDao.count(map);
    }

    @Override
    public int save(AppUserDO appUser) {
        return appUserDao.save(appUser);
    }

    @Override
    public int update(AppUserDO appUser) {
        return appUserDao.update(appUser);
    }

    @Override
    public int remove(Integer id) {
        return appUserDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return appUserDao.batchRemove(ids);
    }

}
