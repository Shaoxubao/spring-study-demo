package com.baoge;

import com.baoge.constants.SexEnum;
import com.baoge.entity.UserInfoEntity;
import com.yomahub.liteflow.core.FlowExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LiteFlowApplication.class)
public class ComponentTest {

    @Autowired
    private FlowExecutor flowExecutor;

    @Test
    public void componentTest() {
        // 创建一个人
        UserInfoEntity personEntity = new UserInfoEntity();
        personEntity.setName("zhf");
        personEntity.setSex(SexEnum.BOY);
        // 执行回家流程
        flowExecutor.execute2Resp("goHome", personEntity);
    }
}
