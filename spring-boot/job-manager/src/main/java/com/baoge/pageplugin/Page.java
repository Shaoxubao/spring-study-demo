package com.baoge.pageplugin;

import java.io.Serializable;

public class Page implements Serializable {
    protected int showCount;
    protected int totalPage;
    protected int totalResult;
    protected int currentPage;
    protected int currentResult;
    private boolean entityOrField;
    private static final long serialVersionUID = 1L;

    public Page() {
    }

    public int getTotalPage() {
        if (this.totalResult % this.getShowCount() == 0) {
            this.totalPage = this.totalResult / this.getShowCount();
        } else {
            this.totalPage = this.totalResult / this.getShowCount() + 1;
        }

        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalResult() {
        return this.totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public int getCurrentPage() {
        if (this.currentPage <= 0) {
            this.currentPage = 1;
        }

        if (this.currentPage > this.getTotalPage()) {
            this.currentPage = this.getTotalPage();
        }

        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getShowCount() {
        return this.showCount == 0 ? 10 : this.showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public int getCurrentResult() {
        this.currentResult = (this.getCurrentPage() - 1) * this.getShowCount();
        if (this.currentResult < 0) {
            this.currentResult = 0;
        }

        return this.currentResult;
    }

    public void setCurrentResult(int currentResult) {
        this.currentResult = currentResult;
    }

    public boolean isEntityOrField() {
        return this.entityOrField;
    }

    public void setEntityOrField(boolean entityOrField) {
        this.entityOrField = entityOrField;
    }
}
