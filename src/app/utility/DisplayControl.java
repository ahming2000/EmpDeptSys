package app.utility;

import app.auth.AuthUser;

public class DisplayControl {

    public AuthUser authUser;
    public String empId;
    public String deptId;
    public boolean isManager;

    public DisplayControl(AuthUser authUser, String empId, String deptId, boolean isManager) {
        this.authUser = authUser;
        this.empId = empId;
        this.deptId = deptId;
        this.isManager = isManager;
    }

    public boolean canEditProfile(){
        if (authUser.getId().equals(empId)) return true;

        if (authUser.isManager()){
            if (isManager) return false;
            return authUser.getDeptId().equals(deptId);
        } else {
            return false;
        }
    }

    public boolean canChangeDept(boolean isDuplicated){
        if (isDuplicated) return false;
        if (!authUser.isManager()) return false;
        return !isManager;
    }

    public boolean canMarkResignedRetired(){
        if (!authUser.isManager()) return false;
        if (deptId.equals("Resigned/Retired")) return false;
        if (!authUser.getDeptId().equals(deptId)) return false;
        return !isManager;
    }

    public boolean canDelete(){
        if (!authUser.isManager()) return false;
        if (deptId.equals("Resigned/Retired")) return false;
        if (!authUser.getDeptId().equals(deptId)) return false;
        return !isManager;
    }
}
