import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.testconnector.automation.unit.TransformerResolverUnitTest;

@RunWith
@SuiteClasses({
        TransformerResolverUnitTest.class })
public class UnitTestSuite { // Noncompliant {{Found @RunWith annotation on Test Suite class 'UnitTestSuite', but no runner specified. It should be Suite.class.}}

}
