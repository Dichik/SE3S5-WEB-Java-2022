package org.agency.service.manager;

import org.agency.delegator.RepositoryDelegator;
import org.agency.entity.Ticket;
import org.agency.service.BaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ManagerService implements BaseService {
    private final Logger logger = LogManager.getLogger(ManagerService.class);

    public ManagerService(RepositoryDelegator repositoryDelegator) {
        // TODO ...
    }

    // TODO another annotation for allowing performing manager's operations
    public void setStatus(/* enum with statuses */) {
        // TODO implement this method
    }

    public List<Ticket> getTickets() {
        // TODO implement this method
        return null;
    }

    // TODO should we add method to get active/new tickets?

    // TODO should we add method for getting notifications? (additional table in database?)

}
