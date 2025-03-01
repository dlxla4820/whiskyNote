package develop.whiskyNote.dto;

import lombok.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseHeaderDto {
    private HttpHeaders headers;
    private InputStreamResource data;
}
