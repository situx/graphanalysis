package de.unifrankfurt.texttechnologie.main;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unifrankfurt.texttechnologie.calc.CalcSteps;
import de.unifrankfurt.texttechnologie.calc.CalcStepsAPI;
import de.unifrankfurt.texttechnologie.data.MatrixException;
import de.unifrankfurt.texttechnologie.data.Parser;
import de.unifrankfurt.texttechnologie.data.ParserAPI;

/**
 * Main class for starting the program and processing the given data.
 * @author Timo Homburg
 *
 */
public class Main {
	/**
	 * The main method of the program.
	 * @param args the input file, the number of steps
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static void main(final String[] args) throws ParserConfigurationException, SAXException, IOException {
		if(args.length<2){
			System.out.println("No input file and/or steps defined.\nExiting....");
			return;
		}
		final File file= new File(args[0]);
		if(!file.exists()){
			System.out.println("Input file does not exist.\nPlease specify a valid file path\nExiting...");
			return;
		}
		try{
		final Integer steps=Integer.valueOf(args[1]);
		final ParserAPI parser= new Parser(file);
		final CalcStepsAPI calc;
		System.out.println(file.getName());
		if(file.getName().substring(file.getName().lastIndexOf('.')).equals(".graphml")){
			calc=new CalcSteps(parser.parseGraphML(), parser);
		}else{
			calc=new CalcSteps(parser.parseData(),parser);
		}
		calc.calcCentrality(steps);
		//calc.degreeCentrality();
		calc.toString();
		}catch(NumberFormatException e){
			System.out.println("No valid step number defined.\nPlease specify an integer value.\nExiting...");
			e.printStackTrace();
			return;
		} catch (final MatrixException e) {
			System.out.println(e.getMessage());
		}

	}

}
