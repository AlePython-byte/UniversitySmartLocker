package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class MaintenanceState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Mantenimiento";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.FINISH_MAINTENANCE, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        return OperationResult.failure("El casillero esta en mantenimiento. Solo es posible finalizar el mantenimiento.");
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        return OperationResult.failure("El casillero esta en mantenimiento. No se pueden guardar paquetes.");
    }

    @Override
    public OperationResult markReadyForPickup(SmartLocker locker) {
        return OperationResult.failure("El casillero esta en mantenimiento. No se puede cambiar el estado del paquete.");
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("El casillero esta en mantenimiento. No se permite la recogida.");
    }

    @Override
    public OperationResult markAsOverdue(SmartLocker locker) {
        return OperationResult.failure("El casillero esta en mantenimiento. No se puede marcar como vencido.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        return OperationResult.failure("El casillero ya se encuentra en mantenimiento.");
    }

    @Override
    public OperationResult finishMaintenance(SmartLocker locker) {
        String message = "Mantenimiento finalizado. El casillero volvio a estar disponible.";
        locker.clearLockerData();
        locker.transitionTo(locker.getAvailableState(), LockerAction.FINISH_MAINTENANCE, message);
        return OperationResult.success(message);
    }
}
