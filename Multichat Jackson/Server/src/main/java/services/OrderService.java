package services;

import models.OrderList;
import protocol.Request;

public interface OrderService {
    OrderList getOrders(Request request);
}
