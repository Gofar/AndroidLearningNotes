package com.gofar.app;

/**
 * @author lcf
 * @date 2018/7/6 17:54
 * @since 1.0
 */
public class Catalog {
    private String title;
    private String path;
    private Catalog[] data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Catalog[] getData() {
        return data;
    }

    public void setData(Catalog[] data) {
        this.data = data;
    }
}
