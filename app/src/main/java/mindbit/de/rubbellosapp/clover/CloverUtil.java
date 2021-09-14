package mindbit.de.rubbellosapp.clover;

import android.accounts.Account;
import android.content.Context;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.customer.CustomerConnector;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.PrinterConnector;

import java.util.List;

public class CloverUtil {

    private static CloverUtil cloverUtil;

    private CloverUtil() {
    }

    public Printer getPrinter(Context context) {
        try {
            PrinterConnector printerConnector = new PrinterConnector(context, getAccount(context), null);
            List<Printer> printers = printerConnector.getPrinters(Category.RECEIPT);
            if (printers != null && !printers.isEmpty()) {
                return printers.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CustomerConnector getCustomerConnector(Context context) {
        return new CustomerConnector(context, getAccount(context), null);
    }

    public Account getAccount(Context context) {
        return CloverAccount.getAccount(context);
    }

    public static CloverUtil getInstance() {
        if(cloverUtil == null) {
            cloverUtil = new CloverUtil();
        }
        return cloverUtil;
    }

}
