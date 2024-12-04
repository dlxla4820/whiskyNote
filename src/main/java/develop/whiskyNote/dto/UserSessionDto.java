package develop.whiskyNote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.StringJoiner;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionDto {
    private UUID uuid;
    private String role;

    @Override
    public String toString() {
        return new StringJoiner(", ", UserSessionDto.class.getSimpleName() + "[", "]")
                .add("uuid='" + uuid + "'")
                .add("uuid='" + role + "'")
                .toString();
    }
}
