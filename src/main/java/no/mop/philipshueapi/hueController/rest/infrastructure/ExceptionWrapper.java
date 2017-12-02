package no.mop.philipshueapi.hueController.rest.infrastructure;

import no.mop.philipshueapi.hueController.rest.infrastructure.ThrowingSupplier;

public class ExceptionWrapper {

    public static <T> T wrapExceptions(ThrowingSupplier<T> throwingSupplier) {
        return throwingSupplier.get();
    }
}
