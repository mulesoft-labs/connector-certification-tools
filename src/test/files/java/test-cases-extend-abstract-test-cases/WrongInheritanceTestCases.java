package org.mule.modules.test.automation.functional;

import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import org.mule.modules.mongo.automation.AbstractTestCases;
import org.junit.Test;

public class WrongInheritanceTestCases extends Object { // Noncompliant {{Test case 'WrongInheritanceTestCases' should inherit from AbstractTestCase.}}

    @Test
    public void testAddUser() {
    }

}
