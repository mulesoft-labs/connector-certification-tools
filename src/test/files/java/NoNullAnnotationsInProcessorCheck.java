import java.util.List;
import java.util.Map;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.jetbrains.annotations.Nullable;
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
    public void aMethodWithNullable(@Nullable List<String> s1) { // Noncompliant {{Replace @Nullable annotation with @Optional in method 'aMethodWithNullable' argument 's1'.}}
    }

    @Processor
    public void aMethodWithNullableAndOptional(@Nullable @Optional List<String> s1) { // Noncompliant {{Remove @Nullable annotation in method 'aMethodWithNullableAndOptional' argument 's1'. Use the @Optional annotation only.}}
    }

    @Processor
    public void aMethodWithNotNull(@NotNull List<Object> s1) { // Noncompliant {{Remove @NotNull annotation in method 'aMethodWithNotNull' argument 's1'.}}
    }

    @Processor
    public void aMethodWithOptional(@Optional List<Object> s1) {
    }

    @Processor
    public void aMethodWithNonNull(@NonNull List<Object> s1) { // Noncompliant {{Remove @NonNull annotation in method 'aMethodWithNonNull' argument 's1'.}}
    }

    @Processor
    public void aMethodWithAnotherNonNull(@Nonnull List<Object> s1) { // Noncompliant {{Remove @Nonnull annotation in method 'aMethodWithAnotherNonNull' argument 's1'.}}
    }

    /**
     * Two or more arguments
     */

    @Processor
    public void aMethodWithNullableAndOptional(String s1, String s2) {
    }

    @Processor
    public void aMethodWithSeparateNullableAndOptional(@Nullable String s1, @Optional String s2) { // Noncompliant {{Replace @Nullable annotation with @Optional in method 'aMethodWithSeparateNullableAndOptional' argument 's1'.}}
    }

    @Processor
    public void aMethodWithMultipleNotNullAndOptional(@Optional String s1, String s2, @NotNull String s3) { // Noncompliant {{Remove @NotNull annotation in method 'aMethodWithMultipleNotNullAndOptional' argument 's3'.}}
    }

    @Processor
    public void aMethodWithMultipleOptional(@Optional String s1, @Optional String s2, @Optional String s3) {
    }


}
