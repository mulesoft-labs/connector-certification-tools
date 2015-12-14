import org.mule.api.annotations.components.ConnectionManagement;

@ConnectionManagement // Noncompliant {{@ConnectionManagement must define a friendlyName. If there is a single configuration, 'Configuration' must be used as friendlyName.}}
public class Config {
}
