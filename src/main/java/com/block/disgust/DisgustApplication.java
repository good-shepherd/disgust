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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
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

    @Value("${app.kakaoapifortags}")
    private String kakaoApiForTags;

    @Value("${app.kakaoapiforadult}")
    private String kakaoApiForAdult;

    private DisgustPicRepository repository;

    public DisgustApplication(DisgustPicRepository repository) {
        this.repository = repository;
    }

    public static void main(String[] args) {
        SpringApplication.run(DisgustApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        getObjectsFromVision().forEach(o -> System.out.println(o.toString()));
    }

    private List<VisionResultForTags> getObjectsFromVision() throws IOException {
        List<VisionResultForTags> objectResponse = new ArrayList<>();
        List<DisgustPic> picList = new ArrayList<>();
        repository.findByFileNameIsNotContainingIgnoreCase(".gif").forEach(o -> picList.add(o));
        // repository.findAll().forEach(o -> picList.add(o));
        /*for (long i = 1; i<=5;i++){
            picList.add(repository.findById(i).get());
        }*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "KakaoAK ".concat(appKey));
        MultiValueMap<String, Object> bodyMap;
        RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(5000).setReadTimeout(10000).build();
        for (DisgustPic picture : picList) {
            bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add("file", new FileSystemResource(filePath.concat(picture.getFileName())));
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
            ResponseEntity<String> response = restTemplate.exchange(kakaoApiForTags, HttpMethod.POST, requestEntity, String.class);
            VisionResultForTags resultForTags = new VisionResultForTags();
            JsonNode node = new ObjectMapper().readTree(response.getBody()).get("result");
            Iterator<JsonNode> it = node.get("label").iterator();
            it.forEachRemaining(o -> resultForTags.labelEn.add(o.asText()));
            it = node.get("label_kr").iterator();
            it.forEachRemaining(o -> resultForTags.labelKr.add(o.asText()));
            response = restTemplate.exchange(kakaoApiForAdult, HttpMethod.POST, requestEntity, String.class);
            node = new ObjectMapper().readTree(response.getBody()).get("result");
            resultForTags.adult = Float.parseFloat(node.get("adult").asText());
            resultForTags.normal = Float.parseFloat(node.get("normal").asText());
            resultForTags.soft = Float.parseFloat(node.get("soft").asText());
            objectResponse.add(resultForTags);
            System.out.println(resultForTags);
        }

        return objectResponse;

    }


    private void indexToElasticsearch() {

    }
}
