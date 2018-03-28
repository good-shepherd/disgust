package com.block.disgust.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class PicCategory {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "picCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DisgustPic> disgustPics;

}
