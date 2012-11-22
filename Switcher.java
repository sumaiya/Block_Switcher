package switchGame;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class Switcher extends JApplet
  implements ActionListener, KeyListener
{


  private static final long serialVersionUID = 1L;
  public static final int EASY = 5;
  public static final int MEDIUM = 7;
  public static final int HARD = 10;
  public static final Font db = new Font("Dialog", Font.BOLD, 18);   
  public static final Font tr = new Font("TimesRoman", Font.PLAIN, 12);
  public static final Font d = new Font("Dialog", Font.PLAIN, 14); 
  public static final Font db25 = new Font("Dialog", Font.BOLD, 35);
  public static final Color BGCOLOR = Color.black;
  public static final Color TEXTC = Color.white;
	  
  Image image;                // off-screen buffer
  Graphics g;                 // the buffer's graphical tools
  LL<Integer> goal = new LL<Integer>();
  LL<Integer> initial = new LL<Integer>();
  LL<Integer> current = new LL<Integer>();
  String messageTop = "";             
  String messageBottom = "";
  int difficulty = 5;
  int numMoves;

  private JButton startButton;
  private JMenu diffMenu;
  private JMenuItem easyItem;
  private JMenuItem mediumItem;
  private JMenuItem hardItem;
  

  /******************************************************************/  

  //Initialize the applet.  This is called once when the applet starts.
  public void init()
  {

    //  other game-based set up is in reset(), so that it 
    //   will be redone each time the game restarts

    this.addKeyListener(this);                // listen for key events
    this.setLayout(new BorderLayout());       //set up layout on the form

    // BEGINNING OF BUTTON CODE

    // Add a panel for buttons
    JPanel buttonPane = new JPanel(new FlowLayout());
    buttonPane.setBackground(BGCOLOR);
    add(buttonPane, BorderLayout.PAGE_END);

    startButton = new JButton("Start");    
    startButton.addActionListener(this);
    startButton.addKeyListener(this);
    buttonPane.add(startButton);
    // END OF BUTTON CODE

    // BEGINNING OF MENU BAR CODE

    // Set up the menu bar
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);
    add(menuBar, BorderLayout.PAGE_START);

    
    diffMenu = new JMenu("Select Difficulty HERE. Then hit Start");
    menuBar.add(diffMenu);
    
    easyItem = new JMenuItem("Easy");
    easyItem.addActionListener(this);     // Watch for button presses
    easyItem.addKeyListener(this);        // Listen for key presses here
    diffMenu.add(easyItem);   
    
    mediumItem = new JMenuItem("Medium");
    mediumItem.addActionListener(this);     // Watch for button presses
    mediumItem.addKeyListener(this);        // Listen for key presses here
    diffMenu.add(mediumItem);   
    
    hardItem = new JMenuItem("Hard");
    hardItem.addActionListener(this);     // Watch for button presses
    hardItem.addKeyListener(this);        // Listen for key presses here
    diffMenu.add(hardItem);   
  
    // END OF MENU BAR CODE

     // Sets up the back (off-screen) buffer for drawing, named image
    image = createImage(getSize().width, 490);
    g = image.getGraphics();                 // g holds drawing routines
    this.clear();                            // clears the screen

    //add a central panel which holds the buffer (the game board)
    add(new ImagePanel(image), BorderLayout.CENTER);
    this.messageTop = "Switch adjacent blocks until you reach the goal." +
	" Press a number to switch that block with the block to its right.";
    displayMessage();
   
    this.drawEnvironment(); // update our offscreen image buffer
    repaint();              // tell Java to update the screen
  }

// Reset the internal representation of the game for every new game
  
  void reset()
  {
	   this.numMoves = 0; //reset numMoves so far
	   this.messageBottom = "";
	    this.messageTop = "Switch adjacent blocks until you reach the goal." +
		" Press a number to switch that block with the block to its right.";    

	    // set up the goal and starting LL here
	    this.goal = Game.generateGoal(difficulty);
	    this.initial = Game.generateInitial(difficulty);
	    this.current = this.initial.copyLL();
	       
        this.drawEnvironment();  // draw things to buffer
        this.displayMessage();   // display messages
        repaint();               // send buffer to the screen
 }

  // check for win
  boolean win()
  {
	  return this.current.equals(this.goal);
  }  
  
  // get a color from rgb values
 private Color fromRGB(int r, int g, int b)
 {
		float[] hsb = Color.RGBtoHSB(r, g, b, null);
		Color color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		return color;
	    
 }
 
 // number the blocks
 void numberBlocks(int i, int top)
 {
	    int LEFT = (getSize().width - (50*current.size())) / 2;

		g.setFont(db);
 		g.setColor(Color.black);
    	if (i != 9)
    	{
    		g.drawString(((Integer)(i+1)).toString(), LEFT + 20 + 50*i, top + 30);
    	}
    	else
    	{
    		g.drawString(((Integer)(i+1)).toString(), LEFT + 14 + 50*i, top + 30);

    	}
    	g.setFont(tr);
    
 }
 
 // check and handle all the updates
 public void allUpdates()
 {
	  this.numMoves += 1;
	  this.messageTop = "Swapping!";
     this.drawEnvironment();
     this.repaint();
     if (this.win())
     {
   	  messageBottom = "You win!";
     }
     this.displayMessage();
 }
  
// draw the environment
  void drawEnvironment()
  {
    this.clear();                 
    int WIDTH = 50;
    int HEIGHT = 50;
    int TOP1 = 50;
    int TOP2 = 250;
    int LEFT = (getSize().width - (50*current.size())) / 2;
    Color ZERO = this.fromRGB(109, 158, 235);
    Color ONE = this.fromRGB(142, 205, 240);
    Color TWO = this.fromRGB(0, 99, 164);
    Color THREE = this.fromRGB(51, 240, 255);
    Color FOUR = this.fromRGB(217, 238, 249);
    Color FIVE = this.fromRGB(80, 220, 150);
    Color SIX = this.fromRGB(32, 178, 170);
    Color SEVEN = this.fromRGB(204, 204, 255); 
    Color EIGHT = this.fromRGB(200, 100, 150);
    Color NINE = this.fromRGB(200, 150, 200);
    Color TEN = this.fromRGB(200, 185, 255);
    
    for(int i = 0; i < current.size(); i++)
     {
    	switch (current.get(i))
    	{
    	case 0: 
        	g.setColor(ZERO);
     		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
    		break;
    	case 1: 
    		g.setColor(ONE);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
    		break;
    	case 2: 
    		g.setColor(TWO);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
    	case 3: 
    		g.setColor(THREE);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case 4: 
    		g.setColor(FOUR);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case  5: 
    		g.setColor(FIVE);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case  6: 
    		g.setColor(SIX);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case  7: 
    		g.setColor(SEVEN);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case 8: 
    		g.setColor(EIGHT);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case  9: 
    		g.setColor(NINE);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	case 10: 
    		g.setColor(TEN);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
     		numberBlocks(i, TOP2);
     		break;
       	default: 
    		g.setColor(Color.GRAY);
       		g.fillRect(LEFT + 50*i,TOP2,WIDTH,HEIGHT);
    		break;
     	}
    }
    
    for(int i = 0; i < goal.size(); i++)
    {
    	switch (goal.get(i))
    	{
    	case 0: 
    		g.setColor(ZERO);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 1: 
    		g.setColor(ONE);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 2: 
    		g.setColor(TWO);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 3: 
    		g.setColor(THREE);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 4: 
    		g.setColor(FOUR);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case  5: 
    		g.setColor(FIVE);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case  6: 
    		g.setColor(SIX);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case  7: 
    		g.setColor(SEVEN);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 8: 
    		g.setColor(EIGHT);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case  9: 
    		g.setColor(NINE);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	case 10: 
    		g.setColor(TEN);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
    	default: 
    		g.setColor(Color.GRAY);
       		g.fillRect(LEFT + 50*i,TOP1,WIDTH,HEIGHT);
    		break;
     	}
    }
  }
  
  
  // draw a status String on the screen
  void displayMessage()
  {
    int CENTER = getSize().width/2;
    g.setColor(TEXTC);
    g.drawString(this.messageTop, 15, 25);
    g.setFont(db25);
    g.drawString(this.messageBottom, CENTER - 70, 400);
    g.setFont(tr);
    String messageScore = this.numMoves + " moves so far";  
    g.drawString(messageScore, CENTER - 40, 185);
    g.setFont(db);
    g.drawString("TARGET", CENTER - 35, 125);
    g.drawString("YOUR BLOCKS", CENTER - 65, 325);
    g.setFont(tr);
 
  }

  /******************************************************************/  
// handle buttons and menu items
  public void actionPerformed(ActionEvent evt)
  {
    Object source = evt.getSource();

    if (source == easyItem)  
    {
    	difficulty = EASY;
    }
    if (source == mediumItem)  
    {
    	difficulty = MEDIUM;
    }
    if (source == hardItem)  
    {
    	difficulty = HARD;
    }
 
    if (source == startButton)  
    {
    	this.reset();
    }

    this.requestFocus(); // make sure the Applet keeps keyboard focus
  }
  
  
  // Here's how keyboard events are handled...
  public void keyPressed(KeyEvent evt)
  {
		int ch = evt.getKeyCode(); // method saying what key was pressed

	  switch(ch)  // Do different things depending on the character
	  {
      case KeyEvent.VK_1: 
          if(this.current.swap(0)) {
              this.allUpdates();
           };
          break;
      case KeyEvent.VK_2: 
          if(this.current.swap(1)) {
              this.allUpdates();
          };
          break;
      case KeyEvent.VK_3: 
          if(this.current.swap(2)) {
              this.allUpdates();
          };
        break;
      case KeyEvent.VK_4: 
          if(this.current.swap(3)) {
              this.allUpdates();
          };
        break;
      case KeyEvent.VK_5: 
          if(this.current.swap(4)) {
              this.allUpdates();
          };
           break;
      case KeyEvent.VK_6: 
          if(this.current.swap(5)) {
              this.allUpdates();
          };
             break;
      case KeyEvent.VK_7: 
          if(this.current.swap(6)) {
              this.allUpdates();
          };
           break;
      case KeyEvent.VK_8: 
          if(this.current.swap(7)) {
              this.allUpdates();
          };
         break;
      case KeyEvent.VK_9: 
          if(this.current.swap(8)) {
              this.allUpdates();
          };
             break;
      case KeyEvent.VK_0: 
          if(this.current.swap(9)) {
              this.allUpdates();
          };
          break;
          
      default:
        break;
    }

  }
  
  public void keyReleased(KeyEvent evt) {
      return;
  }
  public void keyTyped(KeyEvent evt) {
      return;
  }

  
   // clear the applet's drawing area
  void clear()
  {
    g.setColor(BGCOLOR);
    g.fillRect(0, 0, getSize().width, getSize().height);
    g.drawRect(0, 0, getSize().width-1, 490-1);
  }

  
/******************************************************************/  

//a panel that contains an image
  public class ImagePanel extends JPanel{

  	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//The image that this panel draws
  	Image myImage;

  	public ImagePanel(Image me)
  	{
  		myImage = me;

  		//Makes the panel the same size as the image
  		setPreferredSize(new java.awt.Dimension(me.getWidth(null),
  												me.getHeight(null)));
  	}

  	//override the paint method to draw the image on the panel
  	public void paint(Graphics g)
  	{
  		g.drawImage(myImage, 0, 0, null);
  	}
  }
  
}
