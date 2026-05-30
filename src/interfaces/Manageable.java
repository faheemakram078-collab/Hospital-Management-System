package interfaces;

// INTERFACE: defines a contract — any class that "implements Manageable"
// MUST provide these 3 methods. We don't write the body here.
public interface Manageable {
    void add();       // add a record
    void update();    // update a record
    void delete();    // delete a record
}
