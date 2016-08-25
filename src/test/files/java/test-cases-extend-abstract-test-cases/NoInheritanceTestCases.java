package org.mule.modules.test.automation.functional;

import org.mule.modules.mongo.automation.AbstractTestCases;
import org.junit.Test;

public class NoInheritanceTestCases { // Noncompliant {{Test case 'NoInheritanceTestCases' should inherit from AbstractTestCase.}}

    @Test
    public void testAddUser() {
    }

}
