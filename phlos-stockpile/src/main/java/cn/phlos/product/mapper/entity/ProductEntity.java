package cn.phlos.product.mapper.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductEntity {

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
    /** 乐观锁 */
    private Integer revision;
    /** 创建人 */
    private String createdBy;
    /** 创建时间 */
    private Date createdTime;
    /** 更新人 */
    private String updatedBy;
    /** 更新时间 */
    private Date updatedTime;



}
