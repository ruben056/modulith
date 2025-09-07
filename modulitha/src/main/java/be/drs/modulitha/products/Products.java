package be.drs.modulitha.products;

import be.drs.modulitha.orders.OrderPlacedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
class Products {

    Logger logger = LoggerFactory.getLogger(getClass());
// todo
//    private final PrivateThing privateThing;
//
//    Products(PrivateThing privateThing) {
//        this.privateThing = privateThing;
//    }

    private final ProductsCollaborator productsCollaborator;

    Products(ProductsCollaborator productsCollaborator) {
        this.productsCollaborator = productsCollaborator;
    }

    @EventListener
    void on(OrderPlacedEvent ope) throws Exception {
        Thread.sleep(5_000);
        logger.info("order placed event [{}], wordt altijd getriggered, ook zonder tx context", ope);
        Thread.sleep(5_000);
        this.productsCollaborator.create(ope.order());
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener //only when event is published in a tx context
    void outboxOn(OrderPlacedEvent ope) throws Exception {
        Thread.sleep(5_000);
        logger.info("outbox order placed event  [{}] , wordt normaal enkel getriggered als de event een tx context heeft", ope);
        if(ope.quantity() == 0){
            throw new IllegalStateException("foutje, quantity mag niet 0 zijn"); //->DANGER  poison event!!!
        }
        Thread.sleep(5_000);
        this.productsCollaborator.create(ope.order());
    }

//    @Async
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    //TODO deze hier kunnen we wss gebruiken voor migratie redenen, werkt altijd, en in geval de publisher tx heeft, zal dit outbox pattern volgen...
    @TransactionalEventListener(fallbackExecution = true) //when event is published in a tx context AND when not published in a tx context
    void outboxOnAlways(OrderPlacedEvent ope) throws Exception {
        Thread.sleep(5_000);
        logger.info("outbox order placed event  [{}] , wordt ALTIJD getriggered", ope);
        if(ope.quantity() == 0){
            throw new IllegalStateException("foutje, quantity mag niet 0 zijn"); //->DANGER  poison event!!!
        }
        Thread.sleep(5_000);
        this.productsCollaborator.create(ope.order());
    }
}




@Component
class ProductsCollaborator {

    Logger logger = LoggerFactory.getLogger(getClass());

    int  create(int id) {
        logger.info("create({})", id);
        return id + id ;
    }
}
