package cn.phlos.product.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductDto {

    /** 主键ID */
    private Long id;
    /** 类型ID */
    private Long categoryId;
    /** 名称 */
    private String name;
    /** 小标题 */
    private String subtitle;
    /** 主图像 */
    private String mainImage;
    /** 小标题图像 */
    private String subImages;
    /** 描述 */
    private String detail;
    /** 商品规格 */
    private String attributeList;
    /** 价格 */
    private BigDecimal price;
    /** 库存 */
    private Integer stock;
    /** 状态 */
    private Integer status;

}
