package cn.shuwei.service.impl;

import cn.shuwei.common.ResponseCode;
import cn.shuwei.common.ServerResponse;
import cn.shuwei.dao.CategoryMapper;
import cn.shuwei.dao.ProductMapper;
import cn.shuwei.pojo.Category;
import cn.shuwei.pojo.Product;
import cn.shuwei.service.IProductService;
import cn.shuwei.utils.DateTimeUtil;
import cn.shuwei.utils.PropertiesUtil;
import cn.shuwei.vo.ProductDetailVo;
import cn.shuwei.vo.ProductListVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 保存或更新
     *
     * @param product 前端传递过来的参数
     * @return
     */
    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            // 子图不为空 将子图第一张复制给主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                // 有id表示为更新
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createBySuccess("更新产品失败");
            } else {
                // 没有id则为新增
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createBySuccess("新增产品失败");
            }
        }
        return ServerResponse.createByErrorMessage("参数不正确");
    }

    /**
     * 修改上下架状态
     *
     * @param prodcutId 产品id
     * @param status    状态
     * @return
     */
    @Override
    public ServerResponse<String> setSaleStatus(Integer prodcutId, Integer status) {
        if (prodcutId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(prodcutId);
        product.setStatus(status);
        int rowCounbt = productMapper.updateByPrimaryKeySelective(product);
        if (rowCounbt > 0) {
            return ServerResponse.createBySuccess("修改状态成功");
        }
        return ServerResponse.createByErrorMessage("修改状态失败");
    }

    /**
     * 查询单个产品信息
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVO = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        productDetailVo.setImageHost(PropertiesUtil.getProperty("oss.host","https://img.zhangding.work"));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            // 默认根节点
            productDetailVo.setParentCategoryId(0);
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        // mybatis  pagehelper 使用方法
        // startpage  开始
        PageHelper.startPage(pageNum,pageSize);
        // 填充sql查询逻辑
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = new ArrayList<>();
        for (Product product:productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        // pageHelper收尾
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("oss.host","https://img.zhangding.work"));
        return productListVo;
    }
}
