package switchGame;


import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Switcher extends JApplet
  implements ActionListener, KeyListener, Runnable
{

  Image image;                // off-screen buffer
  Graphics g;                 // that buffer's graphical tools
  LL<Integer> goal = new LL<Integer>();
  LL<Integer> initial = new LL<Integer>();
  LL<Integer> current = new LL<Integer>();
  int sleepTime = 50;         // 50 milliseconds between updates
  int cycleNum;               // number of update cycles so far
  int topScore;
  String messageTop = "";             // A String that will be printed on screen
  String messageBottom = "";
  int difficulty = 5;
  int numMoves;
  int EASY = 5;
  int MEDIUM = 7;
  int HARD = 10;
  Font db = new Font("Dialog", Font.BOLD, 18);   
  Font tr = new Font("TimesRoman", Font.PLAIN, 12);
  Font d = new Font("Dialog", Font.PLAIN, 14); 
  Font db25 = new Font("Dialog", Font.BOLD, 35);

 
  // DEFINE CONSTANTS FOR YOUR PROGRAM HERE TO AVOID MAGIC VALUES!
  public static final int STRINGX = 15;
  public static final int STRINGY = 25;
  public static final int GAMEBOARDHEIGHT = 490; 
                               // Recommended values: 
                               // 490 with both menu bar and buttons
                               // 525 with only the menu bar
                               // 515 with only buttons
  public static final Color BGCOLOR = Color.black;
  public static final Color TEXTC = Color.white;

  // BELOW ARE DEFINITIONS OF BUTTONS AND MENU ITEMS WHICH WILL APPEAR
  private JButton newGameButton;
  private JButton pauseButton;
  private JButton startButton;
  
  private JMenu diffMenu;
  private JMenuItem easyItem;
  private JMenuItem mediumItem;
  private JMenuItem hardItem;
  
  private JMenu gameMenu;
  private JMenuItem newGameItem;
  private JMenuItem pauseItem;
  private JMenuItem startItem;

  private Color currentColor; // This is for the big square

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


    newGameButton = new JButton("New Game"); // the text in the button
    newGameButton.addActionListener(this);   // watch for button presses
    newGameButton.addKeyListener(this);      // listen for key presses here
   // buttonPane.add(newGameButton);           // add button to the panel

    startButton = new JButton("Start");      // a third button
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
    
    // Add a menu to contain items
    gameMenu = new JMenu("Game");            // The menu name
  //  menuBar.add(gameMenu);                   // Add the menu to menu bar
    
    newGameItem = new JMenuItem("New Game"); // the text in the menu item
    newGameItem.addActionListener(this);     // Watch for button presses
    newGameItem.addKeyListener(this);        // Listen for key presses here
    gameMenu.add(newGameItem);               // Add the item to the menu


    startItem = new JMenuItem("Start");      // A third menu item
    startItem.addActionListener(this);
    startItem.addKeyListener(this);
    gameMenu.add(startItem);

    // END OF MENU BAR CODE

    // If you choose not to use either menus or buttons, comment out the
    // related code, and adjust GAMEBOARDHEIGHT to account for the 
    // increased amount of space available to the game board

    // Sets up the back (off-screen) buffer for drawing, named image
    image = createImage(getSize().width, GAMEBOARDHEIGHT);
    g = image.getGraphics();                 // g holds drawing routines
    this.clear();                            // clears the screen
//    this.reset();                            // Set up the game internals!

    //add a central panel which holds the buffer (the game board)
    add(new ImagePanel(image), BorderLayout.CENTER);
    this.messageTop = "Switch adjacent blocks until you reach the goal." +
	" Press a number to switch that block with the block to its right.";
    displayMessage();
   
    this.drawEnvironment(); // update our offscreen image buffer
    repaint();              // tell Java to update the screen
  }

  
  boolean win()
  {
	  return this.current.equals(this.goal);
	  }

//Each time you start a new game, you will want to reset the
  // internal representation of the game.  Here's a good place to do it!
  // Remember, the applet will be initialized just once, but you may
  // play the game many times within that run of the applet!
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
	  
	  threadSuspended = true;
	  

  }
  
  
 private Color fromRGB(int r, int g, int b)
 {
		float[] hsb = Color.RGBtoHSB(r, g, b, null);
		Color color = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
		return color;
	    
 }
 
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
  
  // This is where you will draw your linked list of colored squares
  // Notice that all drawing occurs in the off-screen buffer "image".
  //   and that the drawing commands themselves are held in the Graphics g
  // Later, repaint() will copy the image to the screen.
  void drawEnvironment()
  {
    this.clear();                  // first, clear out our image buffer
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
  
  
  // You might use this method to draw a status String on the screen
  void displayMessage()
  {
    int CENTER = getSize().width/2;
    g.setColor(TEXTC);
    g.drawString(this.messageTop, STRINGX, STRINGY);
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
  
  // Things you want to happen at each update step
  // should be placed in this method. It's called from run().
  void cycle()
  { 
    this.drawEnvironment();  // draw things to buffer
    this.displayMessage();   // display messages
    repaint();               // send buffer to the screen
    this.cycleNum++;         // One cycle just elapsed
  }
  
  /******************************************************************/  
  // Here is how buttons and menu items work...
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
 
    if (source == startButton || source == startItem)  
    {
    	this.reset();
    }

    this.requestFocus(); // make sure the Applet keeps kbd focus
  }
  
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
        currentColor = Color.cyan;
        break;
    }

  }
  
  public void keyReleased(KeyEvent evt) {
      // We don't care if use *stops* pressing a key.
      // So, we do nothing when it happens.
      return;
  }
  public void keyTyped(KeyEvent evt) {
      // another keyboard-related event we don't care about
      return;
  }

  /*
   * A handy method to clear the applet's drawing area
   */
  void clear()
  {
    g.setColor(BGCOLOR);
    g.fillRect(0, 0, getSize().width, getSize().height);
    g.drawRect(0, 0, getSize().width-1, GAMEBOARDHEIGHT-1);
  }

  
/******************************************************************/  
  /*
   * The following methods and data members are used
   *   to implement the Runnable interface and to
   *   support pausing and resuming the applet.
   *
   */
  Thread thread;           // the thread controlling the updates
  boolean threadSuspended; // whether or not the thread is suspended
  boolean running;         // whether or not the thread is stopped

  /*
   * This is the method that calls the "cycle()"
   * method every so often (every sleepTime milliseconds).
   */
  public void run()
  {
    while (running) {
      try {
        if (thread != null) {
          thread.sleep(sleepTime);
          synchronized(this) {
            while (threadSuspended)
              wait(); // sleeps until notify() wakes it up
          }
        }
      }
      catch (InterruptedException e) { ; }

      cycle();  // this represents 1 update cycle for the environment
    }
    thread = null;
  }

  /* This is the method attached to the "Start" button
   */
  public synchronized void go()
  {
	  this.reset();
	  if (thread == null)  {
      thread = new Thread(this);
      running = true;
      thread.start();
      threadSuspended = false;
    } else {
      threadSuspended = false;
    }
    notify(); // wakes up the call to wait(), above
  }

  /*
   * This is the method attached to the "Pause" button
   */
  void pause()
  {
    if (thread == null)
      ;
    else
      threadSuspended = true;
  }

  /*
   * This is a method called when you leave the page
   *   that contains the applet. It stops the thread altogether.
   */
  public synchronized void stop()
  {
    running = false;
    notify();
  }

//a panel that contains an image
  public class ImagePanel extends JPanel{

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
