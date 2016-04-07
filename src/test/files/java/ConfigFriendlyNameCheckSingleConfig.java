import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.components.Configuration;

@ConnectionManagement(friendlyName = "Config")  // Noncompliant {{Single connector configurations annotated with @ConnectionManagement or @Configuration must use 'Configuration' as friendly name (instead of 'Config'). If there are multiple configurations, none of which is of type @OAuth or @OAuth2, use a brief description to specify the configuration name (e.g, 'SAML 2.0').}}
public class Config {

}

@Configuration(friendlyName = "Config", configElementName = "config")  // Noncompliant {{Single connector configurations annotated with @ConnectionManagement or @Configuration must use 'Configuration' as friendly name (instead of 'Config'). If there are multiple configurations, none of which is of type @OAuth or @OAuth2, use a brief description to specify the configuration name (e.g, 'SAML 2.0').}}
public class Config2 {

}
