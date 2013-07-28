import junit.framework.TestCase;


public class LineNumberTest extends TestCase {
	public void testIncrement(){
		LineNumber n = new LineNumber();
		n.increment();
		assertEquals(n.toString(), "2");
		n.addPoint();
		assertEquals(n.toString(), "2.1");
		n.increment();
		assertEquals(n.toString(), "2.2");
		n.addPoint();
		assertEquals(n.toString(), "2.2.1");
		n.resetPoint();
		assertEquals(n.toString(), "2.3");
	}
	
	public void testisLegal() throws IllegalLineException{
		LineNumber n = new LineNumber();
		LineNumber m = new LineNumber();
		n.increment();
		n.addPoint();
		m.increment();
		m.increment();
		assertFalse(LineNumber.isLegal(n.toString(), m));
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
