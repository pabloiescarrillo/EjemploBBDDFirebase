package es.iescarrillo.android.ejemplobbddfirebase.models;

import java.io.Serializable;

public class Film implements Serializable {

    private String id;
    private String title;
    private Double duration;

    public Film(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}
