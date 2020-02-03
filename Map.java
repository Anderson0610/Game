import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Map {

	final static int ground_width = 10;
	private final Color dirt = new Color(150, 75, 0);
	private final Color grass = new Color(132, 222, 2);

	private ArrayList<Block> blocks;
	private ArrayList<SpringPlant> springes;
	private ArrayList<Slope> slopes;
	private ArrayList<Plant> plants;
	private ArrayList<Cloud> clouds;

	private Map(ArrayList<Block> blocks, ArrayList<SpringPlant> springes, ArrayList<Slope> slopes,
			ArrayList<Plant> plants, ArrayList<Cloud> clouds) {
		this.blocks = blocks;
		this.springes = springes;
		this.slopes = slopes;
		this.plants = plants;
		this.clouds = clouds;
	}

	public void display(Graphics g, int x, int y) {
		g.setColor(grass);
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).display(g, x, y);
		}
		for (int i = 0; i < slopes.size(); i++) {
			slopes.get(i).display(g, x, y);
		}

		g.setColor(dirt);
		for (int i = 0; i < springes.size(); i++) {
			springes.get(i).display(g, x, y);
		}

		for (Plant p : plants) {
			p.display(g, x, y);
		}
	}

	public Block getBlock(int x, int y) {
		for (Block g : blocks) {
			if (g.x < x && g.x + g.w > x && g.y < y && g.y + g.h > y)
				return g;
		}
		return null;
	}

	public Slope getSlope(int x, int y) {
		for (Slope s : slopes) {
			if (x >= s.x1 && x <= s.x2 && s.getY(x) - y < 2) {
				return s;
			}
		}
		return null;
	}

	public SpringPlant getSpringe(int x, int y) {
		for (SpringPlant sp : springes) {
			if (sp.x < x && sp.x + SpringPlant.width > x && sp.y < y && sp.y + SpringPlant.height > y)
				return sp;
		}
		return null;
	}

	public static Map getByID(int ID) {
		return loadMap();
	}

	private static Map loadMap() {
		final int w = ground_width;

		ArrayList<Block> blocks = new ArrayList<>();
		ArrayList<SpringPlant> springes = new ArrayList<>();
		ArrayList<Slope> slopes = new ArrayList<>();
		ArrayList<Plant> plants = new ArrayList<>();
		ArrayList<Cloud> clouds = new ArrayList<>();

		blocks.add(gB(-1, 700, 860, 500));
		// blocks.add(gB(860, 300, w, 400 + w));
		blocks.add(gB(860 - w, 300, 1600 - 860 + w, 800));
		// blocks.add(gB(1600, -110, w, 400 + w));
		blocks.add(gB(1600, -110, 500, 1000));
		blocks.add(gB(3000, 500, 2200, w));

		springes.add(gSP(710, 695));
		springes.add(gSP(1600 - SpringPlant.width, 300 - w));

		slopes.add(gS(2100, -110, 3000, 500));
		slopes.add(gS(5200, 500, 6100, -110));

		for (Block b : blocks)
			for (int i = 0; i < b.w - 50; i += new Random().nextInt(75) + 100)
				plants.add(gP(b.x + i, b.y));

		for (int i = 0; i < 6; i++)
			clouds.add(new Cloud(new Random().nextInt(Game.d.width), new Random().nextInt(Game.d.height / 3),
					new Random().nextInt(20) + 50));

		return new Map(blocks, springes, slopes, plants, clouds);

	}

	private static Block gB(int x, int y, int w, int h) {
		return new Block(x, y, w, h);
	}

	private static SpringPlant gSP(int x, int y) {
		return new SpringPlant(x, y);
	}

	private static Slope gS(int x1, int y1, int x2, int y2) {
		return new Slope(x1, y1, x2, y2);
	}

	private static Plant gP(int x, int y) {
		return new Plant(x, y, new Random().nextInt(Plant.plants.length));
	}

	private static class Cloud {
		int x, y;
		int size;
		int cons;

		public Cloud(int x, int y, int size) {
			this.x = x;
			this.y = y;
			this.size = size;
			cons = (int) (size * 0.7);
		}

		public void display(Graphics g) {
			g.fillOval(x - size / 2, y - size / 2, size, size);
			g.fillOval(x - size / 2 - cons / 2, y - cons / 3, cons, cons);
			g.fillOval(x + size / 2 - cons / 2, y - cons / 3, cons, cons);
		}

	}

	public void displayClouds(Graphics g) {
		for (Cloud c : clouds)
			c.display(g);
	}

}