package mindbit.de.rubbellosapp.fragments;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantConnector;

import java.util.List;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.clover.CloverUtil;
import mindbit.de.rubbellosapp.clover.printing.PrintPrizeTask;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.persistence.model.User;
import mindbit.de.rubbellosapp.util.FragmentNavigationUtil;
import mindbit.de.rubbellosapp.util.PrizeSelectorUtil;
import mindbit.de.rubbellosapp.viewmodel.PrizeViewModel;

public class BoardFragment extends Fragment {

    private static final String USER_PARAMETER_KEY = "userKey";

    private PrizeViewModel prizeViewModel;
    private List<Prize> prizeList;
    private String merchantName;

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User that logged in.
     * @return A new instance of fragment BoardFragment.
     */
    public static BoardFragment newInstance(User user) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_PARAMETER_KEY, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        updatePrizeList();

        MerchantConnector merchantConnector = new MerchantConnector(getContext(), CloverUtil.getInstance().getAccount(getContext()), null);
        merchantConnector.getMerchant(new ServiceConnector.Callback<Merchant>() {
            @Override
            public void onServiceSuccess(Merchant result, ResultStatus status) {
                merchantName = result.getName();
            }

            @Override
            public void onServiceFailure(ResultStatus status) {

            }

            @Override
            public void onServiceConnectionFailure() {

            }
        });

        ImageButton firstPrizeSelector = rootView.findViewById(R.id.item_prize_card_1).findViewById(R.id.prize_selector);
        ImageButton secondPrizeSelector = rootView.findViewById(R.id.item_prize_card_2).findViewById(R.id.prize_selector);
        ImageButton thirdPrizeSelector = rootView.findViewById(R.id.item_prize_card_3).findViewById(R.id.prize_selector);

        firstPrizeSelector.setOnClickListener(getOnClickListenerForPrizeSelector());
        secondPrizeSelector.setOnClickListener(getOnClickListenerForPrizeSelector());
        thirdPrizeSelector.setOnClickListener(getOnClickListenerForPrizeSelector());
        return rootView;
    }

    private View.OnClickListener getOnClickListenerForPrizeSelector() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(getSelectorAnimation());
            }
        };
    }

    private RotateAnimation getSelectorAnimation() {
        final RotateAnimation rotate = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Prize selectedPrize = PrizeSelectorUtil.selectPrize(prizeList);
                if(selectedPrize != null) {
                    showWonPrizeDialog(selectedPrize);
                    removePrizeFromStock(selectedPrize);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return rotate;
    }

    private void removePrizeFromStock(Prize prize) {
        prize.setAmount(prize.getAmount() - 1);
        if (prize.getAmount() == 0) {
            prizeViewModel.delete(prize);
            prizeList.remove(prize);
            PrizeSelectorUtil.resetRanges(prizeList);
            prizeViewModel.insertAll(prizeList);
        } else {
            prizeViewModel.insert(prize);
        }
    }

    private void showWonPrizeDialog(final Prize prize) {
        View wonPrizeView = View.inflate(getContext(), R.layout.dialog_won_prize, null);

        TextView tvErrorMessage = wonPrizeView.findViewById(R.id.tv_won_prize);
        tvErrorMessage.setText(prize.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setCancelable(false)
                .setView(wonPrizeView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        Button okButton = wonPrizeView.findViewById(R.id.btn_won_prize_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrintPrizeTask(getContext(), prize, merchantName).execute();
                dialog.dismiss();
                FragmentNavigationUtil.goToFormFragment(getActivity().getSupportFragmentManager());
            }
        });

    }

    private void updatePrizeList() {
        prizeViewModel = ViewModelProviders.of(this).get(PrizeViewModel.class);
        prizeViewModel.getInStockPrizes().observe(this, new Observer<List<Prize>>() {
            @Override
            public void onChanged(@Nullable final List<Prize> prizes) {
                prizeList = prizes;
            }
        });
    }

}
