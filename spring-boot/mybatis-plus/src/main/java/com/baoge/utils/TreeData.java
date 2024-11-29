package com.baoge.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreeData {
    private int Id;

    private String name;

    private int parentId;

    private List<TreeData> children;

}
