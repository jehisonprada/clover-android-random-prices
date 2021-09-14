package mindbit.de.rubbellosapp.clover;

import android.accounts.Account;
import android.content.Context;
import android.os.IInterface;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v3.employees.AccountRole;
import com.clover.sdk.v3.employees.Employee;
import com.clover.sdk.v3.employees.EmployeeConnector;

public class CloverSessionConnector extends EmployeeConnector.EmployeeCallback<Employee> implements ServiceConnector.OnServiceConnectedListener, EmployeeConnector.OnActiveEmployeeChangedListener {

    private final Context context;
    private final CloverSessionConnectorListener cloverSessionConnectorListener;
    private Account mAccount;
    private EmployeeConnector mEmployeeConnector;

    public static AccountRole currentEmployeeRole;

    public CloverSessionConnector(Context context, CloverSessionConnectorListener cloverSessionConnectorListener) {
        this.context = context;
        this.cloverSessionConnectorListener = cloverSessionConnectorListener;
    }

    @Override
    public void onServiceSuccess(Employee result, ResultStatus status) {
        super.onServiceSuccess(result, status);
        currentEmployeeRole = result.getRole();
        cloverSessionConnectorListener.onEmployeeServiceSuccess(result);
    }

    @Override
    public void onServiceFailure(ResultStatus status) {
        super.onServiceFailure(status);
        currentEmployeeRole = AccountRole.EMPLOYEE;
        cloverSessionConnectorListener.onEmployeeServiceError();
    }

    @Override
    public void onServiceConnected(ServiceConnector<? extends IInterface> connector) {

    }

    @Override
    public void onServiceDisconnected(ServiceConnector<? extends IInterface> connector) {

    }

    @Override
    public void onActiveEmployeeChanged(Employee employee) {
        if (employee != null) {
            currentEmployeeRole = employee.getRole();
            cloverSessionConnectorListener.onActiveEmployeeChanged(employee);
        }
    }

    public void connect() {
        disconnect();
        if (getAccount() != null) {
            mEmployeeConnector = new EmployeeConnector(context, getAccount(), this);
            mEmployeeConnector.connect();
            mEmployeeConnector.addOnActiveEmployeeChangedListener(this);
            mEmployeeConnector.getEmployee(this);
        }
    }

    public void disconnect() {
        if (mEmployeeConnector != null) {
            mEmployeeConnector.removeOnActiveEmployeeChangedListener(this);
            mEmployeeConnector.disconnect();
            mEmployeeConnector = null;
        }
    }

    public Account getAccount() {
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(context);
        }
        return mAccount;
    }

    public interface CloverSessionConnectorListener {
        void onEmployeeServiceSuccess(Employee result);
        void onEmployeeServiceError();
        void onActiveEmployeeChanged(Employee employee);
    }

}
