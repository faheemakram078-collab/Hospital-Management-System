package interfaces;

// INTERFACE: anything that can be billed must implement this.
// Patient implements Billable — so Patient MUST have calculateBill().
public interface Billable {
    double calculateBill();   // returns total bill amount
    String getBillSummary();  // returns a readable bill breakdown
}
