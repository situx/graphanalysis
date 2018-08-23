package de.unifrankfurt.texttechnologie.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
/**Parser for parsing the Matrix input file format and the GraphML format.
 * 
 * @author Timo Homburg
 *
 */
public class Parser implements ParserAPI{
	/**The adjacency matrix.*/
	private double[][] matrix;
	/**Map from Nodeid to Nodecount.*/
	private Map<String,Integer> deftocolmap;
	/**The file to parse.*/
	private File file;
	/**Format the output for the matrix toString().*/
	private DecimalFormat format;
	/**Map from Nodecount to Nodelabel (if it exists).*/
	private Map<Integer,String> wordmap;

	/**
	 * Constructor for Parser.
	 * @param file the file to parse
	 */
	public Parser(final File file){
		this.file=file;
		this.wordmap=new TreeMap<Integer,String>();
		this.deftocolmap=new TreeMap<String,Integer>();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(','); 
		this.format = new DecimalFormat("#.#################", otherSymbols);
	}
	
	@Override
	public void exportResult(final double[] exportVector) throws IOException {
		BufferedWriter writer= new BufferedWriter(new FileWriter(new File("out/"+this.file.getName()+"_out.dat")));
		int i=0;

		for(Double d:exportVector){
			writer.write(i+++".00000000000000000 ");
		}
		writer.write('\n');
		for(Double d:exportVector){
			writer.write(format.format(d)+" ");
		}
		writer.write('\n');
		writer.close();
	}

	/**
	 * Prints the given matrix toString() for debugging purposes.
	 */
	public void matrixToString(){
		System.out.println("Parsed Matrix: ");
		for(int i=0;i<matrix.length;i++){
			for(Double d:this.matrix[i]){
				System.out.print(d+" ");
			}
			System.out.print("\n");
		}
	}

	@Override
	public double[][] parseData() throws MatrixException{
		final BufferedReader reader;
		int count=0;
		String temp;
		String[] temparray;
		try {
			reader = new BufferedReader(new FileReader(this.file));
			while((temp=reader.readLine())!=null){
				temparray=temp.split(" ");
				if(this.matrix==null){
					this.matrix=new double[temparray.length][temparray.length];
					for(int i=0;i<this.matrix.length;i++){
						this.wordmap.put(i, i+"");
					}
				}else{
					if(temparray.length!=this.matrix[0].length){
						reader.close();
						throw new MatrixException("No square matrix defined in the source file!");
					}
				}
				for(int i=0;i<temparray.length;i++){
					this.matrix[count][i]=Double.valueOf(temparray[i]);
				}
				count++;
			}
			reader.close();
			if(count!=this.matrix[0].length){
				throw new MatrixException("No square matrix defined in the source file!");
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.matrixToString();
		return this.matrix;
	}

	@Override
	public double[][] parseGraphML() throws ParserConfigurationException, SAXException, IOException {
		SAXParser sax= SAXParserFactory.newInstance().newSAXParser();
		GraphMLHandler handler=new GraphMLHandler(this.wordmap,this.deftocolmap);
		sax.parse(this.file,handler);
		this.wordmap=handler.getWordmap();
		this.matrix=handler.getAdjMatrix();
		return this.matrix;
	}
	
	@Override
	public Map<Integer, String> getWordmap() {
		return this.wordmap;
	}
	
}
