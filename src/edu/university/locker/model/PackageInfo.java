package edu.university.locker.model;

public class PackageInfo {

    private final String packageCode;
    private final String securityCode;

    public PackageInfo(String packageCode, String securityCode) {
        this.packageCode = packageCode;
        this.securityCode = securityCode;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public String getSecurityCode() {
        return securityCode;
    }
}
