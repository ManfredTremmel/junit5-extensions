package name.falgout.jeffrey.testing.junit.disable;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import name.falgout.jeffrey.testing.junit.testing.ExtensionTester;
import name.falgout.jeffrey.testing.junit.testing.TestPlanExecutionReport;
import name.falgout.jeffrey.testing.junit.testing.TestPlanExecutionReport.DisplayName;
import org.junit.jupiter.api.Test;

class TestDisablerMetaTest {
  @Test
  void metaTest() {
    TestPlanExecutionReport report = ExtensionTester.runTests(TestDisablerTest.class);

    DisplayName foo = DisplayName.create("foo()");
    DisplayName disableSpecificTest = DisplayName.create("disableSpecificTest()");

    DisplayName nestedFoo = DisplayName.create("NestedTests", "foo()");
    DisplayName nestedBar = DisplayName.create("NestedTests", "bar()");
    DisplayName nestedBaz = DisplayName.create("NestedTests", "baz()");
    DisplayName nestedDifferentDisplayName = DisplayName
        .create("NestedTests", "Different.Display.Name!");

    DisplayName nested2 = DisplayName.create("NestedTests2");

    DisplayName parameterized1 = DisplayName.create("parameterizedTest(int)", "1");
    DisplayName parameterized2 = DisplayName.create("parameterizedTest(int)", "2");
    DisplayName parameterized3 = DisplayName.create("parameterizedTest(int)", "3");

    DisplayName badTest = DisplayName.create("Subclass", "badTest()");
    assertAll(
        () -> assertThat(report.getTests()).hasSize(10),
        () ->
            assertThat(report.getSuccessful())
                .containsAtLeastElementsIn(Arrays.asList(
                    nestedFoo,
                    parameterized1,
                    parameterized2)),
        () ->
            assertThat(report.getSkipped().keySet())
                .containsAtLeastElementsIn(Arrays.asList(
                    foo,
                    disableSpecificTest,
                    nestedBar,
                    nestedBaz,
                    nestedDifferentDisplayName,
                    nested2,
                    parameterized3,
                    badTest)),
        () -> assertThat(report.getSkippedCause(foo)).hasValue("foo!"),
        () -> assertThat(report.getSkippedCause(nestedBaz)).hasValue("baz!")
    );
  }
}
