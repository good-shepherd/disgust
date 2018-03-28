package com.block.disgust.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class DisgustPic {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_dir")
    private String flilDir;

    @Column(name = "file_bno")
    private int boardNumber;

    @Column(name = "date_created")
    private Date dateCreated;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private PicCategory picCategory;

    public DisgustPic(String fileName, String flilDir, int boardNumber, PicCategory picCategory) {
        this.fileName = fileName;
        this.flilDir = flilDir;
        this.boardNumber = boardNumber;
        this.dateCreated = new Date();
        this.picCategory = picCategory;
    }
}
