package cn.phlos.mapper;

import cn.phlos.mapper.entity.UserTokenDo;
import org.apache.ibatis.annotations.*;

/**
 * 用户TokenMapper
 *
 * @Author: Penghong Li
 * @Date: Create in 20:31 2020/4/4
 */

@Mapper
public interface UserTokenMapper {


    /**
     * 根据userid+loginType +is_availability=0 进行查询
     *
     * @param userId
     * @param loginType
     * @return
     */
    @Select("SELECT id as id ,token as token ,login_type as LoginType, device_info as deviceInfo ,is_availability as isAvailability,user_id as userId"
            + " , create_time as createTime,update_time as updateTime   FROM user_token WHERE user_id=#{userId} AND login_type=#{loginType} and is_availability ='0'; ")
    UserTokenDo selectByUserIdAndLoginType(@Param("userId") Long userId, @Param("loginType") String loginType);

    /**
     * 根据userId+loginType token的状态修改为不可用
     *
     * @param token
     * @return
     */
    // @Update(" update meite_user_token set is_availability
    // ='1',update_time=now() where user_id=#{userId} and login_type
    // =#{loginType} ")
    // int updateTokenAvailability(@Param("userId") Long userId,
    // @Param("loginType") String loginType);
    @Update(" update user_token set is_availability  ='1', update_time=now() where token=#{token}")
    int updateTokenAvailability(@Param("token") String token);

    // INSERT INTO `user_token` VALUES ('2', '1', 'PC', '苹果7p', '1', '1');

    /**
     * token记录表中插入一条记录
     *
     * @param userTokenDo
     * @return
     */
    @Insert("    INSERT INTO `user_token` VALUES (null, #{token},#{loginType}, #{deviceInfo}, 0, #{userId} ,now(),null ); ")
    int insertUserToken(UserTokenDo userTokenDo);


}
