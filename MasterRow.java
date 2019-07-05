import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MasterRow{
  public JPanel guessPanel;
  public JLabel[] guess, result;
  public final static int NLABELS = 5;
  public JButton done;
  public JButton randomize;
  private static final Color BROWN = new Color(102,51,0);
  private static final Color ORANGE = new Color(255,102,0);
  private static final Color PINK = new Color(255,153,203);
  public static final Color LGRAY = new Color(179,179,179);
  private int nCorrects=0;
  private int nWrongs=0;
  private MasterMaind m;

  public MasterRow(MasterMaind m){//should create a new panel with a row
    this.m = m;
    guessPanel = new JPanel();
    guessPanel.setBackground(LGRAY);
    JLabel grayLabel = new JLabel();//to align it better
    grayLabel.setBackground(LGRAY);
    grayLabel.setPreferredSize(new Dimension(66,30));
    guessPanel.add(grayLabel);
    guess = new JLabel[NLABELS];
    Dimension butDim = new Dimension(100,30);
    for (int i=0; i<NLABELS; i++){
      guess[i] = new JLabel();
      changeColor(guess[i],Color.white);
      guess[i].setPreferredSize(butDim);
      guess[i].addMouseListener(m);
      guessPanel.add(guess[i]);
    }
    done = new JButton("Klar");
    randomize = new JButton("Slumpa");
    initBut(randomize,butDim);
    initBut(done,butDim);
    done.setVisible(false);
    result = new JLabel[NLABELS];
    for (int i=0; i<NLABELS; i++){
      result[i] = new JLabel();
      result[i].setPreferredSize(new Dimension(30,30));
      if (i>1){
        result[i].setVisible(false);
      }
      guessPanel.add(result[i]);
    }
  }

  public boolean checkFinished(){//should check if all lables have been giving a color
    for (int i=0;i<NLABELS;i++){
      if (guess[i].getBackground().equals(Color.white)){
        return false;
      }
    }
    return true;
  }

  public void checkResult(Color[] testAgainst){
    boolean[] checkedG = new boolean[NLABELS];
    boolean[] checkedH = new boolean[NLABELS];
    for (int i=0;i<NLABELS;i++){
      if (guess[i].getBackground().equals(testAgainst[i])){
        nCorrects++;
        checkedG[i]=true;
        checkedH[i]=true;
      }
    }
    for (int i=0;i<NLABELS;i++){
      if (!checkedG[i]){
        for (int j=0;j<NLABELS;j++){
          if (!checkedH[j]){
            if (guess[i].getBackground().equals(testAgainst[j])){
              nWrongs++;
              checkedH[j]=true;
              break;
            }
          }
        }
      }
    }
    for (int i=0; i<nCorrects; i++){
      changeColor(result[i],Color.black);
    }
    for (int i=nCorrects; i<nWrongs+nCorrects;i++){
      changeColor(result[i],Color.white);
    }
    for (int i=nCorrects+nWrongs; i<NLABELS;i++){
      changeColor(result[i],LGRAY);
    }
  }

  public void changeColor(JLabel l, Color c){//sets correct background
    l.setVisible(true);
    l.setOpaque(true);
    l.setBackground(c);
  }

  public void initBut(JButton b, Dimension butDim){//gives correct properties to a button
    b.setPreferredSize(butDim);
    b.addActionListener(m);
    guessPanel.add(b);
  }

  public void reset(){//resets to initial state with a new game
    for (int i=0; i<NLABELS; i++){
      guess[i].setBackground(Color.white);
      guessPanel.setVisible(false);
      if (i<2){
        result[i].setBackground(LGRAY);
      }
      else{
        result[i].setVisible(false);
      }
    }
    randomize.setVisible(true);
    done.setVisible(false);
    nCorrects=0;
    nWrongs=0;
  }

  public int getCorrects(){
    return nCorrects;
  }

  public int getWrongs(){
    return nWrongs;
  }
}
