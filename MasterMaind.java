import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MasterMaind extends JFrame implements MouseListener, ActionListener{
  private JPanel bigPanel;
  public static final int NCOLORS = 8;
  private JLabel[] colors;
  private static final Color BROWN = new Color(102,51,0);
  private static final Color ORANGE = new Color(255,102,0);
  private static final Color PINK = new Color(255,153,203);
  public static final Color LGRAY = new Color(179,179,179);
  private MasterRow[] rows;
  private Color[] hidden;
  private Color chosenColor;
  private int rowN=0;
  public static final int MAXROWS=12;
  public static final Color[] col = new Color[] {Color.blue,Color.green,Color.red,Color.yellow,BROWN,Color.cyan,ORANGE,PINK};
  private static JFrame frame;
  private JButton newGame;
  private JRadioButton hideOption, guessOption;
  private boolean hiding=false;

  public MasterMaind(){
    JPanel bigPanel = new JPanel();
    bigPanel.setBackground(LGRAY);
    JPanel colorPanel = new JPanel();//upper panel
    colorPanel.setLayout(new FlowLayout());
    colorPanel.setBackground(LGRAY);
    colorPanel.setPreferredSize(new Dimension(860,40));
    colors = new JLabel[NCOLORS];
    for (int i=0; i<NCOLORS; i++){
      colors[i] = new JLabel();
      colors[i].setOpaque(true);
      colors[i].setBackground(col[i]);
      colors[i].setPreferredSize(new Dimension(100,30));
      colors[i].addMouseListener(this);
      colorPanel.add(colors[i]);
    }
    rows = new MasterRow[MAXROWS];
    hidden = new Color[rows[rowN].NLABELS];
    for (int i=0; i<rows[rowN].NLABELS; i++){//creates the hidden row
      hidden[i]=col[(int)(NCOLORS*Math.random())];
    }
    bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
    bigPanel.add(colorPanel);
    rows[0] = new MasterRow(this);
    JPanel centralPanel = new JPanel();
    centralPanel.setBackground(LGRAY);
    centralPanel.setPreferredSize(new Dimension(860,580));
    centralPanel.add(rows[0].guessPanel);
    for (int i=1; i<MAXROWS ;i++){
      rows[i] = new MasterRow(this);
      centralPanel.add(rows[i].guessPanel);
      rows[i].guessPanel.setVisible(false);
    }
    bigPanel.add(centralPanel);
    JPanel lowerPanel = new JPanel(new FlowLayout());
    lowerPanel.setBackground(LGRAY);
    lowerPanel.setPreferredSize(new Dimension(860,40));
    ButtonGroup option = new ButtonGroup();
    guessOption = new JRadioButton("Gissa",true);
    guessOption.setBackground(LGRAY);
    initRadBut(guessOption,option,lowerPanel);
    hideOption = new JRadioButton("Göm",false);
    hideOption.setBackground(LGRAY);
    initRadBut(hideOption,option,lowerPanel);
    newGame = new JButton("Nytt spel");
    newGame.addActionListener(this);
    lowerPanel.add(newGame);
    bigPanel.add(lowerPanel);
    add(bigPanel);
  }

  public void initRadBut(JRadioButton rb, ButtonGroup bg, JPanel p){//gives the radiobuttons their properties
    rb.addActionListener(this);
    bg.add(rb);
    p.add(rb);
  }

  public Color[] getHidden(){
    return hidden;
  }

  public static void main(String[] args) {
    frame = new MasterMaind();
    frame.setPreferredSize(new Dimension(860,660));
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void actionPerformed(ActionEvent e) {
      if (e.getSource().equals(rows[rowN].done)){//if a row is finshed
        if (rows[rowN].checkFinished()){
          rows[rowN].done.setVisible(false);
          if (!hiding){//if we're guessing, the result should be printed, an new row should show
            rows[rowN].checkResult(hidden);
            if (rowN<MAXROWS-1 && rows[rowN].getCorrects()!=rows[rowN].NLABELS){
              rowN++;
              rows[rowN].guessPanel.setVisible(true);
            }
          }
          else{//if we're hiding, the computer should find the correct row
            for (int i=0; i<rows[rowN].NLABELS; i++){
              hidden[i]=rows[rowN].guess[i].getBackground();
            }
            MasterComputer comp = new MasterComputer(this,rows);
            comp.checkComputer();
          }
        }
      }
      else if (e.getSource().equals(rows[rowN].randomize)){//should randimize the remaining labels
        for (int i=0; i<rows[rowN].NLABELS; i++){
          if (rows[rowN].guess[i].getBackground().equals(Color.white)){
            rows[rowN].guess[i].setBackground(col[(int)(NCOLORS*Math.random())]);
          }
        }
        rows[rowN].randomize.setVisible(false);
        rows[rowN].done.setVisible(true);
      }
      else if (e.getSource().equals(newGame)){//should start a new game and check if it is for hiding or guessing
        for (int i=0; i<MAXROWS; i++){
          rows[i].reset();
        }
        rows[0].guessPanel.setVisible(true);
        rowN=0;
        if (guessOption.isSelected()){
          for (int i=0; i<rows[rowN].NLABELS; i++){
            hidden[i]=col[(int)(NCOLORS*Math.random())];
          }
          hiding=false;
        }
        else if (hideOption.isSelected()){
          hiding=true;
        }
      }
  }

  public void mouseClicked(MouseEvent e){}

  public void mouseExited(MouseEvent e) {}

  public void mouseEntered(MouseEvent e) {}

  public void mousePressed(MouseEvent e) {//to see if we have chosen a color
      for (int i=0; i<NCOLORS; i++){
        if (e.getSource().equals(colors[i])){
          for (int j=0;j<NCOLORS;j++){
            colors[j].setBorder(javax.swing.BorderFactory.createEmptyBorder());
          }
          chosenColor=colors[i].getBackground();
          colors[i].setBorder(BorderFactory.createLineBorder(new Color(0,101,101), 2));
        }
      }
  }

  public void mouseReleased(MouseEvent e) {//checks if we have chosen one of the available labels and then sees if all labels have a color
      for (int i=0;i<rows[rowN].NLABELS;i++){
        if (e.getSource().equals(rows[rowN].guess[i]) && chosenColor!=null){
          rows[rowN].guess[i].setBackground(chosenColor);
        }
      }
      boolean anyWhite=false;
      for (int i=0;i<rows[rowN].NLABELS;i++){
        if (rows[rowN].guess[i].getBackground().equals(Color.white)){
          anyWhite=true;
          break;
        }
      }
      if (!anyWhite){
        rows[rowN].randomize.setVisible(false);
        rows[rowN].done.setVisible(true);
      }
  }
}
