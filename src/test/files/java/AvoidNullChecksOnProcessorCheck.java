import java.util.List;
import java.util.Map;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.jetbrains.annotations.org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import com.sun.javafx.beans.annotations.NonNull;
import javax.annotation.Nonnull;
import org.mule.api.annotations.param.Optional;

@Connector
public class NoNullAnnotationsInProcessorCheck {

    /*
    * Only one argument
    */

    @Processor
    public void aMethodWithNullable(@Nullable List<String> s1) { // Noncompliant {{Remove @org.jetbrains.annotations.Nullable annotation in method 'aMethodWithNullable' argument 's1'.}}
    }

    @Processor
    public void aMethodWithNotNull(@NotNull List<Object> s1) { // Noncompliant {{Remove @org.jetbrains.annotations.NotNull annotation in method 'aMethodWithNotNull' argument 's1'.}}
    }

    @Processor
    public void aMethodWithNonNull(@Nonnull List<Object> s1) { // Noncompliant {{Remove @javax.annotation.Nonnull annotation in method 'aMethodWithNonNull' argument 's1'.}}
    }

    @Processor
    public void aMethodWithAnotherNonNull(@NonNull List<Object> s1) { // Noncompliant {{Remove @com.sun.javafx.beans.annotations.NonNull annotation in method 'aMethodWithAnotherNonNull' argument 's1'.}}
    }

    /**
     * Two or more arguments
     */

    @Processor
    public void aMethodWithNullableAndOptional(String s1, String s2) {
    }

    @Processor
    public void aMethodWithMultipleParams(@Nullable String s1, @Optional String s2, String s3) { // Noncompliant {{Remove @org.jetbrains.annotations.Nullable annotation in method 'aMethodWithMultipleParams' argument 's1'.}}
    }


}
