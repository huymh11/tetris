package controller;

import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Shape;
import model.Shape.Tetrominoe;
import view.ScrGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Game extends JPanel {

	private static final long serialVersionUID = 1L;
	private final int WIDTH = 10;
	private final int HEIGHT = 22;
	private int SPEED = 400;

	public Timer timer;
	private boolean isFallingFinished = false;
	private boolean isPaused = false;
	private int numLinesRemoved = 0;
	public int curX = 0;
	public int curY = 0;
	private JLabel statusbar;
	private JLabel pause;
	private JLabel sound;
	private Clip music;
	private boolean ismute = true;
	public Shape curPiece;

	
	
	public int getCurX() {
		return curX;
	}

	public void setCurX(int curX) {
		this.curX = curX;
	}

	public int getCurY() {
		return curY;
	}

	public void setCurY(int curY) {
		this.curY = curY;
	}

	public Shape getCurPiece() {
		return curPiece;
	}

	public void setCurPiece(Shape curPiece) {
		this.curPiece = curPiece;
	}

	private Tetrominoe[] board;

	public Game(ScrGame parent) {
		initBoard(parent);

	}

	public void initBoard(ScrGame parent) {

		setFocusable(true);
		music = parent.getMusic();
		sound = parent.getSound();
		statusbar = parent.getStatusBar();
		pause = parent.getPause();
		addKeyListener(new TAdapter());

	}

	private int squareWidth() {

		return (int) getSize().getWidth() / WIDTH;
	}

	private int squareHeight() {

		return (int) getSize().getHeight() / HEIGHT;
	}

	private Tetrominoe shapeAt(int x, int y) {

		return board[(y * WIDTH) + x];
	}

	public void start() {

		curPiece = new Shape();
		board = new Tetrominoe[WIDTH * HEIGHT];

		clearBoard();
		newPiece();

		timer = new Timer(SPEED, new GameCycle());
		timer.start();
	}

	public void stop() {
		timer.stop();
		music.stop();
	}

	public void pause() {
		isPaused = !isPaused;

		if (!isPaused) {
			pause.setText("run");
		} else {
			pause.setText("stop");
			statusbar.setText(String.valueOf(numLinesRemoved));
		}

		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		BufferedImage backGround;
		backGround = FileLoader.loadImage("/backGround.png");
		g.drawImage(backGround, 0, 0, 450, 530, null);
		g.setColor(Color.white);
		g.drawLine(0, 0, 0, 533);
		g.drawLine(299, 0, 299, 533);
		g.drawLine(0, 0, 299, 0);
		g.drawLine(0, 527, 299, 527);
		doDrawing(g);	
	}

	private void doDrawing(Graphics g) {

		var size = getSize();

		int boardTop = (int) size.getHeight() - HEIGHT * squareHeight();

		for (int i = 0; i < HEIGHT; i++) {

			for (int j = 0; j < WIDTH; j++) {

				Tetrominoe shape = shapeAt(j, HEIGHT - i - 1);

				if (shape != Tetrominoe.NoShape) {

					drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
				}
			}
		}

		if (curPiece.getShape() != Tetrominoe.NoShape) {

			for (int i = 0; i < 4; i++) {

				int x = curX + curPiece.x(i);
				int y = curY - curPiece.y(i);

				drawSquare(g, x * squareWidth(), boardTop + (HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
			}
		}
	}

	private void dropDown() {

		int newY = curY;

		while (newY > 0) {

			if (!tryMove(curPiece, curX, newY - 1)) {

				break;
			}

			newY--;
		}

		pieceDropped();
	}

	private void oneLineDown() {

		if (!tryMove(curPiece, curX, curY - 1)) {

			pieceDropped();
		}
	}

	private void clearBoard() {

		for (int i = 0; i < HEIGHT * WIDTH; i++) {

			board[i] = Tetrominoe.NoShape;
		}
	}

	private void pieceDropped() {

		for (int i = 0; i < 4; i++) {

			int x = curX + curPiece.x(i);
			int y = curY - curPiece.y(i);
			board[(y * WIDTH) + x] = curPiece.getShape();
		}

		removeFullLines();

		if (!isFallingFinished) {

			newPiece();
		}
	}

	private void newPiece() {

		curPiece.setRandomShape();
		curX = WIDTH / 2 ;
		
		curY = HEIGHT - 1 + curPiece.minY();

		if (!tryMove(curPiece, curX, curY)) {

			curPiece.setShape(Tetrominoe.NoShape);

			timer.stop();

			JOptionPane.showMessageDialog(statusbar, " gameOver \n Score: " + String.valueOf(numLinesRemoved));

		}
	}

	private boolean tryMove(Shape newPiece, int newX, int newY) {

		for (int i = 0; i < 4; i++) {

			int x = newX + newPiece.x(i);
			int y = newY - newPiece.y(i);

			if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {

				return false;
			}

			if (shapeAt(x, y) != Tetrominoe.NoShape) {

				return false;
			}
		}

		curPiece = newPiece;
		curX = newX;
		curY = newY;

		repaint();

		return true;
	}

	private void removeFullLines() {

		int numFullLines = 0;

		for (int i = HEIGHT - 1; i >= 0; i--) {

			boolean lineIsFull = true;

			for (int j = 0; j < WIDTH; j++) {

				if (shapeAt(j, i) == Tetrominoe.NoShape) {

					lineIsFull = false;
					break;
				}
			}

			if (lineIsFull) {

				numFullLines++;

				for (int k = i; k < HEIGHT - 1; k++) {
					for (int j = 0; j < WIDTH; j++) {
						board[(k * WIDTH) + j] = shapeAt(j, k + 1);
					}
				}
			}
		}

		if (numFullLines > 0) {

			numLinesRemoved += numFullLines;
			statusbar.setText(String.valueOf(numLinesRemoved));
			isFallingFinished = true;
			curPiece.setShape(Tetrominoe.NoShape);
		}
	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {

		Color colors[] = { new Color(0, 0, 0), new Color(254, 56, 56), new Color(236, 255, 22), new Color(113, 252, 45),
				new Color(21, 110, 255), new Color(204, 102, 204), new Color(102, 204, 204), new Color(140, 0, 252) };

		var color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	}

	public class GameCycle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			doGameCycle();
		}
	}

	private void doGameCycle() {

		update();
		repaint();
	}

	private void update() {

		if (isPaused) {
			return;
		}

		if (isFallingFinished) {

			isFallingFinished = false;
			newPiece();
		} else {

			oneLineDown();
		}
	}
	

	private void isMute() {
		if (ismute) {
			sound.setText("Mute");
			music.stop();
			ismute = false;
		} else {
			sound.setText("Music");
			music.start();
			ismute = true;
		}
	}

	public class TAdapter extends KeyAdapter {

		@Override

		public void keyPressed(KeyEvent e) {

			if (curPiece.getShape() == Tetrominoe.NoShape) {

				return;
			}

			int keycode = e.getKeyCode();

			// Java 12 switch expressions
			switch (keycode) {

			case KeyEvent.VK_SPACE -> pause();
			case KeyEvent.VK_LEFT -> tryMove(curPiece, curX - 1, curY);
			case KeyEvent.VK_RIGHT -> tryMove(curPiece, curX + 1, curY);
			case KeyEvent.VK_DOWN -> dropDown();
			case KeyEvent.VK_UP -> tryMove(curPiece.rotate(), curX, curY);
			case KeyEvent.VK_M -> isMute();
			}
		}
	}

}
