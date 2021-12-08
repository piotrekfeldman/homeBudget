import java.time.LocalDate;
import java.util.Scanner;

public class TransactionApp {

    private static final TransactionDAO DAO = new TransactionDAO();
    private Scanner scanner;

    public TransactionApp() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {

        while (true) {
            printMenu();
            switch (scanner.nextLine()) {
                case "1":
                    addNewTransaction();
                    break;
                case "2":
                    modifyTransaction();
                    break;
                case "3":
                    deleteTransaction();
                    break;
                case "4":
                    displaySumAmount(TransactionType.INCOME);
                    break;
                case "5":
                    displaySumAmount(TransactionType.EXPENSE);
                    break;
                case "0":
                    DAO.close();
                    return;
                default:
                    System.out.println("Podałeś nieprawidłową opcję");
            }
        }
    }

    private void modifyTransaction() {
        System.out.println("Podaj id transakcji, którą chcesz zmodyfikować");
        Integer id = scanner.nextInt();
        scanner.nextLine();
        Transaction transaction = transactionCreator();
        transaction.setId(id);
        DAO.update(transaction);
    }


    private void deleteTransaction() {
        System.out.println("Podaj id transakcji");
        Integer id = scanner.nextInt();
        scanner.nextLine();
        DAO.delete(id);

    }

    private void addNewTransaction() {
        DAO.save(transactionCreator());
    }

    private void displaySumAmount(TransactionType transactionType) {
        Double byTransactionType = DAO.findByTransactionType(transactionType);

        System.out.println("Suma to: " + byTransactionType);
    }


    private Transaction transactionCreator() {
        TransactionType type1 = null;
        System.out.println("Podaj typ transakcji");
        System.out.println("1. Przychód");
        System.out.println("2. Wydatek");
        String option = scanner.nextLine();
        while (true) {
            if (option.equals("1")) {
                type1 = TransactionType.INCOME;
            } else if (option.equals("2")) {
                type1 = TransactionType.EXPENSE;
            } else {
                System.out.println("Podana wartość jest nieprawidłowa. Spróbuj ponownie");
            }

            System.out.println("Podaj opis transakcji");
            String description = scanner.nextLine();
            System.out.println("Podaj kwotę transakcji");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            return new Transaction(type1, description, amount, LocalDate.now());

        }
    }

    private void printMenu() {
        System.out.println("1. Dodaj transakcję");
        System.out.println("2. Zmodyfikuj transakcję");
        System.out.println("3. Usuń transakcję");
        System.out.println("4. Wyświetl wszystkie przychody");
        System.out.println("5. Wyświetl wszystkie wydatki");
        System.out.println("0. Zakończ program");
    }

}