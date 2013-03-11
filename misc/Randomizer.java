package se.xalan.misc;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: folke
 * Date: 2005-jul-11
 * Time: 09:54:01
 * To change this template use File | Settings | File Templates.
 */
public class Randomizer{

    private static List pairIdList = Collections.synchronizedList(new ArrayList());

    private static List maleDigList = Collections.synchronizedList(new LinkedList());
    private static List femaleDigList = Collections.synchronizedList(new LinkedList());

    private Iterator maleDigIter;
    private Iterator femaleDigIter;

    private int firstPossibleYear = 1910;
    private int lastPossibleYear = 1959;



    //Load the set with pairid's
    protected void setPairIdList(int rowcount){
        int numberOfTwinpairs = rowcount/2;
        for(int i=0; i<numberOfTwinpairs; i++){
            pairIdList.add(new Integer(90001+i));
        }
    }

    //Check if there are more pairs to load
    protected boolean areMorePairs(){
        return pairIdList.iterator().hasNext();
    }

    //get a random pair number from list and delete it from list
    protected String getPairId(){
        long randomLong = Math.round(Math.random()*(pairIdList.size()-1));
        int randomInt = new Long(randomLong).intValue();
        return ((Integer)pairIdList.remove(randomInt)).toString();
    }

    protected String getBirthdate(){
        String year = getRandomYearOfBirth();
        String month = getRandomMonthOfBirth();
        return year + month + getRandomDayOfBirth(year,month);
    }

    protected String getUniqePrnNum(String sex){
        String uniqePrnNum = "";

        long twoFirst = Math.round(Math.random()*(100));
        if(twoFirst==100){
            twoFirst=0;
        }
        if(twoFirst<10){
            uniqePrnNum = "0" + twoFirst;
        }
        else uniqePrnNum = new Long(twoFirst).toString();

        if(sex.equals("1")){
            if(!maleDigIter.hasNext()){
                maleDigIter = maleDigList.iterator();
            }
            uniqePrnNum = uniqePrnNum + maleDigIter.next();
        }
        else{
            if(!femaleDigIter.hasNext()){
                femaleDigIter = femaleDigList.iterator();
            }
            uniqePrnNum = uniqePrnNum + femaleDigIter.next();
        }

        long forthControllerDigNotLogic = Math.round(Math.random()*(10));
        if(forthControllerDigNotLogic==10){
            forthControllerDigNotLogic = 0;
        }

        uniqePrnNum = uniqePrnNum + forthControllerDigNotLogic;

        return uniqePrnNum;
    }

    /**
     *
     * @return Returns the next pseudorandom, Gaussian ("normally")
     *         distributed double value with mean 0.0 and standard
     *         deviation 1.0. <br/>The value stays within range
     *         -0.5 to 0.5.
     */
    private double nextOneDevGauss(){
        double gauss = 999999;
        while(gauss<-0.5||gauss>0.5){
            gauss = new Random().nextGaussian();
        }
        return gauss;
    }

    private String getRandomYearOfBirth(){
        int yearSpan = lastPossibleYear - firstPossibleYear;
        int meanYear = firstPossibleYear + (yearSpan)/2;

        // get the double value of a positive or negativ
        // number of years with a maximum and minimum value
        // within the yearSpan and a gausian mean at zero years.
        double yearsFromMeanDouble = yearSpan*nextOneDevGauss();

        // randomIntYearsFromMean is the double value that is closest in value
        // to the yearsFromMeanDouble and is equal to a mathematical integer.
        double randomIntYearsFromMean = Math.rint(yearsFromMeanDouble);

        // intYearsFromMean is the int value of randomIntYearsFromMean
        int intYearsFromMean = new Double(randomIntYearsFromMean).intValue();

        return new Integer(meanYear + intYearsFromMean).toString();
    }

    private String getRandomMonthOfBirth(){
        long randomLong = Math.round(Math.random()*12);

        if(randomLong==0){
            randomLong = 12;
        }

        String randomString = new Long(randomLong).toString();

        if(randomLong<10){
            randomString = "0"+randomString;
        }
        return randomString;
    }

    private String getRandomDayOfBirth(String inyear, String inmonth){

        int year = Integer.parseInt(inyear);
        int month = Integer.parseInt(inmonth);

        int numDays = 0;

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numDays = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numDays = 30;
                break;
            case 2:
                if ( ((year % 4 == 0) && !(year % 100 == 0))
                        || (year % 400 == 0) )
                    numDays = 29;
                else
                    numDays = 28;
                break;
            default:
                return "ooups";
        }

        long randomLong = Math.round(Math.random()*numDays);

        if(randomLong==0){
            randomLong = numDays;
        }

        String randomString = new Long(randomLong).toString();

        if(randomLong<10){
            randomString = "0"+randomString;
        }

        return randomString;

    }

    protected String getRandomSex(){
        return new Long(Math.round(Math.random()+1)).toString();
    }

    protected String getRandomCivilStatus(){

        long randomLong = Math.round(Math.random()*10);
        if(randomLong==0) randomLong = 10;

        // [0 or 1 = not maried, 2 = maried, 7 = ?, 9 = unknown]
        // 60% of all twins are maried
        return randomLong>4?"2":"1";
    }

    public Randomizer(int firstPossibleYear, int lastPossibleYear) {

        this.firstPossibleYear = firstPossibleYear;
        this.lastPossibleYear = lastPossibleYear;

        for(int i=0; i<10;i++){

            switch (i) {
                case 0: femaleDigList.add(new Integer(i));
                    break;
                case 1: maleDigList.add(new Integer(i));
                    break;
                case 2: femaleDigList.add(new Integer(i));
                    break;
                case 3: maleDigList.add(new Integer(i));
                    break;
                case 4: femaleDigList.add(new Integer(i));
                    break;
                case 5: maleDigList.add(new Integer(i));
                    break;
                case 6: femaleDigList.add(new Integer(i));
                    break;
                case 7: maleDigList.add(new Integer(i));
                    break;
                case 8: femaleDigList.add(new Integer(i));
                    break;
                case 9: maleDigList.add(new Integer(i));
                    break;
                default:
                    break;
            }

            this.maleDigIter = maleDigList.iterator();
            this.femaleDigIter = femaleDigList.iterator();
        }

    }

    public Randomizer() {
        this(1910,1959);
    }

}
