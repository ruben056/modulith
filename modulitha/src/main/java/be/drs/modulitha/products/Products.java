package be.drs.modulitha.products;

import be.drs.modulitha.orders.OrderPlacedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
class Products {

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
        System.out.println("order placed event [" + ope + "], wordt altijd getriggered, ook zonder tx context");
        Thread.sleep(5_000);
        this.productsCollaborator.create(ope.order());
    }

    @TransactionalEventListener
    void outboxOn(OrderPlacedEvent ope) throws Exception {
        Thread.sleep(5_000);
        System.out.println("outbox order placed event  [" + ope + "] , wordt normaal enkel getriggered als de event een tx context heeft");
        Thread.sleep(5_000);
        this.productsCollaborator.create(ope.order());
    }
}

@Component
class ProductsCollaborator {

    int  create(int id) {
        System.out.println("create(" + id + ")");
        return id + id ;
    }
}
