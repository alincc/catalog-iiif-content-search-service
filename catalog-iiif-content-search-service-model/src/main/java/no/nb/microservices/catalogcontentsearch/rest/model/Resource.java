package no.nb.microservices.catalogcontentsearch.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "type", "chars" })
public class Resource {
    @JsonProperty("@type")
    private String type;
    private String chars;

    public Resource() {
        super();
        this.type = "cnt:ContentAsText";
    }
    
    public Resource(String chars) {
        this();
        this.chars = chars;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

}
