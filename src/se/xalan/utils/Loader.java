package se.xalan.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import se.xalan.domain.DataContainer;

/**
 *
 *
 */
public class Loader {

	private static String DELIMITER = "";

	public Loader(String delimiter) {
		DELIMITER = delimiter;
	}

	public DataContainer loadStringArrays(String dataRow, DataContainer cont) {
		cont.loadArrays(extractFields(loadInList(dataRow)));
		return cont;
	}

	private ArrayList<String> loadInList(String dataRow) {
		ArrayList<String> inlist = new ArrayList<>();
		Pattern p = Pattern.compile(DELIMITER);
		String[] items = p.split(dataRow);

		/*
		 * This for-loop will put all column values into
		 * an ArrayList and also is assigned to free all
		 * Strings in the ASCII-file from their double-
		 * quotations. If such a String contains a delimiter
		 * this has to be kept in place and it's function
		 * as delimiter has to be omitted.
		 *
		 * The quotation boolean is set to true if next value 
		 * starts with a quotation character and doesn't end 
		 * with one. It's set back to false as soon as the 
		 * quotation ending is appended.
		 */
		boolean quotation = false;
		String nextItem = "";
		String tempString = "";
		for (int i = 0; i < items.length; i++) {

			if (!quotation) {

				nextItem = (String) items[i];

				/**
				 * If next item is the 'null'-String or the '""'-String, change
				 * it to an empty String.
				 */
				if (nextItem.equals("null")
						|| nextItem.equals("\"\"")) {

					nextItem = "";

				}

				/*
				 * If next item is a String in form "someString", 
				 * then delete the quotations and load it into the list.
				 */
				if (nextItem.startsWith("\"")
						&& nextItem.endsWith("\"")) {

					nextItem = nextItem.substring(1, nextItem.length() - 1);

				}

				/*
				 * If next item starts with a quotation but doesn't end with one, 
				 * keep the item and merge it with all following items until
				 * an item ends with a quotation.
				 */
				if (nextItem.startsWith("\"")
						&& !nextItem.endsWith("\"")) {

					tempString = nextItem;
					quotation = true;

				} else {

					/*
					 * If all checks are done and the String
					 * has passed validation.
					 */
					inlist.add(nextItem);

				}
			} else {

				/*
				 * If next item is part of a String that contains a delimiter, 
				 * then this item should be concatenated with the preceding item
				 * until the concatenated item ends with a quotation character. 
				 * Between all concatenated items a new delimiter character has 
				 * to be inserted.
				 */
				tempString = tempString + DELIMITER + (String) items[i];

				/*
				 * If next item has become a String in form "someString", 
				 * then delete the quotations and load it to the list.
				 */
				if (tempString.startsWith("\"")
						&& tempString.endsWith("\"")) {

					quotation = false;
					tempString = tempString.substring(1, tempString.length() - 1);
					inlist.add(tempString);
					tempString = "";

				}
			}
		}
		return inlist;
	}

	private static LinkedList<String> extractFields(ArrayList<String> inlist) {
		LinkedList<String> outlist = new LinkedList<>();
		outlist.add(!"".equals((String) inlist.get(22)) ? (String) inlist.get(22) : "");   //sex 2(=female) 1(=male)
		outlist.add(!"".equals((String) inlist.get(6)) ? (String) inlist.get(6) : "");    //givenName

		outlist.add(!"".equals((String) inlist.get(8)) ? (String) inlist.get(8) : "");    //surname

		outlist.add(!"".equals((String) inlist.get(11)) ? (String) inlist.get(11) : "");   //address

		if (((String) inlist.get(12)).length() == 5) {

			// Replace the two last integers in the postal code with
			// two zeroes.
			outlist.add(new StringBuffer((String) inlist.get(12)).substring(0, 3) + "00"); //postalCode
			outlist.add(!"".equals((String) inlist.get(13)) ? (String) inlist.get(13) : "z");   //city
			outlist.add(!"".equals((String) inlist.get(14)) ? (String) inlist.get(14) : "z");   //countyCode
			outlist.add(!"".equals((String) inlist.get(15)) ? (String) inlist.get(15) : "z");   //council
			outlist.add(!"".equals((String) inlist.get(16)) ? (String) inlist.get(16) : "z");   //parish
			outlist.add(!"".equals((String) inlist.get(17)) ? (String) inlist.get(17) : "z");   //country
		} else {
			outlist.add("ignore");
		}
		return outlist;
	}
}
