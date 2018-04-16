package com.block.disgust.controllers;

import com.block.disgust.entities.DisgustPic;
import com.block.disgust.repositories.DisgustPicRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pictures")
public class PictureController {

    private DisgustPicRepository disgustPicRepository;

    @GetMapping
    public List<DisgustPic> pictures() {
        List<DisgustPic> l = new ArrayList<>();
        disgustPicRepository.findAll().forEach(o -> l.add(o));
        return l;
    }

    @GetMapping("/{id}")
    public DisgustPic getOnePicture(@PathVariable Long id) {
        return disgustPicRepository.findById(id).get();
    }
}
