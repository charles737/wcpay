package com.charli.wcpay.mapper;

import com.charli.wcpay.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    /**
     * 根据主键Id查询
     * select这里用#，不要用$符号，会有注入风险
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{id}")
    User findById(@Param("id") int userId);

    /**
     * 根据openid找用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User findByOpenId(@Param("openid") String openid);

    /**
     * 保存用户信息
     * @param user
     * Options作用：拿到主键信息，自动放到对象信息中
     * @return
     */
    @Insert("INSERT INTO `wechatpay`.`user`(`openid`, `name`, `head_img`, `phone`," +
            " `sign`, `sex`, `city`, `create_time`)" +
            " VALUES (#{openid},#{name},#{headImg},#{phone}," +
            "#{sign},#{sex},#{city},#{createTime});")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int save(User user);

    /**
     * 通过手机号注册
     * @param user
     * @return
     */
    @Insert("INSERT INTO `wechatpay`.`user`(`openid`, `name`, `head_img`, `phone`," +
            " `sign`, `sex`, `city`, `create_time`)" +
            " VALUES (#{openid},#{name},#{headImg},#{phone}," +
            "#{sign},#{sex},#{city},#{createTime});")
    @Options(useGeneratedKeys = true, keyProperty = "phone", keyColumn = "phone")
    int saveByPhone(User user);
}
