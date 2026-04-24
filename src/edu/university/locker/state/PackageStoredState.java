package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class PackageStoredState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Paquete almacenado";
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("No se puede reservar nuevamente porque ya hay un paquete asociado al casillero.");
    }

    @Override
    public OperationResult markReadyForPickup(SmartLocker locker) {
        locker.transitionTo(locker.getReadyForPickupState());
        return OperationResult.success("El paquete quedó listo para ser recogido.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        return OperationResult.failure("No se puede iniciar mantenimiento porque hay un paquete dentro del casillero.");
    }
}
