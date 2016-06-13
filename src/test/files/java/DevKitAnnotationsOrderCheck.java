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
    * Only one annotation
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
    * Two annotations
    */

    @Processor
    public void aMethodWithRefOnlyAndDefault(@RefOnly @Default Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithDefaultAndRefOnly(@Default @RefOnly Map<String, Object> s1) { // Noncompliant {{Annotation(s) '@Default @RefOnly' in method 'aMethodWithDefaultAndRefOnly' argument 's1' do not have expected order.}}
    }

    @Processor
    public void aMethodWithRefOnlyAndOptional(@RefOnly @Optional Map<String, Object> s1) {
    }

    @Processor
    public void aMethodWithOptionalAndRefOnly(@Optional @RefOnly Map<String, Object> s1) { // Noncompliant {{Annotation(s) '@Optional @RefOnly' in method 'aMethodWithOptionalAndRefOnly' argument 's1' do not have expected order.}}
    }

    /*
     * Three annotations
     */

    @Processor
    public void aMethodWithRefOnlyAndDefaultAndOptional(@RefOnly @Default @Optional Map<String, Object> s1) { // Noncompliant {{Annotation(s) '@RefOnly @Default @Optional' in method 'aMethodWithRefOnlyAndDefaultAndOptional' argument 's1' do not have expected order.}}
    }


}
