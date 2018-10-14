package com.bootdo.miniApp.controller;

import com.bootdo.common.utils.Result;
import com.bootdo.miniApp.service.AppCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 小程序公共接口
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:29
 */

@Controller
@RequestMapping("/miniApp/common")
public class AppCommonController {

    @Autowired
    private AppCommonService appCommonService;

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/login")
    public Result save(@RequestParam("code") String code) {
        Result result = new Result();
        result.put("data", appCommonService.login(code));
        return result;
    }

}
