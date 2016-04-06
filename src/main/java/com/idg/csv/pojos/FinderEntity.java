package com.idg.csv.pojos;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by agermenos on 4/6/16.
 */
@Entity
@Table(name = "findings", schema = "csv", catalog = "csv_parser")
public class FinderEntity {
    @Id
    private Integer id;
    @Column(name = "url")
    private String url;
    @Column(name = "response_code")
    private Integer responseCode;
    @Column(name = "date_detected")
    private Date dateDetected;
    @Column(name = "category")
    private String category;
    @Column(name = "platform")
    private String platform;
    @Column(name = "date_crawled")
    private Date dateCrawled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Date getDateDetected() {
        return dateDetected;
    }

    public void setDateDetected(Date dateDetected) {
        this.dateDetected = dateDetected;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getDateCrawled() {
        return dateCrawled;
    }

    public void setDateCrawled(Date dateCrawled) {
        this.dateCrawled = dateCrawled;
    }
}
