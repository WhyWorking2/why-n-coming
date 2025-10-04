package org.sparta.whyncoming.test.presentaion.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.s3.S3Util;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test")
public class S3TestController {

    private final S3Util s3Util;

    public S3TestController(S3Util _S3Utils){
        this.s3Util = _S3Utils;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFileTest(@RequestParam("imageFile")MultipartFile file){
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body("파일을 선택해주세요.");
        }

        try {
            String fileUrl = s3Util.uploadFile(file);

            return ResponseEntity.ok("파일 업로드 성공 " + fileUrl);
        } catch (IOException e){
            return ResponseEntity.internalServerError().body("파일 업로드 오류: " + e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
