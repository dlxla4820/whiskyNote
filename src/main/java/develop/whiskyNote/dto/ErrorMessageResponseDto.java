package develop.whiskyNote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import develop.whiskyNote.enums.ErrorCode;
import lombok.*;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorMessageResponseDto<E, D> {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private E errors;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private D data;
}
