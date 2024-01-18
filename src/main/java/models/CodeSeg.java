package models;

public class CodeSeg {
    public String codeLanguage;
    public String codeSequence;
    public String description;
    String csvsplit = ":-:";

    public CodeSeg(String codeLanguage, String codeSequence, String description) {
        this.codeLanguage = codeLanguage;
        this.codeSequence = codeSequence;
        this.description = description;
    }


    @Override
    public String toString() {
        String retS = "";
        try { retS = "|-" + codeLanguage + ": " + description.substring(0,20) + "..."; }
        catch (StringIndexOutOfBoundsException str) { retS = "|-" + codeLanguage + ": " + description.substring(0,description.length()-1) + "..."; }
        return retS;
    }

    public String csvFormatted() {
        return codeLanguage + csvsplit + codeSequence + csvsplit + description;
    }
}
