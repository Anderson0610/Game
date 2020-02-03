import java.awt.Color;
import java.awt.Graphics;

public class SpringPlant {

	public static final Color dirt = new Color(150, 75, 0);
	public static final Color grass = new Color(132, 222, 2);

	static final int width = 160;
	static final int height = 30;

	int x, y;

	public SpringPlant(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void display(Graphics g, int x, int y) {

		g.setColor(dirt);
		g.fillOval(this.x - x, this.y - y, width, height);
		g.setColor(grass);
		g.fillOval(this.x - x + 10, this.y - y + 3, width - 20, height - 6);
	}

	public void bounce(Game player) {
		player.jump(30);
	}
}
