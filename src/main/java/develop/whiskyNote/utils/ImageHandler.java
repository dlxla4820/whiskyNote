package develop.whiskyNote.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ImageHandler {
    @Value("${file.path}")
    private String preFixFilePath;

    public Map<Long, String> save(List<MultipartFile> images, UUID uuid) throws IOException {
        Map<Long, String> fullPathNames = new HashMap<>();
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String fullPathName = preFixFilePath + uuid.toString() + File.separator + getOriginName(image);
            fullPathNames.put((long) i, fullPathName);
            File file = new File(fullPathName);
            File parentDir = file.getParentFile();
            // 디렉토리가 없으면 생성
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                throw new IOException("Failed to create directories: " + parentDir.getAbsolutePath());
            }
            image.transferTo(file);
        }
        return fullPathNames;
    }

    public String save(MultipartFile image, UUID uuid, String name) throws IOException {
        String fullPathName = preFixFilePath + uuid.toString() + File.separator + name;
        File file = new File(fullPathName);
        File parentDir = file.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create directories: " + parentDir.getAbsolutePath());
        }
        image.transferTo(file);
        return fullPathName;
    }

    public String getOriginName(MultipartFile image){
        return image.getOriginalFilename();
    }
}
