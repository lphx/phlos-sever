package cn.phlos.product.service;

import cn.phlos.product.dto.ProductDto;
import cn.phlos.product.mapper.entity.ProductEntity;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface ProductService {

    public int saveProduct(ProductEntity productEntity);

    public ProductEntity selectById(Long id);

    public BaseResponse<Object> selectProductByPageable(int currPage , int pageSize);

}
