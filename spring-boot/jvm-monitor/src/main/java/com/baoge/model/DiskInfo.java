package com.baoge.model;

import lombok.Data;

@Data
public class DiskInfo {

    private String size;

    private String used;

    private String available;

    private String usePercent;
}
