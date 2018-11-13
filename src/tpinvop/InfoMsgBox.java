package tpinvop;
import javax.swing.JOptionPane;

/**
 *
 * @author matias
 */

//******************************************************************************
//CLASS InfoMsgBox: Provides error and information output via message boxes
//******************************************************************************

public class InfoMsgBox {
    
    public static void infoBox(String infoMsg, String titleBar){
        
        JOptionPane.showMessageDialog(null, infoMsg, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void errBox(String errMsg, String titleBar){
        JOptionPane.showMessageDialog(null, errMsg, titleBar, JOptionPane.ERROR_MESSAGE);
    }
            
}
