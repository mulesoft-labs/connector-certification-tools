import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class NoAssertionsInBeforeOrAfterCheckTest {

    String a;

    @Before
    public void setup() {
        assertNull(a); // Noncompliant {{No assertions allowed in methods annotated with @After or @Before.}}
    }

    @Test
    public void someTest() {
    }
}

public class NoAssertionsInBeforeOrAfterCheckTest2 {

    String a;

    @After
    public void setup() {
        Assert.assertThat(a, is("something")); // Noncompliant {{No assertions allowed in methods annotated with @After or @Before.}}
    }

    @Test
    public void someTest() {
        Assert.assertThat(a, is("something"));
    }
}
