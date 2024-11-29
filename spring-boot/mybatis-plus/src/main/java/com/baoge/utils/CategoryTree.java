package com.baoge.utils;

import java.sql.*;
import java.util.ArrayList;   
import java.util.HashMap;   
import java.util.List;   
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryTree {

    // Category class to hold data
    static class Category {
        int id;
        String name;

        int parentId;

        List<Category> children = new ArrayList<>();

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Category(int id, String name, int parentId) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
        }

        @Override
        public String toString() {
            return "Category{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/babytun?useUnicode=true&characterEncoding=UTF-8&useAffectedRows=true&allowMultiQueries=true&useSSL=false&zeroDateTimeBehavior=convertToNull&failOverReadOnly=false&maxReconnects=10&connectTimeout=60000&socketTimeout=60000&serverTimezone=GMT%2B8";
        String user = "root";
        String password = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Map<Integer, Category> categoryMap = new HashMap<>();

            // Query to get all categories
            String sql = "SELECT id, name, parent_id FROM Category";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                List<Category> categories = new ArrayList<>();
                // Create Category objects and store them in a map
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int parentId = rs.getInt("parent_id");

                    Category category = new Category(id, name);
                    categoryMap.put(id, category);

                    Category categoryNew = new Category(id, name, parentId);
                    categories.add(categoryNew);
                }
                // 返回树形结构
                List<TreeData> result = buildPlantResultData(categories);
                System.out.println(result);

                for (Category category : categories) {
                    int parentId = category.parentId;
                    // If parentId is not null, add this category to its parent's children list
                    if (parentId != 0) {
                        Category parentCategory = categoryMap.get(parentId);
                        if (parentCategory != null) {
                            parentCategory.children.add(category);
                        }
                    }
                }
            }

            // Find root categories (categories with no parent)
            List<Category> rootCategories = new ArrayList<>();
            for (Category category : categoryMap.values()) {
                if (categoryMap.get(category.id).children.isEmpty()) {
                    rootCategories.add(category);
                }
            }

            // Print the category tree
            for (Category root : rootCategories) {
                printCategoryTree(root, 0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to print the category tree
    private static void printCategoryTree(Category category, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(category.name);
        for (Category child : category.children) {
            printCategoryTree(child, level + 1);
        }
    }

    private static List<TreeData> buildPlantResultData(List<Category> categoryList) {
        // 所有数据
        List<TreeData> treeData = categoryList.stream()
                .map(item -> new TreeData(item.id, item.name, item.parentId, new ArrayList<>()))
                .collect(Collectors.toList());
        // 根节点
        List<TreeData> rootData = treeData.stream().filter(d -> d.getParentId() == 0).collect(Collectors.toList());
        // 填充数据
        for (TreeData tree : rootData) {
            treeData.add(findChildren(tree, treeData));
        }

        return rootData;
    }

    private static TreeData findChildren(TreeData tree, List<TreeData> list) {
        List<TreeData> treeDataList = list.stream()
                .filter(node -> tree.getId() == node.getParentId())
                .collect(Collectors.toList());
        for (TreeData treeData : treeDataList) {
            if (tree.getChildren() == null) {
                tree.setChildren(new ArrayList<>());
            }
            tree.getChildren().add(findChildren(treeData, list));
        }

//        list.stream()
//                .filter(node -> tree.getId() == node.getParentId())
//                .collect(Collectors.toList())
//                .forEach(node -> {
//                    if (tree.getChildren() == null) {
//                        tree.setChildren(new ArrayList<>());
//                    }
//                    tree.getChildren().add(findChildren(node, list));
//                });
        return tree;
    }
}
