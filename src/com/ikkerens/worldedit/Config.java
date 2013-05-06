package com.ikkerens.worldedit;

public class Config {
    private int limit;
    private int undo_history;

    public Config() {
        this.limit = 500;
        this.undo_history = 10;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getUndoHistoryCount() {
        return this.undo_history;
    }
}
