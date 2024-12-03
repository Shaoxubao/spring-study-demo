package com.baoge.service;

import com.baoge.entity.UserInfoEntity;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * 吃饭流程 HavingDinnerComponent
 */
@Slf4j
@LiteflowComponent("havingDinnerComponent")
public class HavingDinnerComponent extends NodeComponent {

    @Override
    public void process() throws Exception {
        UserInfoEntity person = this.getRequestData();
        log.info("我的名字是{}，我在吃饭！", person.getName());
    }
}