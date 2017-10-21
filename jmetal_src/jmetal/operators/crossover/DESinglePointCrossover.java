package jmetal.operators.crossover;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Differential evolution crossover operators
 * Comments:
 * - The operator receives two parameters: the current individual and an array
 *   of three parent individuals
 * - The best and rand variants depends on the third parent, according whether
 *   it represents the current of the "best" individual or a randon one. 
 *   The implementation of both variants are the same, due to that the parent 
 *   selection is external to the crossover operator. 
 * - Implemented variants:
 *   - rand/1/bin (best/1/bin)
 *   - rand/1/exp (best/1/exp)
 *   - current-to-rand/1 (current-to-best/1)
 *   - current-to-rand/1/bin (current-to-best/1/bin)
 *   - current-to-rand/1/exp (current-to-best/1/exp)
 */
public class DESinglePointCrossover extends Crossover {
	/**
	 * DEFAULT_CR defines a default CR (crossover operation control) value
	 */
	private static final double DEFAULT_CR = 0.5;

	/**
	 * DEFAULT_F defines the default F (Scaling factor for mutation) value
	 */
	private static final double DEFAULT_F = 0.5;

	/**
	 * DEFAULT_K defines a default K value used in variants current-to-rand/1
	 * and current-to-best/1
	 */
	private static final double DEFAULT_K = 0.5;

	/**
	 * DEFAULT_VARIANT defines the default DE variant
	 */
	
	
	private static final double ETA_C_DEFAULT_ = 20.0;
	private Double realCrossoverProbability_ = null;
	private Double binaryCrossoverProbability_ = null;
	private double distributionIndex_ = ETA_C_DEFAULT_;
	
	private static final String DEFAULT_DE_VARIANT = "rand/1/bin";

  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class,
  		                                            ArrayRealSolutionType.class) ;

	private double CR_  ;
	private double F_   ;
	private double K_   ;
	private String DE_Variant_ ; // DE variant (rand/1/bin, rand/1/exp, etc.)

	/**
	 * Constructor
	 */
	public DESinglePointCrossover(HashMap<String, Object> parameters) {
		super(parameters) ;
		
		CR_ = DEFAULT_CR ;
		F_  = DEFAULT_F  ;
		K_  = DEFAULT_K   ;
		DE_Variant_ = DEFAULT_DE_VARIANT ;
		
  	if (parameters.get("CR") != null)
  		CR_ = (Double) parameters.get("CR") ;  		
  	if (parameters.get("F") != null)
  		F_ = (Double) parameters.get("F") ;  		
  	if (parameters.get("K") != null)
  		K_ = (Double) parameters.get("K") ;  		
  	if (parameters.get("DE_VARIANT") != null)
  		DE_Variant_ = (String) parameters.get("DE_VARIANT") ; 
  	
  	
  	if (parameters.get("realCrossoverProbability") != null)
		realCrossoverProbability_ = (Double) parameters.get("realCrossoverProbability") ;  		
	if (parameters.get("binaryrossoverProbability") != null)
		binaryCrossoverProbability_ = (Double) parameters.get("binaryrossoverProbability") ;  		
	if (parameters.get("distributionIndex") != null)
		distributionIndex_ = (Double) parameters.get("distributionIndex") ;  		

	} // Constructor


	/**
	 * Constructor
	 */
	//public DESinglePointCrossover(Properties properties) {
	//	this();
	//	CR_ = (new Double((String)properties.getProperty("CR_")));
	//	F_  = (new Double((String)properties.getProperty("F_")));
	//	K_  = (new Double((String)properties.getProperty("K_")));
	//	DE_Variant_ = properties.getProperty("DE_Variant_") ;
	//} // Constructor

	public Solution doCrossover(Double realProbability,
			Double binaryProbability,
			Solution parent1, 
			Solution parent2,Solution parent3,Solution current) throws JMException {
	
		int jrand ;

		
	Solution child = new Solution(current) ;
	
	XReal xParent0 = new XReal(parent1) ;
	XReal xParent1 = new XReal(parent2) ;
	XReal xParent2 = new XReal(parent3) ;
	XReal xCurrent = new XReal(current) ;
	XReal xChild   = new XReal(child) ;

	
	
	//DE Crossover
	//numberofVariables-just ArrayReal part
	int numberOfVariables = xParent0.size() ;
	jrand = PseudoRandom.randInt(0, numberOfVariables - 1);

	// STEP 4. Checking the DE variant
	if ((DE_Variant_.compareTo("rand/1/bin") == 0) || 
			(DE_Variant_.compareTo("best/1/bin") == 0)) { 
		for (int j=0; j < numberOfVariables; j++) {
			if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
				double value ;
				value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
						                                  xParent1.getValue(j)) ;
				
				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;
      /*
				if (value < xChild.getLowerBound(j)) {
        double rnd = PseudoRandom.randDouble(0, 1) ;
        value = xChild.getLowerBound(j) + rnd *(xParent2.getValue(j) - xChild.getLowerBound(j)) ;
				}
      if (value > xChild.getUpperBound(j)) {
        double rnd = PseudoRandom.randDouble(0, 1) ;
        value = xChild.getUpperBound(j) - rnd*(xChild.getUpperBound(j)-xParent2.getValue(j)) ;
      }
      */
				xChild.setValue(j, value) ;
			}
			else {
				double value ;
				value = xCurrent.getValue(j);
				xChild.setValue(j, value) ;
			} // else
		} // for
	} // if
	else if ((DE_Variant_.compareTo("rand/1/exp") == 0) || 
			     (DE_Variant_.compareTo("best/1/exp") == 0)) {
		for (int j=0; j < numberOfVariables; j++) {
			if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
				double value ;
				value = xParent2.getValue(j)  + F_ * (xParent0.getValue(j) -
						xParent1.getValue(j)) ;

				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;

				xChild.setValue(j, value) ;
			}
			else {
				CR_ = 0.0 ;
				double value ;
				value = xCurrent.getValue(j);
				xChild.setValue(j, value) ;
		  } // else
		} // for		
	} // if
	else if ((DE_Variant_.compareTo("current-to-rand/1") == 0) || 
         (DE_Variant_.compareTo("current-to-best/1") == 0)) { 
		for (int j=0; j < numberOfVariables; j++) {
			double value ;
			value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
				    xCurrent.getValue(j)) +					
					  F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

			if (value < xChild.getLowerBound(j))
				value =  xChild.getLowerBound(j) ;
			if (value > xChild.getUpperBound(j))
				value = xChild.getUpperBound(j) ;

			xChild.setValue(j, value) ;
		} // for		
	} // if
	else if ((DE_Variant_.compareTo("current-to-rand/1/bin") == 0) ||
			     (DE_Variant_.compareTo("current-to-best/1/bin") == 0)) { 
		for (int j=0; j < numberOfVariables; j++) {
			if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
				double value ;
				value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
						xCurrent.getValue(j)) +					
						F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;

				xChild.setValue(j, value) ;
			}
			else {
				double value ;
				value = xCurrent.getValue(j);
				xChild.setValue(j, value) ;
			} // else
		} // for
	} // if
	else if ((DE_Variant_.compareTo("current-to-rand/1/exp") == 0) || 
			(DE_Variant_.compareTo("current-to-best/1/exp") == 0)) {
		for (int j=0; j < numberOfVariables; j++) {
			if (PseudoRandom.randDouble(0, 1) < CR_ || j == jrand) {
				double value ;
				value = xCurrent.getValue(j) + K_ * (xParent2.getValue(j) - 
						xCurrent.getValue(j)) +					
						F_ * (xParent0.getValue(j) - xParent1.getValue(j)) ;

				if (value < xChild.getLowerBound(j))
					value =  xChild.getLowerBound(j) ;
				if (value > xChild.getUpperBound(j))
					value = xChild.getUpperBound(j) ;

				xChild.setValue(j, value) ;
			}
			else {
				CR_ = 0.0 ;
				double value ;
				value = xCurrent.getValue(j);
				xChild.setValue(j, value) ;
			} // else
		} // for		
	} // if		
	else {
		Configuration.logger_.severe("DESinglePointCrossover.execute: " +
				" unknown DE variant (" + DE_Variant_ + ")");
		Class<String> cls = java.lang.String.class;
		String name = cls.getName(); 
		throw new JMException("Exception in " + name + ".execute()") ;
	} // else
	
	
	
	// Single point crossover
			if (PseudoRandom.randDouble()<=binaryProbability) {
				Binary binaryChild0 = (Binary)offSpring[0].getDecisionVariables()[1] ;
				Binary binaryChild1 = (Binary)offSpring[1].getDecisionVariables()[1] ;

				int totalNumberOfBits = binaryChild0.getNumberOfBits() ;

				//2. Calcule the point to make the crossover
				int crossoverPoint = PseudoRandom.randInt(0, totalNumberOfBits - 1);

				//5. Make the crossover;
				for (int i = crossoverPoint; i < totalNumberOfBits; i++) {
					boolean swap = binaryChild0.bits_.get(i);
					binaryChild0.bits_.set(i, binaryChild1.bits_.get(i));
					binaryChild1.bits_.set(i, swap);
				} // for
			} // if

			return offSpring;      
		} // doCrossover
	
	
	return child ;
}
	
	
	
	
	/**
	 * Executes the operation
	 * @param object An object containing an array of three parents
	 * @return An object containing the offSprings
	 */
	public Object execute(Object object) throws JMException {
		Object[] parameters = (Object[])object ;
		Solution current   = (Solution) parameters[0];
		Solution [] parent = (Solution [])parameters[1];
		
    if (!(VALID_TYPES.contains(parent[0].getType().getClass()) &&
          VALID_TYPES.contains(parent[1].getType().getClass()) &&
          VALID_TYPES.contains(parent[2].getType().getClass())) ) {

			Configuration.logger_.severe("DESinglePointCrossover.execute: " +
					" the solutions " +
					"are not of the right type. The type should be 'Real' or 'ArrayReal', but " +
					parent[0].getType() + " and " + 
					parent[1].getType() + " and " + 
					parent[2].getType() + " are obtained");

			Class cls = java.lang.String.class;
			String name = cls.getName(); 
			throw new JMException("Exception in " + name + ".execute()") ;
		}
    	Solution child;
    	child = doCrossover(realCrossoverProbability_, 
			binaryCrossoverProbability_, parent[0], parent[1],parent[2],current);

    	return child ;
	}//execute

		
} // DESinglePointCrossover

