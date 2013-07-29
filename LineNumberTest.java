import junit.framework.TestCase;


public class LineNumberTest extends TestCase {
	
	public void testaddPoint() throws IllegalLineException {
		LineNumber n = new LineNumber();
		n.addPoint();
		assertEquals(n.toString(), "1.1");
		n.addPoint();
		assertEquals(n.toString(), "1.1.1");
		n.increment();
		assertEquals(n.toString(), "1.1.2");
		n.removePeriod();
		assertEquals(n.toString(), "1.1");
		n.addPoint();
		assertEquals(n.toString(), "1.1.1");
	}
	
	public void testRemovePeriod() throws IllegalLineException{
		LineNumber n = new LineNumber();
		n.increment();
		assertEquals(n.toString(), "2");
		n.increment();
		assertEquals(n.toString(), "3");
		n.addPoint();
		assertEquals(n.toString(), "3.1");
		n.addPoint();
		assertEquals(n.toString(), "3.1.1");
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
	}
	

	public void testIncrement() throws IllegalLineException{
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
	
	public void testisLegal() throws IllegalLineException {
		try{
			LineNumber n = new LineNumber("a.b.d.c.s");
			fail("Missing exception");
		} catch (IllegalLineException e) {
			assertEquals("Line not valid.", e.getMessage());
		}	
	}
	
	public void testisLegalReference() throws IllegalLineException{
		LineNumber n = new LineNumber();
		LineNumber m = new LineNumber();
		n.increment();
		n.addPoint();
		m.increment();
		m.increment();
		assertFalse(n.isLegalReference(m.toString()));
		LineNumber p = new LineNumber();
		LineNumber o = new LineNumber();
		p.increment();
		o.increment();
		o.addPoint();
		assertTrue(o.isLegalReference(p.toString()));
	}
	
	public void testLineLevel() throws IllegalLineException {
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
