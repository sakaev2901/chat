package services;

import models.Pagination;
import protocol.Request;

public interface PaginationService {
    Pagination getMessages(Request request);
}
