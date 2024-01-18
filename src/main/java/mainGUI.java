import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainGUI extends JFrame {

    private JPanel mainpanel;
    private JButton JSONFixerButton;
    private JButton CODEhelperButton;

    public mainGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setContentPane(mainpanel);
        this.pack();
        this.setSize(400, 300);

        JSONFixerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainClass.StartJSONFixer();
            }
        });
        CODEhelperButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { MainClass.StartCODEhelper(); }
        });
    }
}
