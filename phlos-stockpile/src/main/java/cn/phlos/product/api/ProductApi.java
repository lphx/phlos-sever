package cn.phlos.product.api;

import cn.phlos.product.dto.ProductDto;
import cn.phlos.util.base.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class ProductApi {

    @GetMapping("/search")
    public BaseResponse<List<ProductDto>> search(String name, @PageableDefault(page = 0, value = 10) Pageable pageable){
        return null;
    }

}
