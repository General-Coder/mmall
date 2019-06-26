package cn.shuwei.controller.backend;

import cn.shuwei.common.Const;
import cn.shuwei.common.ResponseCode;
import cn.shuwei.common.ServerResponse;
import cn.shuwei.pojo.User;
import cn.shuwei.service.ICategoryService;
import cn.shuwei.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/admin/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ServerResponse addCategory(HttpSession session, String category, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        // 校验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 增加处理分类的逻辑
            return categoryService.addCategory(category, parentId);
        }
        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public ServerResponse setCategoryName(HttpSession session, int categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        // 校验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 增加处理分类的逻辑
            return categoryService.updateCategory(categoryId, categoryName);
        }
        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ServerResponse<List> getChildParalleCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        // 校验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 增加处理分类的逻辑
            return categoryService.getChildParalleCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }

    @RequestMapping(value = "/category/deep", method = RequestMethod.GET)
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        // 校验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 查询当前节点的id和递归子节点的id
            return categoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
    }
}

