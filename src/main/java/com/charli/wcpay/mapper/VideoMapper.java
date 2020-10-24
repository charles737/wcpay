package com.charli.wcpay.mapper;

import com.charli.wcpay.domain.Video;
import com.charli.wcpay.provider.VideoProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * video数据访问层
 */
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE id = #{id}")
    Video findById(int id);

    //@Update("UPDATE video SET title=#{title} WHERE id=#{id}")
    @UpdateProvider(type = VideoProvider.class,method = "updateVideo")
    int update(Video video);

    @Delete("DELETE FROM video WHERE id=#{id}")
    int delete(int id);

    @Insert("INSERT INTO `wechatpay`.`video`(`title`, `summary`, `cover_img`, `view_num`, " +
            "`price`, `create_time`, `online`, `point`) VALUES (#{title}, #{summary}, #{coverImg}," +
            " #{viewNum}, #{price}, #{createTime}, #{online}, #{point});")
    /**
     * 保存对象，获取数据库自增id
     */
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    int insert(Video video);
}
