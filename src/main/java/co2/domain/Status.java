package co2.domain;

import java.util.List;

public enum Status {
    ALERT, WARN, OK;

  public static boolean allWarn(List<Status> status){
       return status.stream().allMatch(e->e==WARN);
    }
    public static  boolean allOk(List<Status> status){
        return status.stream().allMatch(e->e==OK);
    }
}
