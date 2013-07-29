import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;


public class ExpressionTest extends TestCase {

	private static final String[] legalStrings = {
			"p",
			"~a",
			"~~p",
			"~~~p",
			"~(p=>q)",
			"(p|q)",
			"~(p|q)",
			"~~(p=>q)",
			"(q=>q)",
			"(~p=>q)",
			"(p=>(~p=>q))",
			"(p&q)",
			
			"((p=>q)&(r=>s))",
			"((r|s)=>(x|~y))",
			"(~(r|s)=>(x|~y))",
			"((((r|s)=>(x|~y))&(~(r|s)=>(x|~y)))=>(x|~y))",
			"((a=>b)=>((b=>c)=>(a=>c)))",
			"(p=>((p=>q)=>q))",
			"(((p=>q)=>q)=>((q=>p)=>p))",
			"(~~p=>p)",
			"(~~~a=>~a)",
			"(~p=>(~q=>(~p&~q)))",
			"~(~(p=>q)=>(a=>b))",
			"(~(~(p|q)&~(p=>q))&~q)"
			
	};
	
	private static final String[] illegalStrings = {
			"",
			"&",
			"1",
			"(a|3)",
			"a|b",
			"(~a)",
			"aa",
			"a(",
			"(a)",
			"(~(q=>p))",
			"((a))",
			"(p=>)",
			"(p= >q)",
			"(p=>p)(q=>q)",
			"(p*q)",				
			"~(r|s)=>(x|~y)",
			"~(~(p|q)&~(p=>q))&~q"
	};
	
	// Tests constructing expression from a String
	public void testExpression() {
		boolean pass = true;

		
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
	
	public void testToInorderString() {
		for (String string : legalStrings) {
			try {
				Expression exp = new Expression(string);
				assertTrue(exp.toInorderString().equals(string));
			} catch (IllegalLineException e) {
				fail(e.getMessage());
			}
		}
		
	}
	
	// TESTS for ExpNode methods
	
	public void testReplaceLeaves() {
		
	}
	
	//testing theorem application
	public void testIsApplicable() { 
		String[] theorems = {
			"(~~p=>p)", //testing double negative theorem
			"((x&y)=>x)", //testing and1 theorem
			"(a=>(b=>(a&b)))", //testing buildAnd theorem
			"((~a&~b)=>~(a|b))" //testing demorgan2
		};
		
		String[] matchingExps = {
			"(~~(p=>q)=>(p=>q))",
			"(((a|b)&~c)=>(a|b))",
			"(~p=>(~q=>(~p&~q)))",
			"((~p&~q)=>~(p|q))"
		};
		
		for (int i = 0; i < theorems.length; i++) {
			try {
				Expression thm = new Expression(theorems[i]);
				Expression exp = new Expression(matchingExps[i]);
				assertTrue(thm.isApplicable(exp));
			} catch (IllegalLineException e) {
				fail(e.getMessage());
			}
		}
	}
	
	public void testIsLeftBranchOf() {
		
	}
	
	public void testIsRightBranchOf() {
		
	}
	
	public void testIsNegation() {
		
	}
	
	public void testIsFollows() {
		
	}
	
	private static final String[] expressions = {
		"(a=>b)",
		"(~~p=>p)", 
		"((x&y)=>x)",
		"~(a|b)",
		"(a|b)",
		"(a&b)",
		"a"
	};
	
	public void testGetMyRight() {
		String[] rights = {
				"b",
				"p",
				"x",
				"(a|b)",
				"b",
				"b",
				null
			};
			
			for (int i = 0; i < expressions.length; i++) {
				try {
					Expression exp = new Expression(expressions[i]);
					Expression right;
					if (rights[i] == null) {
						right = null;
					} else {
						right = new Expression(rights[i]);
					}
					if (exp.getMyRight() != null) {
						assertTrue(exp.getMyRight().isEqual(right.getMyRoot()));
					} else {
						assertTrue(right == null);
					}
				} catch (IllegalLineException e) {
					fail(e.getMessage());
				}
			}		
	}
	
	public void testGetMyLeft() {			
			String[] lefts = {
				"a",
				"~~p",
				"(x&y)",
				null,
				"a",
				"a",
				null
			};
			
			for (int i = 0; i < expressions.length; i++) {
				try {
					Expression exp = new Expression(expressions[i]);
					Expression left;
					if (lefts[i] == null) {
						left = null;
					} else {
						left = new Expression(lefts[i]);
					}
					if (exp.getMyLeft() != null) {
						assertTrue(exp.getMyLeft().isEqual(left.getMyRoot()));
					} else {
						assertTrue(left == null);
					}
				} catch (IllegalLineException e) {
					fail(e.getMessage());
				}
			}
	}
	
	public void testGetMyItem() {			
		String[] roots = {
				"=>",
				"=>",
				"=>",
				"~",
				"|",
				"&",
				"a"
			};
			
			for (int i = 0; i < expressions.length; i++) {
				try {
					Expression exp = new Expression(expressions[i]);
					assertTrue(exp.getMyItem().equals(roots[i]));
				} catch (IllegalLineException e) {
					fail(e.getMessage());
				}
			}
	}
		
}
