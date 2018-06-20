package com.bootdo.system.controller;

import com.bootdo.common.config.Constant;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.domain.Tree;
import com.bootdo.common.utils.Result;
import com.bootdo.system.domain.DeptDO;
import com.bootdo.system.service.DeptService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:40:36
 */

@Controller
@RequestMapping("/system/sysDept")
public class DeptController extends BaseController {
	private String prefix = "system/dept";
	@Autowired
	private DeptService sysDeptService;

	@GetMapping()
	@RequiresPermissions("system:sysDept:sysDept")
	String dept() {
		return prefix + "/dept";
	}

	@ApiOperation(value="获取部门列表", notes="")
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:sysDept:sysDept")
	public List<DeptDO> list() {
		Map<String, Object> query = new HashMap<>(16);
		List<DeptDO> sysDeptList = sysDeptService.list(query);
		return sysDeptList;
	}

	@GetMapping("/add/{pId}")
	@RequiresPermissions("system:sysDept:add")
	String add(@PathVariable("pId") Long pId, Model model) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "总部门");
		} else {
			model.addAttribute("pName", sysDeptService.get(pId).getName());
		}
		return  prefix + "/add";
	}

	@GetMapping("/edit/{deptId}")
	@RequiresPermissions("system:sysDept:edit")
	String edit(@PathVariable("deptId") Long deptId, Model model) {
		DeptDO sysDept = sysDeptService.get(deptId);
		model.addAttribute("sysDept", sysDept);
		if(Constant.DEPT_ROOT_ID.equals(sysDept.getParentId())) {
			model.addAttribute("parentDeptName", "无");
		}else {
			DeptDO parDept = sysDeptService.get(sysDept.getParentId());
			model.addAttribute("parentDeptName", parDept.getName());
		}
		return  prefix + "/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:sysDept:add")
    public Result save(DeptDO sysDept) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		if (sysDeptService.save(sysDept) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:sysDept:edit")
    public Result update(DeptDO sysDept) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		if (sysDeptService.update(sysDept) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:remove")
    public Result remove(Long deptId) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentId", deptId);
		if(sysDeptService.count(map)>0) {
            return Result.error(1, "包含下级部门,不允许修改");
        }
		if(sysDeptService.checkDeptHasUser(deptId)) {
			if (sysDeptService.remove(deptId) > 0) {
                return Result.ok();
            }
		}else {
            return Result.error(1, "部门包含用户,不允许修改");
        }
        return Result.error();
    }

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:sysDept:batchRemove")
    public Result remove(@RequestParam("ids[]") Long[] deptIds) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		sysDeptService.batchRemove(deptIds);
        return Result.ok();
    }

	@GetMapping("/tree")
	@ResponseBody
	public Tree<DeptDO> tree() {
		Tree<DeptDO> tree = new Tree<DeptDO>();
		tree = sysDeptService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return  prefix + "/deptTree";
	}

	@ApiOperation(value = "获取部门列表-前后端分离项目", notes = "")
	@ResponseBody
	@GetMapping("/queryList")
	public Result queryList() {
		Result result = new Result();
		Map<String, Object> query = new HashMap<>(16);
		result.put("data", sysDeptService.list(query));
		return result;
	}

	@ApiOperation(value = "获取部门树-前后端分离项目", notes = "")
	@ResponseBody
	@GetMapping("/queryTree")
	public Result queryTree() {
		Result result = new Result();
		result.put("data", sysDeptService.getTree());
		return result;
	}

}
