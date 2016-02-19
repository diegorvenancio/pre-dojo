package br.com.drv.gamereviewer;

import br.com.drv.gamereviewer.exceptions.GameReviewerException;
import br.com.drv.gamereviewer.services.LogReviewerService;
import br.com.drv.gamereviewer.services.LogReviewerServiceImpl;

/**
 * 
 * Classe inicial para geração de report a partir do log de uma ou mais partidas
 * @author Diego Venancio
 * @version 1.0
 * @since 2016
 *
 */
public class GameReviewer {

	public static void main(String[] args) {

		if (args.length != 1) {
			
			System.out.println("Por favor, passe como parâmetro o caminho do arquivo de log a revisar.");
		} else {
			LogReviewerService service = new LogReviewerServiceImpl();
			
			try {
				System.out.println(service.getReport(service.reviewLog(args[0])));
				
			} catch (GameReviewerException e) {

				System.out.format("Erro durante execução do GameReviewer: %n");
				e.printStackTrace();
			}
		}
	}

}
