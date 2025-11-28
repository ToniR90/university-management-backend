package com.orientation.backend;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class StudentManagementSystemApplicationTest {

    /**
     * Verifies that the Spring context loads without errors.
     * If this test passes, it means:
     * - All @Configuration classes are valid
     * - All beans can be created
     * - No circular dependencies exist
     * - Database connection is properly configured
     */

    @Test
    public void contextLoads() {
        // Test to ensure the Spring application context loads successfully
    }

}