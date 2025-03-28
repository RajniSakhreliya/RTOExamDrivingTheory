package com.example.rtoexamdrivingtheory.model;

import java.io.Serializable;
import java.util.ArrayList;

public class HazardTestModel implements Serializable {
    String id;
    String videoUrl;
    String title;
    String description;
    ArrayList<Hazards> listOfHazards = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Hazards> getListOfHazards() {
        return listOfHazards;
    }

    public void setListOfHazards(ArrayList<Hazards> listOfHazards) {
        this.listOfHazards = listOfHazards;
    }

    public static class Hazards implements Serializable {
        String start;
        String end;

        public String getStart() {
            return start;
        }

        public long getStartLong() {
            String[] split = this.start.split(":");
            long parseLong = (Long.parseLong(split[1]) * 60 * 1000) + (((long) Double.parseDouble(split[2])) * 1000);
            return parseLong;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public long getEndLong() {
            String[] split = this.end.split(":");
            long parseLong = (Long.parseLong(split[1]) * 60 * 1000) + (((long) Double.parseDouble(split[2])) * 1000);
            return parseLong;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }
}
