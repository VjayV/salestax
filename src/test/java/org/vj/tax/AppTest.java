package org.vj.tax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    
    @Test
    public void printRoundedDec()
    {
    	BigDecimal exp = new BigDecimal("1.50");
    	BigDecimal rConst = new BigDecimal("0.05");
    	BigDecimal val = new BigDecimal("1.499");
    	BigDecimal res = Receipt.round(val, rConst, RoundingMode.UP);
    	assertEquals(exp, res);
    }
    
}
