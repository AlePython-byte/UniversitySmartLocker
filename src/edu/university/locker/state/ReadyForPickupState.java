package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class ReadyForPickupState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Listo para recoger";
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        if (!locker.matchesSecurityCode(securityCode)) {
            return OperationResult.failure("El código de seguridad es incorrecto. Verifica el código e inténtalo nuevamente.");
        }

        String studentName = locker.getStudentName();
        locker.clearLockerData();
        locker.transitionTo(locker.getAvailableState());
        return OperationResult.success("Paquete entregado correctamente a " + studentName + ". El casillero volvió a estar disponible.");
    }

    @Override
    public OperationResult markAsOverdue(SmartLocker locker) {
        locker.transitionTo(locker.getOverdueState());
        return OperationResult.success("El tiempo de recogida venció. El casillero quedó marcado como vencido.");
    }
}
