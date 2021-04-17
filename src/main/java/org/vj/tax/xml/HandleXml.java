package org.vj.tax.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class HandleXml extends DefaultHandler {

    private StringBuilder currentValue = new StringBuilder();
    private StringBuilder allProductValues = new StringBuilder();

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {

        // reset the tag value at the start of each tag in xml
        currentValue.setLength(0);
    }

    @Override
    public void endElement(String uri,
                           String localName,
                           String qName) {

    	//append values without unnecessary xml tag spaces
        if (qName.equalsIgnoreCase("name")) {
            allProductValues.append(currentValue.toString() + " ");
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
    	
    	//stream xml tags to get the names of the goods
        currentValue.append(ch, start, length);
    }
    
    public String[] getString()
    {
    	//return the goods exempted list
    	return allProductValues.toString().split(" ");
    }

}