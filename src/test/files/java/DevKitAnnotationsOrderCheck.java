import java.util.List;
import java.util.Map;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.RefOnly;

@Connector
public class DevKitAnnotationsOrderCheck {

    /*
    * Only one argument
    */

    @Processor
    public void aMethodWithDefault(@Default List<String> s1) {
    }

    @Processor
    public void aMethodWithRefOnly(@RefOnly List<Object> s1) {
    }

    @Processor
    public void aMethodWithOptional(@Optional List<Object> s1) {
    }

   /*
    * Two arguments
    */

    @Processor
    public void aMethodWithBothOk(@RefOnly @Default Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithBothWrongOrder(@Default @RefOnly Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithBothWrongOrder' argument 's1'.}}
    }

    @Processor
    public void aMethodWithOptionalAndDefaultOk(@Optional @Default Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptionalAndRefOnlyOk(@Optional @RefOnly Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptionalAndDefaultWrong(@Default @Optional Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithOptionalAndDefaultWrong' argument 's1'.}}
    }

    @Processor
    public void aMethodWithOptionalAndRefOnlyWrong(@RefOnly @Optional Map<String, Object> s1) { // Noncompliant {{@RefOnly annotation must be the last one in method 'aMethodWithOptionalAndRefOnlyWrong' argument 's1'.}}
    }

   /*
    * Three arguments
    */

    @Processor
    public void aMethodWithThreeOk(@Optional @RefOnly @Default Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptionalDefaultRefOnlyWrong(@Optional @Default @RefOnly Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithOptionalDefaultRefOnlyWrong' argument 's1'.}}
    }

    @Processor
    public void aMethodWithRefOnlyOptionalDefaultWrong(@RefOnly @Optional @Default Map<String, Object> s1) { // Noncompliant {{@RefOnly annotation must be right before @Default in method 'aMethodWithRefOnlyOptionalDefaultWrong' argument 's1'.}}
    }

    @Processor
    public void aMethodWithRefOnlyDefaultOptionalWrong(@RefOnly @Default @Optional Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithRefOnlyDefaultOptionalWrong' argument 's1'.}}
    }

    @Processor
    public void aMethodWithDefaultRefOnlyOptionalWrong(@Default @RefOnly @Optional Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithDefaultRefOnlyOptionalWrong' argument 's1'.}}
    }

    @Processor
    public void aMethodWithDefaultOptionalRefOnlyWrong(@Default @Optional @RefOnly Map<String, Object> s1) { // Noncompliant {{@Default annotation must be the last one in method 'aMethodWithDefaultOptionalRefOnlyWrong' argument 's1'.}}
    }
}
