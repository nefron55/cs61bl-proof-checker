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
	
	public static final List<String[]> sampleProofs = new ArrayList<String[]>();
	
		public void testIsComplete() {
			sampleProofs.add(sampleProof1);
			sampleProofs.add(sampleProof2);
			
			for (String[] sampleProof : sampleProofs) {
				Proof p = new Proof();
				for (String line : sampleProof) {
					try {
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
	
		public static void testProofs() throws IllegalLineException, IllegalInferenceException {
			/*TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show (p=>(~p=>q))");*/
		}

		public static void testPrint() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
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
			p.extendProof("show q");
			p.extendProof("assume p");
			p.extendProof("assume (p=>q)");
			p.extendProof("mp 2 3 q");
			
			Proof p2 = new Proof(t);
			p2.extendProof("show (q=>p)");
			p2.extendProof("assume (p=>q)");
			p2.extendProof("assume ((p=>q)=>(q=>p))");
			p2.extendProof("mp 2 3 (q=>p)");
			
			Proof p3 = new Proof(t);
			p3.extendProof("show (~(q=>p))");
			p3.extendProof("assume (~(p=>q))");
			p3.extendProof("assume ((~(p=>q))=>(~(q=>p)))");
			p3.extendProof("mp 2 3 (~(q=>p))");
			
			Proof p4 = new Proof(t);
			p4.extendProof("show ((~(p=>q))=>(~(k=>l)))");
			p4.extendProof("assume ((p=>q)=>(k=>l))");
			p4.extendProof("assume (((p=>q)=>(k=>l))=>((~(p=>q))=>(~(k=>l))))");
			p4.extendProof("mp 2 3 ((~(p=>q))=>(~(k=>l)))");
		}
		
		public static void testMT() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show ~p");
			p.extendProof("assume ~q");
			p.extendProof("assume (p=>q)");
			p.extendProof("mt 2 3 ~p");
			
			Proof p2 = new Proof(t);
			p2.extendProof("show (~(p=>q))");
			p2.extendProof("assume (~(q=>p))");
			p2.extendProof("assume ((p=>q)=>(q=>p))");
			p2.extendProof("mt 2 3 (~(p=>q))");
			
			Proof p4 = new Proof(t);
			p4.extendProof("show (~((p=>q)=>(q=>r)))");
			p4.extendProof("assume (~((a=>b)=>(b=>c)))");
			p4.extendProof("assume (((p=>q)=>(q=>r))=>((a=>b)=>(b=>c)))");
			p4.extendProof("mt 2 3 (~((p=>q)=>(q=>r)))");
		}
		
		public static void testIC() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show (p=>q)");
			p.extendProof("assume q");
			p.extendProof("ic 2 (p=>q)");
			
			p = new Proof(t);
			p.extendProof("show ((~(p=>q))=>(p=>q))");
			p.extendProof("assume (p=>q)");
			p.extendProof("ic 2 ((~(p=>q))=>(p=>q))");
		}
		
		public static void testRepeat() throws IllegalLineException, IllegalInferenceException {
			TheoremSet t = new TheoremSet();
			Proof p = new Proof(t);
			p.extendProof("show (p=>p)");
			p.extendProof("show (p=>p)");
			p.extendProof("assume p");
			p.extendProof("ic 2.1 (p=>p)");
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
