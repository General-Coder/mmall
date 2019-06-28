package cn.shuwei.service;

import cn.shuwei.common.ServerResponse;
import cn.shuwei.pojo.Product;
import cn.shuwei.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer prodcutId,Integer status);

    ServerResponse<ProductDetailVo> managerProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
}
