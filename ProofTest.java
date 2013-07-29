import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;


public class ProofTest extends TestCase {
	private static final String[] sampleProof1 = {
		"show (q=>q)",
		"assume q",
		"ic 2 (q=>q)"
	};
	
	private static final String[] sampleProof2 = {
		"show (p=>(~p=>q))",
		"assume p",
		"show (~p=>q)",
		"assume ~p",
		"co 2 3.1 (~p=>q)",
		"ic 3 (p=>(~p=>q))"
	};
	
	private static final String[] sampleProof3 = {
		"show (((p=>q)=>q)=>((q=>p)=>p))", //1
		"assume ((p=>q)=>q)", 	//2
		"show ((q=>p)=>p)",		//3
		"assume (q=>p)",		//3.1
		"show p",				//3.2
		"assume ~p",			//3.2.1
		"mt 3.2.1 3.1 ~q",		//3.2.2
		"mt 2 3.2.2 ~(p=>q)",	//3.2.3
		"show (p=>q)",			//3.2.4
		"assume p",				//3.2.4.1
		"co 3.2.4.1 3.2.1 (p=>q)",	// 3.2.4.2
		"co 3.2.4 3.2.3 p",		// 3.2.5
		"ic 3.2 ((q=>p)=>p)",	// 3.3
		"ic 3 (((p=>q)=>q)=>((q=>p)=>p))"	//4
	};
	
	private static final String[] sampleProof4 = {
		"show (~~p=>p)", //1
		"assume ~~p", //2
		"show p", //3
		"assume ~p", //3.1
		"co 2 3.1 p", //3.2
		"ic 3 (~~p=>p)", //4
	};
	
	private static final String[] sampleProof5 = {
		"show ((a=>(b=>c))=>((a=>b)=>(a=>c)))",
		"assume (a=>(b=>c))",
		"show ((a=>b)=>(a=>c))",
		"assume (a=>b)",
		"show (a=>c)",
		"assume a",
		"mp 2 3.2.1 (b=>c)",
		"mp 3.2.1 3.1 b",
		"mp 3.2.3 3.2.2 c",
		"ic 3.2.4 (a=>c)",
		"ic 3.2 ((a=>b)=>(a=>c))",
		"ic 3 ((a=>(b=>c))=>((a=>b)=>(a=>c)))"
	};
	
	private static final String[] sampleProof6 = {
		"show (p=>((p=>q)=>q))",
		"assume p",
		"show ((p=>q)=>q)",
		"assume (p=>q)",
		"show q",
		"mp 2 3.1 q",
		"ic 3.2 ((p=>q)=>q)",
		"ic 3 (p=>((p=>q)=>q))"
	};
	
	public static final List<String[]> sampleProofs = new ArrayList<String[]>();
	
		public void testIsComplete() {
			sampleProofs.add(sampleProof1);
			sampleProofs.add(sampleProof2);
			sampleProofs.add(sampleProof3);
			sampleProofs.add(sampleProof4);
			sampleProofs.add(sampleProof5);
			sampleProofs.add(sampleProof6);
			
			for (String[] sampleProof : sampleProofs) {
				Proof p = new Proof();
				for (String line : sampleProof) {
					try {
						//System.out.println(p.getLine() + " - extending proof with: " + line);
						p.extendProof(line);
					} catch (IllegalLineException e) {
						fail(e.getMessage());
						e.printStackTrace();
					} catch (IllegalInferenceException e) {
						fail(e.getMessage());
						e.printStackTrace();
					}
				}
				if (!p.isComplete()) fail("Incomplete");
			}
		}
		
		public void testFacts() {
			
		}
		
		public void testLineNumber() {
			
		}
		
		public void testisNegation() throws IllegalLineException {
			Expression e = new Expression("~p");
			Expression e1 = new Expression("~(p|q)");
			Expression e2 = new Expression("a");
			Expression e3 = new Expression("(p=>q)");
			Expression e4 = new Expression("~(p=>q)");
			assertTrue(e.isNegation());
			assertTrue(e1.isNegation());
			assertFalse(e2.isNegation());
			assertFalse(e3.isNegation());
			assertTrue(e4.isNegation());
		}
		
		public void testisFollows() throws IllegalLineException {
			Expression e = new Expression("~(p=>q)");
			Expression e1 = new Expression("~(p|q)");
			Expression e2 = new Expression("a");
			Expression e3 = new Expression("(p=>q)");
			assertFalse(e.isFollows());
			assertFalse(e1.isFollows());
			assertFalse(e2.isFollows());
			assertTrue(e3.isFollows());
		}
	

		public static void testProofs() throws IllegalLineException, IllegalInferenceException {
			/*TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show (p=>(~p=>q))");*/
		}

		public static void testPrint() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.startDebugging();
			p.extendProof("show (p=>q)");
			p.extendProof("assume a");
			p.extendProof("assume b");
			p.extendProof("assume c");
			p.extendProof("show p");
			p.extendProof("assume p");
			p.extendProof("assume q");
			p.extendProof("print");
		}
		
		public static void testMP() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.startDebugging();
			p.extendProof("show q");
			p.extendProof("assume p");
			p.extendProof("assume (p=>q)");
			p.extendProof("mp 2 3 q");
			
			Proof p2 = new Proof(t);
			p2.startDebugging();
			p2.extendProof("show (q=>p)");
			p2.extendProof("assume (p=>q)");
			p2.extendProof("assume ((p=>q)=>(q=>p))");
			p2.extendProof("mp 2 3 (q=>p)");
			
			Proof p3 = new Proof(t);
			p3.startDebugging();
			p3.extendProof("show ~(q=>p)");
			p3.extendProof("assume ~(p=>q)");
			p3.extendProof("assume (~(p=>q)=>~(q=>p))");
			p3.extendProof("mp 2 3 ~(q=>p)");
			
			Proof p4 = new Proof(t);
			p4.startDebugging();
			p4.extendProof("show (~(p=>q)=>~(k=>l))");
			p4.extendProof("assume ((p=>q)=>(k=>l))");
			p4.extendProof("assume (((p=>q)=>(k=>l))=>(~(p=>q)=>~(k=>l)))");
			p4.extendProof("mp 2 3 (~(p=>q)=>~(k=>l))");
		}
		
		public static void testMT() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.startDebugging();
			p.extendProof("show ~p");
			p.extendProof("assume ~q");
			p.extendProof("assume (p=>q)");
			p.extendProof("mt 2 3 ~p");
			
			Proof p2 = new Proof(t);
			p2.startDebugging();
			p2.extendProof("show ~(p=>q)");
			p2.extendProof("assume ~(q=>p)");
			p2.extendProof("assume ((p=>q)=>(q=>p))");
			p2.extendProof("mt 2 3 ~(p=>q)");
			
			Proof p4 = new Proof(t);
			p4.startDebugging();
			p4.extendProof("show ~((p=>q)=>(q=>r))");
			p4.extendProof("assume ~((a=>b)=>(b=>c))");
			p4.extendProof("assume (((p=>q)=>(q=>r))=>((a=>b)=>(b=>c)))");
			p4.extendProof("mt 2 3 ~((p=>q)=>(q=>r))");
		}
		
		public static void testIC() throws IllegalLineException, IllegalInferenceException {
			//System.out.println("-------------------------------");
			
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.startDebugging();
			p.extendProof("show (p=>q)");
			p.extendProof("assume q");
			p.extendProof("ic 2 (p=>q)");
			
			p = new Proof(t);
			p.startDebugging();
			p.extendProof("show (~(p=>q)=>(p=>q))");
			p.extendProof("assume (p=>q)");
			p.extendProof("ic 2 (~(p=>q)=>(p=>q))");
						
		}
		
		public static void testRepeat() throws IllegalLineException, IllegalInferenceException {
			//System.out.println("-------------------------------");
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show (p=>p)");
			p.extendProof("show (p=>p)");
			p.extendProof("assume p");
			p.extendProof("ic 2.1 (p=>p)");
			p.extendProof("print");
			p.extendProof("repeat 2 (p=>p)");
		}

		/*public static void testnotMP() throws IllegalLineException, IllegalInferenceException {	
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show q");
			p.extendProof("assume ~p");
			p.extendProof("assume (~p=>q)");
			p.extendProof("mp 2 3 q");
		}*/
		
	}
