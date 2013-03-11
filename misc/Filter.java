package se.xalan.misc;
import java.util.LinkedList;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class Filter
{
    private static ArrayList inlist = new ArrayList();
    private static LinkedList outlist = new LinkedList();

    /**
     * Creates a new <code>ASCIIFilter</code> instance.
     *
     */
    public Filter()
    {
    }

    /**
     * Describe <code>arrangeFields</code> method here.
     *
     * @param nextRow a <code>String</code> value
     * @return a <code>LinkedList</code> value
     */
    public LinkedList arrangeFields(String nextRow, String delimiter)
    {
        loadList(nextRow, delimiter);
        outlist.clear();
        outlist.add(inlist.get(3));    //pin
        outlist.add(inlist.get(1));    //PAIRID
        outlist.add(inlist.get(2));    //TVAB
        outlist.add(inlist.get(4));    //birthdate yyyy-mm-dd
        outlist.add(inlist.get(6));    //givenName
        outlist.add(inlist.get(7));    //middleName null
        outlist.add(inlist.get(8));    //surname
        outlist.add(inlist.get(11));   //address
        outlist.add(inlist.get(9));   //coAddress
        outlist.add(inlist.get(10));   //foAddress
        outlist.add(inlist.get(12));   //postalCode
        outlist.add(inlist.get(13));   //city
        outlist.add(inlist.get(14));   //countyCode
        outlist.add(inlist.get(15));   //council
        outlist.add(inlist.get(16));   //parish
        outlist.add(inlist.get(17));   //country
        outlist.add(inlist.get(18));   //addrSource
        outlist.add(inlist.get(19));   //telephone
        outlist.add("");                       //cellPhone
        outlist.add("");                       //email
        outlist.add(inlist.get(31));   //civilStat 1,2 eller ...
        outlist.add(inlist.get(20));/////vitalStat 0 (=alive) eller ...
        outlist.add(inlist.get(21));   //deathdate
        outlist.add(inlist.get(22));   //sex 2(=female) 1(=male)
        outlist.add(inlist.get(23));   //zygosity 4 eller
        outlist.add("");                       //comment
        return outlist;
    }

    private void loadList(String inrow, String delim){
        //   private void loadList(String inrow){

        //delimiters
        String delimiter = delim;
        String charDelim = "";

        //character representation
        if(delim.equals("tabulation")){
            delimiter = "\t";
            charDelim = "\t";
        }

        if(delim.equals("comma")){
            delimiter = "\u002C";
            charDelim = ",";
        }

        if(delim.equals("semicolon")){
            delimiter = "\u003B";
            charDelim = ";";
        }

        inlist.clear();
        Pattern p = Pattern.compile(delimiter);
        String[] items = p.split(inrow);

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
                nextItem = items[i];
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
                tempString = tempString + charDelim + items[i];
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
    }

}// ASCIIFilter

