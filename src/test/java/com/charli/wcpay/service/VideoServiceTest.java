package com.charli.wcpay.service;

import com.charli.wcpay.domain.Video;
import com.charli.wcpay.mapper.VideoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoMapper videoMapper;

    @Test
    public void findAll() {

        List<Video> list = videoService.findAll();
        assertNotNull(list);
        for (Video video : list) {
            System.out.println(video.getTitle());
        }
    }

    @Test
    public void findById() {

        Video video = videoService.findById(3);
        assertNotNull(video);
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void save() {
    }
}