package cn.phlos.product.api;

import cn.phlos.product.dto.ProductDto;
import cn.phlos.product.service.ProductService;
import cn.phlos.util.base.BaseResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品的api接口   4/1目前测试
 */
@Api("商品的api接口")
@RestController
public class ProductApi {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public BaseResponse<List<ProductDto>> search(String name, @PathVariable("currPage") int currPage, @PathVariable("pageSize") int pageSize){
        return null;
    }

    @GetMapping("/productList")
    public BaseResponse<Object> productList(@RequestParam("currPage")int currPage,  @RequestParam("pageSize")int pageSize){
        return productService.selectProductByPageable(currPage,pageSize);
    }



}
