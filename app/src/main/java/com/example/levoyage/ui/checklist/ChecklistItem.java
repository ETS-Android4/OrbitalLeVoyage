package com.example.levoyage.ui.checklist;

public class ChecklistItem implements Comparable<ChecklistItem> {
    private boolean checked;
    private String task, id;

    public ChecklistItem() {
    }

    public ChecklistItem(boolean checked, String task, String id) {
        this.checked = checked;
        this.task = task;
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(ChecklistItem o) {
        if (this.checked) {
            return o.checked ? -1 : 1;
        } else {
            return -1;
        }
    }
}
