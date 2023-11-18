import javax.swing.*;

class PopUpWindowMaker {
    private ErrorPopUpWindow error;
    private MessagePopUpWindow message;
    private WarningPopUpWindow warning;
    public PopUpWindowMaker() {
        error = new ErrorPopUpWindow();
        message = new MessagePopUpWindow();
        warning = new WarningPopUpWindow();
    }
    public void showError(String text){
        error.show(text);
    }
    public void showMessage(String text){
        message.show(text);
    }
    public void showWarning(String text){
        warning.show(text);
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

    }
}
class WarningPopUpWindow implements PopUpWindow {

    @Override
    public void show(String text) {

    }
}