package com.tqmall.legend.biz.sys.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by zsy on 17/5/9.
 */
@Getter
@Setter
public class UserOperateDictSearchBO {
    private Integer operateApplication;//应用0：legend1：app
    private String operateModule;//所属模块，如首页guide，接车维修reception，财务settlement

    public enum Application {
        LEGEND(0, "legend"),
        APP(1, "app"),
        MACE(2, "mace");

        private final int code;
        private final String application;

        Application(int code, String application) {
            this.code = code;
            this.application = application;
        }

        public int getCode() {
            return this.code;
        }

        public String getApplication() {
            return this.application;
        }
    }
}
