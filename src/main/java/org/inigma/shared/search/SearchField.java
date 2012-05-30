package org.inigma.shared.search;

public class SearchField {
    public enum Operation {
        UNUSED, IS, LT, LTE, GT, GTE, REGEX
    }

    private String key;
    private boolean visible = true;
    private int sorting; // 0 means no sorting preference
    private Operation operation = Operation.UNUSED;

    public String getKey() {
        return key;
    }

    public Operation getOperation() {
        return operation;
    }

    public int getSorting() {
        return sorting;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
