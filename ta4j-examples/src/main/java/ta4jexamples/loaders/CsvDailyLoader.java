package ta4jexamples.loaders;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.Tick;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Month;
import org.joda.time.DateTime;

/**
 * <p>
 * Class for loading data over a period of time (until present day). <br>
 * Problems with class: <br>
 *  Google doesn't fetch historical data for stocks from Exchange:TLV <br>
 *  Google doesn't fetch some historical data for stocks from Exchange:NYSE<br>
 *  Google has some data gaps in few stocks, that makes tracking harder
 *  Google has bot detections that prevent threading after a short period of time <br>
 *  <br>
 * Class uses 2 fetching operations: <br>
 *     * Yahoo Current Data             <br>
 *     * Google Historical Data
 * </p>
 * @deprecated - Use {@link CsvHistoricalLoader} does the same functionality but better.
 */
public class CsvDailyLoader extends AbstractCsvLoader {

    private         TimeSeries series;
    private final int MINIMUM_TICKS = 5;

    /**
     * @param market            String - Exchange market on which stock is being traded
     * @param symbol            String - Stock Symbol that we want to load.
     * @param start_date        DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvDailyLoader(String market, String symbol , DateTime start_date) {

        // TODO: DELETE THIS DEBUG
        if (market.toUpperCase().equals("TLV"))
            System.out.println("April 2017 - Google not yet provide TLV Ex historical data in CSV format.");

        // Create a List of ticks, and load streams.
        List<Tick> ticks            = new ArrayList<Tick>();
        InputStream googleStream    = loadGoogleHist(market, symbol, start_date);
        ticks.addAll(loadGoogleHistTicks(googleStream));
        if (ticks.size() < MINIMUM_TICKS-1) {
            return;
        }

        InputStream yahooStream     = loadYahooCurr(market, symbol);
        ticks.addAll(loadYahooCurrTick(yahooStream));

        // When Last Date had already been reported, delete the tick
        int numOfTicks =  ticks.size();
        if (numOfTicks > 1){
            if (ticks.get(numOfTicks-1).getEndTime().equals(ticks.get(numOfTicks-2).getEndTime()))
                ticks.remove(numOfTicks-1);
        }

        this.series = new TimeSeries(market +" "+ symbol + " Stock Ticks", ticks);
    }

    /**
     * @param symbol            String - Stock Symbol that we want to load.
     * @param start_date    - DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvDailyLoader(String symbol , DateTime start_date) {
        this("", symbol, start_date);
    }

    public TimeSeries getSeries(){
        return series;
    }

    //================================================================================================

    public static void main(String args[]) throws IOException{
        // "AAPL" Regular , "AMT" 1 Tick
        CsvDailyLoader liveData = new CsvDailyLoader("AAAPL" , new DateTime(2017 , Month.FEBRUARY , 1 , 0 , 0));
        TimeSeries series = liveData.getSeries();

//        System.out.println("Series: " + series.getName() + " (" + series.getSeriesPeriodDescription() + ")");
//        System.out.println("Number of ticks: " + series.getTickCount());
//        System.out.println("First tick: \n"
//                + "\tTick: " + series.getTick(0) + "\n"
//                + "\tVolume: " + series.getTick(0).getVolume() + "\n"
//                + "\tOpen price: " + series.getTick(0).getOpenPrice()+ "\n"
//                + "\tClose price: " + series.getTick(0).getClosePrice());
//        System.out.println("Second tick: \n"
//                + "\tTick: " + series.getTick(1) + "\n"
//                + "\tVolume: " + series.getTick(1).getVolume() + "\n"
//                + "\tOpen price: " + series.getTick(1).getOpenPrice()+ "\n"
//                + "\tClose price: " + series.getTick(1).getClosePrice());
//        System.out.println("Last tick: \n"
//                + "\tTick: " + series.getLastTick() + "\n"
//                + "\tVolume: " + series.getLastTick().getVolume() + "\n"
//                + "\tOpen price: " + series.getLastTick().getOpenPrice()+ "\n"
//                + "\tClose price: " + series.getLastTick().getClosePrice());
//        System.out.println("Last tick - 1: \n"
//                + "\tTick: " + series.getTick(series.getTickCount()-2) + "\n"
//                + "\tVolume: " + series.getTick(series.getTickCount()-2).getVolume() + "\n"
//                + "\tOpen price: " + series.getTick(series.getTickCount()-2).getOpenPrice()+ "\n"
//                + "\tClose price: " + series.getTick(series.getTickCount()-2).getClosePrice());

        for (int i = 0; i <series.getTickCount() ; i++) {
            System.out.println("tick: " + i + "\n"
                    + "\tTick: " + series.getTick(i) + "\n"
                    + "\tVolume: " + series.getTick(i).getVolume() + "\n"
                    + "\tOpen price: " + series.getTick(i).getOpenPrice()+ "\n"
                    + "\tClose price: " + series.getTick(i).getClosePrice());
        }
    }

}


