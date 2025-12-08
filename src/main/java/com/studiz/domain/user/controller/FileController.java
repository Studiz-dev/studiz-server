package com.studiz.domain.user.controller;

import com.studiz.domain.user.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 업로드 API")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @Operation(
            summary = "파일 업로드",
            description = "이미지 파일을 업로드하고 접근 가능한 URL을 반환합니다.\n\n" +
                    "**요청 형식**: multipart/form-data\n" +
                    "**파라미터**:\n" +
                    "- `file`: 업로드할 이미지 파일 (필수)\n" +
                    "- `type`: 파일 타입 (예: \"profiles\", 선택사항, 기본값: \"profiles\")\n\n" +
                    "**파일 제한**:\n" +
                    "- 최대 크기: 10MB\n" +
                    "- 이미지 파일만 허용\n\n" +
                    "**응답**:\n" +
                    "- 업로드된 파일의 접근 URL 반환"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업로드 성공"),
            @ApiResponse(responseCode = "400", description = "파일이 없거나 유효하지 않음"),
            @ApiResponse(responseCode = "500", description = "파일 저장 실패")
    })
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "profiles") String type
    ) {
        log.info("파일 업로드 요청: filename={}, size={}, type={}", 
                file.getOriginalFilename(), file.getSize(), type);

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어있습니다."));
        }

        // 이미지 파일 검증
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미지 파일만 업로드 가능합니다."));
        }

        try {
            String fileUrl = fileStorageService.storeFile(file, type);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            
            log.info("파일 업로드 성공: url={}", fileUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 저장 중 오류가 발생했습니다."));
        }
    }
}







