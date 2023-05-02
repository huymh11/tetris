package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import controller.FileLoader;

public class window extends JFrame {

	private static final long serialVersionUID = 1L;
	private ScrGame tetris;

	public ScrGame getTetris() {
		return tetris;
	}

	public void setTetris(ScrGame tetris) {
		this.tetris = tetris;
	}

	public window() {
		
		setResizable(false); //prevent frame from being resized
		setTitle("Tetris");
		setSize(454, 567);
		setDefaultCloseOperation(EXIT_ON_CLOSE); //exit out of application
		setLocationRelativeTo(null); 
		getContentPane().setLayout(null);
 
		JButton htp = new JButton("");
		htp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(htp,
						" RIGHT: move Right \n LEFT: move Left\n UP: Rorate\n DOWN: speed\n SPACE: Stop\n M: Stop Music");
			}
		});

		htp.setBounds(241, 327, 180, 53);
		htp.setIcon(new ImageIcon(FileLoader.loadImage("/howtoplay.png")));
		getContentPane().add(htp);

		JButton quit = new JButton("");
		quit.setBounds(241, 412, 180, 53);
		quit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(EXIT_ON_CLOSE);
			}
		});

		JButton newgame = new JButton("");
		newgame.setBounds(241, 235, 180, 53);
		newgame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				dispose();
				var ar = new ScrGame();
				ar.setVisible(true);
			}
		});

		newgame.setIcon(new ImageIcon(FileLoader.loadImage("/newgame.png")));
		getContentPane().add(newgame);
		quit.setIcon(new ImageIcon(FileLoader.loadImage("/quit.png")));
		getContentPane().add(quit);

		JLabel bgrh = new JLabel(""); 
		bgrh.setBounds(0, 0, 448, 538);  
		bgrh.setIcon(new ImageIcon(FileLoader.loadImage("/bgm.png"))); 
		getContentPane().add(bgrh);

	}

}
