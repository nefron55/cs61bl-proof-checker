import junit.framework.TestCase;


public class ExpressionTest extends TestCase {

	public void testExpression() {
		boolean pass = true;
		String[] testStrings = {
				"(q=>q)",
				"(p=>(~p=>q))",
				"~a",
				"p",
				"(~p=>q)",
				"(p*q)",
				"(((p=>q)=>q)=>((q=>p)=>p))",
				"(p&q)",
				"((p=>q)&(r=>s))",
				"((r|s)=>(x|~y))",
				//"~(r|s)=>(x|~y)",
				"(~(r|s)=>(x|~y))",
				"((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))"
		};
		for (String testString : testStrings) {
			try {
				Expression exp = new Expression(testString);
				System.out.println(exp.toInorderString() + " - GOOD");
				exp.print();
				System.out.println("------------------------------------------------");
			} catch (IllegalLineException e) {
				System.out.println(e.getMessage());
				System.out.println(testString + " - BAD");
				System.out.println("------------------------------------------------");
				pass = false;
			}
		}
		assertTrue(pass);
	}
}
