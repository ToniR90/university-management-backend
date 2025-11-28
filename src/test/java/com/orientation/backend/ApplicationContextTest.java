package com.orientation.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic test to verify that the Spring application context loads successfully.
 * This test ensures that all beans are properly configured and can be instantiated.
 */
@SpringBootTest
@ActiveProfiles("test")
class ApplicationContextTest {  // ✅ Cambié 'public class' a 'class' (convención JUnit 5)

    /**
     * Verifies that the Spring context loads without errors.
     * If this test passes, it means:
     * - All @Configuration classes are valid
     * - All beans can be created
     * - No circular dependencies exist
     * - Database connection is properly configured
     */
    @Test
    void contextLoads() {
        // If the application context loads successfully, this test passes
        // No assertions needed - Spring will fail the test if context loading fails
    }
}