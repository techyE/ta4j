package ta4jexamples.loaders;

import ta4jexamples.scraperPaths.*;
import eu.verdelhan.ta4j.Decimal;
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
 *  Yahoo doesn't fetch some historical data for stocks from Exchange:TLV<br>
 *  Yahoo doesn't fetch few historical data for stocks from Exchange:NYSE<br>
 *  <br>
 * Class uses 2 fetching operations: <br>
 *     * Yahoo Current Data             <br>
 *     * Yahoo Historical Data
 * </p>
 */
public class CsvHistoricalLoader extends AbstractCsvLoader {

    private  TimeSeries series = null;
    private final int MINIMUM_TICKS = 5;

    /**
     * <p>
     *     Some Exchanges show different currencies, this function comes to match <br>
     *     between data that being sampled with different type of measurement in order to match <br>
     *     with the common type.
     * </p>
     * @param market    String - Them market in which that stock is being traded
     * @return          int    - The coefficient in which data should change according to
     */
    private int fixCoefficient(String market) {
        if (market.toUpperCase().equals("TLV"))
            return 100;
        else
            return 1;
    }

    /**
     * @param market            String - Exchange market on which stock is being traded
     * @param symbol            String - Stock Symbol that we want to load.
     * @param startDate    - DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvHistoricalLoader(String market, String symbol , DateTime startDate, DateTime endDate,YahooHistCsvPath.YahooHistInterval interval) {
        // Create a List of ticks
        List<Tick> ticks = new ArrayList<>();
        Tick fixTick;

        // Load History data, without it, there is no reason to continue.
        InputStream yahooHistStream = loadYahooHist(market, symbol, startDate, endDate, interval);
        ticks.addAll(loadYahooHistTicks(yahooHistStream));
        if (ticks.size() < MINIMUM_TICKS-1) {
            return;
        }

        // Ask for recent data only if end date is current date
        if (endDate.equals(new DateTime())) {
            //Ticks sanity check
            if (ticks.get(ticks.size() - 1).getClosePrice().isLessThan(Decimal.ONE)) {
                return;
            }
            if (ticks.get(ticks.size() - 1).getEndTime().isBefore(new DateTime().minusMonths(2))) {
                return;
            }

            // Load current data, If has a problem return only historical data.
            InputStream yahooCurrStream = loadYahooCurr(market, symbol);
            try {
                fixTick = loadYahooCurrTick(yahooCurrStream).get(0);
            } catch (IndexOutOfBoundsException e) {
                this.series = new TimeSeries(market + " " + symbol + " Stock Ticks", ticks);
                return;
            }

            fixTick.setClosePrice(fixTick.getClosePrice().dividedBy(Decimal.valueOf(fixCoefficient(market))));
            fixTick.setMaxPrice(fixTick.getMaxPrice().dividedBy(Decimal.valueOf(fixCoefficient(market))));
            fixTick.setMinPrice(fixTick.getMinPrice().dividedBy(Decimal.valueOf(fixCoefficient(market))));
            fixTick.setOpenPrice(fixTick.getOpenPrice().dividedBy(Decimal.valueOf(fixCoefficient(market))));
            ticks.add(fixTick);

            // When Last Date had already been reported, delete the tick
            int numOfTicks = ticks.size();
            DateTime currentTickDate = ticks.get(numOfTicks - 1).getEndTime();
            DateTime LastHistTickDate = ticks.get(numOfTicks - 2).getEndTime();
            boolean removeFlag = false;
            switch (interval) {
                case DAILY:
                    if (currentTickDate.dayOfYear().equals(LastHistTickDate.dayOfYear())) {
                        removeFlag = true;
                    }
                    break;
                case WEEKLY:
                    if (currentTickDate.weekOfWeekyear().equals(LastHistTickDate.weekOfWeekyear())) {
                        removeFlag = true;
                    }
                    break;
                case MONTHLY:
                    if (currentTickDate.monthOfYear().equals(LastHistTickDate.monthOfYear())) {
                        removeFlag = true;
                    }
                    break;
            }
            if (removeFlag) {
                ticks.remove(numOfTicks - 1);
            }
        }

        this.series = new TimeSeries(market +" "+ symbol + " Stock Ticks", ticks);
    }

    /**
     * @param symbol        String - Stock Symbol that we want to load.
     * @param startDate    DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvHistoricalLoader(String market, String symbol , DateTime startDate,YahooHistCsvPath.YahooHistInterval interval) {
        this(market, symbol, startDate, new DateTime(), interval);
    }

    /**
     * @param symbol        String - Stock Symbol that we want to load.
     * @param startDate    DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvHistoricalLoader(String symbol , DateTime startDate, DateTime endDate,YahooHistCsvPath.YahooHistInterval interval) {
        this("", symbol, startDate, endDate, interval);
    }

    /**
     * @param symbol        String - Stock Symbol that we want to load.
     * @param startDate    DateTime - Start date that we want to start load from.
     * @return TimeSeries - Stock parameters from start date until most recent trade date.
     */
    public CsvHistoricalLoader(String symbol , DateTime startDate,YahooHistCsvPath.YahooHistInterval interval) {
        this("", symbol, startDate, new DateTime(), interval);
    }

    @Override
    public TimeSeries getSeries() {
        return series;
    }

    //================================================================================================


    public static void main(String args[]) throws IOException
    {
        CsvLoader liveData  = new CsvHistoricalLoader("AGRO" , new DateTime(2014 , Month.FEBRUARY , 1 , 0 , 0), YahooHistCsvPath.YahooHistInterval.WEEKLY);
        TimeSeries series   = liveData.getSeries();

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


