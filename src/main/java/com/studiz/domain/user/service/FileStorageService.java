package com.studiz.domain.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${file.base-url:http://localhost:8080/api/files}")
    private String baseUrl;

    /**
     * 파일을 저장하고 접근 가능한 URL을 반환합니다.
     * 
     * @param file 업로드할 파일
     * @param subDirectory 파일을 저장할 하위 디렉토리 (예: "profiles")
     * @return 파일 접근 URL
     * @throws IOException 파일 저장 실패 시
     */
    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        // 파일명 생성 (UUID + 원본 파일명)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String filename = UUID.randomUUID().toString() + extension;

        // 저장 경로 생성
        Path uploadPath = Paths.get(uploadDir, subDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 파일 저장
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        log.info("파일 저장 완료: {}", filePath);

        // URL 반환
        return baseUrl + "/" + subDirectory + "/" + filename;
    }

    /**
     * 파일을 삭제합니다.
     * 
     * @param fileUrl 삭제할 파일의 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // URL에서 파일 경로 추출
            String relativePath = fileUrl.replace(baseUrl + "/", "");
            Path filePath = Paths.get(uploadDir, relativePath);
            
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("파일 삭제 완료: {}", filePath);
            }
        } catch (IOException e) {
            log.error("파일 삭제 실패: {}", fileUrl, e);
        }
    }
}


