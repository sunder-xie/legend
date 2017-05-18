package com.tqmall.legend.facade.report.vo;

import java.io.File;

/**
 * Created by majian on 16/10/10.
 */
public class ReportFileVo {
    private boolean recent;
    private File content;

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }
}
