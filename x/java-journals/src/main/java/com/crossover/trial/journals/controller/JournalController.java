package com.crossover.trial.journals.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crossover.trial.journals.model.Journal;
import com.crossover.trial.journals.service.CurrentUser;
import com.crossover.trial.journals.service.FileService;
import com.crossover.trial.journals.service.JournalService;

@Controller
public class JournalController {

	@Autowired
	private JournalService journalService;

	@Autowired
	private FileService fileService;

	@ResponseBody
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity renderDocument(@AuthenticationPrincipal Principal principal, @PathVariable("id") Long id) {
		CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();

		Optional<Journal> result = journalService.findJournalIfHasAccess(id, activeUser.getUser().getId());

		if (result.isPresent()) {
			Journal journal = result.get();
			Long bucket = journal.getPublisher().getId();
			String uuid = journal.getUuid();
			try (InputStream input = fileService.readFile(bucket, uuid)) {
				return ResponseEntity.ok(IOUtils.toByteArray(input));
			} catch (IOException e) {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
