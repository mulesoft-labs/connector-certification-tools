import java.util.List;
import java.util.Map;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;

@Connector
public class DevKitRedundantAnnotationsCheck {

    @Processor
    public void aMethodWithDefault(@Default Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptional(@Optional Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithDefaultAndOptional(@Default @Optional Map<String, Object> s1) { // Noncompliant {{@Default and @Optional annotations cannot be used at the same time in method 'aMethodWithDefaultAndOptional' argument 's1'. Discard @Optional.}}
    }

    @Processor
    public void aMethodWithOptionalAndDefault(@Optional @Default Map<String, Object> s1) { // Noncompliant {{@Default and @Optional annotations cannot be used at the same time in method 'aMethodWithOptionalAndDefault' argument 's1'. Discard @Optional.}}
    }

    @Processor
    public void aMethodWithRefOnlyAndDefaultAndOptional(@RefOnly @Default @Optional Map<String, Object> s1) { // Noncompliant {{@Default and @Optional annotations cannot be used at the same time in method 'aMethodWithRefOnlyAndDefaultAndOptional' argument 's1'. Discard @Optional.}}
    }

    /*
     * With FQN
     */

    @Processor
    public void aMethodWithOptionalAndDefaultFQN(@org.mule.api.annotations.param.Optional Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptionalAndDefaultFQN(@org.mule.api.annotations.param.Optional @org.mule.api.annotations.param.Default Map<String, Object> s1) { // Noncompliant {{@Default and @Optional annotations cannot be used at the same time in method 'aMethodWithOptionalAndDefaultFQN' argument 's1'. Discard @Optional.}}
    }


}
