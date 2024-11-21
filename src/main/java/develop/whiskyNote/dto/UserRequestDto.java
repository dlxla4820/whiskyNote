package develop.whiskyNote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import develop.whiskyNote.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @JsonProperty("device_id")
    private String deviceId;

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("deviceId='" + deviceId + "'")
                .toString();
    }
}
