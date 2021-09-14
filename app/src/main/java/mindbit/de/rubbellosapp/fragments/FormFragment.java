package mindbit.de.rubbellosapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.clover.customer.CreateCustomerFromUserTask;
import mindbit.de.rubbellosapp.interfaces.presenters.IFormPresenter;
import mindbit.de.rubbellosapp.interfaces.views.IFormView;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.persistence.model.User;
import mindbit.de.rubbellosapp.presenter.FormPresenter;
import mindbit.de.rubbellosapp.util.FragmentNavigationUtil;
import mindbit.de.rubbellosapp.util.SettingsFragmentDialogUtil;
import mindbit.de.rubbellosapp.viewmodel.PrizeViewModel;
import mindbit.de.rubbellosapp.viewmodel.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends Fragment implements IFormView {

    private IFormPresenter formPresenter;
    private UserViewModel userViewModel;
    private PrizeViewModel prizeViewModel;

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private TextView tvDataPolicyLabel;
    private CheckBox cbAcceptPolicy;

    private List<User> notInCloverMarket;
    private boolean wrongPrizesConfiguration;

    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FormFragment.
     */
    public static FormFragment newInstance() {
        return new FormFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etFirstName = getView().findViewById(R.id.et_form_first_name);
        etLastName = getView().findViewById(R.id.et_form_last_name);
        etEmail = getView().findViewById(R.id.et_form_email);
        tvDataPolicyLabel = getView().findViewById(R.id.tv_data_policy_label);
        cbAcceptPolicy = getView().findViewById(R.id.cb_accept_policy);

        prizeViewModel.getInStockPrizes().observe(this, new Observer<List<Prize>>() {
            @Override
            public void onChanged(@Nullable List<Prize> prizes) {
                wrongPrizesConfiguration = prizes == null || prizes.isEmpty();
            }
        });

        userViewModel.getNotInCloverMarket().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                notInCloverMarket = users;
            }
        });

        Button buttonFormNext = getView().findViewById(R.id.btn_form_next);
        formPresenter = new FormPresenter(this);
        buttonFormNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = formPresenter.buildUser(etFirstName.getText().toString(), etLastName.getText().toString(), etEmail.getText().toString());
                saveUser(user);
            }
        });

        manageClickableDataPolicy();
    }

    private void manageClickableDataPolicy() {
        String dataPolicyLabel = getResources().getString(R.string.data_policy_label);

        SpannableString ss = new SpannableString(dataPolicyLabel);

        ClickableSpan dataPrivacyPolicyClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                String dataPrivacyPolicyUrl = getResources().getString(R.string.data_privacy_policy_url);
                FragmentNavigationUtil.createAndGoToWebViewFragment(getActivity().getSupportFragmentManager(), dataPrivacyPolicyUrl);
            }
        };

        String dataPolicyKeyword = getResources().getString(R.string.data_policy_keyword);
        int dataPolicyKeywordStartIndex = dataPolicyLabel.indexOf(dataPolicyKeyword);
        int dataPolicyKeywordEndIndex = dataPolicyKeywordStartIndex + dataPolicyKeyword.length();
        ss.setSpan(dataPrivacyPolicyClickableSpan, dataPolicyKeywordStartIndex, dataPolicyKeywordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ClickableSpan termsOfUseClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                String dataPrivacyPolicyUrl = getResources().getString(R.string.terms_of_use_url);
                FragmentNavigationUtil.createAndGoToWebViewFragment(getActivity().getSupportFragmentManager(), dataPrivacyPolicyUrl);
            }
        };
        String termsOfUseKeyword = getResources().getString(R.string.terms_of_use_keyword);
        int termsOfUseKeywordStartIndex = dataPolicyLabel.indexOf(termsOfUseKeyword);
        int termsOfUseKeywordEndIndex = termsOfUseKeywordStartIndex + termsOfUseKeyword.length();
        ss.setSpan(termsOfUseClickableSpan, termsOfUseKeywordStartIndex, termsOfUseKeywordEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvDataPolicyLabel.setText(ss);
        tvDataPolicyLabel.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void saveUser(User user) {
        if (wrongPrizesConfiguration) {
            SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.no_prizes_configured);
        } else if (formPresenter.validateFields(user.getFirstName(), user.getLastName(), user.getEmail(), cbAcceptPolicy.isChecked())) {
            userViewModel.insert(user);
            FragmentNavigationUtil.goToBoardFragment(getActivity().getSupportFragmentManager(), user);
            addUserIfNotAlreadyAdded(user);
            new CreateCustomerFromUserTask(getContext(), notInCloverMarket, userViewModel).execute();
        }
    }

    private void addUserIfNotAlreadyAdded(User userToAdd) {
        boolean inList = false;
        for (User user : notInCloverMarket) {
            if (user.getEmail().equals(userToAdd.getEmail())) {
                inList = true;
                break;
            }
        }
        if (!inList) {
            notInCloverMarket.add(userToAdd);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        prizeViewModel = ViewModelProviders.of(this).get(PrizeViewModel.class);
        return inflater.inflate(R.layout.fragment_form, container, false);
    }



    @Override
    public void showEmptyFirstNameError() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.empty_first_name);
    }

    @Override
    public void showEmptyLastNameError() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.empty_last_name);
    }

    @Override
    public void showInvalidEmailError() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.invalid_email);
    }

    @Override
    public void showDataPolicyError() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.data_policy_error);
    }

}
