import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingConstants;
import javax.swing.JSlider;

public class PainterFrame123 extends JFrame
{
	private DrawPanel drawpanel;//宣告一個畫布
	private JLabel statusBar,slidersize;//狀態列
	private JPanel west,south,east;//三個Panel
    private static final String[] names={"筆刷","直線","橢圓形","矩形","圓角矩形","橡皮擦"};//工具列名稱
    private static final Color[] colornames={Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,
                                             Color.GREEN,Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,
                                             Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};//顏色色塊
    private JButton[] buttons;//顏色按鈕
   	private Color forecolor =Color.BLACK, backcolor=Color.WHITE,color1,color2;//前景色 ,背景色,漸層色二
    private JRadioButton small,medium,big,hidden,display;//小、中、大、隱藏、顯示 radiobutton
   	private ButtonGroup pensizeradioGroup,displayradioGroup;
   	private JLabel label1,label2;
   	private JComboBox combobox;
	private JButton gradient,back,delete;//漸層 背景  刪除按鈕
	int tool=0,size=5, x=0,Ctype,a;//選擇工具列 筆刷大小  記錄點數  選擇是否漸層
	public BufferedImage image;//緩衝圖像
	private int x1, y1, x2, y2,drag=0;//初始位置  結束位置
    private JCheckBox filled;//填滿按鈕
	private boolean fill = false;//是否填滿
	private JSlider slider;//邊框大小
	
	
    /*******************************主畫面的排列*************************************/	
	public PainterFrame123()
	{
		super("小畫家");
		
		drawpanel=new DrawPanel();
	    west=new JPanel();
	    south=new JPanel();
	   
	    east=new JPanel();
	    statusBar=new JLabel("滑鼠在外面");
		add(drawpanel,BorderLayout.CENTER);
		add(west,BorderLayout.WEST);//將之加入JFrame裡
		add(south,BorderLayout.SOUTH);
		add(east,BorderLayout.EAST);
		
		drawpanel.setBackground(Color.WHITE);//畫布背景設成白色
	    west.setLayout(new GridLayout(10,1));//設成GridLayout
	  	south.setLayout(new GridLayout(2,1));//設成GridLayout
	  	east.setLayout(new GridLayout(13,1));//設成GridLayout
	    
        label1=new JLabel("[繪圖工具]");
        combobox=new JComboBox(names);// new 繪圖工具的JComboBox
        combobox.setMaximumRowCount(6);// 出現六個選項
        label2=new JLabel("[筆刷大小]");
        small=new JRadioButton("小",true);// new 小筆刷的JRadioButton
        medium=new JRadioButton("中",false);// new 中筆刷的JRadioButton
     	big=new JRadioButton("大",false);// new 大筆刷的JRadioButton
        filled=new JCheckBox("填滿");
	    back=new JButton("背景色"); // 背景色的按鈕
	    delete=new JButton("刪除畫面"); // 刪除畫面的按鈕
	    display=new JRadioButton("顯示工具列",true);//預設是顯示
     	hidden=new JRadioButton("隱藏工具列",false);
     	gradient=new JButton("漸層");//new 漸層按鈕
     	slidersize=new JLabel("[筆刷粗細]:"+size);
    	slider=new JSlider(SwingConstants.HORIZONTAL,0,30,5);
		slider.setMajorTickSpacing(1);
		slider.setPaintTicks(true);
     	
     	buttons=new JButton[13];//new 13個JButton
     	for(int j=0;j<=12;j++)
     	{
     	buttons[j]=new JButton();//把每個button new出來
     	east.add(buttons[j]);//把13個Button加入east顏色列
     	}
    	
     	for(int f=0;f<=12;f++)
     	buttons[f].setBackground(colornames[f]);//按鈕設成不同顏色
     	
     
     	
      	south.add(statusBar);//加入狀態列
     	south.add(display);//加入顯示選項
     	south.add(hidden);//加入隱藏選項
    	south.add(slidersize);  
     	south.add(slider);
     	
     	//將所有button加入west的工具列中
	    west.add(label1); 
      	west.add(combobox);
        west.add(filled);
      	west.add(label2);
        west.add(small);       
        west.add(medium);
        west.add(big);  
        west.add(gradient);
        west.add(back);
        west.add(delete);
        
       
        
                
        pensizeradioGroup=new ButtonGroup();//建一個筆刷大小的ButtonGroup
        pensizeradioGroup.add(small);
        pensizeradioGroup.add(medium); 
        pensizeradioGroup.add(big);
        
        displayradioGroup=new ButtonGroup();//建一個隱藏選項的ButtonGroup
        displayradioGroup.add(hidden);
        displayradioGroup.add(display);
       
        hidden.addItemListener(new RadioButtonHandler());
        display.addItemListener(new RadioButtonHandler());
        
        CheckBoxHandler mhandler=new CheckBoxHandler();//建填滿checkbox的mhandler
        filled.addItemListener(mhandler);//把fill變數加入mhandler
       
        //顯示選項的Listener
        display.addItemListener(
	    		new ItemListener()
	    		{
	    			public void itemStateChanged(ItemEvent event)
	    			{
	    				if(event.getStateChange()==ItemEvent.SELECTED)
	    					west.setVisible(true);
	    				    
	    				
	    			}
	    
	    		}
	    		);
        //隱藏選項的Listener
        hidden.addItemListener(
	    		new ItemListener()
	    		{
	    			public void itemStateChanged(ItemEvent event)
	    			{
	    				if(event.getStateChange()==ItemEvent.SELECTED)
	    					west.setVisible(false);
	    				    
	    				
	    			}
	    
	    		}
	    		);
        
        
        //把大中小的選項加入RadioButtonHandler
        small.addItemListener(new RadioButtonHandler());
        medium.addItemListener(new RadioButtonHandler());
        big.addItemListener(new RadioButtonHandler());
        //將按鈕加入handler 的ButtonHandler
        ButtonHandler handler = new ButtonHandler();
        for(int k=0;k<=12;k++)
        buttons[k].addActionListener(handler);
        gradient.addActionListener(handler);
 	    back.addActionListener(handler);
	    delete.addActionListener(handler);
	    slider.addChangeListener(handler);
	    
	        
 		//將tool只到哪個工具上
	    combobox.addItemListener(
	    		new ItemListener()
	    		{
	    			public void itemStateChanged(ItemEvent event)
	    			{
	    				if(event.getStateChange()==ItemEvent.SELECTED)
	    					tool=combobox.getSelectedIndex();
	    				    
	    				
	    			}
	    
	    		}
	    		);
	  	    
	 }//end paintFrame123
	
	/*******************************填滿按鈕的實作************************************/
	private class CheckBoxHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent event)
		{
			if (filled.isSelected()) 
				fill = true;
			else 
				fill = false;
		}
	}//end CheckBoxHandler
	/*******************************筆刷大小的實作************************************/
	private class RadioButtonHandler implements ItemListener
	{	
		public void itemStateChanged(ItemEvent event)
		{
		if (event.getSource() == small)
		{	
			size = 5;
			
		}
		if (event.getSource() == medium) 
		{
			size = 10;
			
		}
		if (event.getSource() == big)
		{
			size = 15;
			
		}
	    }//end itemStateChanged
 			
    }//end RadioButtonHandler
	
	/*******************************顏色列、漸層、背景色、刪除畫面的實作***************/
	private class ButtonHandler implements ActionListener,ChangeListener
	{
		
      	public void actionPerformed(ActionEvent event)
		{
      	
      		if(event.getSource()==buttons[0])
		    {forecolor=colornames[0];Ctype=0;}
	    else if(event.getSource()==buttons[1])
	        {forecolor=colornames[1];Ctype=0;}
		else if(event.getSource()==buttons[2])
			{forecolor=colornames[2];Ctype=0;}
		else if(event.getSource()==buttons[3])
			{forecolor=colornames[3];Ctype=0;}
		else if(event.getSource()==buttons[4])
            {forecolor=colornames[4];Ctype=0;}
		else if(event.getSource()==buttons[5])
			{forecolor=colornames[5];Ctype=0;}
		else if(event.getSource()==buttons[6])
			{forecolor=colornames[6];Ctype=0;}
		else if(event.getSource()==buttons[7])
			{forecolor=colornames[7];Ctype=0;}
		else if(event.getSource()==buttons[8])
	        {forecolor=colornames[8];Ctype=0;}
		else if(event.getSource()==buttons[9])
			{forecolor=colornames[9];Ctype=0;}
		else if(event.getSource()==buttons[10])
			{forecolor=colornames[10];Ctype=0;}
		else if(event.getSource()==buttons[11])
			{forecolor=colornames[11];Ctype=0;}
		else if(event.getSource()==buttons[12])
			{forecolor=colornames[12];Ctype=0;}
			  		
			
			if (event.getSource() == gradient)
			{
				
				color1=JColorChooser.showDialog(PainterFrame123.this,"選擇顏色一",color1);
				color2=JColorChooser.showDialog(PainterFrame123.this,"選擇顏色二",color2);
				Ctype=1;
				
				if(color1==null||color2==null)
				Ctype=0;
			}
			if (event.getSource() == back)
			{
				backcolor = JColorChooser.showDialog(PainterFrame123.this,"Choose a color", backcolor);
				if (backcolor == null)
				{
					backcolor = Color.WHITE;
				}
				back.setBackground(backcolor);
				drawpanel.setBackground(backcolor);
			}
			
			 if (event.getSource() == delete)
			{
				drawpanel.pointCount = 0;
				x1 = 0; x2 = 0; y1 = 0; y2 = 0; //重設x、y
				x=0;
				image = new BufferedImage(2000,2000,BufferedImage.TYPE_INT_ARGB_PRE);
				drawpanel.setBackground(backcolor);
				repaint();
		    }
		}
      	
    	public void stateChanged(ChangeEvent event) 
		{
			size=slider.getValue();
			slidersize.setText("[筆刷粗細]:"+size);
		}//設定邊框粗細
      	
      	
	}//end  ButtonHandler
	
	/***********************************畫布******************************************/
	public class DrawPanel extends JPanel
	{	
	
	 private int pointCount=0;

	 private Point[] point=new Point[100000];
		
	   public DrawPanel()
	   {			     
 	   image = new BufferedImage(2000,2000,BufferedImage.TYPE_INT_ARGB_PRE);//設定可以使用圖形的image
	   //建一個mousehandler並加MouseListener、MouseMotionListener加入
 	   MouseHandler handler=new MouseHandler();
	   addMouseListener(handler);
	   addMouseMotionListener(handler);
		
	   }//end DrawPanel()
	 
	  /*******************************滑鼠的實作*****************************************/
       private class MouseHandler implements MouseListener,MouseMotionListener
       {
		public void mouseClicked(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠位置 [%d, %d] (點擊)", event.getX(), event.getY() ) );
		}	
		public void mousePressed(MouseEvent event)
		{
			 statusBar.setText( String.format( "滑鼠位置 [%d, %d] (按下)", event.getX(), event.getY() ) );
	         x1 = event.getX();//設定初始座標
	         y1 = event.getY();
		}		
		public void mouseReleased(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠位置 [%d, %d] (放開)", event.getX(), event.getY() ) );
	        x2 = event.getX();
		    y2 = event.getY(); //設定終點座標
		    drag=0;
		    repaint();
		}
		 
		public void mouseEntered(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠位置 [%d, %d] (進入畫布)", event.getX(), event.getY() ) );
		}
		
		public void mouseExited(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠離開畫布外"));
			
		}
		public void mouseDragged(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠位置 [%d, %d] (拖曳)", event.getX(), event.getY() ) );
			
			if(pointCount<point.length)
			{
				if (tool == 0 || tool == 5) //紀錄筆刷或橡皮擦的拖曳動作
				{
				point[pointCount]=event.getPoint();
				pointCount++;
				repaint();
				}
				
				if(tool==2)
				{
					x2=event.getX();
					y2=event.getY();
					repaint();
					drag=1;
				}
				if(tool==3)
				{
					x2=event.getX();
					y2=event.getY();
					repaint();
					drag=1;
				}
				if(tool==4)
				{
					x2=event.getX();
					y2=event.getY();
					repaint();
					drag=1;
				}
			}
		}
		
		public void mouseMoved(MouseEvent event)
		{
			statusBar.setText( String.format( "滑鼠位置 [%d, %d] (移動)", event.getX(), event.getY() ) );
		}
	
		
      } //end MouseHandler   
      /****************************工具種類的實作****************************************************/
      public void paintComponent(Graphics g)
      {
    	  super.paintComponent(g);//呼叫JPanel的paintComponet
    	  
    	  Graphics gg = image.createGraphics();//呼叫BufferedImage中的createGraphics的method
    	  Graphics2D g2d = ( Graphics2D ) gg;	 //將gg投給Graphics2D
    	  //選擇是否使用漸層功能
    	  if(Ctype==0)
    	  {
    		  g2d.setPaint(forecolor);
    	  }
    	  else
    	  {
    		  g2d.setPaint(new GradientPaint(x1,y1,color1,x2,y2,color2,false));  
          }
    	   
    	  g2d.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER));//設定邊框粗細與圓滑邊角    	  
    	  
    	  //功能列的實作
    	  switch(tool)
    	  {
    	  case 0://筆刷
    		 
    		  for(int i =x  ; i < pointCount ; i++)
				g2d.draw(new Ellipse2D.Double(point[i].x,point[i].y,size,size));
    		  x=drawpanel.pointCount;			
		     	break;
    	  case 1://直線
    		
    		  g2d.draw(new Line2D.Double(x1,y1,x2,y2));
    		
    		  break;
    	  case 2://橢圓形
    		 
    		  if(drag==1)
    		  {
    			  g.drawOval(Math.min(x1,x2), Math.min(y1,y2),Math.abs(x1-x2), Math.abs(y1-y2));
    		  }
    		  if(drag==0)
    		  {
    		  if( fill ) //如果填滿
					g2d.fill(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
				else
    		        g2d.draw(new Ellipse2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1-y2)));
    		  }
			  break;
    	  case 3://矩形
    		if(drag==1)
    		{
    			 g.drawRect(Math.min(x1,x2), Math.min(y1,y2),Math.abs(x1-x2), Math.abs(y1-y2));
    					 
    		}
    		if(drag==0)
    		{
    		  if( fill ) //如果填滿
				g2d.fill(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
			else
    		    g2d.draw(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
    		}
    		  break;
    	  case 4://圓角矩形
    		 if(drag==1)
    		 {
    			 g.drawRoundRect(Math.min(x1,x2), Math.min(y1,y2),Math.abs(x1-x2), Math.abs(y1-y2),50,50);
    		 }
    		 if(drag==0)
    		 {
    		  if( fill ) //如果填滿
					g2d.fill(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),50,50));
				else
    		        g2d.draw(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),50,50));
    		 }
    		 
    		  break;
    	  case 5://橡皮擦
    		  for(int i = x; i < pointCount ; i++)
				{
					g2d.setColor(backcolor);//設成背景色的筆刷
					g2d.draw(new Ellipse2D.Double(point[i].x,point[i].y,size,size));
				}
			
				break;
    	  }
    	  g.drawImage(image, 0, 0, this); //繪出圖像
          }
      
	}//end DrawPanel	

}//end PainterFrame123 