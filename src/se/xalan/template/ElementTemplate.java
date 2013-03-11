package se.xalan.template;

import java.io.*;

/**
 * Contains information of about single variables in each row. The variable
 * rules what validations has to be done by the user.
 */
public class ElementTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    private String tagname;
    private String type;
    private int start;
    private int end;
    private int dateStart = -1;
    private int dateEnd = -1;

    public ElementTemplate(String name, String type, int start, int end) {
        this.tagname = name;
        this.type = type;
        this.start = start;
        this.end = end;
        if (type.equals("Code")) {
            dateStart = 6;
            setDateEnd(13);
        }
    }

    public String getName() {
        return tagname;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("                                                        ");
        String s = new String(sb.insert(0, tagname).insert(25, type).insert(42, start).insert(48, end));
        return s.trim();
    }

    public int getDateEnd() {
        return dateEnd;
    }

    private void setDateEnd(int dateEnd) {
        this.dateEnd = dateEnd;
    }
}
