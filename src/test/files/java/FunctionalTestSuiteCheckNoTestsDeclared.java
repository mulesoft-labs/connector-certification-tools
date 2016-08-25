import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({}) // Noncompliant {{No tests have been declared under @SuiteClasses.}}
public class FunctionalTestSuite {

}
