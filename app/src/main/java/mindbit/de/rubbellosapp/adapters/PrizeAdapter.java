package mindbit.de.rubbellosapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Collections;
import java.util.List;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.databinding.ItemInputPrizeBinding;
import mindbit.de.rubbellosapp.interfaces.presenters.ISettingsPresenter;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.util.SettingsFragmentDialogUtil;
import mindbit.de.rubbellosapp.viewmodel.PrizeViewModel;

public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder>{

    private List<Prize> prizes = Collections.emptyList();
    private final Context context;
    private final PrizeViewModel prizeViewModel;
    private final ISettingsPresenter iSettingsPresenter;

    public PrizeAdapter(PrizeViewModel prizeViewModel, Context context, ISettingsPresenter iSettingsPresenter) {
        this.prizeViewModel = prizeViewModel;
        this.context = context;
        this.iSettingsPresenter = iSettingsPresenter;
    }

    @NonNull
    @Override
    public PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemInputPrizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_input_prize, parent, false);
        return new PrizeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final PrizeViewHolder holder, int position) {
        final Prize prize = prizes.get(position);
        holder.itemInputPrizeBinding.setPrize(prize);

        ImageButton deletePrize = holder.itemView.findViewById(R.id.btn_delete_item);
        deletePrize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prizeViewModel.delete(prize);
                prizes.remove(holder.getAdapterPosition());
                prizeViewModel.insertAll(prizes);
            }
        });

        holder.itemInputPrizeBinding.btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragmentDialogUtil.showEditPrizeDialog(context, prize, prizeViewModel, iSettingsPresenter);
            }
        });
    }

    public void setPrizes(List<Prize> prizes){
        this.prizes = prizes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return prizes.size();
    }

    public static class PrizeViewHolder extends RecyclerView.ViewHolder {

        final ItemInputPrizeBinding itemInputPrizeBinding;

        private PrizeViewHolder(ItemInputPrizeBinding binding) {
            super(binding.getRoot());
            itemInputPrizeBinding = binding;
        }
    }

}
