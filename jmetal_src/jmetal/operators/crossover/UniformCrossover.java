package jmetal.operators.crossover;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealAndBinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

public class UniformCrossover extends Crossover {

	  //Valid solution types to apply this operator 
	  
	  private static final List VALID_TYPES = Arrays.asList(ArrayRealAndBinarySolutionType.class) ;

	  private Double realCrossoverProbability_ = null;
	  private Double binaryCrossoverProbability_ = null;

	 // Constructor, Creates a new instance of the parameters operator
	  public UniformCrossover(HashMap<String, Object> parameters) {
	  	super(parameters);
	  	if (parameters.get("realCrossoverProbability") != null)
			realCrossoverProbability_ = (Double) parameters.get("realCrossoverProbability") ;  		
		if (parameters.get("binaryrossoverProbability") != null)
			binaryCrossoverProbability_ = (Double) parameters.get("binaryrossoverProbability") ;  		
	  } // UniformCrossover


	
	  // Perform the crossover operation.
	   
	  public Solution doCrossover(Double realProbability,Double binaryProbability,
	          Solution parent1,Solution parent2, Solution parent3) throws JMException {
	    
//		  Solution offSpring = (Solution) parent1;

		  Solution offSpring = new Solution(parent1);
		 
		  
	    try {
	        
	        	
	    		//ArrayReal
	    		if (PseudoRandom.randDouble() <= realProbability) {
	    			 XReal x1 = new XReal(parent1) ;		
	    		      XReal x2 = new XReal(parent2) ;		
	    		      XReal x3 = new XReal(parent3) ;	
	    		      XReal offs = new XReal(offSpring) ;
	    		     
	    		      
	    			int numberOfVariables = x1.size() ;
	    			for (int i=0; i<x1.size(); i++){
	    				double[] x=new double[3];
	    				x[0]=x1.getValue(i);
	    				x[1]=x2.getValue(i);
	    				x[2]=x3.getValue(i);
	    				
	    				int rand=PseudoRandom.randInt(0, 2);
	    				offs.setValue(i, x[rand]);
	    				
	    			}
	    			
	    		}
	    		
	    		
	    			
	    			//Binary
	    			if (PseudoRandom.randDouble()<=binaryProbability) {
	    				
	    				Binary binaryChild = (Binary)offSpring.getDecisionVariables()[1] ;
	    				Binary y1 = (Binary)parent1.getDecisionVariables()[1] ;
	    				Binary y2 = (Binary)parent2.getDecisionVariables()[1] ;
	    				Binary y3 = (Binary)parent3.getDecisionVariables()[1] ;
	    				
	    			
	    				int totalNumberOfBits = binaryChild.getNumberOfBits() ;
	    				for (int i = 0; i < totalNumberOfBits; i++) {
	    					
	    					boolean[] y=new boolean[3];
		    				y[0]=y1.bits_.get(i);
		    				y[1]=y2.bits_.get(i);
		    				y[2]=y3.bits_.get(i);
		    				
		    				int rand=PseudoRandom.randInt(0, 2);
		    				binaryChild.bits_.set(i, y[rand]);
		
	    					
	    					
	    				}
	    				
	    			} 
		    			
	  				
	     
	    } catch (ClassCastException e1) {
	      Configuration.logger_.severe("UniformCrossover.doCrossover: Cannot perform " +
	              "UniformCrossover");
	      Class cls = java.lang.String.class;
	      String name = cls.getName();
	      throw new JMException("Exception in " + name + ".doCrossover()");
	    }
	    return offSpring;
	  } // doCrossover

	  /**
	   * Executes the operation
	   * @param object An object containing an array of two solutions
	   * @return An object containing an array with the offSprings
	   * @throws JMException
	   */
	  public Object execute(Object object) throws JMException {
		  Object[] parameters = (Object[])object ;
			
			Solution [] parents = (Solution [])parameters[1];
	 

	    if (!(VALID_TYPES.contains(parents[0].getType().getClass())  &&
	        VALID_TYPES.contains(parents[1].getType().getClass()) &&
		        VALID_TYPES.contains(parents[2].getType().getClass())) ) {

	      Configuration.logger_.severe("UniformCrossover.execute: the solutions " +
	              "are not of the right type. ");

	      Class cls = java.lang.String.class;
	      String name = cls.getName();
	      throw new JMException("Exception in " + name + ".execute()");
	    } // if

	    if (parents.length < 3) {
	      Configuration.logger_.severe("UniformCrossover.execute: operator " +
	              "needs 3 parents");
	      Class cls = java.lang.String.class;
	      String name = cls.getName();
	      throw new JMException("Exception in " + name + ".execute()");
	    } 
	    
	   
	    Solution offSpring = doCrossover(realCrossoverProbability_,binaryCrossoverProbability_,parents[0],parents[1],parents[2]);

	    
	    
	   
	    return offSpring;
	  } // execute
	
	
	
}
