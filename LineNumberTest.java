import junit.framework.TestCase;


public class LineNumberTest extends TestCase {
	
	public void testaddPoint() {
		LineNumber n = new LineNumber();
		n.addPoint();
		assertEquals(n.toString(), "1.1");
		n.addPoint();
		assertEquals(n.toString(), "1.1.1");
		n.increment();
		assertEquals(n.toString(), "1.1.2");
		try {
			n.removePeriod();
		} catch (IllegalLineException e) {
			fail(e.getMessage());
		}
		assertEquals(n.toString(), "1.1");
		n.addPoint();
		assertEquals(n.toString(), "1.1.1");
	}
	
	public void testRemovePeriod() {
		LineNumber n = new LineNumber();
		n.increment();
		assertEquals(n.toString(), "2");
		n.increment();
		assertEquals(n.toString(), "3");
		n.addPoint();
		assertEquals(n.toString(), "3.1");
		n.addPoint();
		assertEquals(n.toString(), "3.1.1");
		try {
			n.removePeriod();
			assertEquals(n.toString(), "3.1");
			n.removePeriod();
			assertEquals(n.toString(), "3");
			try {
				n.removePeriod();
				fail("Missing exception");
			} catch(IllegalLineException e) {
				assertEquals("No period to remove", e.getMessage());
			}
		} catch (IllegalLineException e) {
			fail(e.getMessage());
		}
	}
	

	public void testIncrement() {
		LineNumber n = new LineNumber();
		n.increment();
		assertEquals(n.toString(), "2");
		n.addPoint();
		assertEquals(n.toString(), "2.1");
		n.increment();
		assertEquals(n.toString(), "2.2");
		n.addPoint();
		assertEquals(n.toString(), "2.2.1");
		try {
			n.removePeriod();
		} catch(IllegalLineException e) {
			assertEquals("No period to remove", e.getMessage());
		}
		n.increment();
		assertEquals(n.toString(), "2.3");
	}
	
	public void testisLegal() {
		try{
			LineNumber n = new LineNumber("a.b.d.c.s");
			fail("Missing exception");
		} catch (IllegalLineException e) {
			assertEquals("Line not valid.", e.getMessage());
		}	
	}
	
	public void testisLegalReference() {
		LineNumber n = new LineNumber();
		LineNumber m = new LineNumber();
		n.increment();
		n.addPoint();
		m.increment();
		m.increment();
		try {
			assertFalse(n.isLegalReference(m.toString()));
		} catch (IllegalLineException e) {
			fail(e.getMessage());
		}
		LineNumber p = new LineNumber();
		LineNumber o = new LineNumber();
		p.increment();
		o.increment();
		o.addPoint();
		try {
			assertTrue(o.isLegalReference(p.toString()));
		} catch (IllegalLineException e) {
			fail(e.getMessage());
		}
	}
	
	public void testLineLevel() {
		LineNumber n = new LineNumber();
		n.increment();
		assertEquals(n.toString(), "2");
		n.addPoint();
		assertEquals(n.toString(), "2.1");
		n.increment();
		assertEquals(n.toString(), "2.2");
		n.addPoint();
		n.addPoint();
		assertTrue(LineNumber.lineLevel(n.toString()) == 3);
	}
}
