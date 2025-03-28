package com.example.rtoexamdrivingtheory.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HighWayItemModel implements Parcelable {
    public static final Creator<HighWayItemModel> CREATOR = new Creator<HighWayItemModel>() {

        @Override
        public HighWayItemModel createFromParcel(Parcel parcel) {
            return new HighWayItemModel(parcel);
        }

        @Override
        public HighWayItemModel[] newArray(int i) {
            return new HighWayItemModel[i];
        }
    };
    private int _id;
    private int category_root_card_id;
    private int direct_parent_id;
    private String full_html;
    private int index_within_category;
    private int is_flagged;
    private int is_seen;
    private String long_title;
    private String searchable_content;
    private String short_title;
    private String url;

    public HighWayItemModel() {
    }

    protected HighWayItemModel(Parcel parcel) {
        this._id = parcel.readInt();
        this.direct_parent_id = parcel.readInt();
        this.category_root_card_id = parcel.readInt();
        this.index_within_category = parcel.readInt();
        this.is_seen = parcel.readInt();
        this.is_flagged = parcel.readInt();
        this.url = parcel.readString();
        this.long_title = parcel.readString();
        this.short_title = parcel.readString();
        this.full_html = parcel.readString();
        this.searchable_content = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public int getCategory_root_card_id() {
        return this.category_root_card_id;
    }

    public int getDirect_parent_id() {
        return this.direct_parent_id;
    }

    public String getFull_html() {
        return this.full_html;
    }

    public int getIndex_within_category() {
        return this.index_within_category;
    }

    public int getIs_flagged() {
        return this.is_flagged;
    }

    public int getIs_seen() {
        return this.is_seen;
    }

    public String getLong_title() {
        return this.long_title;
    }

    public String getSearchable_content() {
        return this.searchable_content;
    }

    public String getShort_title() {
        return this.short_title;
    }

    public String getUrl() {
        return this.url;
    }

    public int get_id() {
        return this._id;
    }

    public void setCategory_root_card_id(int i) {
        this.category_root_card_id = i;
    }

    public void setDirect_parent_id(int i) {
        this.direct_parent_id = i;
    }

    public void setFull_html(String str) {
        this.full_html = str;
    }

    public void setIndex_within_category(int i) {
        this.index_within_category = i;
    }

    public void setIs_flagged(int i) {
        this.is_flagged = i;
    }

    public void setIs_seen(int i) {
        this.is_seen = i;
    }

    public void setLong_title(String str) {
        this.long_title = str;
    }

    public void setSearchable_content(String str) {
        this.searchable_content = str;
    }

    public void setShort_title(String str) {
        this.short_title = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void set_id(int i) {
        this._id = i;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this._id);
        parcel.writeInt(this.direct_parent_id);
        parcel.writeInt(this.category_root_card_id);
        parcel.writeInt(this.index_within_category);
        parcel.writeInt(this.is_seen);
        parcel.writeInt(this.is_flagged);
        parcel.writeString(this.url);
        parcel.writeString(this.long_title);
        parcel.writeString(this.short_title);
        parcel.writeString(this.full_html);
        parcel.writeString(this.searchable_content);
    }
}
