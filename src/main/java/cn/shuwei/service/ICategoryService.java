package cn.shuwei.service;

import cn.shuwei.common.ServerResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ICategoryService {

    ServerResponse addCategory(String categoryName,int parentId);

    ServerResponse updateCategory(int categoryId, String categoryName);

    ServerResponse<List> getChildParalleCategory(int categoryId);

    ServerResponse selectCategoryAndChildrenById(int categoryId);
}
