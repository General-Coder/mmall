package cn.shuwei.controller.backend;

import cn.shuwei.common.Const;
import cn.shuwei.common.ResponseCode;
import cn.shuwei.common.ServerResponse;
import cn.shuwei.pojo.Product;
import cn.shuwei.pojo.User;
import cn.shuwei.service.IProductService;
import cn.shuwei.service.IUserService;
import cn.shuwei.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台产品类
 */

@RestController
@RequestMapping("/admin/product")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    /**
     * 保存或更新
     *
     * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //产品业务逻辑 保存或更新
            return iProductService.saveOrUpdateProduct(product);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 修改状态
     *
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public ServerResponse<String> setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //产品业务逻辑 保存或更新
            return iProductService.setSaleStatus(productId, status);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取单个商品信息
     *
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //产品业务逻辑 保存或更新
            return iProductService.managerProductDetail(productId);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ServerResponse<PageInfo> getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //产品业务逻辑 保存或更新
            return iProductService.getProductList(pageNum, pageSize);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


}
