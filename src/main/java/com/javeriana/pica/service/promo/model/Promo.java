package com.javeriana.pica.service.promo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "promos")
public class Promo extends AuditModel {
    @Id
    @GeneratedValue(generator = "promo_generator")
    @SequenceGenerator(
            name = "promo_generator",
            sequenceName = "promo_sequence",
            initialValue = 1
    )
    private Long id;

    @NotBlank
    @Size(min = 3, max = 200)
    private String name;

    @Column(columnDefinition = "text")
    private String image_url;

    @Column(columnDefinition = "text")
    private String id_product;

    @Temporal(TemporalType.DATE)
    private Date initial_date;

    @Temporal(TemporalType.DATE)
    private Date end_date;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "boolean default true")
    private Boolean estate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getInitial_date() {
        return initial_date;
    }

    public void setInitial_date(Date initial_date) {
        this.initial_date = initial_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEstate() {
        return estate;
    }

    public void setEstate(Boolean estate) {
        this.estate = estate;
    }

    public String getId_product() {
        return id_product;
    }

    public void setId_product(String id_product) {
        this.id_product = id_product;
    }
}
