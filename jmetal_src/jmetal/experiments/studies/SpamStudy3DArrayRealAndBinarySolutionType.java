//  ZEDStudy.java
//
//  Authors:
//       Jiaqi Zhao <jiaqizhao.xd@gmail.com>
//  At IPIU at xidian University
//  Last Modified:
//       9 June 2014 
// 
package jmetal.experiments.studies;

import jmetal.core.Algorithm;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
//import jmetal.experiments.settings.GDE3_Settings_ArrayRealAndBinarySolutionType;
import jmetal.experiments.settings.NSGAII_Settings_ArrayRealAndBinarySolutionType;
//import jmetal.experiments.settings.SPEA2_Settings_ArrayRealAndBinarySolutionType;
//import jmetal.experiments.settings.MOEAD_Settings_ArrayRealAndBinarySolutionType;
//import jmetal.experiments.settings.MOEAD_DRA_Settings_ArrayRealAndBinarySolutionType;
//import jmetal.experiments.settings.SMSEMOA_Settings_ArrayRealAndBinarySolutionType;
//import jmetal.experiments.settings.CHEMOA3D_Settings_ArrayRealAndBinarySolutionType;
import jmetal.experiments.util.Friedman;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class implementing an example experimental study. Three algorithms are 
 * compared when solving the benchmarks, and the hypervolume,
 * spread and additive epsilon indicators are used for performance assessment.
 */
public class SpamStudy3DArrayRealAndBinarySolutionType extends Experiment {

	  /**
	   * Configures the algorithms in each independent run
	   * @param problemName The problem to solve
	   * @param problemIndex
	   * @throws ClassNotFoundException 
	   */
	  public void algorithmSettings(String problemName, 
	      int problemIndex, 
	      Algorithm[] algorithm) throws ClassNotFoundException {
	    try {
	      int numberOfAlgorithms = algorithmNameList_.length;

	      HashMap[] parameters = new HashMap[numberOfAlgorithms];

	      for (int i = 0; i < numberOfAlgorithms; i++) {
	        parameters[i] = new HashMap();
	      } // for

      if (!paretoFrontFile_[problemIndex].equals("")) {
        for (int i = 0; i < numberOfAlgorithms; i++)
          parameters[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
        } // if
        
         parameters[0].put("maxEvaluations_", 300);
         algorithm[0] = new NSGAII_Settings_ArrayRealAndBinarySolutionType(problemName).configure(parameters[0]);
/*         parameters[1].put("maxEvaluations_", 300);
         algorithm[1] = new SPEA2_Settings_ArrayRealAndBinarySolutionType(problemName).configure(parameters[1]);
	     parameters[2].put("maxEvaluations_", 25000);
	     parameters[2].put("dataDirectory_", "MOEADWeightConfiguration");
	     algorithm[2] = new MOEAD_Settings_ArrayRealAndBinarySolutionType(problemName).configure(parameters[2]);
         parameters[3].put("maxEvaluations_", 300);	      
         algorithm[3] = new SMSEMOA_Settings_ArrayRealAndBinarySolutionType(problemName).configure(parameters[3]);
         parameters[4].put("maxEvaluations_", 300);	      
         algorithm[4] = new CHEMOA3D_Settings_ArrayRealAndBinarySolutionType(problemName).configure(parameters[4]);
*/
	    } catch (IllegalArgumentException ex) {
	      Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IllegalAccessException ex) {
	      Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
	    } catch  (JMException ex) {
	      Logger.getLogger(StandardStudy.class.getName()).log(Level.SEVERE, null, ex);
	    }
	  } // algorithmSettings

	  /**
	   * Main method
	   * @param args
	   * @throws jmetal.util.JMException
	   * @throws java.io.IOException
	   */
	  public static void main(String[] args) throws JMException, IOException {
	    SpamStudy3DArrayRealAndBinarySolutionType exp = new SpamStudy3DArrayRealAndBinarySolutionType();
		
	    exp.experimentName_ = "SpamStudy3DArrayRealAndBinarySolutionType";
	    exp.algorithmNameList_ = new String[]{"NSGAII"/*, "SPEA2", "MOEAD", "SMSEMOA", "CHEMOA3D" "AugmentedCH"*/};
	    exp.problemList_ = new String[]{"SpamProblem3DDeactivateRules"};
	    exp.paretoFrontFile_ = new String[]{""};

	    exp.indicatorList_ = new String[]{"SPREAD",/*"VUS","IGD",*/"HV","EPSILON"};

	    int numberOfAlgorithms = exp.algorithmNameList_.length;

	    exp.experimentBaseDirectory_ = "./jmetal-optimization-output/" +
	        exp.experimentName_;
	    exp.paretoFrontDirectory_ = "";

	    exp.algorithmSettings_ = new Settings[numberOfAlgorithms];

	    exp.independentRuns_ = 2;//30;//35;

	    exp.initExperiment();

	    // Run the experiments
	    int numberOfThreads ;
	    exp.runExperiment(numberOfThreads = 4) ;
//	    exp.runExperiment(numberOfThreads = 10) ;

	    exp.generateQualityIndicators() ;

	    // Generate latex tables
	    exp.generateLatexTables() ;

	    // Configure the R scripts to be generated
	    int rows  ;
	    int columns  ;
	    String prefix ;
	    String [] problems ;
	    boolean notch ;

	    // Configuring scripts for ZDT
	    rows = 1 ;
	    columns = 1 ;
	    prefix = new String("SpamProblem3DDeactivateRulesPrefix");
	    problems = new String[]{"SpamProblem3DDeactivateRules"} ;

	    exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = false, exp) ;
	    exp.generateRWilcoxonScripts(problems, prefix, exp) ;

	    // Friedman Tests
	    Friedman test = new Friedman(exp);
	    test.executeTest("SPREAD");
//	    test.executeTest("VUS");
//	    test.executeTest("IGD");
		test.executeTest("HV");	    
		test.executeTest("EPSILON");

	  } // main
	} // ZEDStudy
