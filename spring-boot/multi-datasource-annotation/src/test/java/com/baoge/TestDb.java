package com.baoge;

import com.baoge.entity.User;
import com.baoge.mapper.TestDb2Mapper;
import com.baoge.mapper.TestDbMapper;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class TestDb {

    @Resource
    private TestDbMapper testDbMapper;

    @Resource
    private TestDb2Mapper testDb2Mapper;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("test6");
        testDbMapper.insert(user);
        testDb2Mapper.insert(user);
    }

    @Test
    public void test2() {
        User user = new User();
        user.setUsername("test6");
        // 切换指定数据源
        DynamicDataSourceContextHolder.push("slave_1");
        testDbMapper.insert(user);
        // 查看当前数据源
        log.info(DynamicDataSourceContextHolder.peek());
        // 移除数据源，恢复master数据源
        DynamicDataSourceContextHolder.clear();
        testDbMapper.insert(user);
    }
}
