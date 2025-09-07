package be.drs.modulitha.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    void placeOrder(@RequestBody Order order) {
        this.orderService.placeOrder(order);
    }

    @Transactional
    @PostMapping("/orderstx")
    void placeOrdertx(@RequestBody Order order) {
        this.orderService.placeOrder(order);
    }
}



@Service
class OrderService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationEventPublisher publisher;

    private final OrderRepository repository;

    OrderService(ApplicationEventPublisher publisher, OrderRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    void placeOrder(Order order) {
        var saved = this.repository.save(order);
        logger.info("saved [{}]", saved);
        saved.lineItems()
                .stream()
                .map(li -> new OrderPlacedEvent(li.id(), li.quantity(), li.product()))
                .forEach(this.publisher::publishEvent);

        //TODO nog testen of alles werkt zoals verwacht als we een fout gooien na het publishen van de events
    }

}

//TODO why? usecases ...
//@Component
//class ModuleInitializer implements ApplicationModuleInitializer {
//
//    @Override
//    public void initialize() {
//        System.out.println("initializing module");
//    }
//}

interface OrderRepository extends ListCrudRepository<Order, Integer> {
}

@Table("orders_line_items")
record LineItem(@Id Integer id, int product, int quantity) {
}

@Table("orders")
record Order(@Id Integer id, Set<LineItem> lineItems) {
}
