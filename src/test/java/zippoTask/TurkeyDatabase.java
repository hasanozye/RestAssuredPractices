package zippoTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TurkeyDatabase {
    @JsonProperty("post code")
    private String postCode;
    private String country;
    @JsonProperty("country abbreviation")
    private String countryAbbreviation;
    private List<Place> places;

    @Getter
    @Setter
    @ToString
    public static class Place {
        @JsonProperty("place name")
        private String placeName;
        private String longitude;
        private String latitude;
        private String state;
        @JsonProperty("state abbreviation")
        private String stateAbbreviation;
    }
}
