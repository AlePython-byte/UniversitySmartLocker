package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class ReadyForPickupState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Listo para recoger";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.PICKUP_PACKAGE, LockerAction.MARK_AS_OVERDUE, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        if (!locker.matchesSecurityCode(securityCode)) {
            return OperationResult.failure("El codigo de seguridad es incorrecto. Verifica el codigo e intentalo nuevamente.");
        }

        String studentName = locker.getStudentName();
        String message = "Paquete entregado correctamente a " + studentName + ". El casillero volvio a estar disponible.";
        locker.clearLockerData();
        locker.transitionTo(locker.getAvailableState(), LockerAction.PICKUP_PACKAGE, message);
        return OperationResult.success(message);
    }

    @Override
    public OperationResult markAsOverdue(SmartLocker locker) {
        String message = "El tiempo de recogida vencio. El casillero quedo marcado como vencido.";
        locker.transitionTo(locker.getOverdueState(), LockerAction.MARK_AS_OVERDUE, message);
        return OperationResult.success(message);
    }
}
