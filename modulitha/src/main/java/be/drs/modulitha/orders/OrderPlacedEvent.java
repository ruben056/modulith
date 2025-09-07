package be.drs.modulitha.orders;

public record OrderPlacedEvent(
        int order,
        int quantity ,
        int product
) {
}
