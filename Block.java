import java.awt.Graphics;

public class Block {
	int x;
	int y;
	int w;
	int h;

	public Block(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void display(Graphics g, int x, int y) {
		g.fillRoundRect(this.x - x, this.y - y, w, h,5,5);
	}

}//what?
