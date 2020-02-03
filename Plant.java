import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Plant {
	int x;
	int y;
	int ID;
	static BufferedImage[] plants;
	static {
		plants = new BufferedImage[5];
		try {
			for (int i = 0; i < plants.length; i++)
				plants[i] = ImageIO.read(Plant.class.getResourceAsStream("grass_" + i + ".PNG"));
		} catch (IOException e) {
			System.exit(0);
		}
	}

	public Plant(int x, int y, int ID) {
		this.x = x;
		this.y = y;
		this.ID = ID;
	}

	public void display(Graphics g, int x, int y) {
		g.drawImage(plants[ID], this.x - x, this.y - y - plants[ID].getHeight(), null);
	}

}
