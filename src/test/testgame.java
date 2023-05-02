package test;

import java.awt.EventQueue;

import view.window;

public class testgame {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {

			var game = new window();
			game.setVisible(true);
		});
	}
}
