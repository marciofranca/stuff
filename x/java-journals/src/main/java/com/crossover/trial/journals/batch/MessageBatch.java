package com.crossover.trial.journals.batch;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.crossover.trial.journals.service.JournalService;
import com.crossover.trial.journals.service.MessageServiceImpl;

/**
 * Scheduler class implemented to send on a daily basis notification to users
 * subscribed regarding new journals published. It is configured to run every
 * day at 00:00:00 (12 AM).
 * 
 * @author marciofranca
 *
 */
@Component
public class MessageBatch {
	private static final Logger LOG = Logger.getLogger(MessageServiceImpl.class);

	@Autowired
	private JournalService journalService;

	// // TODO: Just for testing purposes as it is scheduled once a day. Should be
	// // removed.
	// private static boolean run = true;
	// @Scheduled(cron = "* * * * * ?")
	// public void onceAMinute() {
	// if (run) {
	// this.sendDailyJournalUpdates();
	// run = false;
	// }
	// }

	/**
	 * This method is configured to execute every day at 00:00:00 (12 AM). It
	 * retrieves new published journals on previous day and notify users subscribed.
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void onceADay() {
		this.sendDailyJournalUpdates();
	}

	public void sendDailyJournalUpdates() {
		LOG.info("Starting sendDailyJournalUpdates batch.");
		try {
			Date start = createStartDate();
			Date end = createEndDate();

			journalService.notifyNewPublishedJournals(start, end);
		} catch (Exception e) {
			LOG.error("Error executing sendDailyJournalUpdates batch.", e);
		}
		LOG.info("Finishing sendDailyJournalUpdates batch.");
	}

	// Yesterday 00:00:00.000
	private static Date createStartDate() {
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.MILLISECOND, 0);
		startCalendar.set(Calendar.SECOND, 0);
		startCalendar.set(Calendar.MINUTE, 0);
		startCalendar.set(Calendar.HOUR, 0);
		startCalendar.add(Calendar.DAY_OF_MONTH, -1);
		return startCalendar.getTime();
	}

	// Yesterday 23:59:59.999
	private static Date createEndDate() {
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Calendar.MILLISECOND, 0);
		endCalendar.set(Calendar.SECOND, 0);
		endCalendar.set(Calendar.MINUTE, 0);
		endCalendar.set(Calendar.HOUR, 0);
		endCalendar.add(Calendar.MILLISECOND, -1);

		return endCalendar.getTime();
	}

}
