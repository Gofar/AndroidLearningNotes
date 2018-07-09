package com.gofar.app.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author lcf
 * @date 2018/7/9 15:41
 * @since 1.0
 */
@Entity(tableName = "catalogs")
public class CatalogEntity {
    @PrimaryKey
    private int id;
    private String catalog;
    private String secCatalog;
    private String title;
    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSecCatalog() {
        return secCatalog;
    }

    public void setSecCatalog(String secCatalog) {
        this.secCatalog = secCatalog;
    }

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
}
