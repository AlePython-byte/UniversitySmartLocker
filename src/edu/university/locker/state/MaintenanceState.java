package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class MaintenanceState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Mantenimiento";
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("El casillero está en mantenimiento. Solo es posible finalizar el mantenimiento.");
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        return OperationResult.failure("El casillero está en mantenimiento. No se pueden guardar paquetes.");
    }

    @Override
    public OperationResult markReadyForPickup(SmartLocker locker) {
        return OperationResult.failure("El casillero está en mantenimiento. No se puede cambiar el estado del paquete.");
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El casillero está en mantenimiento. No se permite la recogida.");
    }

    @Override
    public OperationResult markAsOverdue(SmartLocker locker) {
        return OperationResult.failure("El casillero está en mantenimiento. No se puede marcar como vencido.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        return OperationResult.failure("El casillero ya se encuentra en mantenimiento.");
    }

    @Override
    public OperationResult finishMaintenance(SmartLocker locker) {
        locker.clearLockerData();
        locker.transitionTo(locker.getAvailableState());
        return OperationResult.success("Mantenimiento finalizado. El casillero volvió a estar disponible.");
    }
}
