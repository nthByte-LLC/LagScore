package net.dohaw.lagscore.utils;

import java.math.BigDecimal;
import java.math.MathContext;

public class ScoreUtils {

    public static double roundToDecimalPoints(double score, int numOfDecimalPlaces){
        BigDecimal bd = new BigDecimal(score);
        bd = bd.round(new MathContext(numOfDecimalPlaces));
        double rounded = bd.doubleValue();
        return rounded;
    }

}
