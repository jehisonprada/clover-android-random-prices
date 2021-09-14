package mindbit.de.rubbellosapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.clover.sdk.v3.employees.AccountRole;
import com.clover.sdk.v3.employees.Employee;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.clover.CloverSessionConnector;
import mindbit.de.rubbellosapp.fragments.FormFragment;
import mindbit.de.rubbellosapp.util.DrawerLayoutListener;
import mindbit.de.rubbellosapp.util.FragmentNavigationUtil;

public class RubbellosActivity extends AppCompatActivity {

    private CloverSessionConnector cloverSessionConnector;
    private DrawerLayout drawerLayout;
    private boolean activeEmployeeChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cloverSessionConnector = new CloverSessionConnector(this, new CloverSessionConnector.CloverSessionConnectorListener() {
            @Override
            public void onEmployeeServiceSuccess(Employee employee) {
                AccountRole role = employee != null ? employee.getRole() : AccountRole.EMPLOYEE;
                showMenuItemsAccordingToRole(role);
                goToStartPage();
            }

            @Override
            public void onEmployeeServiceError() {
                //Pass role employee as default for security reasons when current role can't be retrieved
                showMenuItemsAccordingToRole(AccountRole.EMPLOYEE);
                goToStartPage();
            }

            @Override
            public void onActiveEmployeeChanged(Employee employee) {
                activeEmployeeChanged = true;
                showMenuItemsAccordingToRole(employee.getRole());
            }
        });

        setContentView(R.layout.activity_rubbellos);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        switch (menuItem.getItemId()) {
                            case R.id.menu_item_home:
                                FragmentNavigationUtil.goToFormFragment(getSupportFragmentManager());
                                return true;
                            case R.id.menu_item_settings:
                                FragmentNavigationUtil.goToSettingsFragment(getSupportFragmentManager());
                                return true;
                            case R.id.menu_item_data_protection:
                                String dataPrivacyPolicyUrl = getResources().getString(R.string.data_privacy_policy_url);
                                FragmentNavigationUtil.createAndGoToWebViewFragment(getSupportFragmentManager(), dataPrivacyPolicyUrl);
                                return true;
                            case R.id.menu_item_imprint:
                                FragmentNavigationUtil.goToImprintFragment(getSupportFragmentManager());
                                return true;
                            case R.id.menu_item_help:
                                String helpUrl = CloverSessionConnector.currentEmployeeRole == AccountRole.ADMIN ? getResources().getString(R.string.help_url_for_merchant) : getResources().getString(R.string.help_url_for_customer);
                                FragmentNavigationUtil.createAndGoToWebViewFragment(getSupportFragmentManager(), helpUrl);
                                return true;
                            default:
                                return true;
                        }
                    }
                }
        );

        drawerLayout.addDrawerListener(new DrawerLayoutListener(this));
        Toolbar toolbar = findViewById(R.id.rubbellos_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black);
            actionbar.setDisplayShowTitleEnabled(false);
        }
    }

    private void goToStartPage() {
        FragmentNavigationUtil.goToFormFragment(getSupportFragmentManager());
    }

    private void showMenuItemsAccordingToRole(AccountRole role) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.menu_item_settings).setVisible(role == AccountRole.ADMIN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToClover();
        if (activeEmployeeChanged) {
            goToStartPage();
        }
        activeEmployeeChanged = false;
    }

    private void connectToClover() {
        if (cloverSessionConnector == null || cloverSessionConnector.getAccount() == null) {
            Toast.makeText(this, getString(R.string.no_account), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cloverSessionConnector.connect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Home here is related to android default navigation drawer home button
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.flContainerRubbellos) instanceof FormFragment)) {
            super.onBackPressed();
        } else {
            finishAffinity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cloverSessionConnector.disconnect();
    }

}
