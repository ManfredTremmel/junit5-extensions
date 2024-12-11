package name.falgout.jeffrey.testing.junit.testing;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import name.falgout.jeffrey.testing.junit.testing.TestPlanExecutionReport.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ExtentionTesterTest {
  static class Tests {
    @Test
    void successful() {}

    @Test
    @Disabled("reasons")
    void skipped() {}

    @Test
    void failure() {
      throw new IllegalArgumentException();
    }

    @Nested
    class NestedTests {
      @Test
      void nestedTest() {}
    }
  }

  @Test
  void testTheTester() {
    TestPlanExecutionReport report = ExtensionTester.runTests(Tests.class);

    assertAll(
        () -> assertThat(report.getTests()).hasSize(4),
        () -> assertThat(report.getSuccessful())
            .containsAtLeastElementsIn(Arrays.asList(
                DisplayName.create("successful()"),
                DisplayName.create("NestedTests", "nestedTest()"))),
        () ->
            assertThat(report.getSkippedCause(DisplayName.create("skipped()")))
                .hasValue("reasons"),
        () ->
            assertThat(report.getFailures().keySet())
                .containsExactly(DisplayName.create("failure()")),
        () ->
            assertThat(report.getFailure(DisplayName.create("failure()")).get())
                .isInstanceOf(IllegalArgumentException.class)
    );
  }
}
