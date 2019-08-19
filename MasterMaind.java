import java.awt.*;
import javax.swing.*;

public class MasterMaind{
  public static void main(String[] args) {
    JFrame frame = new MasterControl();
    frame.setPreferredSize(new Dimension(860,660));
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
