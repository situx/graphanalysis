package de.unifrankfurt.texttechnologie.data;

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Handler for the GraphML format.
 * @author Timo Homburg
 *
 */
public class GraphMLHandler extends DefaultHandler {
	/**The adjacency matrix to be created.*/
	private double[][] adjmatrix;
	/**Maps from Nodeid to Nodenumber.*/
	private Map<String,Integer> deftocolmap;
	/**Indicates that the graph is directed or undirected/mixed.*/
	private Boolean directed;
	/**Indicates that an edge is being processed.*/
	private Boolean edge;
	/**A global count of the nodes.*/
	private Integer globalcount;
	/**Indicates that a nodelabel has already been found for this node.*/
	private Boolean gotnodelabel;
	/**Indicates that a node has been found.*/
	private boolean node;
	/**Indicates that a nodelabel has been found.*/
	private Boolean nodelabel;
	/**Temp String for remembering the current nodes' id.*/
	private String tempnodeid;
	/**Maps from Nodenumber to NodeLabel.*/
	private Map<Integer,String> wordmap;
	/**
	 * Constructor for GraphMLHandler.
	 * @param wordmap map for mapping Nodenumber to NodeLabel
	 * @param deftocolmap map for mapping Nodeid to Nodenumber
	 */
	public GraphMLHandler(final Map<Integer,String> wordmap, final Map<String,Integer> deftocolmap){
		this.wordmap=wordmap;
		this.deftocolmap=deftocolmap;
		this.globalcount=0;
		this.edge=false;
		this.nodelabel=false;
		this.directed=false;
		this.node=false;
		this.gotnodelabel=false;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(this.node && this.nodelabel){
			this.wordmap.put(this.globalcount,(this.globalcount++)+": "+new String(ch,start,length));
			System.out.println(globalcount-1+": "+new String(ch,start,length));
			this.nodelabel=false;
			this.gotnodelabel=true;
		}
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		switch(qName){
		case "node": if(!this.gotnodelabel){ 
							this.wordmap.put(this.globalcount++, this.tempnodeid);
							System.out.println(globalcount-1+": "+this.tempnodeid);
					}
					this.node=false;this.gotnodelabel=false;break;
		default:
		}
	}
	@Override
	public void startElement(final String uri, final String localName, final String qName,
			final Attributes attributes) throws SAXException {
		switch(qName){
		case "graph":     if(attributes.getValue("edgedefault")!=null && "directed".equals(attributes.getValue("edgedefault"))){
							directed=true;
							System.out.println("Directed Graph");
						  }else{
							System.out.println("Undirected or Mixed Graph");
							directed=false;
						  }break;
		case "node": this.node=true; tempnodeid=attributes.getValue("id");System.out.println(attributes.getValue("id"));this.deftocolmap.put(attributes.getValue("id"),this.globalcount);break;
		case "y:NodeLabel": nodelabel=true;break;
		case "edge": 		if(!edge){
			             this.adjmatrix=new double[this.globalcount][this.globalcount];
			             System.out.println("Globalcount: "+this.globalcount);
			             this.edge=true;
					 }
			          else{
			        	  System.out.println("Update Adj["+this.deftocolmap.get(attributes.getValue("source"))+"]["+this.deftocolmap.get(attributes.getValue("target"))+"]");
			        	  this.adjmatrix[this.deftocolmap.get(attributes.getValue("source"))][this.deftocolmap.get(attributes.getValue("target"))]=1.;
			        	  if(!this.directed || (attributes.getValue("directed")!=null && !Boolean.getBoolean(attributes.getValue("directed")))){
			        		  this.adjmatrix[this.deftocolmap.get(attributes.getValue("target"))][this.deftocolmap.get(attributes.getValue("source"))]=1.;
			        	  }
			          }
		default:
		}
	}

	/**
	 * Getter for AdjMatrix.
	 * @return the adjmatrix
	 */
	public double[][] getAdjMatrix(){
		return this.adjmatrix;
	}

	/**Getter for Wordmap.
	 * 
	 * @return the map from id to words (according to the adjmatrix)
	 */
	public Map<Integer,String> getWordmap(){
		return this.wordmap;
	}
}
