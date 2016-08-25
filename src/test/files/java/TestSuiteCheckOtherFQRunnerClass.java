import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.testconnector.automation.unit.TransformerResolverUnitTest;

@RunWith(org.junit.experimental.categories.Categories.class) // Noncompliant {{Found @RunWith annotation on Test Suite class 'UnitTestSuite', but different runner specified ('org.junit.experimental.categories.Categories.class' instead of 'Suite.class').}}
@SuiteClasses({ TransformerResolverUnitTest.class })
public class UnitTestSuite {


}
