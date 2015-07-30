package org.mule.tools.devkit.sonar.loader;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL) @Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "rules" }) class JsonRules {

    @JsonProperty("rules")  private List<JsonRule> rules = new ArrayList<JsonRule>();

    @JsonIgnore private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("rules") public List<JsonRule> getRules() {
        return rules;
    }

    @JsonProperty("rules") public void setRules(List<JsonRule> rules) {
        this.rules = rules;
    }

    @JsonAnyGetter public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}