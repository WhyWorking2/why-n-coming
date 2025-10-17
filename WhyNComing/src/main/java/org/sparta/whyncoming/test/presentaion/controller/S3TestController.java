package org.sparta.whyncoming.test.presentaion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sparta.whyncoming.common.s3.S3Util;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/test/s3")
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
            String fileUrl = s3Util.uploadFile(file, "test");

            return ResponseEntity.ok("파일 업로드 성공 " + fileUrl);
        } catch (IOException e){
            return ResponseEntity.internalServerError().body("파일 업로드 오류: " + e.getMessage());
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "S3 파일 삭제 테스트 (URL 기반)")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFileTest(@RequestParam("fileUrl") String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return ResponseEntity.badRequest().body("삭제할 파일 URL을 입력해주세요.");
        }

        try {
            s3Util.deleteFileByUrl(fileUrl);
            return ResponseEntity.ok("S3 파일 삭제 성공: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("S3 파일 삭제 실패: " + e.getMessage());
        }
    }
}
