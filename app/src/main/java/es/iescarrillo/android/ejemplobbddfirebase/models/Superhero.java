package es.iescarrillo.android.ejemplobbddfirebase.models;

import java.io.Serializable;
import java.util.List;

public class Superhero implements Serializable {
    private String id;
    private  String name;
    private List<String> powers;
    private Boolean active;

    public Superhero(){
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPowers() {
        return powers;
    }

    public void setPowers(List<String> powers) {
        this.powers = powers;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
