package cn.phlos.mapper;

import cn.phlos.mapper.entity.UserDo;
import org.apache.ibatis.annotations.*;


/**
 * 用户表的数据库操作层
 *
 * @Author: Penghong Li
 * @Date: Create in 22:24 2020/4/1
 */
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, null, null, null,null, '1', null, null, null);")
    int register(UserDo userDo);

    @Insert("INSERT INTO `user` VALUES (null,#{mobile}, #{email}, #{password}, #{userName}, #{sex}, #{age}, #{createTime},#{updateTime}, #{isAvailble}, #{picImg}, #{qqOpenid}, #{wxOpenid});")
    int saveUser(UserDo userDo);

    @Select("SELECT * FROM `user` WHERE mobile=#{mobile};")
    UserDo existmobile(@Param("mobile") String mobile);

    @Select("SELECT user_id AS userId ,mobile AS mobile,email AS email,password AS password, user_name AS userName ,sex AS sex ,age AS age ,create_time AS createTime,is_availble AS isAvailble,pic_img AS picImg,qq_openid AS qqOpenid,wx_openid AS wxOpenid "
            + "  FROM `user`  WHERE mobile=#{0} and password=#{1};")
    UserDo login(@Param("mobile") String mobile, @Param("password") String password);

    @Select("SELECT user_id AS userId ,mobile AS mobile,email AS email,password AS password, user_name AS user_name ,sex AS sex ,age AS age ,create_time AS createTime,is_availble AS isAvailble,pic_img AS picImg,qq_openid AS qqOpenid,wx_openid AS wxOpenid"
            + " FROM `user` WHERE user_id=#{userId}")
    UserDo findByuserId(@Param("userId") Long userId);

    @Select("SELECT user_id AS userId ,mobile AS mobile,email AS email,password AS password, user_name AS user_name ,sex AS sex ,age AS age ,create_time AS createTime,is_availble AS isAvailble,pic_img AS picImg,qq_openid AS qqOpenid,wx_openid AS wxOpenid"
            + " FROM `user` WHERE qq_openid=#{qqOpenid}")
    UserDo findByOpenId(@Param("qqOpenid") String qqOpenid);

    @Update("update `user` set qq_openid =#{0} WHERE user_id=#{1}")
    int updateUserOpenId(@Param("qqOpenid") String qqOpenid, @Param("userId") Long userId);

}
