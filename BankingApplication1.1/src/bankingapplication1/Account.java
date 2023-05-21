
package bankingapplication1;


public interface Account {
    public void deposit(double amount);
    public void withdraw(double amount);
    public double getBalance();
    public int getAccountNumber();
    public String getAccountType();
    public String getAccountName();
    
}
