package com.bootdo.miniApp.controller;

import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.Result;
import com.bootdo.miniApp.domain.AppUserDO;
import com.bootdo.miniApp.service.AppUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 小程序用户表
 *
 * @author sky.liu
 * @email 18798620563@163.com
 * @date 2018-10-14 14:48:29
 */

@Controller
@RequestMapping("/miniApp/appUser")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @GetMapping()
    @RequiresPermissions("miniApp:appUser:appUser")
    String AppUser() {
        return "miniApp/appUser/appUser";
    }

    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("miniApp:appUser:appUser")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<AppUserDO> appUserList = appUserService.list(query);
        int total = appUserService.count(query);
        PageUtils pageUtils = new PageUtils(appUserList, total);
        return pageUtils;
    }

    @GetMapping("/add")
    @RequiresPermissions("miniApp:appUser:add")
    String add() {
        return "miniApp/appUser/add";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("miniApp:appUser:edit")
    String edit(@PathVariable("id") Integer id, Model model) {
        AppUserDO appUser = appUserService.get(id);
        model.addAttribute("appUser", appUser);
        return "miniApp/appUser/edit";
    }

    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    @RequiresPermissions("miniApp:appUser:add")
    public Result save(AppUserDO appUser) {
        if (appUserService.save(appUser) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("miniApp:appUser:edit")
    public Result update(AppUserDO appUser) {
        appUserService.update(appUser);
        return Result.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("miniApp:appUser:remove")
    public Result remove(Integer id) {
        if (appUserService.remove(id) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("miniApp:appUser:batchRemove")
    public Result remove(@RequestParam("ids[]") Integer[] ids) {
        appUserService.batchRemove(ids);
        return Result.ok();
    }

}
