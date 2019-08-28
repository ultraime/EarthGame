package com.ultraime.game.metier.thread;

import com.ultraime.game.utile.Parametre;

public abstract class SuperThread implements Runnable {
	public static final int tempsAppel = 250;// 250

	public boolean running = true;
	protected Thread thread;

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
		}
		thread.start();
	}

	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
		thread = null;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	@SuppressWarnings("deprecation")
	public void suspend() {
		this.thread.suspend();
	}

	@SuppressWarnings("deprecation")
	public void resume() {
		this.thread.resume();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(tempsAppel / Parametre.VITESSE_DE_JEU);
				if (!Parametre.PAUSE) {
					doActionThread();
				}
			}
		} catch (InterruptedException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
		}
	}

	public abstract void doActionThread();
}
