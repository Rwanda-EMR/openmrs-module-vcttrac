package org.openmrs.module.vcttrac;

/**
 * Created by k-joseph on 11/06/2017.
 */
public class RegistrationEntryPointClass {
    public RegistrationEntryPointClass(VCTClient.RegistrationEntryPoint rep) {
        this.name = rep.name();
        this.displayName = rep.displayName();
    }
    private String name;
    private String displayName;

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }
}
