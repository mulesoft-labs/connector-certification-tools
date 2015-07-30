package org.mule.tools.devkit.sonar.loader;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL) @Generated("org.jsonschema2pojo") @JsonPropertyOrder({
        "id", "type", "acceptRegexp", "xpath", "brief", "description" }) public class JsonRule {

    @JsonProperty("id") private String id;
    @JsonProperty("type") private String type;
    @JsonProperty("acceptRegexp") private String acceptRegexp;
    @JsonProperty("xpath") private String xpath;
    @JsonProperty("brief") private String brief;
    @JsonProperty("description") private String description;

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type") public String getType() {
        return type;
    }

    @JsonProperty("type") public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("acceptRegexp") public String getAcceptRegexp() {
        return acceptRegexp;
    }

    @JsonProperty("acceptRegexp") public void setAcceptRegexp(String acceptRegexp) {
        this.acceptRegexp = acceptRegexp;
    }

    @JsonProperty("xpath") public String getXPath() {
        return xpath;
    }

    @JsonProperty("xpath") public void setXPath(String xpath) {
        this.xpath = xpath;
    }

    @JsonProperty("brief") public String getBrief() {
        return brief;
    }

    @JsonProperty("brief") public void setBrief(String brief) {
        this.brief = brief;
    }

    @JsonProperty("id") public String getId() {
        return this.id;
    }

    @JsonProperty("id") public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("description") public String getDescription() {
        return description;
    }

    @JsonProperty("description") public void setDescription(String description) {
        this.description = description;
    }

    @JsonAnyGetter public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}