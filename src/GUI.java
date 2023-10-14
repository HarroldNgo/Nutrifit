import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame jframe;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    public GUI() {
        jframe = new JFrame("Nutrifit");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(400, 300);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel mainmenu = createMainMenu();
        cardPanel.add(mainmenu, "MainMenu");

        jframe.add(cardPanel);

    }

    private JPanel createMainMenu() {
        JPanel mainmenu = new JPanel();
        JButton showDietLogsButton = new JButton("Show Diet Logs");
        showDietLogsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DietLogs");
            }
        });
        mainmenu.add(showDietLogsButton);
        return mainmenu;
    }
    public void showApp(){
        jframe.setVisible(true);
    }
    public static void main(String[] arg) {
        GUI nutrifit = new GUI();
        nutrifit.showApp();
    }
}
