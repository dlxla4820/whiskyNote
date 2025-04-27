package develop.whiskyNote.service;

import develop.whiskyNote.dto.ResponseDto;
import develop.whiskyNote.dto.ResponseHeaderDto;
import develop.whiskyNote.dto.UserWhiskyDto;
import develop.whiskyNote.entity.ImageFile;
import develop.whiskyNote.entity.User;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.RoleType;
import develop.whiskyNote.exception.ModelNotFoundException;
import develop.whiskyNote.repository.ImageFileRepository;
import develop.whiskyNote.utils.CommonUtils;
import develop.whiskyNote.utils.ImageHandler;
import develop.whiskyNote.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final ImageHandler imageHandler;
    private final ImageFileRepository imageFileRepository;
    public ResponseDto<?> uploadFile(MultipartFile image) {
        UUID userUuid = CommonUtils.getUserUuidIfAdminOrUser();
        String path;
        String imageName = UUID.randomUUID() + "_" + imageHandler.getOriginName(image);
        try {
            path = imageHandler.save(image, userUuid, imageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageFile imageFile = ImageFile.builder()
                .path(path)
                .name(imageName)
                .isSaved(false)
                .userUuid(userUuid)
                .createdAt(LocalDateTime.now())
                .build();
        imageFileRepository.save(imageFile);
        return ResponseDto.builder()
                .description(Description.SUCCESS)
                .code(HttpStatus.OK.value())
                .data(imageName)
                .build();
    }

    public ResponseDto<?> showImage(String fileName)  {
        ImageFile imageFile = imageFileRepository.findByName(fileName).orElseThrow(() -> new ModelNotFoundException(fileName));
        try {
            Path tempFilePath = Paths.get(imageFile.getPath());
            File tempFile = tempFilePath.toFile();
            if (!tempFile.exists() || !tempFile.isFile()) {
                throw new RuntimeException("File Not Found");
            }

            String contentType = Files.probeContentType(tempFilePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(tempFilePath));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));  // 파일의 Content-Type 설정
            headers.setContentDispositionFormData("inline", URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            return ResponseDto.builder()
                    .code(200)
                    .data(ResponseHeaderDto.builder()
                            .headers(headers)
                            .data(resource)
                            .build())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
