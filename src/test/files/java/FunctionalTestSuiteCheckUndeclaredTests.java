import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.certification.automation.functional.MethodATestCases;
import org.mule.modules.certification.automation.functional.MethodBTest;
import org.mule.modules.certification.automation.system.MethodCTestCases;
import org.mule.modules.certification.automation.functional.MethodDTestCases;

@RunWith(Suite.class)
@SuiteClasses({
        MethodATestCases.class,
        MethodBTest.class, // Noncompliant {{Functional test classes must end with 'TestCases'. Rename test 'MethodBTest' accordingly.}}
        MethodCTestCases.class, // Noncompliant {{A file named 'MethodCTestCases.java' must exist in directory 'src/test/java/../automation/functional'.}}
        MethodDTestCases.class })
public class FunctionalTestSuite {

}
