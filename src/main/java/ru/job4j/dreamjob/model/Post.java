package ru.job4j.dreamjob.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Модель данных.
 * Класс описывает вакансию.
 */
public class Post implements Serializable {
    private int id;
    private String name;
    private String description;
    private String created;
    private boolean visible;

    private City city;

    public Post(int id, String name, String description, String created, Boolean visible) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.visible = (visible != null) ? visible : false;
        setCity(new City(0, "none"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id
                + ", name='" + name + '\'' + '}';
    }
}

