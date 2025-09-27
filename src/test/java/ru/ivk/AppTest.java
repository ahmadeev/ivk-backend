package ru.ivk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ivk.utils.math.Coordinates;
import ru.ivk.utils.math.PlaneMath;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppTest {
    @Test
    public void testApp() {
        assertTrue( true );
    }

    @ParameterizedTest
    @MethodSource("provideTestData_isRightAngleTriangle")
    public void test_isRightAngleTriangle(double hypotenuse, double leg1, double leg2, boolean expected) {
        assertEquals(PlaneMath.isRightAngleTriangle(hypotenuse, leg1, leg2), expected);
    }

    static Stream<Arguments> provideTestData_isRightAngleTriangle() {
        return Stream.of(
                Arguments.of(5.0, 3.0, 4.0, true),
                Arguments.of(3.0, 5.0, 4.0, true),
                Arguments.of(25.0, 7.0, 24.0, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData_getTwoPointsDistance")
    public void test_getTwoPointsDistance(Coordinates p1, Coordinates p2, double expected) {
        assertEquals(PlaneMath.getTwoPointsDistance(p1, p2), expected);
    }

    static Stream<Arguments> provideTestData_getTwoPointsDistance() {
        return Stream.of(
                Arguments.of(new Coordinates(0, 0), new Coordinates(3, 4), 5.0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData_getRightAngleVertex")
    public void test_getRightAngleVertex(Coordinates p1, Coordinates p2, Coordinates p3, Coordinates expected) {
        assertEquals(PlaneMath.getRightAngleVertex(p1, p2, p3), expected);
    }

    static Stream<Arguments> provideTestData_getRightAngleVertex() {
        return Stream.of(
                Arguments.of(new Coordinates(0, 3), new Coordinates(4, 0), new Coordinates(0, 0), new Coordinates(0, 0)),
                Arguments.of(new Coordinates(0, 4), new Coordinates(4, 0), new Coordinates(0, 0), new Coordinates(0, 0))
        );
    }
}
