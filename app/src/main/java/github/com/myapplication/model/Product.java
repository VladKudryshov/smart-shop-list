package github.com.myapplication.model;

import java.io.Serializable;


public class Product implements Serializable {
    private String name;
    private String cat_uid;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCat_uid() {
        return cat_uid;
    }

    public void setCat_uid(String cat_uid) {
        this.cat_uid = cat_uid;
    }
}
