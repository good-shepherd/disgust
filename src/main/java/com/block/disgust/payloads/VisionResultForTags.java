package com.block.disgust.payloads;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class VisionResultForTags {
    public List<String> labelEn = new ArrayList<>();
    public List<String> labelKr = new ArrayList<>();
    public float normal;
    public float soft;
    public float adult;
}
