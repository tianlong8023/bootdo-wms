package com.bootdo.miniApp.service.impl;

import com.bootdo.common.constants.DeleteStatusEnum;
import com.bootdo.common.exception.BDException;
import com.bootdo.common.exception.ExceptionTypeEnum;
import com.bootdo.common.service.IdWorkerService;
import com.bootdo.common.utils.BeanUtils;
import com.bootdo.common.utils.CommonUtil;
import com.bootdo.common.utils.JSONUtils;
import com.bootdo.common.utils.https.HttpClientSender;
import com.bootdo.miniApp.domain.AppUserDO;
import com.bootdo.miniApp.domain.AppUserLoginLogDO;
import com.bootdo.miniApp.service.AppCommonService;
import com.bootdo.miniApp.service.AppUserLoginLogService;
import com.bootdo.miniApp.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppCommonServiceImpl implements AppCommonService {
    private Logger logger = LoggerFactory.getLogger(AppCommonServiceImpl.class);

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private IdWorkerService idWorkerService;

    @Autowired
    private AppUserLoginLogService appUserLoginLogService;

    //微信小程序参数
    private String host = "https://api.weixin.qq.com";
    private String appid = "wx19a3e128ab36873a";
    private String secret = "15490233e2cbc4f0d591814fa76b83bd";
    private String grant_type = "authorization_code";

    @Override
    public AppUserDO login(String code) {
        if (CommonUtil.isEmpty(code)) {
            logger.error("小程序登錄，用戶code不能為空！");
            throw new BDException(ExceptionTypeEnum.APP, 20004);
        }
        //1、請求微信小程序api獲取用戶openid信息
        String responseBody = null;
        try {
            logger.info("请求微信小程序获取用户openid，js_code[{}]", code);
            // 组装url参数
            String urlParams = "appid=" + appid + "&secret=" + secret + "&grant_type=" + grant_type + "&js_code=" + code;
            responseBody = HttpClientSender.sendHttpGet(this.host, "/sns/jscode2session/", urlParams);
            logger.info("请求微信小程序获取用户openid，返回数据：{}", responseBody);
        } catch (IOException e) {
            logger.error("请求微信小程序接口失败", e);
            throw new BDException(ExceptionTypeEnum.APP, 20001);
        }
        //错误码
        String errcode;
        //错误信息
        String errmsg;
        //用户唯一标识
        String openid;
        //会话密钥
        String session_key;
        //用户在开放平台的唯一标识符
        String unionid;
        try {
            Map<String, Object> map = JSONUtils.jsonToMap(responseBody);
            errcode = String.valueOf(map.get("errcode"));
            errmsg = String.valueOf(map.get("errmsg"));
            openid = String.valueOf(map.get("openid"));
            session_key = String.valueOf(map.get("session_key"));
            unionid = String.valueOf(map.get("unionid"));
        } catch (Exception e) {
            logger.error("微信小程序登录返回值JSON转化失败，", responseBody);
            throw new BDException(ExceptionTypeEnum.APP, 20002);
        }
        //用户微信标识为空，则提示
        if (CommonUtil.isEmpty(openid)) {
            logger.error("获取用户openid失败，{}-{}", errcode, errmsg);
            throw new BDException(ExceptionTypeEnum.APP, 20003);
        }
        //2、根據用戶openid獲取系統用戶信息，不存在則新增一個
        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);
        map.put("isDeleted", DeleteStatusEnum.IsNotDeleted.getCode());
        List<AppUserDO> appUserDOList = appUserService.list(map);
        AppUserDO appUserDO = null;
        Date nowTime = new Date();
        if (CommonUtil.isEmpty(appUserDOList)) {
            appUserDO = new AppUserDO(idWorkerService.nextStringId(), openid, session_key, unionid);
            appUserDO.setIsDeleted(DeleteStatusEnum.IsNotDeleted.getCode());
            appUserDO.setGmtCreate(nowTime);
            appUserService.save(appUserDO);
        } else {
            appUserDO = appUserDOList.get(0);
            appUserDO.setSessionKey(session_key);
            appUserDO.setUnionid(unionid);
            appUserDO.setGmtModified(nowTime);
            appUserService.update(appUserDO);
        }
        //3、保存登錄日誌
        AppUserLoginLogDO loginLogDO = BeanUtils.copyProperties(AppUserLoginLogDO.class, appUserDO);
        loginLogDO.setId(null);
        loginLogDO.setLoginCode(code);
        appUserLoginLogService.save(loginLogDO);
        return appUserDO;
    }
}
