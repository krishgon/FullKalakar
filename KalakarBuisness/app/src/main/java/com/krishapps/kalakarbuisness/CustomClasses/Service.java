package com.krishapps.kalakarbuisness.CustomClasses;

public class Service {
    public String getServiceFor() {
        return serviceFor;
    }

    public void setServiceFor(String serviceFor) {
        this.serviceFor = serviceFor;
    }

    public String getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(String serviceRate) {
        this.serviceRate = serviceRate;
    }

    public Service(String serviceFor, String serviceRate) {
        this.serviceFor = serviceFor;
        this.serviceRate = serviceRate;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    private String serviceID;
    private String serviceFor;
    private String serviceRate;
}
