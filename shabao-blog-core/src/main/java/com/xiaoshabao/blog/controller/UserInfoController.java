package com.xiaoshabao.blog.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoshabao.base.component.oss.OSSFactory;
import com.xiaoshabao.base.controller.BaseController;
import com.xiaoshabao.blog.component.ContextHolder;
import com.xiaoshabao.blog.dto.AccountProfile;
import com.xiaoshabao.blog.dto.Data;
import com.xiaoshabao.blog.lang.Consts;
import com.xiaoshabao.blog.service.UserService;

/**
 * 用户信息
 */
@Controller
@RequestMapping("/user")
public class UserInfoController extends BaseController {
	@Autowired
	private ContextHolder contentHolder;
	@Autowired
	private UserService userService;
	@Autowired
	private OSSFactory ossFactory;

	@RequestMapping(value = "/avatar", method = RequestMethod.GET)
	public String avatarView(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+Views.USER_AVATAR;
	}
	
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public String post(String path, Float x, Float y, Float width, Float height, ModelMap model
			,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		AccountProfile profile = contentHolder.getProfile();
		
		if (StringUtils.isEmpty(path)) {
			model.put("data", Data.failure("请选择图片"));
			return skin+Views.USER_AVATAR;
		}
		
		if (width != null && height != null) {

			Long fileId=Long.valueOf(FilenameUtils.getBaseName(path));
			String yPath=ossFactory.build().getRealFilePath(fileId);
			String url=ossFactory.build(Consts.FilePath.AVA_DIR, "yyyy").upload(yPath,x.intValue(), y.intValue(), width.intValue(), width.intValue(), 100);
			AccountProfile user = userService.updateAvatar(profile.getId(), url);
			contentHolder.putProfile(user);
			/*
			String root = sysConfig.getString(Consts.FilePath.ROOT_PATH);
			File temp = new File(root + path);
			File scale = null;
			
			// 目标目录
			String ava100 = sysConfig.getString(Consts.FilePath.AVA_DIR) + getAvaPath(profile.getId(), 100);
			String dest = root + ava100;
			try {
				// 判断父目录是否存在
				File f = new File(dest);
		        if(!f.getParentFile().exists()){
		            f.getParentFile().mkdirs();
		        }
		        // 在目标目录下生成截图
		        String scalePath = f.getParent() + "/" + profile.getId() + ".jpg";
				ImageUtils.truncateImage(temp.getAbsolutePath(), scalePath, x.intValue(), y.intValue(), width.intValue());
		        
				// 对结果图片进行压缩
				ImageUtils.scaleImage(scalePath, dest, 100);

				AccountProfile user = userService.updateAvatar(profile.getId(), ava100);
				ShiroUtil.putProfile(user);
				
				scale = new File(scalePath);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				temp.delete();
				if (scale != null) {
					scale.delete();
				}
			}*/
		}
		return "redirect:/user/profile";
	}
	/*
	public String getAvaPath(long uid, int size) {
		String base = FilePathUtils.getAvatar(uid);
		return String.format("/%s_%d.jpg", base, size);
	}*/
	
	
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String pwdView(@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		return skin+Views.USER_PASSWORD;
	}
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String post(String oldPassword, String password, ModelMap model,@RequestParam(defaultValue=Consts.skin.DEFAULT) String skin) {
		Data data;
		try {
			AccountProfile profile = contentHolder.getProfile();
			userService.updatePassword(profile.getId(), oldPassword, password);
			
			data = Data.success("操作成功", Data.NOOP);
		} catch (Exception e) {
			data = Data.failure(e.getMessage());
		}
		model.put("data", data);
		return skin+Views.USER_PASSWORD;
	}
	
}
