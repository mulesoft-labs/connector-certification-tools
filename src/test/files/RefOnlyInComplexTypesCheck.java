/**
 * This file is the sample code against we run our unit test.
 * It is placed src/test/files in order to not be part of the maven compilation.
 **/

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.RefOnly;

@Connector
class RefOnlyInComplexTypesCheck {

    @Processor
    public void aMethod(@RefOnly SomeComplexType s1) {
    }

    @Processor // Noncompliant {{Processor failingMethod has 1 complex-type parameter wihtout @RefOnly)}}
    public void failingMethod(SomeComplexType s1) {
    }

}
