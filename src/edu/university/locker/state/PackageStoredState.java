package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class PackageStoredState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Paquete almacenado";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.MARK_READY_FOR_PICKUP, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("No se puede reservar nuevamente porque ya hay un paquete asociado al casillero.");
    }

    @Override
    public OperationResult markReadyForPickup(SmartLocker locker) {
        String message = "El paquete quedo listo para ser recogido.";
        locker.transitionTo(locker.getReadyForPickupState(), LockerAction.MARK_READY_FOR_PICKUP, message);
        return OperationResult.success(message);
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        return OperationResult.failure("No se puede iniciar mantenimiento porque hay un paquete dentro del casillero.");
    }
}
