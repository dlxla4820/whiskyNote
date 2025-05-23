package develop.whiskyNote.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Description {
    SUCCESS("SUCCESS"), FAIL("FAIL");

    private final String description;

}
