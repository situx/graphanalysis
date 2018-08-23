package de.unifrankfurt.texttechnologie.calc;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import de.unifrankfurt.texttechnologie.data.ParserAPI;

/**
 * Provided the given data class this class provides the needed functions to calculate the Eigenwertcentrality.
 * @author Timo Homburg
 *
 */
public class CalcSteps implements CalcStepsAPI{
	/**The adjmatrix.*/
	private double[][] adjmatrix;
	/**Formater to costumize the vector output.*/
	private DecimalFormat format;
	/**The parser for parsing the input file.*/
	private ParserAPI parser;
	/**Maps from nodeid to NodeLabel.*/
	private Map<Integer,String> wordmap;
	
	/**
	 * Constructor for this class.
	 * @param doubles the adjacency matrix
	 * @param parser the parser for mapping purposes
	 */
	public CalcSteps(final double[][] doubles, final ParserAPI parser){
		this.adjmatrix=doubles;
		this.parser=parser;
		this.format= new DecimalFormat("#.##########");
		this.wordmap=parser.getWordmap();
	}


	@Override
	public void calcCentrality(final Integer steps) {
		int iter ;
		double[] tmpVector=new double [this.adjmatrix[0].length];
		/**The result vector.*/
		double[] result=new double[this.adjmatrix[0].length];
		double tmpABS=0,tmpSum;
		//Initialize starting vector with 1
		for (iter=0; iter<result.length; iter++){ 
			result[iter]=1.0;
		}
		// Write starting vector in a temp vector for calculation
		for (iter=0; iter<tmpVector.length; iter++){ 
			tmpVector[iter]=result[iter];
		}
		//Iterate steps times
		for (iter=0; iter<steps; iter++) {
				tmpABS = 0;
				for (int k = 0; k<tmpVector.length; k++) {
					tmpSum = this.adjmatrix[k][0]*tmpVector [0];
					for (int j=1;j<tmpVector.length; j++){
						tmpSum+=this.adjmatrix [k][j]*tmpVector[j];
					}
					result[k]=tmpSum;
					tmpABS+=tmpSum*tmpSum;
				}

				for (int l=0;l<tmpVector.length;l++) {
					result[l]/=Math.sqrt(tmpABS);
					tmpVector[l]=result[l];
				}
				System.out.print("Step"+iter+":\t [ ");
				for(Double d:tmpVector){
					System.out.print(this.format.format(d)+" ");
				}
				System.out.println(" ]");
		 }
		this.resultOrdered(result);
	}
	
	/**
	 * Degree Centrality als Vergleichsausgabe.
	 */
	public void degreeCentrality(){
		System.out.println("Degree Centrality: ");
		double[] result=new double[this.adjmatrix[0].length];
		for(int i=0;i<this.adjmatrix[0].length;i++){
			for(int j=0;j<this.adjmatrix[0].length;j++){
				result[i]+=this.adjmatrix[i][j];
			}
		}
		this.resultOrdered(result);
	}

	/**
	 * Orders the result nodes in a descending order and exports them
	 * @param result
	 */
	public void resultOrdered(final double[] result){
		try {
			this.parser.exportResult(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print("Result:\t [ ");
		for(Double db:result){
			System.out.print(this.format.format(db)+" ");
		}
		System.out.println("]");
		System.out.println("Nodes in descending order: ");
		double max;
		int id;
		for(int j=0;j<result.length;j++){
			max=-1.;
			id=0;
			for(int i=0;i<result.length;i++){
				if(result[i]>max){
					max=result[i];
					id=i;
				}
			}
			System.out.println(this.wordmap.get(id)+" : "+max);
			result[id]=-1.;
		}
	}
	
}
