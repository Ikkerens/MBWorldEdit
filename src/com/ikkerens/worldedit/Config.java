package com.ikkerens.worldedit;

public class Config {
    private final int limit;
    private final int undo_treshold;
    private final int undo_history;

    public Config() {
        this.limit = 500;
        this.undo_treshold = 2000000;
        this.undo_history = 10;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getUndoTreshold() {
        return this.undo_treshold;
    }

    public int getUndoHistoryCount() {
        return this.undo_history;
    }
}
