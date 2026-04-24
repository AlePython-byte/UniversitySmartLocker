package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public interface LockerState {

    String getDisplayName();

    default List<String> getAllowedActions() {
        return List.of(LockerAction.GET_STATUS);
    }

    default OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("No se puede reservar el casillero mientras esta en estado " + getDisplayName() + ".");
    }

    default OperationResult storePackage(SmartLocker locker, String packageCode) {
        return OperationResult.failure("No se puede guardar el paquete mientras el casillero esta en estado " + getDisplayName() + ".");
    }

    default OperationResult markReadyForPickup(SmartLocker locker) {
        return OperationResult.failure("No se puede marcar como listo para recoger mientras el casillero esta en estado " + getDisplayName() + ".");
    }

    default OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("No se puede recoger el paquete mientras el casillero esta en estado " + getDisplayName() + ".");
    }

    default OperationResult markAsOverdue(SmartLocker locker) {
        return OperationResult.failure("No se puede marcar como vencido mientras el casillero esta en estado " + getDisplayName() + ".");
    }

    default OperationResult startMaintenance(SmartLocker locker) {
        return OperationResult.failure("No se puede iniciar mantenimiento mientras el casillero esta en estado " + getDisplayName() + ".");
    }

    default OperationResult finishMaintenance(SmartLocker locker) {
        return OperationResult.failure("No se puede finalizar mantenimiento porque el casillero no esta en mantenimiento.");
    }

    default OperationResult getStatus(SmartLocker locker) {
        return OperationResult.success("Estado consultado correctamente.");
    }
}
