package develop.whiskyNote.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WhiskyCategory {
    AMERICAN_WHISKEY("American Whiskey"),
    BLEND("Blend"),
    BLENDED_GRAIN("Blended Grain"),
    BLENDED_MALT("Blended Malt"),
    BOURBON("Bourbon"),
    CANADIAN_WHISKY("Canadian Whisky"),
    CORN("Corn"),
    RYE("Rye"),
    SINGLE_GRAIN("Single Grain"),
    SINGLE_MALT("Single Malt"),
    SINGLE_POT_STILL("Single Pot Still"),
    SPIRIT("Spirit"),
    TENNESSEE("Tennessee"),
    WHEAT("Wheat"),
    OTHER("Other");
    private final String category;
}
