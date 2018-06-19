package com.bootdo.system.service;

import com.bootdo.common.domain.Tree;
import com.bootdo.system.domain.DeptDO;
import com.bootdo.system.domain.UserDO;
import com.bootdo.system.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface UserService {
	UserDO get(Long id);

	List<UserDO> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);

	int batchremove(Long[] userIds);

	boolean exit(Map<String, Object> params);

	Set<String> listRoles(Long userId);

	int resetPwd(UserVO userVO,UserDO userDO) throws Exception;
	int adminResetPwd(UserVO userVO) throws Exception;
	Tree<DeptDO> getTree();

	/**
	 * 更新个人信息
	 * @param userDO
	 * @return
	 */
	int updatePersonal(UserDO userDO);

	/**
	 * 更新个人图片
	 * @param file 图片
	 * @param avatar_data 裁剪信息
	 * @param userId 用户ID
	 * @throws Exception
	 */
    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;

    /**
     * @param username
     * @return
     * @MethodName queryByUsername
     * @Description 根据用户名查询用户信息
     * @Author sky.liu
     * @Date 2018/6/19 14:59
     * @ChangeLog
     */
    UserDO queryByUsername(String username);

    /**
     * @param user
     * @return
     * @MethodName register
     * @Description 用户注册
     * @Author sky.liu
     * @Date 2018/6/19 14:04
     * @ChangeLog
     */
    int register(UserDO user);

}
