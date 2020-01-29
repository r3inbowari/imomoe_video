package com.r3inbowari.imo.imomoeAPI;

import java.io.Serializable;

public class ImomoeSearch implements Serializable {
    public String alt;
    public String img;

    public String alias;
    public String update;
    public String description;
    public String detailPath;
    public int pages;

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDetailPath(String detailPath) {
        this.detailPath = detailPath;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
