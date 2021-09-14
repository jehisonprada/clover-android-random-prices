package mindbit.de.rubbellosapp.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.adapters.PrizeAdapter;
import mindbit.de.rubbellosapp.interfaces.presenters.ISettingsPresenter;
import mindbit.de.rubbellosapp.interfaces.views.ISettingsView;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.presenter.SettingsPresenter;
import mindbit.de.rubbellosapp.util.SettingsFragmentDialogUtil;
import mindbit.de.rubbellosapp.util.PrizeSelectorUtil;
import mindbit.de.rubbellosapp.viewmodel.PrizeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements ISettingsView {
    private List<Prize> prizeList;

    private PrizeAdapter prizeAdapter;

    private ISettingsPresenter settingsPresenter;

    private PrizeViewModel prizeViewModel;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        RecyclerView recyclerViewPrizes = rootView.findViewById(R.id.rv_settings_prizes_list);
        recyclerViewPrizes.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Get a new or existing ViewModel from the ViewModelProvider.
        prizeViewModel = ViewModelProviders.of(this).get(PrizeViewModel.class);

        settingsPresenter = new SettingsPresenter(this);
        prizeAdapter = new PrizeAdapter(prizeViewModel, this.getContext(), settingsPresenter);
        recyclerViewPrizes.setAdapter(prizeAdapter);

        prizeViewModel.getAllPrizes().observe(this, new Observer<List<Prize>>() {
            @Override
            public void onChanged(@Nullable final List<Prize> prizes) {
                prizeList = prizes;
                prizeAdapter.setPrizes(prizes);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final ImageButton buttonAddItem = getView().findViewById(R.id.fab_add_item);
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragmentDialogUtil.showEditPrizeDialog(getContext(), null, prizeViewModel, settingsPresenter);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        PrizeSelectorUtil.resetRanges(prizeList);
        prizeViewModel.insertAll(prizeList);
    }

    @Override
    public void showDescriptionErrorMessage() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.empty_description);
    }

    @Override
    public void showAmountErrorMessage() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.invalid_amount);
    }

    @Override
    public void showOddsErrorMessage() {
        SettingsFragmentDialogUtil.showValidationErrorMessage(getContext(), R.string.invalid_odds);
    }

}
