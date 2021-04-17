package org.vj.tax;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.*;
import java.text.DecimalFormat;

public class Receipt {

	private ArrayList<Goods> goods = new ArrayList<Goods>();
	private double total;
	private double taxTotal;
	
	@SuppressWarnings("resource")
	//accept goods file and exempted list from xml and convert to Goods
	public Receipt(String fileName, String[] exemptedGoods){
		try {

            Scanner input = new Scanner(System.in);
            File file = new File(fileName);
            input = new Scanner(file);
            
            while (input.hasNextLine()) {
            	
            	/*
            	 *read the file and split into words to parse the goods,quantity,price and flag imported/local goods
            	 *for applying approriate tax later on 
            	 */
            	String line = input.nextLine(); 
            	String[] words = line.split(" ");
            	int qty = Integer.parseInt(words[0]);
            	boolean isImported = line.contains("imported");
            	int exemptedGoodsInd = containsGoodsFromArray(line,exemptedGoods); 
            	String exemptedType = null;
            	
            	if(exemptedGoodsInd != -1){
            		//tax exempted goods
            		exemptedType = exemptedGoods[exemptedGoodsInd];
            	}

            	int splitIndex = line.lastIndexOf("at");
          
            	if(splitIndex == -1){  		
            		System.out.println("Something's wrong, missing at in sentence");
            	} else {
            		
                	float price = Float.parseFloat((line.substring(splitIndex + 2))); 
                	String name = line.substring(1, splitIndex); 

                	for(int i = 0;i<qty;i++){                    	
                    	Goods newGoods = null;
                    	if(isImported){
                    		
                    		//imported Goods
                        	if(exemptedType != null){
                        	
                        		//Goods is imported and is exempt of sales tax
                        		//but taxed on import
                        		if(exemptedType.equals("book")){
                        			newGoods = new Goods(name,price,GoodsEnum.IMPORTED_BOOK);
                        		} else if(exemptedType.equals("pills")){
                        			newGoods = new Goods(name,price,GoodsEnum.IMPORTED_MEDICAL);
                        		} else if(exemptedType.equals("chocolate")){
                        			newGoods = new Goods(name,price,GoodsEnum.IMPORTED_FOOD);
                        		}

                        	} else {
                        		
                        		//the Goods is imported and sales taxed
                        		newGoods = new Goods(name,price,GoodsEnum.IMPORTED_OTHERS);
                        	}
                        	
                    	} else {
                        	if(exemptedType != null){
                        		
                        		//not imported and exempt of sales tax
                        		if(exemptedType.equals("book")){
                        			newGoods = new Goods(name,price,GoodsEnum.BOOK);
                        		} else if(exemptedType.equals("pills")){
                        			newGoods = new Goods(name,price,GoodsEnum.MEDICAL);
                        		} else if(exemptedType.equals("chocolate")){
                        			newGoods = new Goods(name,price,GoodsEnum.FOOD);
                        		}

                        	} else {
                        		
                        		//not exempted goods
                        		newGoods = new Goods(name,price,GoodsEnum.OTHERS);
                        	}
                    	}
                    	
                        goods.add(newGoods);
                    }
            	}
            	
            }
            input.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	
	public void calculateTotals(){
		
		int numOfGoods = goods.size();
		
		BigDecimal runningSum = new BigDecimal("0");
		BigDecimal runningTaxSum = new BigDecimal("0");
		
		for(int i = 0;i<numOfGoods;i++){
			
			runningTaxSum = BigDecimal.valueOf(0);
			
			BigDecimal totalBeforeTax = new BigDecimal(String.valueOf(this.goods.get(i).getPrice()));
			
			runningSum = runningSum.add(totalBeforeTax);
			
			//Check for sales and import tax and apply 15%
			if(goods.get(i).isSalesTaxable() && goods.get(i).isImportedTaxable()){			
			    BigDecimal taxPercent = new BigDecimal("0.15");
			    BigDecimal tax = taxPercent.multiply(totalBeforeTax);
			    tax = round(tax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    runningTaxSum = runningTaxSum.add(tax);
			} 
			//check for only salestax and apply sales tax
			else if(goods.get(i).isSalesTaxable()){
			    BigDecimal taxPercent = new BigDecimal("0.10");
			    BigDecimal tax = taxPercent.multiply(totalBeforeTax);
			    tax = round(tax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    runningTaxSum = runningTaxSum.add(tax);
			} 
			//check for only import and apply import tax
			else if(goods.get(i).isImportedTaxable()){
			    BigDecimal taxPercent = new BigDecimal("0.05");
			    BigDecimal tax = taxPercent.multiply(totalBeforeTax);
			    tax = round(tax, BigDecimal.valueOf(0.05), RoundingMode.UP);
			    runningTaxSum = runningTaxSum.add(tax);
			} 
			
			//not taxable local goods
			else {
				System.out.println("No Tax Applied to Goods "+goods.get(i));
			}
			
			goods.get(i).setPrice(runningTaxSum.floatValue() + goods.get(i).getPrice());
			taxTotal += runningTaxSum.doubleValue();
			runningSum = runningSum.add(runningTaxSum);
		}
			//save out sales tax, and total
			taxTotal = roundTwoDecimals(taxTotal);
			total = runningSum.doubleValue();
	}
	
	public static BigDecimal round(BigDecimal val, BigDecimal roundConst,RoundingMode roundingMode) {
		/*
		 * rounding to nearest 0.05
		 * changed formula to x * Round(y/x) since xy/100 doesn't seem to work 
		 * refer to following stackexchange and stackoverflow answers for more clarity on this method
		 * https://math.stackexchange.com/questions/457877/rounding-number-nearest-0-05
		 * https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
		 * 
		 */
			BigDecimal divided = val.divide(roundConst, 0, roundingMode);
			BigDecimal result = divided.multiply(roundConst);
			result.setScale(2, RoundingMode.UNNECESSARY);
			return result;
	}
	
	/*
	 * for 2 decimals
	 * refer https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
	 */
	public double roundTwoDecimals(double d) {
	    DecimalFormat twoDec = new DecimalFormat("#.##");
	    return Double.valueOf(twoDec.format(d));
	}
	
	//helper to find goods
	public static int containsGoodsFromArray(String inputString, String[] goods) {
		
		//returns the index String if found
		int index = -1;
		
		for(int i = 0;i<goods.length;i++){
			index = inputString.indexOf(goods[i]);
			if(index != -1)
				return i;
		}
		//-1 is returned if nothing found
		return -1;
	}
	
	public void printReceipt(){
		int totalGoods = goods.size();
		for(int i = 0;i<totalGoods;i++){
			System.out.println("1" + goods.get(i).getName() + "at " + goods.get(i).getPrice());
		}
		System.out.printf("Total Tax Applied: %.2f\n", taxTotal);
		System.out.println("Total: " + total);
	}
	
}