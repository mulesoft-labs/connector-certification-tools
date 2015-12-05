import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.certification.automation.functional.MethodATestCases;
import org.mule.modules.certification.automation.functional.MethodBTest;

@RunWith(Suite.class)
@SuiteClasses({
        MethodATestCases.class,
        MethodBTest.class })
public class FunctionalTestSuite { // Noncompliant {{Functional tests must end with 'TestCases'. Rename test 'MethodBTest' accordingly.}}

}