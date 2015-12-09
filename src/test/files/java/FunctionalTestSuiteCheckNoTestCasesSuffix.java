import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.certification.automation.functional.MethodATestCases;
import org.mule.modules.certification.automation.functional.MethodBTest;

@RunWith(Suite.class)
@SuiteClasses({
        MethodATestCases.class,
        MethodBTest.class // Noncompliant {{Functional test classes must end with 'TestCases'. Rename 'MethodBTest.java' accordingly.}}
})
public class FunctionalTestSuite {

}
