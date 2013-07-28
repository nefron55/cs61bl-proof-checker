import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;


public class ExpressionTest extends TestCase {

	public void testExpression() {
		boolean pass = true;
		String[] legalStrings = {
				"p",
				"~a",
				"~~p",
				"~~~p",
				"(q=>q)",
				"(~p=>q)",
				"(p=>(~p=>q))",
				"(p&q)",
				"(p|q)",
				
				"((p=>q)&(r=>s))",
				"((r|s)=>(x|~y))",
				"(~(r|s)=>(x|~y))",
				"((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))",
				"((a=>b)=>((b=>c)=>(a=>c)))",
				"(p=>((p=>q)=>q))",
				"(((p=>q)=>q)=>((q=>p)=>p))",
				"(~~p=>p)",
				"(~~~a=>~a)",
				"(~p=>(~q=>(~p&~q)))"
		};
		
		String[] illegalStrings = {
				"(p*q)",				
				"~(r|s)=>(x|~y)"
		};
		
		int legalCount = legalStrings.length;
		List<List<String>> testStrings = new ArrayList<List<String>>();
		testStrings.add(new ArrayList<String> (Arrays.asList(legalStrings)));
		(testStrings.get(0)).addAll(new ArrayList<String>(Arrays.asList(illegalStrings)));
		
		int failCount = 0;
		
		for (String testString : testStrings.get(0)) {
			legalCount--;
			try {
				Expression exp = new Expression(testString);
				if (legalCount >= 0) {
					System.out.println(exp.toInorderString() + " - GOOD");
					exp.print();					
				} else {
					System.out.println(exp.toInorderString() + " - BAD, " +
							"Shouldn't be able to construct Expression from this string");
					failCount++;
					pass = false;					
				}
				System.out.println("------------------------------------------------");
			} catch (IllegalLineException e) {
				System.out.println(e.getMessage());
				if (legalCount >= 0) {
					System.out.println(testString + " - BAD");
					System.out.println(e.getMessage());
					failCount++;
					pass = false;
				} else {
					System.out.println(testString + " - GOOD, Exception thrown as expected.");
				}
				
				System.out.println("------------------------------------------------");
				
			}
		}
		System.out.println("LEGAL STRINGS: " + legalStrings.length + ". ILLEGAL STRINGS: " + illegalStrings.length + ".");
		System.out.println("FAILS: " + failCount);
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
