package de.unifrankfurt.texttechnologie.calc;


/**
 * API for the necessary calc functions.
 * @author Timo Homburg
 *
 */
public interface CalcStepsAPI {
	/**Calculates one centrality step.*/
	public void calcCentrality(Integer steps);
	/**Calculates the degree centrality.*/
	public void degreeCentrality();
}
