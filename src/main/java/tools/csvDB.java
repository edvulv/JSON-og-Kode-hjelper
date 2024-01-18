package tools;

import models.CodeSeg;

import java.io.*;
import java.util.ArrayList;

public class csvDB {

    static String codefile = "src/main/java/data/codes.csv";
    static String csvsplit = ":-:";

    public static boolean FileWipe(File file) {
        boolean success = false;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("");
            success = true;
        } catch (IOException e) {
            success = false;
        }
        return success;
    }
    public static void WriteToFile(ArrayList<String> stringList) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(codefile))){
            for (String s : stringList) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public ArrayList<CodeSeg> getCodeList() {
        BufferedReader fileReader = null;
        ArrayList<CodeSeg> arrayList = new ArrayList<>();
        try {
            fileReader = new BufferedReader(new FileReader(codefile));
            String ob = "";
            while ((ob = fileReader.readLine()) != null) {
                String[] th = ob.split(csvsplit);
                try {
                    CodeSeg codeSeg = new CodeSeg(th[0], th[1], th[2]);
                    System.out.println( codeSeg.csvFormatted() );
                    arrayList.add(codeSeg);
                }
                catch (NumberFormatException nbr) {
                    CodeSeg codeSeg = new CodeSeg("", "", "");
                    arrayList.add(codeSeg);
                }
                catch (ArrayIndexOutOfBoundsException a)  {
                    System.out.println("List out of bounds");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
        }
        return arrayList;
    }
}
