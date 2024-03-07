package com.example.russionTest.domain;

        import jakarta.persistence.*;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "care_production")
public class CareProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private float price;

    private String description;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<TattooCatalog> getTattooCatalogList() {
//        return tattooCatalogList;
//    }
//
//    public void setTattooCatalogList(List<TattooCatalog> tattooCatalogList) {
//        this.tattooCatalogList = tattooCatalogList;
//    }

    //private String urlImageCareProduct;

//    @OneToMany(mappedBy = "careProduction")
//    private List<TattooCatalog> tattooCatalogList;


}
