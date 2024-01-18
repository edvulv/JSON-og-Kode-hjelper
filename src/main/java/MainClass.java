import tools.CodeHelper;
import tools.JSONFixer;

public class MainClass {

    static JSONFixer jsonFixer = new JSONFixer("Json Fixer");
    static CodeHelper codeHelper = new CodeHelper("Code Helper");
    static mainGUI mainG = new mainGUI("FCS Hub");

    public static void main(String[] args) {
        mainG.setVisible(true);
        String test = "bady \n *VALUE* : nothing";
        String test2 = "*VALUE*";
        String test3 = "var_one.";

        String sentc = "this is a " +
                "long long sentance";
        System.out.println(sentc);
        String text = sentc.replaceAll("\\s+", " ");
        text = text.replaceAll("\\s+", " ");
        System.out.println(text);

        /* ERROR MELDINGER FRA CODEHELPER
        Exception "java.lang.ClassNotFoundException: com/intellij/codeInsight/editorActions/FoldingData"while constructing DataFlavor for: application/x-java-jvm-local-objectref; class=com.intellij.codeInsight.editorActions.FoldingData
Exception "java.lang.ClassNotFoundException: com/intellij/codeInsight/editorActions/FoldingData"while constructing DataFlavor for: application/x-java-jvm-local-objectref; class=com.intellij.codeInsight.editorActions.FoldingData
Exception "java.lang.ClassNotFoundException: com/intellij/openapi/editor/impl/EditorCopyPasteHelperImpl$CopyPasteOptionsTransferableData"while constructing DataFlavor for: application/x-java-serialized-object; class=com.intellij.openapi.editor.impl.EditorCopyPasteHelperImpl$CopyPasteOptionsTransferableData
Exception "java.lang.ClassNotFoundException: com/intellij/openapi/editor/impl/EditorCopyPasteHelperImpl$CopyPasteOptionsTransferableData"while constructing DataFlavor for: application/x-java-serialized-object; class=com.intellij.openapi.editor.impl.EditorCopyPasteHelperImpl$CopyPasteOptionsTransferableData
         */

        System.out.println(test.contains("*VALUE*"));
        System.out.println(test2.equals("*VALUE*"));
        System.out.println(test.replace("*VALUE*", "value?"));



    }

    public static void StartJSONFixer() {
        mainG.setVisible(false);
        jsonFixer.setVisible(true);
    }
    public static void StartCODEhelper() {
        mainG.setVisible(false);
        codeHelper.setVisible(true);
    }
}
