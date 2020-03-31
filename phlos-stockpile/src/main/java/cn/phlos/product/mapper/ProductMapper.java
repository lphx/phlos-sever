package cn.phlos.product.mapper;

import cn.phlos.product.mapper.entity.ProductEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

@Mapper
public interface ProductMapper {

    @Insert("insert `product` values(#{id},#{categoryId},#{name},#{subtitle},#{mainImage},#{subImages},#{detail},#{attributeList},#{price},#{stock},#{status},#{revision},#{createdBy},#{createdTime},#{updatedBy},#{updatedTime})")
    public int saveProduct(ProductEntity productEntity);

    @Select("select id as id ,category_id as categoryId ,name as name ,subtitle as subtitle ,main_image as mainImage ,sub_images as subImages ,detail as detail ,attribute_list as attributeList ,price as price ,stock as stock ,status as status ,revision as revision ,created_by as createdBy ,created_time as createdTime ,updated_by as updatedBy ,updated_time as updatedTime from `product`")
    public ProductEntity select();

    @Select("select id as id ,category_id as categoryId ,name as name ,subtitle as subtitle ,main_image as mainImage ,sub_images as subImages ,detail as detail ,attribute_list as attributeList ,price as price ,stock as stock ,status as status ,revision as revision ,created_by as createdBy ,created_time as createdTime ,updated_by as updatedBy ,updated_time as updatedTime from `product` limit #{page} , #{size}")
    public ProductEntity selectProductByPageable(Integer  page , Integer size);

    @Select("select count(*) from `product`")
    public Integer count();

}
