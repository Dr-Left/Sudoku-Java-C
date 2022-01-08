import java.awt.*;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class myFrame extends JFrame
{
	private static final long serialVersionUID = -8135876226362375861L;
	/**
	 * Java frame, for the Sudoku
	 * @author Chris
	 * 
	 */
	private JPanel contentPane;
	private static JButton[][] btnField;
	public static JLabel lbl6;	// timer
	public static JLabel lbl4;	// steps
	private int[][] problem, answer;	//it's name is obvious
	public static JButton pt;	// the pointer to record which button is clicked
	private static int level = 1;	//it's name is obvious
	public static int mouseX, mouseY;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					myFrame frame = new myFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public myFrame() {
		setResizable(false);
		setTitle("Sudoku -----by Chris Zuo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 790, 650);
		
		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("楷体", Font.PLAIN, 15)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("楷体", Font.PLAIN, 15)));
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel1 = new JPanel();
		contentPane.add(panel1, BorderLayout.WEST);
		panel1.setLayout(new GridLayout(9, 9, 3, 3));
		panel1.setSize(800, 650);
		panel1.setBackground(myColor.dancuilv);
		panel1.setBorder(new EmptyBorder(5, 5, 5, 5));
		btnField = new JButton[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				btnField[i][j] = new JButton();
				btnField[i][j].setPreferredSize(new Dimension(70, 100));
				btnField[i][j].setText("");
				btnField[i][j].setHorizontalAlignment(JButton.CENTER);
				Color bgColor, frColor = Color.white;
				switch (i / 3 * 3 + j / 3) {
					case 0:case 2:case 6:case 8:
						bgColor = myColor.qiubolan;
						break;
					case 1:case 3:case 5:case 7:
						bgColor = myColor.danqianniuzi; break;
					case 4:
						bgColor = myColor.mudanfenhong; break;
					default :
						bgColor = Color.GRAY;
				}
				btnField[i][j].setBackground(bgColor);
				btnField[i][j].setForeground(frColor);
				btnField[i][j].setFont(new Font("微软雅黑", Font.BOLD, 42));
				panel1.add(btnField[i][j]);
			}
		}
		JPanel panel2 = new JPanel();
		contentPane.add(panel2, BorderLayout.EAST);
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel2.setBackground(myColor.xingrenhuang);
		panel2.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel lbl1 = new JLabel("当前关卡");
		lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl1.setFont(new Font("楷体", Font.PLAIN, 24));
		panel2.add(lbl1);
		panel2.add(Box.createVerticalStrut(24));
		
		JLabel lbl2 = new JLabel("1");
		lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl2.setFont(new Font("微软雅黑", Font.BOLD, 48));
		panel2.add(lbl2);
		panel2.add(Box.createVerticalStrut(24));
		
		JLabel lbl3 = new JLabel("已用步数");
		lbl3.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl3.setFont(new Font("楷体", Font.PLAIN, 24));
		panel2.add(lbl3);
		panel2.add(Box.createVerticalStrut(24));
		
		lbl4 = new JLabel("0");
		lbl4.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl4.setFont(new Font("微软雅黑", Font.PLAIN, 36));
		panel2.add(lbl4);
		panel2.add(Box.createVerticalStrut(24));
		
		JLabel lbl5 = new JLabel("已用时间");
		lbl5.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl5.setFont(new Font("楷体", Font.PLAIN, 24));
		panel2.add(lbl5);
		panel2.add(Box.createVerticalStrut(24));
		
		lbl6 = new JLabel("0s");
		lbl6.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl6.setFont(new Font("微软雅黑", Font.PLAIN, 20));
//		lbl6.setBackground(myColor.yanlan);
		panel2.add(lbl6);
		panel2.add(Box.createVerticalStrut(24));
		
		Counter ct = new Counter();
		ct.timer(System.currentTimeMillis());
		
		JButton btn1 = new JButton("重置");
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (problem[i][j] == 0)
							btnField[i][j].setText("");
					}
				}
			}
		});
		btn1.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn1.setFont(new Font("楷体", Font.BOLD, 24));
		panel2.add(btn1);
		panel2.add(Box.createVerticalStrut(12));
		
		JButton btn2 = new JButton("提交");
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checkAnswer()) {
					if (level < 20)
						JOptionPane.showMessageDialog(null, String.format("恭喜！答对了~送你一朵小鲜花~~~\n恭喜通过第%d关", level));
					else {
						JOptionPane.showMessageDialog(null, "恭喜你通过了所有关卡！你真棒！");
						myFrame.this.dispose();
						System.exit(0);
					}
					level++;
					lbl2.setText(Integer.toString(level));
					loadSudoku(level);
					ct.restart();
				}
				else {
					JOptionPane.showMessageDialog(null, "答错了。再试试吧~");
				}
			}
		});
		btn2.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn2.setFont(new Font("楷体", Font.BOLD, 24));
		panel2.add(btn2);
		panel2.add(Box.createVerticalStrut(12));
		
		JButton btn3 = new JButton("求助");
		btn3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean hasHelped = false;
				outside: for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						int entry;
						try {
							entry = Integer.parseInt(btnField[i][j].getText());
						}
						catch(Exception err) {
							entry = 0;
						}
						if (entry != answer[i][j]) {
							hasHelped = true;
							btnField[i][j].setText(Integer.toString(answer[i][j]));
							btnField[i][j].setForeground(Color.RED);
							btnField[i][j].setFont(new Font("微软雅黑", Font.BOLD, 42));
							ActionListener[] listeners = btnField[i][j].getActionListeners();
							for (ActionListener listener : listeners) {
							    btnField[i][j].removeActionListener(listener);
							}
							break outside;
						}
					}
				}
				if (hasHelped)
					lbl4.setText(Integer.toString(Integer.parseInt(lbl4.getText()) + 50));
			}
		});
		btn3.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn3.setFont(new Font("楷体", Font.BOLD, 24));
		panel2.add(btn3);
		panel2.add(Box.createVerticalStrut(12));
		
		JLabel lbl7 = new JLabel("<html> @author:<br/> Chris Zuo<br/>2021012328</html> ");
		lbl7.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl7.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		panel2.add(lbl7);
		
		
		loadSudoku(1);
	}
	
	private void loadSudoku(int level) {
		final String filename = "newSudoku.txt";
		try {
			interateWithC(level, filename);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// done: 读取文件中的数独题目，并且保存至数组
		
		problem = new int[9][];
		answer = new int[9][];
		lbl4.setText("0");
		try (Scanner sc = new Scanner(new FileReader(filename))) {
			sc.useDelimiter("");
			for (int i = 0; i < 9; i++) {
				problem[i] = new int[9];
				for (int j = 0; j < 9; j++) {
					problem[i][j] = sc.nextInt();
					sc.next();
				}
				sc.nextLine();
			}
			sc.nextLine();
			for (int i = 0; i < 9; i++) {
				answer[i] = new int[9];
				for (int j = 0; j < 9; j++) {
					answer[i][j] = sc.nextInt();
					sc.next();
				}
				sc.nextLine();
			}
			sc.close();
			File file = new File(filename);
			file.delete();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (problem[i][j] != 0) {
					btnField[i][j].setText(Integer.toString(problem[i][j]));
					btnField[i][j].setForeground(Color.white);
				}
				else {
					btnField[i][j].setText("");
					btnField[i][j].setForeground(Color.BLUE);
					final int $i = i, $j = j;
					btnField[i][j].addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
//							System.out.println("sfsf");
							//DONE: Create a new frame to ask the user to input the numbers
							myFrame.pt = btnField[$i][$j];
							try {
								myFrame.mouseY = btnField[0][0].getWidth() * ($i + 1);
								myFrame.mouseX = btnField[0][0].getHeight() * ($j + 1);
								CFrame frame2 = new CFrame();
								frame2.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		}
	}
	
	private boolean checkAnswer() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				try {
					if (answer[i][j] != Integer.parseInt(btnField[i][j].getText())) {
						return false;
					}
				}
				catch (Exception e) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void interateWithC(int functioncode, String filename) throws IOException, InterruptedException {
		String[] cmd = {"Sudoku.exe", Integer.toString(functioncode), filename};
		Process p = Runtime.getRuntime().exec(cmd);
		System.out.println("Java program waits");
		int exitVal = p.waitFor();
		System.out.println("Soduku.exe finishes. return value = " + exitVal);
	}
}

class CFrame extends JFrame
{
	private static final long serialVersionUID = -1894795727264408054L;
	/**
	 * Java choosing frame, for the Sudoku
	 * @author chris
	 * 
	 */
	private JPanel contentPane;
	public CFrame() {
		setTitle("选择数字");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(myFrame.mouseX + 120, myFrame.mouseY + 120, 300, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel1 = new JPanel();
		contentPane.add(panel1, BorderLayout.CENTER);
		panel1.setLayout(new GridLayout(3, 3, 2, 2));
		panel1.setBorder(new EmptyBorder(5,5,5,5));
		
		JButton[] btns = new JButton[9];	// btns:小键盘
		for (int i = 0; i < 9; i++) {
			btns[i] = new JButton(Integer.toString(i + 1));
			btns[i].setFont(new Font("微软雅黑", Font.PLAIN, 18));
			btns[i].setBackground(myColor.biqing);
			btns[i].setForeground(Color.WHITE);
			final int $i = i;
			btns[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					// DONE: 当文本里有数字，实现擦除功能
					int newNum = $i + 1;
					String st = myFrame.pt.getText();
					String[] list = st.split("/");
					int[] nums = new int[list.length];	// 记录当前格子填了的数字
					boolean hasNum = false; // 记录当前格子有没有填过当前数字
					for (int i = 0; i < list.length; i++) {
						if (list[i].length() > 0)
							nums[i] = Integer.parseInt(list[i]);
						if (nums[i] == newNum) {
							nums[i] = 0;
							hasNum = true;
						}
					}
					String newString = "";
					if (hasNum) {
						// 当前格子填过了这个数字，执行删除操作
						Arrays.sort(nums);
						for (int i = 0; i < nums.length; i++) {
							if (nums[i] != 0) {
								newString += "/" + nums[i];
							}
						}
					}
					else {
						// 之前没有添加过这个数字，单纯执行添加操作，注意斜杠的摆放位置
						// 类似于插入排序的思想
						boolean hasInserted = false;	// newNum 有没有插入过
						for (int i = 0; i < nums.length; i++) {
							if (nums[i] == 0)
								continue;
							// nums[i]!=0, 要插入nums[i]
							if (hasInserted) {
								// 已经插入过newNum
								newString += "/" + nums[i];
							}
							else {
								// 没有插入过newNum,那么还要比对newNum和nums[i]的大小
								if (newNum < nums[i]) {
									hasInserted = true;
									newString += "/" + newNum + "/" + nums[i];
								}
								else
									newString += "/" + nums[i];
							}
						}
						myFrame.pt.setFont(new Font("微软雅黑", Font.PLAIN, 18));
						if (!hasInserted) {
							// 最后一位插入
							newString += "/" + Integer.toString(newNum);
						}
						
					}
					newString = newString.substring(Math.min(newString.length(), 1));
					myFrame.pt.setText(newString);
					if (newString.length() == 1) {
							myFrame.pt.setFont(new Font("微软雅黑", Font.BOLD, 42));
					}
					// done传递用户选择的值
					CFrame.this.setVisible(false);
					CFrame.this.dispose();
					myFrame.lbl4.setText(Integer.toString(Integer.parseInt(myFrame.lbl4.getText()) + 1));
				}
			});
			btns[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			btns[i].setFont(new Font("微软雅黑", Font.PLAIN, 36));
			panel1.add(btns[i]);
		}
	}
}

class Counter {
	// 计时内部类
    private ScheduledThreadPoolExecutor scheduled;
    /* 构造 实现界面的开发 GUI */
    public Counter() {
        scheduled = new ScheduledThreadPoolExecutor(2);
    }
    /* timer: threading */
    public void timer(long startTime) {
    	DecimalFormat df = new DecimalFormat("0.0");
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
            	double d = (System.currentTimeMillis() - startTime)/1000.0;
                myFrame.lbl6.setText(df.format(d) + "s");
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }
    //停止定时器
    private void stopTimer() {
        if (scheduled != null) {
            scheduled.shutdownNow();
            scheduled = null;
        }
    }
    public void restart() {
    	stopTimer();
    	scheduled = new ScheduledThreadPoolExecutor(2);
    	timer(System.currentTimeMillis());
    }
}

class myColor extends Color
{
	// beautiful colors here
	private static final long serialVersionUID = -112005916683437283L;
	
	public myColor(int r, int g, int b) {
		super(r, g, b);
	}
	public static Color qiubolan = new Color(138, 188, 209);	// 秋波蓝
	public static Color jingtianlan = new Color(195, 215, 223);	// 井天蓝
	public static Color huaqing = new Color(35, 118, 183);	// 花青
	public static Color yanlan = new Color(20, 74, 116);	// 鷃蓝
	public static Color dancuilv = new Color(198, 223, 200);	//淡翠绿
	public static Color xingrenhuang = new Color(249, 236, 195);	//杏仁黄
	public static Color fengxinzi = new Color(200, 173, 196);	//凤信紫
	public static Color mudanfenhong = new Color(238, 162, 164);	//牡丹粉红
	public static Color danqianniuzi = new Color(209, 194, 211);	//淡牵牛紫
	public static Color xunzi = new Color(129, 92, 148);	//蕈紫
	public static Color biqing = new Color(92, 179, 204);	//碧青
}