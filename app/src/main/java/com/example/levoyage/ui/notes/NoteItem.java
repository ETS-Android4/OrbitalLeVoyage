package com.example.levoyage.ui.notes;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteItem implements Parcelable {

    private String content, id, title, date;

    public NoteItem() {
    }

    public NoteItem(String content, String id, String title, String date) {
        this.content = content;
        this.id = id;
        this.title = title;
        this.date = date;
    }

    protected NoteItem(Parcel in) {
        content = in.readString();
        id = in.readString();
        title = in.readString();
        date = in.readString();
    }

    public static final Creator<NoteItem> CREATOR = new Creator<NoteItem>() {
        @Override
        public NoteItem createFromParcel(Parcel in) {
            return new NoteItem(in);
        }

        @Override
        public NoteItem[] newArray(int size) {
            return new NoteItem[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
    }
}
