package develop.whiskyNote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import develop.whiskyNote.enums.Description;
import develop.whiskyNote.enums.ErrorCode;
import lombok.*;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ResponseDto<T> {

    private Integer code; // return code 200, 400 ...
    private Description description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;

    public ResponseDto(Integer code, Description description, ErrorCode errorCode){
        this.code = code;
        this.description = description;
        this.errorCode = errorCode.getErrorCode();
        this.errorDescription = errorCode.getErrorDescription();
    }

    public ResponseDto(Integer code, Description description, ErrorCode errorCode, T data){
        this.code = code;
        this.description = description;
        this.errorCode = errorCode.getErrorCode();
        this.errorDescription = errorCode.getErrorDescription();
        this.data = data;
    }


    @Override
    public String toString(){
        return new StringJoiner(", ", ResponseDto.class.getSimpleName() + "[", "]")
                .add("code='" + code + "'" )
                .add("description='"+ description + "'")
                .add("errorCode='"+ errorCode + "'")
                .add("errorDescription='"+ errorDescription + "'")
                .add("data=" + data)
                .toString();
    }
}