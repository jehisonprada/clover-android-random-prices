package mindbit.de.rubbellosapp.util;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.databinding.DialogEditPrizeBinding;
import mindbit.de.rubbellosapp.interfaces.presenters.ISettingsPresenter;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.viewmodel.PrizeViewModel;

public class SettingsFragmentDialogUtil {

    public static void showValidationErrorMessage(Context context, int stringId) {
        View errorMessageView = View.inflate(context, R.layout.dialog_error_message, null);

        TextView tvErrorMessage = errorMessageView.findViewById(R.id.tv_error_message);
        tvErrorMessage.setText(context.getResources().getString(stringId));

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder
                .setCancelable(false)
                .setView(errorMessageView);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(500, 450);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.colorWhiteWithOpacity)));

        Button okButton = errorMessageView.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void showEditPrizeDialog(Context context, Prize prize, final PrizeViewModel prizeViewModel, final ISettingsPresenter iSettingsPresenter) {
        final DialogEditPrizeBinding dialogEditPrizeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_edit_prize, null, false);
        dialogEditPrizeBinding.setPrize(prize != null ? prize : new Prize());

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_AppCompat_Light_Dialog_Alert));
        builder
                .setCancelable(false)
                .setView(dialogEditPrizeBinding.getRoot());

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialogEditPrizeBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iSettingsPresenter.validatePrize(dialogEditPrizeBinding.getPrize())) {
                    prizeViewModel.insert(dialogEditPrizeBinding.getPrize());
                    dialog.dismiss();
                }
            }
        });

        dialogEditPrizeBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
