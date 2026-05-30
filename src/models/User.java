package models;

// ABSTRACT CLASS: cannot be created directly ("new User()" is not allowed).
// Patient, Doctor, Admin all EXTEND this — they inherit these fields automatically.
// This is INHERITANCE across a package.
public abstract class User {

    // ENCAPSULATION: private fields — nobody can access these directly from outside
    private String userId;
    private String name;
    private String email;
    private String password;
    private String role;   // "PATIENT", "DOCTOR", "ADMIN"

    // Constructor
    public User(String userId, String name, String email, String password, String role) {
        this.userId   = userId;
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.role     = role;
    }

    // ABSTRACT METHOD: every subclass MUST implement this differently
    // Doctor displays specialty, Patient displays medical history, etc.
    public abstract String getDisplayInfo();

    // GETTERS — the only safe way to read private fields
    public String getUserId()   { return userId; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }
    public String getRole()     { return role; }

    // SETTERS — the only safe way to change private fields
    public void setName(String name)         { this.name = name; }
    public void setEmail(String email)       { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    // Convert to one line for saving in .txt file
    // Format: userId|name|email|password|role
    public String toFileString() {
        return userId + "|" + name + "|" + email + "|" + password + "|" + role;
    }
}
