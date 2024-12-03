package com.baoge.service;

import com.baoge.entity.UserInfoEntity;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * 看电视流程 WatchTVComponent
 */
@Slf4j
@LiteflowComponent("watchTVComponent")
public class WatchTVComponent extends NodeComponent {

    @Override
    public void process() throws Exception {
        UserInfoEntity person = this.getRequestData();
        log.info("我的名字是{}，我在看电视！", person.getName());
    }
}