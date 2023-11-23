import javax.swing.*;

class PopUpWindowMaker {
    private String type;
    public PopUpWindowMaker(String type) {
        this.type = type;
    }
    public PopUpWindow createPopUp(){
        switch (this.type.toLowerCase()) {
            case "error":
                return new ErrorPopUpWindow();
            case "message":
                return new MessagePopUpWindow();
            case "warning":
                return new WarningPopUpWindow();
            default:
                throw new IllegalArgumentException("Unknown PopUpWindow type");
        }
    }
}
interface PopUpWindow {
    void show(String text);
}
class ErrorPopUpWindow implements PopUpWindow{
    @Override
    public void show(String text) {
        JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
class MessagePopUpWindow implements PopUpWindow {

    @Override
    public void show(String text) {
        JOptionPane.showMessageDialog(null, text);
    }
}
class WarningPopUpWindow implements PopUpWindow {

    @Override
    public void show(String text) {
        JOptionPane.showMessageDialog(null, text, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}