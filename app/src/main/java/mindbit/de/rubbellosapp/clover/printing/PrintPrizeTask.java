package mindbit.de.rubbellosapp.clover.printing;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.sdk.v1.printer.Printer;
import com.clover.sdk.v1.printer.job.ViewPrintJob;

import java.lang.ref.WeakReference;

import mindbit.de.rubbellosapp.R;
import mindbit.de.rubbellosapp.clover.CloverUtil;
import mindbit.de.rubbellosapp.persistence.model.Prize;
import mindbit.de.rubbellosapp.util.DateUtil;

public class PrintPrizeTask extends AsyncTask<Void, Void, Boolean> {

    private final static int RECOMMENDED_CLOVER_MINI_PRINTER_WIDTH = 384;

    private final WeakReference<Context> contextRef;
    private final Prize prize;
    private final String merchantName;
    private ViewGroup printPrizeView;

    public PrintPrizeTask(Context context, Prize prize, String merchantName) {
        this.contextRef = new WeakReference<>(context);
        this.prize = prize;
        this.merchantName = merchantName;
    }

    @Override
    protected void onPreExecute() {
        printPrizeView = (ViewGroup) View.inflate(contextRef.get(), R.layout.print_prize_layout, null);
        ((TextView)printPrizeView.findViewById(R.id.tv_merchant_name)).setText(merchantName);
        ((TextView)printPrizeView.findViewById(R.id.tv_prize_name)).setText(prize.getDescription());
        ((TextView)printPrizeView.findViewById(R.id.tv_prize_validity)).setText(prize.getValidity());
        ((TextView)printPrizeView.findViewById(R.id.tv_current_date)).setText(DateUtil.getCurrentDateAsString(contextRef.get().getResources().getConfiguration().locale));

        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec( RECOMMENDED_CLOVER_MINI_PRINTER_WIDTH , View.MeasureSpec.EXACTLY);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
        printPrizeView.measure(measureSpecWidth, measureSpecHeight);
        printPrizeView.layout(0, 0, printPrizeView.getMeasuredWidth(), printPrizeView.getMeasuredHeight());
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Printer printer = CloverUtil.getInstance().getPrinter(contextRef.get());
        if(printer != null) {
            try {
                new ViewPrintJob.Builder().view(printPrizeView).build().print(contextRef.get(), CloverUtil.getInstance().getAccount(contextRef.get()), printer);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if(!result) {
            Toast.makeText(contextRef.get(), R.string.printing_fail, Toast.LENGTH_LONG).show();
        }
    }
}
