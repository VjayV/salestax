package org.vj.tax;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.vj.tax.xml.HandleXml;
import org.xml.sax.SAXException;

public class App 
{
	//pass xml as first argument
	//pass the taxable list as second argument
    public static void main( String[] args )
    {
		String[] exemptedItems; 
		
		//Handles file receiving from command line and xml parsing for emempted list
	   	 final String EXEMPTEDFILENAME; //pass path to xml of tax exempted goods, can add more goods to the xml if needed
	   	 final String GOODSFILENAME; //pass path to goods file name to to calculate the total tax
	   	 
	     SAXParserFactory factory = SAXParserFactory.newInstance();
	
	     try {
		   	 
	    	 EXEMPTEDFILENAME = args[0];
		   	 GOODSFILENAME = args[1];
		   	 
	    	 //form a list of xml 
	         SAXParser saxParser = factory.newSAXParser();	
	         HandleXml handler = new HandleXml();
	         saxParser.parse(EXEMPTEDFILENAME, handler);
	         
	         //returns the exempted list
	         exemptedItems = handler.getString();
	        
	         //
	         	for(int i=0;i<exemptedItems.length;i++)
	         		System.out.println(exemptedItems[i]);
	        //
	        //Pass goods file to calculate and print taxed receipt
	 		Receipt receipt = new Receipt(GOODSFILENAME, exemptedItems);	
	 		receipt.calculateTotals();		
	 		receipt.printReceipt();
	
	     } 
	     
	     catch (ParserConfigurationException | SAXException | IOException | ArrayIndexOutOfBoundsException e) {
	    	 if(e.getCause() == null)
	    		 System.out.println("Files may not have been passed properly");	    	 
    		 
	    	 e.printStackTrace();	    	 
	     }
	 }
}