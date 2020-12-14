package daily.boot.gulimall.authserver.security.jacksonmixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"cause", "stackTrace"})
public class DisabledExceptionMixin {
    /**
     * Constructor used by Jackson to create
     * {@link org.springframework.security.authentication.BadCredentialsException} object.
     *
     * @param message the detail message
     */
    @JsonCreator
    DisabledExceptionMixin(@JsonProperty("message") String message) {}
}
