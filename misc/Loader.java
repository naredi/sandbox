package se.xalan.misc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;


/**
 * Created by IntelliJ IDEA.
 * User: folke
 * Date: 2005-jul-11
 * Time: 11:06:18
 * To change this template use File | Settings | File Templates.
 */
public class Loader {

    private static String DELIMITER = "";

    public Loader(String delimiter) {
        DELIMITER = delimiter;
    }

    protected DataContainer loadStringArrays(String twinDataRow, DataContainer cont){
        cont.loadArrays(extractFields(loadInList(twinDataRow)));
        return cont;
    }

    private ArrayList loadInList(String twinDataRow){
        ArrayList inlist = new ArrayList();
        Pattern p = Pattern.compile(DELIMITER);
        String[] items = p.split(twinDataRow);

        /*
        * This for-loop will put all column values into
        * an ArrayList and also is assigned to free all
        * Strings in the ASCII-file from their double-
        * quotations. If such a String contains a delimiter
        * this has to be kept in place and it's function
        * as delimiter has to be omited.
        *
        * The 'doublequotation' boolean is set to true
        * if next value starts with a '"' character but
        * doesn't end with one. It's set back to false
        * as soon as the ending '"' is appended.
        */
        boolean doublequotation = false;
        String nextItem = "";
        String tempString= "";
        for (int i=0; i<items.length; i++){

            if(!doublequotation){

                nextItem = (String)items[i];

                /**
                 * If next item is the 'null'-String or
                 * the '""'-String, change it to an empty String.
                 */
                if(nextItem.equals("null")
                        || nextItem.equals("\"\"")){

                    nextItem = "";

                }

                /*
                *If next item is a String in form "foobar",
                * then delete the doublequotations
                * and load it to inlist.
                */
                if(nextItem.startsWith("\"")
                        && nextItem.endsWith("\"")){

                    nextItem = nextItem.substring(1,nextItem.length()-1);

                }

                /*
                * If next item starts with a doublequotation
                * but doesn't end with one, keep the item and
                * merge it with all following items until
                * an item ends with a doublequotation.
                */
                if(nextItem.startsWith("\"")
                        && !nextItem.endsWith("\"")){

                    tempString = nextItem;
                    doublequotation = true;

                }else{

                    /*
                    * If all checkups are done and the String
                    * has passed validation.
                    */
                    inlist.add(nextItem);

                }
            }else{

                /*
                * If next item is part of a String-item which
                * contained a delimiter, this item should be
                * concatenated with the preceding item until
                * the concatenated item ends with a double-
                * quotation-character. Between all those items
                * a new delimiter character has to be inserted.
                */
                tempString = tempString + DELIMITER + (String)items[i];

                /*
                * If next item has become a String in form
                * "foobar", then delete the doublequotations
                * and load it to inlist.
                */
                if(tempString.startsWith("\"")
                        && tempString.endsWith("\"")){

                    doublequotation = false;
                    tempString = tempString.substring(1,tempString.length()-1);
                    inlist.add(tempString);
                    tempString = "";

                }
            }
        }
        return inlist;
    }

    private static LinkedList extractFields(ArrayList inlist){
        LinkedList outlist = new LinkedList();
        outlist.add((String)inlist.get(22)!=""?(String)inlist.get(22):"");   //sex 2(=female) 1(=male)
        outlist.add((String)inlist.get(6)!=""?(String)inlist.get(6):"");    //givenName

        outlist.add((String)inlist.get(8)!=""?(String)inlist.get(8):"");    //surname

        outlist.add((String)inlist.get(11)!=""?(String)inlist.get(11):"");   //address

        if(((String)inlist.get(12)).length()==5){

        // Replace the two last integers in the postal code with
        // two zeroes.
        outlist.add(new StringBuffer((String)inlist.get(12)).substring(0,3) + "00"); //postalCode
            outlist.add((String)inlist.get(13)!=""?(String)inlist.get(13):"z");   //city
            outlist.add((String)inlist.get(14)!=""?(String)inlist.get(14):"z");   //countyCode
            outlist.add((String)inlist.get(15)!=""?(String)inlist.get(15):"z");   //council
            outlist.add((String)inlist.get(16)!=""?(String)inlist.get(16):"z");   //parish
            outlist.add((String)inlist.get(17)!=""?(String)inlist.get(17):"z");   //country
        }
        else{
            outlist.add("ignore");
                }
        return outlist;
    }

}
