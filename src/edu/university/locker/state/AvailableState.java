package edu.university.locker.state;

import edu.university.locker.context.SmartLocker;
import edu.university.locker.model.OperationResult;

public class AvailableState implements LockerState {

    @Override
    public String getDisplayName() {
        return "Disponible";
    }

    @Override
    public OperationResult reserve(SmartLocker locker, String studentName) {
        String securityCode = locker.generateSecurityCode();
        locker.assignReservation(studentName, securityCode);
        locker.transitionTo(locker.getReservedState());
        return OperationResult.success("Casillero reservado correctamente para " + studentName + ". Código de seguridad generado: " + securityCode + ".");
    }

    @Override
    public OperationResult storePackage(SmartLocker locker, String packageCode) {
        return OperationResult.failure("No se puede guardar un paquete sin una reserva previa.");
    }

    @Override
    public OperationResult pickupPackage(SmartLocker locker, String securityCode) {
        return OperationResult.failure("No hay ningún paquete listo para recoger.");
    }

    @Override
    public OperationResult startMaintenance(SmartLocker locker) {
        locker.transitionTo(locker.getMaintenanceState());
        return OperationResult.success("El casillero entró en mantenimiento.");
    }
}
