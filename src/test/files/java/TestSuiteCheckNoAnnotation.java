import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.testconnector.automation.unit.TransformerResolverUnitTest;

@SuiteClasses({
        TransformerResolverUnitTest.class })
public class UnitTestSuite { // Noncompliant {{Missing @RunWith annotation on Test Suite class 'UnitTestSuite'.}}

}
