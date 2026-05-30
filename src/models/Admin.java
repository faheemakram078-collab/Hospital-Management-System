package models;

import interfaces.Manageable;

public class Admin extends User implements Manageable {

    private String department;

    public Admin(String userId, String name, String email,
                 String password, String department) {
        super(userId, name, email, password, "ADMIN");
        this.department = department;
    }

    @Override
    public String getDisplayInfo() {
        return "Admin: " + getName() + " | Dept: " + department;
    }

    // INTERFACE METHODS — Admin manages the hospital records
    @Override
    public void add()    { System.out.println("Admin: record added."); }
    @Override
    public void update() { System.out.println("Admin: record updated."); }
    @Override
    public void delete() { System.out.println("Admin: record deleted."); }

    public String getDepartment() { return department; }
    public void setDepartment(String d) { this.department = d; }

    // Format: userId|name|email|password|ADMIN|department
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + department;
    }

    public static Admin fromFileString(String line) {
        String[] p = line.split("\\|");
        return new Admin(p[0], p[1], p[2], p[3], p[5]);
    }
}
