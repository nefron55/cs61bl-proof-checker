import junit.framework.TestCase;


public class ExpressionTest extends TestCase {

	public void testExpression() {
		boolean pass = true;
		String[] testStrings = {
				"a",
				"(q=>q)",
				"(p=>(~p=>q))",
				"~a",
				"~~a",
				"p",
				"(~p=>q)",
				"(a=>~~a)",
				"(p|q)",
				"(((p=>q)=>q)=>((q=>p)=>p))",
				"(p&q)",
				"~(p&q)",
				"~~(p&q)",
				"(~~p=>p)",
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
	
	public void testIsApplicable() throws IllegalLineException{ //testing theorem application
		Expression exp = new Expression("(~~p=>p)"); //testing double negative theorem
		Expression test = new Expression("(~~(p=>q)=>(p=>q))");
		assertTrue(exp.isApplicable(test));
		
		exp = new Expression("((x&y)=>x)"); //testing and1 theorem
		test = new Expression("(((a|b)&~c)=>(a|b))");
		assertTrue(exp.isApplicable(test));
		
		exp = new Expression("(a=>(b=>(a&b)))"); //testing buildAnd theorem
		test = new Expression("(~p=>(~q=>(~p&~q)))");
		assertTrue(exp.isApplicable(test));
		
		exp = new Expression("((~a&~b)=>~(a|b))"); //testing demorgan2
		test = new Expression("((~p&~q)=>~(p|q))");
		assertTrue(exp.isApplicable(test));
		
	}
}
