package edu.tuke.beast.fascicle.storage;

import java.util.HashMap;
import java.util.HashSet;
import javax.naming.SizeLimitExceededException;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;

public class CommonMatrixTest {

	@Test
	public void testCommonMatrix() {
		CommonMatrix c1 = new CommonMatrix(0);
        CommonMatrix c2 = new CommonMatrix(13);        
	}

    @Test(expected=NegativeArraySizeException.class)
	public void testCommonMatrixNegative() {
        CommonMatrix c3 = new CommonMatrix(-12);
    }

	@Test
	public void testKeySet() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(4);
        Set<Integer> keys = new HashSet<Integer>();

        m.set(123,45,514);
        keys.add(123);
        keys.add(45);
        m.set(13,435,95);
        keys.add(13);
        keys.add(435);
                
        assertEquals(keys, m.keySet());
	}

    @Test(expected=SizeLimitExceededException.class)
	public void testKeySetExceedSize() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(4);
        Set<Integer> keys = new HashSet<Integer>();

        m.set(123,45,514);
        keys.add(123);
        keys.add(45);
        m.set(13,435,95);
        keys.add(13);
        keys.add(435);

        m.set(344,4435,95);
        keys.add(344);
        keys.add(4435);

        assertEquals(keys, m.keySet());
	}

	@Test
	public void testGet() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(10);
		m.set(887,478,12);
		assertEquals(12, m.get(887,478));
		
		m.set(83,124,889);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));
		
		m.set(-7,1478,102);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));
		assertEquals(102, m.get(-7,1478));
		
		m.set(-1002,-9902,-90);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));
		assertEquals(102, m.get(-7,1478));
		assertEquals(-90, m.get(-1002,-9902));
	}
	
	@Test
	public void testGetNotSet() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(10);
		assertEquals(0, m.get(887,478));
	}
	
	
	@Test
	public void testSet() throws SizeLimitExceededException {
				CommonMatrix m = new CommonMatrix(10);
		m.set(887,478,12);
		assertEquals(12, m.get(887,478));

		m.set(83,124,889);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));

		m.set(-7,1478,102);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));
		assertEquals(102, m.get(-7,1478));

		m.set(-1002,-9902,-90);
		assertEquals(12, m.get(887,478));
		assertEquals(889, m.get(83,124));
		assertEquals(102, m.get(-7,1478));
		assertEquals(-90, m.get(-1002,-9902));
	}

	@Test
	public void testGetRow() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(10);
        Map<Integer, Integer> row1 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> row2 = new HashMap<Integer, Integer>();

        // empty row
		assertEquals(row1, m.getRow(887));

        m.set(887,478,12);
        row1.put(478,12);
		assertEquals(row1, m.getRow(887));

		m.set(887,124,889);
        m.set(887,124,839);
        row1.put(124,839);
        assertEquals(row1, m.getRow(887));

        m.set(887,13,39);
        row1.put(13,39);
        assertEquals(row1, m.getRow(887));

		m.set(-7,1478,102);
        row2.put(1478,102);
        assertEquals(row2, m.getRow(-7));
				
	}

	@Test
	public void testGetColumn() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(10);
		
		Map<Integer,Integer> mex1 = new TreeMap<Integer, Integer>();
		Map<Integer,Integer> mex2 = new TreeMap<Integer, Integer>();
		
		m.set(101,101,11);
		m.set(102,101,12);
		m.set(103,101,13);
		m.set(104,101,14);
		m.set(105,101,15);
		mex1.put(101,11);
		mex1.put(102,12);
		mex1.put(103,13);
		mex1.put(104,14);
		mex1.put(105,15);
		
		m.set(101,101,11);
		m.set(101,102,21);
		m.set(101,103,31);
		m.set(101,104,41);
		m.set(101,105,51);
		mex2.put(101,11);
		mex2.put(102,21);
		mex2.put(103,31);
		mex2.put(104,41);
		mex2.put(105,51);
						
		Map<Integer,Integer> mx1 = m.getColumn(201);
		assertEquals(new TreeMap<Integer,Integer>(), mx1);
		mx1 = m.getColumn(101);
		assertEquals(mex1, mx1);
		
		Map<Integer,Integer> mx2 = m.getRow(201);
		assertEquals(new TreeMap<Integer,Integer>(), mx2);
		mx2 = m.getRow(101);
		assertEquals(mex2, mx2);
	}

	@Test
	public void testGetSize() throws SizeLimitExceededException {
		CommonMatrix m = new CommonMatrix(10);


		m.set(101,101,11);
		m.set(102,101,12);
		m.set(103,101,13);
		m.set(104,101,14);
		m.set(105,101,15);

        assertEquals("size has changed during the usage",10, m.getSize());

		m.set(101,101,11);
		m.set(101,102,21);
		m.set(101,103,31);
		m.set(101,104,41);
		m.set(101,105,51);

        assertEquals("size has changed during the usage",10, m.getSize());
	}

}
