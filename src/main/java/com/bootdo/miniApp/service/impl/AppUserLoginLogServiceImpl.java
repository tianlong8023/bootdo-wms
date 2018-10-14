package com.bootdo.miniApp.service.impl;

import com.bootdo.miniApp.dao.AppUserLoginLogDao;
import com.bootdo.miniApp.domain.AppUserLoginLogDO;
import com.bootdo.miniApp.service.AppUserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AppUserLoginLogServiceImpl implements AppUserLoginLogService {
    @Autowired
    private AppUserLoginLogDao appUserLoginLogDao;

    @Override
    public AppUserLoginLogDO get(Integer id) {
        return appUserLoginLogDao.get(id);
    }

    @Override
    public AppUserLoginLogDO getByBid(String bid) {
        return appUserLoginLogDao.getByBid(bid);
    }

    @Override
    public List<AppUserLoginLogDO> list(Map<String, Object> map) {
        return appUserLoginLogDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return appUserLoginLogDao.count(map);
    }

    @Override
    public int save(AppUserLoginLogDO appUserLoginLog) {
        return appUserLoginLogDao.save(appUserLoginLog);
    }

    @Override
    public int update(AppUserLoginLogDO appUserLoginLog) {
        return appUserLoginLogDao.update(appUserLoginLog);
    }

    @Override
    public int remove(Integer id) {
        return appUserLoginLogDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids) {
        return appUserLoginLogDao.batchRemove(ids);
    }

}
