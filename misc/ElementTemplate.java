package se.xalan.misc;
import java.io.*;

/**
 * Contains information of about single variables in each row. The variable
 * rules what validations has to be done by the user.
 */

public class ElementTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tagname;
    private String type;
    private String description;
    private int start;

    public ElementTemplate(String name, String type, String description, int start) {
	this.tagname = name;
	this.type = type;
	this.description = description;
	this.start = start;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart(int start) {
        this.start = start;
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

    public String toString() {
	StringBuffer sb = new StringBuffer("                                                                      ");
	String s = new String(sb.insert(0, tagname).insert(20, description).insert(53, start));
	return s.trim();
    }
}
