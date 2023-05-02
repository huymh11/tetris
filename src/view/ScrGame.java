package view;

import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import controller.Game;
import controller.FileLoader;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ScrGame extends JFrame {
	private static final long serialVersionUID = 1L;
	public JLabel statusbar;
	public JLabel pause;
	public JLabel sound;
	Clip music;

	public JLabel getSound() {
		return sound;
	}

	public JLabel getStatusbar() {
		return statusbar;
	}

	public JLabel getPause() {
		return pause;
	}

	public Clip getMusic() {
		return music;
	}

	public JLabel getStatusBar() {

		return statusbar;
	}

	public ScrGame() {

		setResizable(false);
		setTitle("Tetris");
		setSize(454, 560);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		statusbar = new JLabel("0");
		pause = new JLabel("run");
		sound = new JLabel("Music");
		sound.setForeground(Color.WHITE);
		sound.setSize(88, 47);
		sound.setLocation(326, 330);
		music = FileLoader.LoadSound("/music.wav");
		music.loop(Clip.LOOP_CONTINUOUSLY);
		var board = new Game(this);

		sound.setFont(new Font("Serif", Font.BOLD, 30));
		getContentPane().add(sound);

		pause.setFont(new Font("Serif", Font.BOLD, 30));
		pause.setHorizontalAlignment(SwingConstants.CENTER);
		pause.setForeground(Color.WHITE);
		pause.setBounds(326, 249, 95, 32);
		getContentPane().add(pause);

		statusbar.setHorizontalAlignment(SwingConstants.CENTER);
		statusbar.setFont(new Font("Serif", Font.BOLD, 49));
		statusbar.setForeground(Color.WHITE);
		statusbar.setBounds(310, 158, 111, 59);
		getContentPane().add(statusbar);

		board.setBounds(0, 0, 300, 530);

		getContentPane().add(board);

		JLabel score = new JLabel("Score");
		score.setHorizontalAlignment(SwingConstants.CENTER);
		score.setForeground(new Color(255, 255, 255));
		score.setFont(new Font("Serif", Font.BOLD, 36));
		score.setBounds(320, 109, 101, 38);
		getContentPane().add(score);

		JLabel exhome = new JLabel("Quit");
		exhome.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				board.stop();
				var ar = new window();
				ar.setVisible(true);
			}
		});
		exhome.setHorizontalAlignment(SwingConstants.CENTER);
		exhome.setFont(new Font("Serif", Font.BOLD, 35));
		exhome.setForeground(Color.WHITE);
		exhome.setBounds(326, 434, 95, 59);
		getContentPane().add(exhome);

		JLabel bgrJLabel = new JLabel("");
		bgrJLabel.setIcon(new ImageIcon(FileLoader.loadImage("/backGround.png")));
		bgrJLabel.setBounds(10, 0, 448, 538);
		getContentPane().add(bgrJLabel);
		board.start();

		
	}

}
