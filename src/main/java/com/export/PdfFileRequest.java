package com.export;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class PdfFileRequest {

    private String fileName;

    private String sourceHtmlUrl;

    public String getFileName() {
        return fileName;
    }

    public String getSourceHtmlUrl() {
        return sourceHtmlUrl;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSourceHtmlUrl(String sourceHtmlUrl) {
        this.sourceHtmlUrl = sourceHtmlUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fileName", this.fileName)
                .append("sourceHtmlUrl", this.sourceHtmlUrl)
                .toString();
    }
}
