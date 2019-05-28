package com.javadi.youtube.models;

public class Videos {
    private String image_url_path="";
    private String video_title="";
    private String video_id="";

    public String getVideo_duration() {
        return video_duration;
    }

    public void setVideo_duration(String video_duration) {
        this.video_duration = video_duration;
    }

    private String video_duration="";

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getImage_url_path() {
        return image_url_path;
    }

    public void setImage_url_path(String image_url_path) {
        this.image_url_path = image_url_path;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }
}
