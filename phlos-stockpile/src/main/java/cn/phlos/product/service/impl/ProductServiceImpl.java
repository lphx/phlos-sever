package cn.phlos.product.service.impl;

import cn.phlos.product.dto.ProductDto;
import cn.phlos.product.mapper.ProductMapper;
import cn.phlos.product.mapper.entity.ProductEntity;
import cn.phlos.product.service.ProductService;
import cn.phlos.util.base.BaseApiService;
import cn.phlos.util.base.BaseResponse;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseApiService<Object> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public int saveProduct(ProductEntity productEntity) {
        return productMapper.saveProduct(productEntity);
    }

    @Override
    public ProductEntity selectById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public BaseResponse<Object> selectProductByPageable(int currPage, int pageSize) {
        int firstIndex = (currPage - 1) * pageSize;
        int lastIndex = currPage * pageSize;
        List<ProductDto> productDtos = productMapper.selectProductByPageable(currPage, pageSize);
        return setResultSuccess(productDtos);
    }
}
