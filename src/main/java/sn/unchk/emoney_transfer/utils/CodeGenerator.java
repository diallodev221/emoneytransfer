package sn.unchk.emoney_transfer.utils;

public final class CodeGenerator {

    private CodeGenerator() {
        // Private constructor to prevent instantiation
    }

  public static String generateBankAccountNumber(int length) {
      if (length < 8) {
          throw new IllegalArgumentException("Bank account number length should be at least 8 digits");
      }
      StringBuilder accountNumber = new StringBuilder();
      accountNumber.append((int) (Math.random() * 9) + 1); // First digit non-zero
      for (int i = 1; i < length; i++) {
          int digit = (int) (Math.random() * 10);
          accountNumber.append(digit);
      }
      return accountNumber.toString();
  }

    public static void main(String[] args) {
        System.out.println(generateBankAccountNumber(16));
    }
}
