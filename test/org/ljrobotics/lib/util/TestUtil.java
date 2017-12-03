package org.ljrobotics.lib.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.ljrobotics.lib.util.Util;

/**
 * Created by leighpauls on 2/16/17.
 */
public class TestUtil {

    public static Matcher<Double> epsilonEqualTo(final double b, final double epsilon) {
        return new BaseMatcher<Double>() {

            @Override
            public boolean matches(Object a) {
                return Util.epsilonEquals((Double) a, b, epsilon);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Should be within +/- ").appendValue(epsilon).appendText(" of ").appendValue(b);
            }
        };
    }

}
