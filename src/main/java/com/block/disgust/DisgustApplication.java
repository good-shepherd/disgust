package com.block.disgust;

import com.block.disgust.entities.DisgustPic;
import com.block.disgust.payloads.VisionResultForTags;
import com.block.disgust.repositories.DisgustPicRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class DisgustApplication implements CommandLineRunner {

    @Value("${app.kakaokey}")
    private String appKey;

    @Value("${app.filepath}")
    private String filePath;

    @Value("${app.kakaoapiendpoint}")
    private String kakaoEndpoint;

    private DisgustPicRepository repository;

    public DisgustApplication(DisgustPicRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DisgustApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*String filePath = "/Users/augustine/crawlertest/1523378057.jpg";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "KakaoAK " + appKey);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", new FileSystemResource(filePath));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(kakaoEndpoint, HttpMethod.POST, requestEntity, String.class);
        VisionResultForTags resultForTags = new VisionResultForTags();
        JsonNode node = new ObjectMapper().readTree(response.getBody()).get("result");
        Iterator<JsonNode> it = node.get("label").iterator();
        it.forEachRemaining(o -> resultForTags.labelEn.add(o.asText()));
        it = node.get("label_kr").iterator();
        it.forEachRemaining(o -> resultForTags.labelKr.add(o.asText()));
        log.info("response status: " + response.getStatusCode());
        log.info("response body: " + resultForTags);*/
        getObjectsFromVision().forEach(o -> System.out.println(o.toString()));
    }

    private List<VisionResultForTags> getObjectsFromVision() throws IOException {
        List<VisionResultForTags> objectResponse = new ArrayList<>();
        List<DisgustPic> picList = new ArrayList<>();
        repository.findAll().forEach(o -> picList.add(o));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "KakaoAK " + appKey);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        RestTemplate restTemplate = new RestTemplate();
        for (DisgustPic picture : picList) {
            bodyMap.add("file", new FileSystemResource(filePath.concat(picture.getFileName())));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
            ResponseEntity<String> response = restTemplate.exchange(kakaoEndpoint, HttpMethod.POST, requestEntity, String.class);
            VisionResultForTags resultForTags = new VisionResultForTags();
            JsonNode node = new ObjectMapper().readTree(response.getBody()).get("result");
            Iterator<JsonNode> it = node.get("label").iterator();
            it.forEachRemaining(o -> resultForTags.labelEn.add(o.asText()));
            it = node.get("label_kr").iterator();
            it.forEachRemaining(o -> resultForTags.labelKr.add(o.asText()));
            objectResponse.add(resultForTags);
        }

        return objectResponse;

    }


    private void indexToElasticsearch() {

    }
}
