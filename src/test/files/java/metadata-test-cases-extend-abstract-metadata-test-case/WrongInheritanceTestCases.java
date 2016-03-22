package org.mule.modules.test.automation.functional;

import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import org.mule.modules.mongo.automation.AbstractTestCases;
import org.junit.Test;

public class WrongInheritanceMetaDataTestCases extends Object { // Noncompliant {{MetaData Test Case 'WrongInheritanceMetaDataTestCases' should inherit from
                                                                // AbstractMetaDataTestCase.}}

    @Test
    public void testAddUser() {
    }

}
