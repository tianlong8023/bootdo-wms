package com.bootdo.common.controller;

import com.bootdo.common.config.BootdoConfig;
import com.bootdo.common.domain.FileDO;
import com.bootdo.common.service.FileService;
import com.bootdo.common.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-19 16:02:20
 */
@Controller
@RequestMapping("/common/sysFile")
public class FileController extends BaseController {

	@Autowired
	private FileService sysFileService;

	@Autowired
	private BootdoConfig bootdoConfig;

	@GetMapping()
	@RequiresPermissions("common:sysFile:sysFile")
	String sysFile(Model model) {
		Map<String, Object> params = new HashMap<>(16);
		return "common/file/file";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("common:sysFile:sysFile")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<FileDO> sysFileList = sysFileService.list(query);
		int total = sysFileService.count(query);
		PageUtils pageUtils = new PageUtils(sysFileList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	// @RequiresPermissions("common:bComments")
	String add() {
		return "common/sysFile/add";
	}

	@GetMapping("/edit")
	// @RequiresPermissions("common:bComments")
	String edit(Long id, Model model) {
		FileDO sysFile = sysFileService.get(id);
		model.addAttribute("sysFile", sysFile);
		return "common/sysFile/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("common:info")
    public Result info(@PathVariable("id") Long id) {
        FileDO sysFile = sysFileService.get(id);
        return Result.ok().put("sysFile", sysFile);
    }

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("common:save")
    public Result save(FileDO sysFile) {
        if (sysFileService.save(sysFile) > 0) {
            return Result.ok();
        }
        return Result.error();
    }

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("common:update")
    public Result update(@RequestBody FileDO sysFile) {
        sysFileService.update(sysFile);

        return Result.ok();
    }

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	// @RequiresPermissions("common:remove")
    public Result remove(Long id, HttpServletRequest request) {
        if ("test".equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		String fileName = bootdoConfig.getUploadPath() + sysFileService.get(id).getUrl();
		if (sysFileService.remove(id) > 0) {
			boolean b = FileUtil.deleteFile(fileName);
			if (!b) {
                return Result.error("数据库记录删除成功，文件删除失败");
            }
            return Result.ok();
        } else {
            return Result.error();
        }
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("common:remove")
    public Result remove(@RequestParam("ids[]") Long[] ids) {
        if ("test".equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		sysFileService.batchRemove(ids);
        return Result.ok();
    }

	@ResponseBody
	@PostMapping("/upload")
    Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if ("test".equals(getUsername())) {
            return Result.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
		String fileName = file.getOriginalFilename();
		Date now = new Date();
		String url = "/files/" + DateUtils.format(now) + "/" + FileUtil.renameToUUID(fileName);
		FileDO sysFile = new FileDO(FileType.fileType(fileName), url, now);
		try {
			FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadPath() + url, fileName);
		} catch (Exception e) {
            return Result.error();
        }

		if (sysFileService.save(sysFile) > 0) {
            return Result.ok().put("fileName", sysFile.getUrl());
        }
        return Result.error();
    }


}
