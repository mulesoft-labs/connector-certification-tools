import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.oauth.OAuth;
import org.mule.api.annotations.oauth.OAuth2;

@ConnectionManagement(friendlyName = "Configuration")
public class MyConnectorBasicAuthConfig {

}

@OAuth(friendlyName = "OAuth 1.0")
public class MyConnectorOAuth1Config {

}

@OAuth2(friendlyName = "OAuth v2.0 Configuration")
public class MyConnectorOAuth2Config { // Noncompliant {{For multiple connector configurations, if one or more are annotated with @OAuth2, the default friendly name must be 'OAuth 2.0' (instead of 'OAuth v2.0 Configuration').}}

}

@ConnectionManagement(friendlyName = "MYSSO 3.0")
public class MyConnectorSAMLConfig { // Noncompliant {{Single connector configurations annotated with @ConnectionManagement or @Configuration must use 'Configuration' as friendly name (instead of 'MYSSO 3.0'). If there are multiple configurations, none of which is of type @OAuth or @OAuth2, use a brief description to specify the configuration name (e.g, 'SAML 2.0').}}

}
