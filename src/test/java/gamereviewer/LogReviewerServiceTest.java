package gamereviewer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.drv.gamereviewer.GameReviewer;
import br.com.drv.gamereviewer.entities.log.GameEnd;
import br.com.drv.gamereviewer.entities.log.GameStart;
import br.com.drv.gamereviewer.entities.log.KillLine;
import br.com.drv.gamereviewer.entities.log.LogLine;
import br.com.drv.gamereviewer.entities.match.Gun;
import br.com.drv.gamereviewer.entities.match.Match;
import br.com.drv.gamereviewer.exceptions.GameReviewerException;
import br.com.drv.gamereviewer.services.LogReviewerService;
import br.com.drv.gamereviewer.services.LogReviewerServiceImpl;

public class LogReviewerServiceTest {

	@Test(expected = GameReviewerException.class)
	public void testReviewLogFileNotFound() {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		lrs.reviewLog("testeNaoExistente.log");
	}

	@Test
	public void testReviewLogFileNotFoundExternal() {

		GameReviewer.main(new String[] { "./src/test/resources/test1.log" });
	}

	@Test
	public void testReviewLogResponseReturned() throws IOException {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		List<Match> response = lrs.reviewLog("./src/test/resources/test1.log");

//		System.out.println(response);

		assertNotNull(response);
	}

	@Test
	public void testParseLogLineMatchGameStart() {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		LogLine response = lrs.parseLogLine("23/04/2013 15:34:22 - New match 11348966 has started");

		assertTrue(response instanceof GameStart);
		assertTrue(((GameStart) response).getGameId().equals("11348966"));
	}

	@Test
	public void testParseLogLineMatchGameEnd() {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		LogLine response = lrs.parseLogLine("23/04/2013 15:39:22 - Match 11348965 has ended");

		assertTrue(response instanceof GameEnd);
		assertTrue(((GameEnd) response).getGameId().equals("11348965"));
	}

	@Test
	public void testParseLogLineKill() {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		LogLine response = lrs.parseLogLine("23/04/2013 15:36:04 - Roman killed Nick using M16");

		assertTrue(response instanceof KillLine);
		assertTrue(((KillLine) response).getMediumId().equals("M16"));
		assertTrue(((KillLine) response).getKilledId().equals("Nick"));
		assertTrue(((KillLine) response).getKillerId().equals("Roman"));
	}

	@Test
	public void testGetReportResponseReturned() throws IOException {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		List<Match> response = lrs.reviewLog("./src/test/resources/test1.log");

		String report = lrs.getReport(response).toString();

//		System.out.println(report);

		assertNotNull(response);
	}

	@Test
	public void testGetReportResponseReturned2() throws IOException {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		List<Match> response = lrs.reviewLog("./src/test/resources/test2.log");

		String report = lrs.getReport(response).toString();

//		System.out.println(report);

		assertNotNull(response);
	}

	@Test
	public void testGetReportResponseReturned3() throws IOException {

		LogReviewerService lrs = new LogReviewerServiceImpl();

		List<Match> response = lrs.reviewLog("./src/test/resources/test3.log");

		String report = lrs.getReport(response).toString();

//		System.out.println(report);

		assertNotNull(response);
	}

	@Test
	public void testPickFavoriteGun() {

		List<Gun> gunsUsed = new ArrayList<Gun>();
		gunsUsed.add(new Gun("AK-47"));
		gunsUsed.add(new Gun("AK-47"));
		gunsUsed.add(new Gun("AWP"));
		gunsUsed.add(new Gun("AK-47"));
		gunsUsed.add(new Gun("Glock-18"));
		gunsUsed.add(new Gun("AK-47"));
		gunsUsed.add(new Gun("Glock-18"));
		gunsUsed.add(new Gun("AK-47"));

		LogReviewerService lrs = new LogReviewerServiceImpl();

		Gun favoriteGun = lrs.pickFavoriteGun(gunsUsed);

		assertTrue(favoriteGun.getName().equals("AK-47"));
	}

	@Test
	public void testPickFavoriteGunNullResponse() {

		List<Gun> gunsUsed = new ArrayList<Gun>();

		LogReviewerService lrs = new LogReviewerServiceImpl();

		Gun favoriteGun = lrs.pickFavoriteGun(gunsUsed);

		assertTrue(favoriteGun == null);
	}

	@Test
	public void testGetAwardsForKillStreakNoAward() {

		List<LocalDateTime> kills = new ArrayList<>();
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 12, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 13, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 14, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 15, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 16, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 17, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 18, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 19, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 20, 25));

		LogReviewerService lrs = new LogReviewerServiceImpl();

		int awardCount = lrs.getAwardsForKillStreak(kills);

		assertTrue(awardCount == 0);
	}
	
	@Test
	public void testGetAwardsForKillStreakAward() {

		List<LocalDateTime> kills = new ArrayList<>();
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 01));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 10));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 11));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 11));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 40));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 45));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 18, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 19, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 20, 25));

		LogReviewerService lrs = new LogReviewerServiceImpl();

		int awardCount = lrs.getAwardsForKillStreak(kills);

		assertTrue(awardCount == 1);
	}
	
	
	@Test
	public void testGetAwardsForKillStreakAward2() {

		List<LocalDateTime> kills = new ArrayList<>();
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 01));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 10));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 11));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 11));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 40));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 11, 45));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 12, 25));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 12, 27));
		kills.add(LocalDateTime.of(2016, 02, 16, 14, 12, 28));

		LogReviewerService lrs = new LogReviewerServiceImpl();

		int awardCount = lrs.getAwardsForKillStreak(kills);

		assertTrue(awardCount == 2);
	}
}
