package org.inigma.shared.tools;

import org.junit.Test;

import static org.inigma.shared.tools.StringUtil.*;
import static org.junit.Assert.*;

public class StringUtilTest {
    @Test
    public void snakeToCamelTest() {
        assertEquals("one","one", snakeToCamel("one"));
        assertEquals("one_two","oneTwo", snakeToCamel("one_two"));
        assertEquals("_two","Two", snakeToCamel("_two"));
        assertEquals("null",null, snakeToCamel(null));
        assertEquals("empty","", snakeToCamel(""));
        assertEquals("oNe_tWo","oneTwo", snakeToCamel("oNe_tWo"));
        assertEquals("one_two_three_four","oneTwoThreeFour", snakeToCamel("one_two_three_four"));
        assertEquals("one__two","oneTwo", snakeToCamel("one__two"));
        assertEquals("one_two_","oneTwo", snakeToCamel("one_two_"));
    }


}
