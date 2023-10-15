import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JFrame jframe;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private Main main;
    public GUI(Main main) {
        this.main = main;
        jframe = new JFrame("Nutrifit");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(400, 300);
        jframe.setVisible(true);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        jframe.add(cardPanel);
        cardLayout.show(cardPanel, "UserSelection");

    }
    public void createUserSelectionPanel(List<UserProfile> userProfiles){
        JPanel userSelection = new JPanel();
        for(UserProfile up : userProfiles) {
            JButton button = new JButton(up.getName());
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    main.selectedProfile = up;
                    cardLayout.show(cardPanel, "MainMenu");
                }
            });
            userSelection.add(button);
        }
        cardPanel.add(userSelection, "UserSelection");
    }

    public void createMainMenuPanel() {
        JPanel mainmenu = new JPanel();
        JButton showDietLogsButton = new JButton("Show Diet Logs");
        showDietLogsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, "DietLogs");
            }
        });
        mainmenu.add(showDietLogsButton);
        cardPanel.add(mainmenu, "MainMenu");
    }

    public static void main(String[] arg)
    {

    }
}
