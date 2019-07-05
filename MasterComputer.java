import java.util.*;
import java.lang.*;
import java.awt.*;
import javax.swing.*;

public class MasterComputer{
  private MasterMaind mm;
  private MasterRow[] rows;
  private int rowN=0;

  public MasterComputer(MasterMaind m, MasterRow[] r){
    mm = m;
    rows = r;
  }

  public void checkComputer(){//creates the first randomized row for the AI
    rows[rowN].guessPanel.setVisible(true);
    rows[rowN].done.setVisible(false);
    rows[rowN].randomize.setVisible(false);
    if (rowN==0){
      for (int i=0;i<rows[rowN].NLABELS;i++){
        rows[0].guess[i].setBackground(mm.col[(int)(mm.NCOLORS*Math.random())]);
      }
      rows[0].checkResult(mm.getHidden());
//      rowN++;
      boolean ok=true;//to see if the attempted row is good enough
      while (rows[rowN].getCorrects()<rows[rowN].NLABELS && rowN<mm.MAXROWS-1){
        if (ok){
          rowN++;
        }
        ok = checkComputer2();
      }
    }
  }

  public boolean checkComputer2(){//computes the rest of the rows for the AI
      boolean[] picked = new boolean[rows[rowN].NLABELS];// to see what positions have been chosen
      Color[] attempt = new Color[rows[rowN].NLABELS];//a test row
      int rand;//a randomized number from what position we should pick a color from
      for (int i=0;i<rows[rowN-1].getCorrects();i++){//randomizes enough colors on the same position as the last row
        rand = (int)(rows[rowN].NLABELS*Math.random());
        while(picked[rand]){
          rand = (int)(rows[rowN].NLABELS*Math.random());
        }
        attempt[rand] = rows[rowN-1].guess[rand].getBackground();
        picked[rand]=true;
      }
      for (int i=0;i<rows[rowN-1].getWrongs();i++){//randomizes colors from the last row and gives them another position
        rand = (int)(rows[rowN].NLABELS*Math.random());
        final int MAXN=25; //how many leaps a loop will go before it gives up
        int n=0; //to see if we reached MAXN
        while(picked[rand] && n<MAXN){
          rand = (int)(rows[rowN].NLABELS*Math.random());
          n++;
        }
        if (n==MAXN){
          return false;
        }
        n=0;
        int rand2 = (int)(rows[rowN].NLABELS*Math.random());//for what position the picked color should be put
        while(((attempt[rand2]!=null) || rand==rand2) && n<MAXN){
          rand2 = (int)(rows[rowN].NLABELS*Math.random());
          n++;
        }
        if (n==MAXN){
          return false;
        }
        attempt[rand2] = rows[rowN-1].guess[rand].getBackground();
        picked[rand]=true;
      }
      for (int i=0;i<rows[rowN].NLABELS;i++){//randomizes the colors for the remaining positions
          while(attempt[i]==null){
            boolean chosen=false; //we need this for the next loop
            Color randCol = mm.col[(int)(mm.NCOLORS*Math.random())];
            for (int j=0;j<rows[rowN].NLABELS;j++){//checks if the randomized color are among the colors we decided were wrong
              if (rows[rowN-1].guess[j].getBackground().equals(randCol) && !picked[j]){
                chosen = true;
                break;
              }
            }
            if (!chosen){
              attempt[i]=randCol;
            }
          }
      }
      picked=null;
      for (int i=0; i<rowN; i++){//should test whether it works in relation to the earlier rows
          MasterRow tempRow = new MasterRow(mm); //only a temporary row we need for the checking
          for (int j=0; j<rows[rowN].NLABELS; j++){
            tempRow.guess[j].setBackground(rows[i].guess[j].getBackground());
          }
          tempRow.checkResult(attempt);
          if (!(tempRow.getCorrects()==rows[i].getCorrects() && tempRow.getWrongs()==rows[i].getWrongs())){
              return false;
          }
      }
      rows[rowN].guessPanel.setVisible(true);//if we reached this, then the attempted row worked
      rows[rowN].randomize.setVisible(false);
      rows[rowN].done.setVisible(false);
      for (int i=0; i<rows[rowN].NLABELS; i++){
          rows[rowN].guess[i].setBackground(attempt[i]);
      }
      rows[rowN].checkResult(mm.getHidden());
      return true;
    }
}
