package com.sprout.web.tree;

public class TreeNode {

    private Long id;
    private String name;
    private Long parentId;
    private String nodeType;
    private boolean open;

    public TreeNode() {
    }

    public TreeNode(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public TreeNode(Long id, String name, Long parentId, String nodeType, boolean open) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.nodeType = nodeType;
        this.open = open;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
