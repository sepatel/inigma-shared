package org.inigma.shared.tools;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:sejal@inigma.org">Sejal Patel</a>
 * @since 3/10/14 10:47 PM
 */
public class EmailUtilTest {
    @Test
    public void validEmail() {
        assertTrue(EmailUtil.isValidEmail(null));
        assertFalse(EmailUtil.isValidEmail(""));
        assertTrue(EmailUtil.isValidEmail("here@home.com"));
        assertTrue(EmailUtil.isValidEmail(" here@home.com "));
        assertFalse(EmailUtil.isValidEmail("here@home.c"));
        assertTrue(EmailUtil.isValidEmail("Here@Home.coM"));
    }


    @Test
    public void fixEmail() {
        assertEquals("here@home.com", EmailUtil.fixEmail("Here@Home.coM"));
        assertEquals("here@home.com", EmailUtil.fixEmail(" here@home.coM "));
        assertEquals(null, EmailUtil.fixEmail(""));
        assertEquals(null, EmailUtil.fixEmail(null));
    }
}
