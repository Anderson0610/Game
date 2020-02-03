import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class Game implements Runnable {
	final static Dimension d = new Dimension(1200, 860);

	final boolean P = false;
	final boolean R = !P;
	static final int w = Map.ground_width;

	Painter p = new Painter();
	final double maxDropSpeed = 20;
	int jumpHeight = -23;
	int walkBreak = 1;
	private double playerSpeed;
	private Map map;
	private Player me;
	private boolean running;

	static int jumpKey = KeyEvent.VK_W;
	static int leftKey = KeyEvent.VK_A;
	static int rightKey = KeyEvent.VK_D;

	boolean jump;
	boolean left;
	boolean right;

	public static void main(String[] args) {
		new Game();
	}

	public static final void p(Object arg) {
		System.out.println(arg.toString());
	}

	public Game() {

		me = new Player(0, 699);

		jump = false;
		left = false;
		right = false;

		running = true;
		playerSpeed = 0;
		map = Map.getByID(0);
		JFrame frame = new JFrame("window");
		frame.setBounds(0, 0, d.width, d.height);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		// frame.setMinimumSize(arg0);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		new Thread(this).start();
	}

	public void jump(int height) {

		if (map.getBlock(me.x, me.y + 1) != null || map.getSlope(me.x, me.y + 1) != null) {
			playerSpeed = height;
			for (int i = 0; i < 5 && me.water > 75; i++) {
				me.water--;
			}
		}
	}

	public static void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
		}
	}

	class Painter extends JComponent {
		private static final long serialVersionUID = 1L;
		static final int playerWidth = 80;
		static final int playerHeight = 110;

		BufferedImage player;
		BufferedImage background;
		Color dirt = new Color(150, 75, 0);
		Color grass = new Color(132, 222, 2);

		public Painter() {
			setFocusTraversalKeysEnabled(false);
			try {
				player = ImageIO.read(getClass().getResourceAsStream("player.png"));
			} catch (IOException e) {
				System.exit(0);
			}

			this.getInputMap().put(KeyStroke.getKeyStroke(Game.rightKey, 0, P), "RIGHT PRESSED");
			this.getActionMap().put("RIGHT PRESSED", new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					right = true;
				}

			});

			this.getInputMap().put(KeyStroke.getKeyStroke(Game.rightKey, 0, R), "RIGHT RELEASED");
			this.getActionMap().put("RIGHT RELEASED", new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					right = false;
				}

			});

			this.getInputMap().put(KeyStroke.getKeyStroke(Game.leftKey, 0, P), "LEFT PRESSED");
			this.getActionMap().put("LEFT PRESSED", new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					left = true;
				}

			});

			this.getInputMap().put(KeyStroke.getKeyStroke(Game.leftKey, 0, R), "LEFT RELEASED");
			this.getActionMap().put("LEFT RELEASED", new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					left = false;
				}

			});

			this.getInputMap().put(KeyStroke.getKeyStroke(Game.jumpKey, 0, P), "W PRESSED");
			this.getActionMap().put("W PRESSED", new AbstractAction() {

				private static final long serialVersionUID = 1L;

				@Override
				public void actionPerformed(ActionEvent arg0) {
					jump(jumpHeight);
				}

			});

		}

		@Override
		public void paintComponent(Graphics g) {
//			map.display(g, me.x < d.width / 2 ? 0 : me.x - d.width / 2, me.y);
//
//			g.drawImage(player, (me.x > d.width / 2 ? d.width / 2 : me.x) - playerWidth / 2, me.y - playerHeight,
//					playerWidth, playerHeight, null);
			g.setColor(new Color(135, 206, 235));
			g.fillRect(0, 0, 2000, 2000);
			g.setColor(Color.white);
			map.displayClouds(g);
			map.display(g, me.x < d.width / 2 ? 0 : me.x - d.width / 2, me.y - 500);

			g.drawImage(player, (me.x > d.width / 2 ? d.width / 2 : me.x) - playerWidth / 2, 499 - playerHeight,
					playerWidth, playerHeight, null);

			g.setColor(Color.black);
			g.setFont(new Font("Times", 0, 12));
			g.drawString("" + me.water, 40, 40);

//			map.display(g, me.x < d.width / 2 ? 0 : me.x - d.width / 2, me.y);
//
//			g.drawImage(player, (me.x > d.width / 2 ? d.width / 2 : me.x) - playerWidth / 2, (me.y < -500 ? -:me.y) ,
//					playerWidth, playerHeight, null);

		}
	}

	public void run() {

		// left / right movement thread
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					sleep(3);
					while (right && !left) {
						// see if there's a block under the right bottom corner of the player
						if (map.getBlock(me.x + Painter.playerWidth / 2 + 1, me.y) == null)
							me.x++;
						sleep(walkBreak);
					}
					// block under the player detecting
					while (left && !right) {
						// see if there's a block under the left bottom corner of the player
						if (map.getBlock(me.x - Painter.playerWidth / 2 - 1, me.y) == null) {
							if (map.getSlope(me.x, me.y + 1) != null) {
								me.x = me.x > 0 ? me.x - 1 : 0;
								me.y -= map.getSlope(me.x - 1, me.y + 1).getSlope();
							} else
								me.x = me.x > 0 ? me.x - 1 : 0;
						}
						sleep(walkBreak);
					}
				}
			}
		}).start();

		// spring thread
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					sleep(10);
					if (map.getSpringe(me.x, me.y + 1) != null) {
						playerSpeed = jumpHeight - 10;
						for (int i = 0; i < 7 && me.water > 75; i++) {
							me.water--;
						}
					}
				}
			}
		}).start();

		// print coordinate point thread
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					sleep(100);
					p(me.x + ", " + (me.y));
				}
			}
		}).start();

		// gravity thread
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					sleep(13);
					if (map.getBlock(me.x, me.y + 1) == null) {
						playerSpeed = playerSpeed + 1 < maxDropSpeed ? playerSpeed + 1 : maxDropSpeed;
					} else if (map.getSlope(me.x, me.y + 1) == null) {
						playerSpeed = playerSpeed + 1 < maxDropSpeed ? playerSpeed + 1 : maxDropSpeed;
					}
					for (int i = 0; i < playerSpeed; i++) {
						boolean drop = true;
						if (map.getBlock(me.x, me.y + 1) != null)
							drop = false;
						else if (map.getSlope(me.x, me.y + 1) != null) {
							drop = false;
							p(drop);
						}

						if (drop)
							me.y += 1;
					}
					for (int i = (int) playerSpeed; i < 0; i++)
						me.y -= 1;
				}
			}
		}).start();

		while (running) {
			p.repaint();
		}
	}
}