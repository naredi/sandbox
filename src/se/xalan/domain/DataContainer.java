package se.xalan.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataContainer {

    private List<String> maleGivenNameArray = Collections.synchronizedList(new ArrayList<String>());
    private List<String> femaleGivenNameArray = Collections.synchronizedList(new ArrayList<String>());
    private List<String> surnameArray = Collections.synchronizedList(new ArrayList<String>());
    private List<String> addressArray = Collections.synchronizedList(new ArrayList<String>());
    private List<String> restAddressArray = Collections.synchronizedList(new ArrayList<String>());

    /**
     *
     *
     * @param feedList
     */
    public void loadArrays(LinkedList<String> feedList) {

        LinkedList<String> feeder = feedList;

        String sex = (String) feeder.removeFirst();
        switch (sex) {
            case "1":
                String maleGivenName = (String) feeder.removeFirst();
                if (!maleGivenNameArray.contains(maleGivenName)) {
                    maleGivenNameArray.add(maleGivenName);
                }
                break;
            case "2":
                String femaleGivenName = (String) feeder.removeFirst();
                if (!femaleGivenNameArray.contains(femaleGivenName)) {
                    femaleGivenNameArray.add(femaleGivenName);
                }
                break;
            default:
                System.out.println(sex);
                return;
        }

        String surname = (String) feeder.removeFirst();
        if (!surnameArray.contains(surname)) {
            surnameArray.add(surname);
        }

        String address = (String) feeder.removeFirst();
        if (!addressArray.contains(address)) {
            addressArray.add(address);
        }

        if (!((String) feeder.getFirst()).equals("ignore")) {

            // Make one string out of all addresses except the street address.
            String restAdress =
                    (String) feeder.removeFirst() + "\t"
                    + (String) feeder.removeFirst() + "\t"
                    + (String) feeder.removeFirst() + "\t"
                    + (String) feeder.removeFirst() + "\t"
                    + (String) feeder.removeFirst() + "\t"
                    + (String) feeder.removeFirst();
            if (!restAddressArray.contains(restAdress)) {
                restAddressArray.add(restAdress);
            }
        }
    }

    //getters and setters
    protected List<String> getMaleGivenNameArray() {
        return maleGivenNameArray;
    }

    /**
     *
     *
     * @return
     */
    protected List<String> getFemaleGivenNameArray() {
        return femaleGivenNameArray;
    }

    /**
     *
     * @return
     */
    protected List<String> getSurnameArray() {
        return surnameArray;
    }

    protected List<String> getAddressArray() {
        return addressArray;
    }

    protected List<String> getRestAddressArray() {
        return restAddressArray;
    }
}
