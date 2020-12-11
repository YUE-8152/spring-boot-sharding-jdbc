package com.cardlan.sharding.jdbc.common.core;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ExportColumn {
    private String enName;
    private String cnName;

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public ExportColumn() {
    }

    public ExportColumn(String enName, String cnName) {
        this.enName = enName;
        this.cnName = cnName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("enName", enName)
                .append("cnName", cnName)
                .toString();
    }
}
