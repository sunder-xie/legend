package com.tqmall.legend.facade.print;

import org.springframework.ui.Model;

/**
 * Created by lixiao on 17/3/22.
 */
public interface PrintFacade {

    /**
     *  打印自定义配置
     * @param shopId
     */
    public void printSelfConfig(Model model, Long shopId);
}
