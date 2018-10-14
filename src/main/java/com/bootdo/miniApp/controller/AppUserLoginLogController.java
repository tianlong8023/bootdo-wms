package com.bootdo.miniApp.controller;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.Result;
import com.bootdo.miniApp.domain.AppUserLoginLogDO;
import com.bootdo.miniApp.service.AppUserLoginLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 小程序用户登录日志表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:38
 */

@Controller
@RequestMapping("/miniApp/appUserLoginLog")
public class AppUserLoginLogController {
    @Autowired
    private AppUserLoginLogService appUserLoginLogService;

    @GetMapping()
    @RequiresPermissions("miniApp:appUserLoginLog:appUserLoginLog")
    String AppUserLoginLog() {
        return "miniApp/appUserLoginLog/appUserLoginLog";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("miniApp:appUserLoginLog:appUserLoginLog")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AppUserLoginLogDO> appUserLoginLogList = appUserLoginLogService.list(query);
        int total = appUserLoginLogService.count(query);
        PageUtils pageUtils = new PageUtils(appUserLoginLogList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("miniApp:appUserLoginLog:add")
    String add() {
        return "miniApp/appUserLoginLog/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("miniApp:appUserLoginLog:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        AppUserLoginLogDO appUserLoginLog = appUserLoginLogService.get(id);
        model.addAttribute("appUserLoginLog", appUserLoginLog);
        return "miniApp/appUserLoginLog/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("miniApp:appUserLoginLog:add")
    public Result save(AppUserLoginLogDO appUserLoginLog) {
        if (appUserLoginLogService.save(appUserLoginLog) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("miniApp:appUserLoginLog:edit")
    public Result update(AppUserLoginLogDO appUserLoginLog) {
        appUserLoginLogService.update(appUserLoginLog);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("miniApp:appUserLoginLog:remove")
    public Result remove(Integer id) {
        if (appUserLoginLogService.remove(id) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("miniApp:appUserLoginLog:batchRemove")
    public Result remove(@RequestParam("ids[]") Integer[] ids) {
        appUserLoginLogService.batchRemove(ids);
        return Result.ok();
    }

}
