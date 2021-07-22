package app.utility;

import app.auth.User;

public class DisplayControl {

    public User user;
    public String empId;
    public String deptId;
    public boolean isManager;

    public DisplayControl(User user, String empId, String deptId, boolean isManager) {
        this.user = user;
        this.empId = empId;
        this.deptId = deptId;
        this.isManager = isManager;
    }

    public boolean canEditProfile(){
        if (user.getId().equals(empId)) return true;

        if (user.isManager()){
            if (isManager) return false;
            return user.getDeptId().equals(deptId);
        } else {
            return false;
        }
    }

    public boolean canChangeDept(boolean isDuplicated){
        if (isDuplicated) return false;
        if (!user.isManager()) return false;
        return !isManager;
    }

    public boolean canMarkResignedRetired(){
        if (!user.isManager()) return false;
        if (deptId.equals("Resigned/Retired")) return false;
        if (!user.getDeptId().equals(deptId)) return false;
        return !isManager;
    }

    public boolean canDelete(){
        if (!user.isManager()) return false;
        if (deptId.equals("Resigned/Retired")) return false;
        if (!user.getDeptId().equals(deptId)) return false;
        return !isManager;
    }
}
