package com.VincenteCodeFellowship.codeFellowship.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String body;
    Date createdAt;

    @ManyToOne
    SiteUser siteUser;

    public Post() {
    }

    public Post(String body, Date createdAt, SiteUser siteUser) {
        this.body = body;
        this.createdAt = createdAt;
        this.siteUser = siteUser;
    }

    public Post(String body) {
    }

    public long getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public SiteUser getSiteUser() {
        return siteUser;
    }

    public void setSiteUser(SiteUser siteUser) {
        this.siteUser = siteUser;
    }
}
