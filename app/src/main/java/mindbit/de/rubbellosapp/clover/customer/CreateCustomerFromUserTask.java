package mindbit.de.rubbellosapp.clover.customer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.customer.Customer;
import com.clover.sdk.v1.customer.CustomerConnector;
import com.clover.sdk.v1.customer.EmailAddress;

import java.lang.ref.WeakReference;
import java.util.List;

import mindbit.de.rubbellosapp.clover.CloverUtil;
import mindbit.de.rubbellosapp.persistence.model.User;
import mindbit.de.rubbellosapp.viewmodel.UserViewModel;

public class CreateCustomerFromUserTask extends AsyncTask<Void, Void, Void> {

    private final WeakReference<Context> contextRef;
    private final List<User> users;
    private final UserViewModel userViewModel;
    private CustomerConnector customerConnector;

    public CreateCustomerFromUserTask(Context context, List<User> users, UserViewModel userViewModel) {
        this.contextRef = new WeakReference<>(context);
        this.users = users;
        this.userViewModel = userViewModel;
    }

    @Override
    protected void onPreExecute() {
        customerConnector = CloverUtil.getInstance().getCustomerConnector(contextRef.get());
        customerConnector.connect();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for (User user : users) {
            try {
                if(!isAlreadyInCloverSystem(user)) {
                    Customer customer = customerConnector.createCustomer(user.getFirstName(), user.getLastName(), true);
                    customerConnector.addEmailAddress(customer.getId(), user.getEmail());
                    customerConnector.setMarketingAllowed(customer.getId(), true);
                    user.setInCloverMarket(true);
                    userViewModel.insert(user);
                }
            } catch (Exception e) {
                Log.e("Clover connection error", "Error trying to save customer in clover dashboard, user:" + user.getEmail() + "\n" + e.getMessage());
            }
        }
        return null;
    }

    private boolean isAlreadyInCloverSystem(User user) throws ServiceException, RemoteException, BindingException, ClientException {
        List<Customer> customers = customerConnector.getCustomers(user.getFirstName());
        for (Customer customer : customers) {
            Customer completeCustomer = customerConnector.getCustomer(customer.getId());
            for (EmailAddress emailAddress : completeCustomer.getEmailAddresses()) {
                if(emailAddress.getEmailAddress().equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }
}
