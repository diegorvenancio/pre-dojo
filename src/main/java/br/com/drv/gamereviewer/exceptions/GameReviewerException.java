package br.com.drv.gamereviewer.exceptions;

public class GameReviewerException extends RuntimeException {

	public GameReviewerException(String msg, Exception e) {

		super(msg, e);
	}

	public GameReviewerException(String msg) {

		super(msg);
	}
}
