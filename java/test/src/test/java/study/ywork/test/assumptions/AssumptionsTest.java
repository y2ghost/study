package study.ywork.test.assumptions;

import study.ywork.test.assumptions.environment.JavaSpecification;
import study.ywork.test.assumptions.environment.OperationSystem;
import study.ywork.test.assumptions.environment.TestsEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class AssumptionsTest {
    private static String EXPECTED_JAVA_VERSION = "11";
    private TestsEnvironment environment = new TestsEnvironment(
        new JavaSpecification(System.getProperty("java.vm.specification.version")),
        new OperationSystem(System.getProperty("os.name"), System.getProperty("os.arch")));

    private SUT systemUnderTest = new SUT();

    @BeforeEach
    void setUp() {
        assumeTrue(environment.isAmd64Architecture());
    }

    @Test
    void testNoJobToRun() {
        assumingThat(() -> environment.getJavaVersion().equals(EXPECTED_JAVA_VERSION),
            () -> assertFalse(systemUnderTest.hasJobToRun()));
    }

    @Test
    void testJobToRun() {
        assumeTrue(environment.isAmd64Architecture());
        systemUnderTest.run(new Job());
        assertTrue(systemUnderTest.hasJobToRun());
    }
}
