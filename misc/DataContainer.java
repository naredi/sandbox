package se.xalan.misc;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by IntelliJ IDEA.
 * User: folke
 * Date: 2005-jul-11
 * Time: 10:33:54
 * To change this template use File | Settings | File Templates.
 */
public class DataContainer {

    private List maleGivenNameArray = Collections.synchronizedList(new ArrayList());
    private List femaleGivenNameArray = Collections.synchronizedList(new ArrayList());
    private List surnameArray = Collections.synchronizedList(new ArrayList());
    private List addressArray = Collections.synchronizedList(new ArrayList());
    private List restAddressArray = Collections.synchronizedList(new ArrayList());

    /**
     *
     *
     * @param feedList
     */
    protected void loadArrays(LinkedList feedList) {

        LinkedList feeder = feedList;

        String sex = (String) feeder.removeFirst();
        if (sex.equals("1")) {
            String maleGivenName = (String) feeder.removeFirst();
            if (!maleGivenNameArray.contains(maleGivenName)) {
                maleGivenNameArray.add(maleGivenName);
            }
        } else if (sex.equals("2")) {
            String femaleGivenName = (String) feeder.removeFirst();
            if (!femaleGivenNameArray.contains(femaleGivenName)) {
                femaleGivenNameArray.add(femaleGivenName);
            }
        } else {
            //Om det inte står en 1:a eller 2:a så hoppa över hela raden
            //Den är ju ändå korrupt
            System.out.println(sex);
            return;
        }

        String surname = (String) feeder.removeFirst();
        if (!surnameArray.contains(surname))
            surnameArray.add(surname);

        String address = (String) feeder.removeFirst();
        if (!addressArray.contains(address))
            addressArray.add(address);

        if (!((String) feeder.getFirst()).equals("ignore")) {

            // Make one string out of all adresses except the street address.
            String restAdress =
                    (String) feeder.removeFirst() + "\t" +
                            (String) feeder.removeFirst() + "\t" +
                            (String) feeder.removeFirst() + "\t" +
                            (String) feeder.removeFirst() + "\t" +
                            (String) feeder.removeFirst() + "\t" +
                            (String) feeder.removeFirst();
            if (!restAddressArray.contains(restAdress))
                restAddressArray.add(restAdress);
        }
    }

    //getters and setters
    protected List getMaleGivenNameArray() {
        return maleGivenNameArray;
    }

    /**
     *
     *
     * @return
     */

    protected List getFemaleGivenNameArray() {
        return femaleGivenNameArray;
    }


    /**
     * 
     * @return
     */
    protected List getSurnameArray() {
        return surnameArray;
    }

    protected List getAddressArray() {
        return addressArray;
    }

    protected List getRestAddressArray() {
        return restAddressArray;
    }

}
