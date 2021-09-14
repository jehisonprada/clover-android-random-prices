package mindbit.de.rubbellosapp.util;

import java.util.List;

import mindbit.de.rubbellosapp.persistence.model.Prize;

public class PrizeSelectorUtil {

    private static final int INITIAL_RANGE_VALUE = -1;

    public static Prize selectPrize(List<Prize> prizes){
        int randomSelectedIndex = (int) (Math.random() * sumRanges(prizes));
        for(Prize prize : prizes){
            if(prize.getInitialRange() <= randomSelectedIndex && randomSelectedIndex <= prize.getFinalRange()) {
                return prize;
            }
        }
        return null;
    }

    private static int sumRanges(List<Prize> prizes){
        int sum = 0;
        for(Prize prize: prizes){
            sum += prize.getOdds();
        }
        return sum;
    }

    public static void resetRanges(List<Prize> prizes){
        int lastAssignedRange = INITIAL_RANGE_VALUE;
        for(Prize prize : prizes){
            prize.setInitialRange(lastAssignedRange + 1);
            prize.setFinalRange(prize.getInitialRange() + (prize.getOdds() - 1));
            lastAssignedRange = prize.getFinalRange();
        }
    }

}
