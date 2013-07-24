import junit.framework.TestCase;


public class ProofTest extends TestCase {
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
	
	public static void testFails() throws IllegalLineException, IllegalInferenceException {
		
	}
}
