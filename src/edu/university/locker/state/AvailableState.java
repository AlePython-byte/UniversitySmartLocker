package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.LockerAction;
import edu.university.locker.model.OperationResult;

import java.util.List;

public class AvailableState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Disponible";
    }

    @Override
    public List<String> getAllowedActions() {
        return List.of(LockerAction.RESERVE, LockerAction.START_MAINTENANCE, LockerAction.GET_STATUS);
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        String securityCode = locker.generateSecurityCode();
        String message = "Casillero reservado correctamente para " + studentName + ". Codigo de seguridad generado: " + securityCode + ".";
        locker.assignReservation(studentName, securityCode);
        locker.transitionTo(locker.getReservedState(), LockerAction.RESERVE, message);
        return OperationResult.success(message);
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        return OperationResult.failure("No se puede guardar un paquete sin una reserva previa.");
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("No hay ningun paquete listo para recoger.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        String message = "El casillero entro en mantenimiento.";
        locker.transitionTo(locker.getMaintenanceState(), LockerAction.START_MAINTENANCE, message);
        return OperationResult.success(message);
    }
}
