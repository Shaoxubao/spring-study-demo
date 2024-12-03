package com.baoge.service;

import com.baoge.entity.UserInfoEntity;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * 洗手流程 WashHandsComponent
 */
@Slf4j
@LiteflowComponent("washHandsComponent")
public class WashHandsComponent extends NodeComponent {
    @Override
    public void process() throws Exception {
        UserInfoEntity person = this.getRequestData();
        log.info("我的名字是{}，我爱干净，我在洗手！", person.getName());
    }
}
