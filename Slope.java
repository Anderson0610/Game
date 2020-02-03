import java.awt.Graphics;

public class Slope {
	int x1, y1, x2, y2;

	public Slope(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public void display(Graphics g, int x, int y) {
		int[] xs = { x1 - x, x2 - x, x2 - x, x1 - x };
		int[] ys = { y1 - y, y2 - y, y2 - y + 1000, y1 - y + 1000 };
		
		g.setColor(SpringPlant.grass);
		g.fillPolygon(xs, ys, xs.length);
	}

	public int getY(int player_x) {
		if (player_x < x1 || player_x > x2)
			return -1;
		return (int) ((0.0 + y2 - y1) / (0.0 + x2 - x1) * (player_x - x1) + y1);
	}

	public double getSlope() {
		return (0.0 + y2 - y1) / (0.0 + x2 - x1);
	}

}
