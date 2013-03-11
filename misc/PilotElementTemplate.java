package se.xalan.misc;
import java.io.*;

/**
 * Innehåller information om enskilda variabler i varje rad. Variabeltypen styr
 * vilka kontroller som krävs av användaren. name innehåller tagname start
 * beskriver startposition i raden end beskriver slutposition i raden type
 * definierar variabeltypen
 */

public class PilotElementTemplate implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5215059152641156339L;
    private String name;
    private String type;
    private String description;
    private int start;
    private int end;
    private int dateStart = -1;
    private int dateEnd = -1;

    public PilotElementTemplate(String name, String type, String description, int start, int end) {
	this.name = name;
	this.type = type;
	this.description = description;
	this.start = start;
	this.end = end;
	if (type.equals("date")) {
	    dateStart = 15;
	    dateEnd = 22;
	}
    }

    public String getName() {
	return name;
    }

    public String getType() {
	return type;
    }

    public int getStart() {
	return start;
    }

    public int getEnd() {
	return end;
    }

    public int getDateStart() {
	return dateStart;
    }

    public void setDateStart(int dateStart) {
	this.dateStart = dateStart;
    }

    public int getDateEnd() {
	return dateEnd;
    }

    public void setDateEnd(int dateEnd) {
	this.dateEnd = dateEnd;
    }

    public String toString() {
	StringBuffer sb = new StringBuffer("                                                                      ");
	/*
	 * sb.insert(0, name); sb.insert(25, type); sb.insert(37, start);
	 * sb.insert(43, end);
	 */
	String s = new String(sb.insert(0, name).insert(20, description).insert(53, start).insert(59, end));
	return s.trim();
    }
}
