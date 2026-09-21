package es.upm.miw.devops.rest.user;

public class UserFindCriteria {

    private Boolean active;
    private String mobile;
    private Boolean billable;

    public UserFindCriteria() {
        // empty
    }

    public UserFindCriteria(Boolean active, String mobile) {
        this.active = active;
        this.mobile = mobile;
    }

    public UserFindCriteria(Boolean active, String mobile, Boolean billable) {
        this.active = active;
        this.mobile = mobile;
        this.billable = billable;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public boolean hasActive() {
        return this.active != null;
    }

    public boolean hasMobile() {
        return this.mobile != null && !this.mobile.isBlank();
    }

    public boolean hasBillable() {
        return this.billable != null;
    }

    public boolean all() {
        return !this.hasActive()
                && !this.hasMobile()
                && !this.hasBillable();
    }

    @Override
    public String toString() {
        return "UserFindCriteria{" +
                "active=" + active +
                ", mobile='" + mobile + '\'' +
                ", billable=" + billable +
                '}';
    }
}
