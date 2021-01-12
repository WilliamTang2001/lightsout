//===================================================================================================================================================
// Lights Out
// William Tang
// 2019/01/21
// Java Photon Release (4.8.0)
//=============================================================================================================================================================================================
//Problem Definition--We will program the Light Bulb Solitaire Game. Given a 5x5 grid of lights (randomly turned on/off) each controlled by a button which toggles the 
//					  respective light and the surrounding non-diagonal lights. The objective is to turn off ALL lights in as few moves as possible.
//=================================================================================================================================================================================================
/*List of Identifiers -Let Width represent the width of the game window (type:int)
					  -Let Length represent the length of the game window (type:int)
					  -Let JB represent the the buttons to be clicked in the game window (type:JButton)
					  -Let countMove represent the number of moves of the player in a single game (type:int) 
					  -Let totalWins represent the total number of times a player has won the game (type:int)
					  -Let userName represent the name of the player entered (type:String)
					  -Let win represent the way to check whether the player has won or not (type:boolean)
					  -Let leastMove represent the best score of a single player (type:int)
					  -Let rememberMove represent the positions where the buttons are clicked by the player (type:int array)
					  -Let showSolution represent the positions to click if the player wants the computer to solve the puzzle (type:int array)
					  -Let replay represent the the original position of the puzzle (type:String array)
*/
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.applet.Applet;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
//====================================================================================================================================
@SuppressWarnings("serial")
public class LightsOutV6 extends JFrame implements ActionListener {
 public static final int Width = 600;
 public static final int Length = 600;
 public JButton JB[][]=new JButton[7][5];
 public int countMove=0,totalWins=0;
 public String userName;
 public boolean win=false;
 public int leastMove=Integer.MAX_VALUE;
 static int rememberMove[][]=new int [5][5];
 static int showSolution[][]=new int [5][5];
 static String replay[][]=new String [5][5];
 //=====================================================================================================================================
 public static void main(String[] args){
       LightsOutV6 LO = new LightsOutV6();
       LO.setVisible(true);
       LO.setResizable(false);
    }
//=======================================================================================================================================
 public void getUserName() { //get the name of the player
     String[] options1 = { "Enter" }; //let the name of the button be "Enter"
     JPanel panel = new JPanel(); //add new Jpanel  
     panel.add(new JLabel("Please enter your name: "));//make "Please enter your name: " display on the panel 
     JTextField textField = new JTextField(10);//set the size of the textField to be 10
     panel.add(textField);//add textField to the panel 
     int result = JOptionPane.showOptionDialog(null, panel, "User Name Input",
             JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null, options1, null);
     if (result == JOptionPane.YES_OPTION){
    	 userName=textField.getText();//assign the user name to the String variable userName
         JOptionPane.showMessageDialog(null, "Welcome! "+textField.getText());//display "Welcome!" and the user name
     }   
 }
//====================================================================================================================================
  public LightsOutV6(){//main method LightsOutV6
  		super("Lights Out");//name the window "Lights Out"
  		setSize(Length,Width); //set the size of the window to be 600x600
  		setLayout(new GridLayout(7, 5));//within the window, there is a 7x5 grid
  		String input[][]=new String[5][5];//introduce a new String array named input[][]
         for(int i=0;i<5;i++){
        	 for(int j=0; j<5;j++){
        		 input[i][j]="0"; //assign "0" to each String in the 5x5 grid, a total of 25 grids
        	 }
         }
         for(int i=0;i<5;i++){
        	 for(int j=0; j<5;j++){
        		 JB[i][j]=new JButton(); //turn the 5x5 game grid into buttons 
                 JB[i][j].addActionListener(this);//enable the user to click the button
                 JB[i][j].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.white));//make the border white
                 JB[i][j].setText(input[i][j]);//assign the value of the input array to the JB array
                 if(input[i][j].equals("1")){
                  JB[i][j].setBackground(Color.green);
                  JB[i][j].setForeground(Color.green);
                 }//if the value of the input array equals to "1", the background and foreground of the button turns green 
                 if(input[i][j].equals("0")){
                  JB[i][j].setBackground(Color.black);
                  JB[i][j].setForeground(Color.black);
                 }//if the value of the input array equals to "0", the background and foreground of the button turns black
                 add(JB[i][j]);//add the buttons to the window
          }
         }
         for(int i=0; i<5; i++) {
        	 JB[5][i] = new JButton();
        	 JB[5][i].addActionListener(this);//enable the user to click the buttons
        	 JB[5][i].setBackground(Color.black);//set background color to black
        	 JB[5][i].setForeground(Color.green);//set foreground color to green
        	 if(i==0) JB[5][i].setText("Rules");//let the button JB[5][0] display "Rules"
        	 else if(i==1) JB[5][i].setText("Replay Game");//let the button JB[5][1] display "Replay Game"
        	 else if(i==2) JB[5][i].setText("Moves: 0");//let the button JB[5][2] display "Moves: 0"
        	 else if(i==3) JB[5][i].setText("Solution");//let the button JB[5][3] display "Solution"
        	 else if(i==4) JB[5][i].setText("New Game");//let the button JB[5][4] display "New Game"
        	 JB[5][i].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.gray));//make the border gray
        	 add(JB[5][i]);//add JButton to the sixth row of the window
         }
         getUserName();//access the getUserName method
         for(int i=0; i<5; i++) {
        	 JB[6][i] = new JButton();
        	 JB[6][i].addActionListener(this);
        	 JB[6][i].setBackground(Color.black);
        	 JB[6][i].setForeground(Color.green);
        	 JB[6][i].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.gray));
        	 if(i==0) JB[6][i].setText("Best Score:");//let the button JB[6][0] display "Best Score: "
        	 else if(i==1) JB[6][i].setText("Name: "+ userName);//let the button JB[6][1] display "Name:" and the user's name
        	 else if(i==2) JB[6][i].setText("Total Wins: 0");//let the button JB[6][2] display "Total Wins: 0"
        	 else if(i==3) JB[6][i].setText("Rank");//let the button JB[6][3] display "Rank"
        	 else if(i==4) JB[6][i].setText("Exit");//let the button JB[6][4] display "Exit"
        	 add(JB[6][i]);
         }
     }
//===========================================================================================================================================
 	public void rememberPattern(){//method to remember the initial pattern of the light puzzle
  		for(int i=0;i<5;i++){	
  			for(int j=0; j<5;j++){
  				replay[i][j]=JB[i][j].getText();//assign the values in the JB array to the replay array	
  			}
       	}
  	}
//==========================================================================================================================================  		
  	public void showSolution() {//get the solution of the puzzle
  		for(int i=0;i<5;i++){
  	       	 for(int j=0; j<5;j++){
  	       		 showSolution[i][j]=rememberMove[i][j];
  	       	 }
  	      }//let showSolution[i][j] equals to the buttons that the data and the user clicked
  	}
//============================================================================================================================================  		
  	public void reverseShowSolution() {
  		for(int i=0;i<5;i++){
 	       	 for(int j=0; j<5;j++){
 	       		rememberMove[i][j]=showSolution[i][j];
 	       	 }
 	    }//assign the values in the showSolution array to the rememberMove array after the rememberMove array has been emptied
  	}
//===============================================================================================================================================
  	public void actionPerformed(ActionEvent e){//method used when the user is playing the game 
        JButton click=(JButton)e.getSource();//identify the buttons that are clicked
        for(int i = 0; i < 5; i++) {
           for(int j = 0; j < 5; j++) {
              if (click == JB[i][j]) {
                 reverse(i, j);//access the reverse method
                 countMove++;//add 1 to the move of the player if a button is clicked
                 rememberMove[i][j] ++;//remember the buttons that are clicked
                 JB[5][2].setText("Moves: "+countMove);//show the updated moves on the button whose location is row 6 column 3
                
					win = winCheck();//access the winCheck method to see if all the lights are turned off or not 
                 JB[i][j].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.white));//set the border in the 5x5 game grid white 
              }
           }
        }
        if(click == JB[5][4]) {
        	Random rand = new Random();	//randomize the puzzle if the user clicks "New Game"
        	for (int i = 0; i < 5; i ++) {
        		for (int j = 0; j < 5; j++) {
        			JB[i][j].setText("0");	//set the text of all elements in the JB array to "0" 
        			JB[i][j].setBackground(Color.black);	//set the background color to black 
        			JB[i][j].setForeground(Color.black);	//set the foreground color to black
        		}
        	}
        	for(int i=0;i<5;i++) {
        		for(int j=0;j<5;j++) {
        			rememberMove [i][j]=0;//empty all the elements in the rememberMove array
        			JB[i][j].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.white));//set border color to white
        		}
        	}
            for(int r=0;r<10;r++) {//randomize the puzzle by clicking the buttons randomly for 10 times
            	int i = rand.nextInt(5);
            	int j = rand.nextInt(5);
            	rememberMove[i][j]++;//remember which buttons are clicked
            	reverse(i, j);//go to the reverse method 
            }
            rememberPattern();//access the rememberPattern method
            showSolution();//access the showSolution method
            countMove = 0;	//reset the number of moves to 0
            JB[5][2].setText("Moves: "+ countMove);//display "Moves: 0"
        }
        else if(click == JB[5][3]) {
        	for (int i = 0; i < 5; i++) {
        		for (int j = 0; j < 5; j++) {
        			if (rememberMove[i][j]%2!=0) {//if a particular button has been clicked for an odd number of times
        				JB[i][j].setBorder(BorderFactory.createMatteBorder(8,8,8,8,Color.pink));//make the border pink for the buttons that you need to click in order to solve the puzzle
        			}
        		}
        	}
        }
        else if (click == JB[5][1] && countMove>=5) {//the button will only response if the user has made 5 moves or more
        	for (int i = 0; i < 5; i ++) {
        		for (int j = 0; j < 5; j++) {
        			JB[i][j].setText("0");
        			JB[i][j].setBackground(Color.black);//set the background color to black
        			JB[i][j].setForeground(Color.black);//set the foreground color to white
        		}
        	}
        	for (int i = 0; i < 5; i++) {
        		for (int j = 0; j < 5; j++) {
        			rememberMove[i][j]=0;//set all the elements in the rememberMove array to "0"
        			JB[i][j].setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.white));
        		}
        	}
        	reverseShowSolution();//access the reverseShowSolution method
        	for(int i=0;i<5;i++){
     	       	 for(int j=0; j<5;j++){
     	       		 JB[i][j].setText(replay[i][j]);//input the value in the replay array into the JB array 
     	       		 if (JB[i][j].getText().equals("0")) {//change the button color to black if the value equals to "0"
     	       			 JB[i][j].setBackground(Color.black);
     	       			 JB[i][j].setForeground(Color.black);
     	       		 }
     	       		 else if (JB[i][j].getText().equals("1")) {//change the button color to green if the value equals to "1"
     	       			 JB[i][j].setBackground(Color.green);
     	       			 JB[i][j].setForeground(Color.green);
     	       		 }
     	       	 }
     		}
        	countMove = 0;//set the number of moves to 0
        	JB[5][2].setText("Moves: "+ countMove);
        }
  	else if(click==JB[5][0]) {
  		JOptionPane.showMessageDialog(this, "<html>Welcome to Lights Out! This game consists of a 5x5 grid of lights. When the game starts, a random number of these lights is switched on.<br>Pressing any of the lights will toggle it and the adjacent lights. "
  				+ "The goal of the puzzle is to switch all the lights off, preferably in as few button presses as possible.<br>After FIVE moves you have the option to see solution or continue playing.");
  	}//display instructions when the user clicks "Rules"
  	else if(click==JB[6][4]) {
  		JOptionPane.showMessageDialog(this, "<html>Thanks for playing! "+userName+" has sloved "+totalWins+" puzzle(s)<br> The best score is "+leastMove+" moves");//display message
  		System.exit(0);//exit the game if the user clicks the "Exit" button
  	}
  }
//============================================================================================================================================================================================================================================================================== 
  	public void reverse(int i, int j) {
      change(i, j);
      change(i+1, j);
      change(i-1, j);
      change(i, j+1);
      change(i, j-1);//access the change method
  }//when a button is clicked, reverse the buttons that are located on the top, left, right, and bottom of the clicked button 
//================================================================================================================================================================================================================================================================================
  	public void change(int i, int j) {
      if (0<=i && i<5 && 0<=j && j<5) {//check to make sure that the button to be clicked is in the 5x5 grid
    	  if ("0".equals(JB[i][j].getText())){ //if the button is black/the value of the button is "0"
    		  JB[i][j].setBackground(Color.green);
    		  JB[i][j].setForeground(Color.green);
    		  JB[i][j].setText("1");//change the color of the button to green      
    	  }
          else if("1".equals(JB[i][j].getText())){//if the button is green/the value of the button is "1"
        	  JB[i][j].setBackground(Color.black);
        	  JB[i][j].setForeground(Color.black);
        	  JB[i][j].setText("0");//change the color of the button to black
          }
      }
  	}
//=================================================================================================================================================================================================================================================================================
	public boolean winCheck() {//method used to check if the user has won the game or not
  		boolean winCheck=false; //let winCheck (type:boolean) be false
  		int off=0;//assign the value "0" to off (type:int) 
  		for(int i = 0; i < 5; i++) {
  			for (int j = 0; j < 5; j++) {
  				if (JB[i][j].getText().equals("0")) {//if the text of l[i][j] is '0' OR if the light is off
  					off++;	//off plus 1
  				}
  			}
  		}
  		if (off==25) {	//if the value of "off" is 25, which means all the lights are turned off
  			winCheck = true;	// winCheck is true, which means the user has won the game
  			totalWins++; //add 1 to totalWins
  			JOptionPane.showMessageDialog(this, "Congratulations! It took you " + countMove + " moves to win.");//show a message to tell the user that (s)he has won and the total steps (s)he has made
        	leastMove = Math.min(leastMove, countMove);//compare countMove with leastMove and assign the smaller value to leastMove
        	JB[6][0].setText("Best Score: "+leastMove);//update the user's least moves to the button
        	JB[6][2].setText("Total Wins: "+totalWins);//update the user's total wins to the button
        	countMove = 0;//reset user's moves to 0	
  		}
		return winCheck;//return the value (true or false) of winCheck
  	}
	public void recordScore() throws IOException{
		 PrintWriter output = new PrintWriter (new FileWriter ("D:/Dice/Score.txt"));
		   	output.println (userName+"  "+countMove);
		   	output.println();
		   	output.close();
	  	}
	}

