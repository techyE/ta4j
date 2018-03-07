package ta4jexamples.loaders;

import java.util.ArrayList;
import java.util.List;

import static ta4jexamples.loaders.CsvFileLoader.loadFileCSV;

/**
 * <p>
 * Created by yony on 4/1/17.                                   <br>
 * Handles a CSV file contains a list of publicly traded companies generated using Stock Exchange Website  <br>
 * Structure by NASDAQ:                                                   <br>
 * Symbol | Company Name | Price | Cap Size | ?ADR/TSO? | IPO Year | Sector | Industry | Quote on Nasdaq Web    <br>
 * Structure by Bursa: <br>
 * Name | Symbol | ISIN?? | Index | Closing Price ILA | Change (%) | Turnover(NIS thousands) (Cap) | Last Trade
 * @see <a href=http://www.nasdaq.com/screening/company-list.aspx>Nasdaq Link</a>
 * @see <a href=http://www.tase.co.il/Eng/MarketData/Stocks/MarketData/Pages/MarketData.aspx?action=1&dualTab=&SubAction=&Date=&issubmitted=1>Bursa Link</a>
 * </p>
 */
public class CsvCompanyListLoader{

    private final int HEADER_SIZE_SKIP;
    private final int SYMBOLS_COL     ;
    private final int PRICE_COL       ;
    private final int CAP_SIZE_COL    ;

    private List<String[]> csvList;

    /**
     * Constructor that loads CSV file and holds basic information about companies in a specific exchange.<br> <br>
     * Example: From TLV Exchange Web use constants: 4,1,4,6 <br>
     * Example: From Nasdaq Web use constants: 1,0,2,3 <br> </br>
     * @param CsvListFilePath     String - path to CSV file that contains list of stocks.
     */
    public CsvCompanyListLoader(String CsvListFilePath, int headerSizeSkip, int symbolsCol, int priceCol, int capSizeCol){
        this.HEADER_SIZE_SKIP   = headerSizeSkip;
        this.SYMBOLS_COL        = symbolsCol;
        this.PRICE_COL          = priceCol;
        this.CAP_SIZE_COL       = capSizeCol;

        this.csvList = loadFileCSV(CsvListFilePath, HEADER_SIZE_SKIP);
        List<String[]>  deletedLines = new ArrayList<>();

        // Remove all the weird records / undefined
        for (String[] line : csvList) {
            try {
                if (Double.parseDouble(line[CAP_SIZE_COL]) == 0) {
                    deletedLines.add(line);
                }
            }
            catch (NumberFormatException e) {deletedLines.add(line);}
        }
        for (String[] line : deletedLines) {
            csvList.remove(line);
        }
    }

    /**
     * Constructor that loads CSV file and holds basic information about companies in a specific exchange.<br> <br>
     * This function is suitable for data being received from "Bursa Website".
     * @param CsvListFilePath     String - path to CSV file that contains list of stocks.
     */
    public static CsvCompanyListLoader listFromBURSA(String CsvListFilePath){
        return new CsvCompanyListLoader(CsvListFilePath, 4,1,4,6);
    }

    /**
     * Constructor that loads CSV file and holds basic information about companies in a specific exchange.<br> <br>
     * This function is suitable for data being received from "NASDAQ Website".
     * @param CsvListFilePath     String - path to CSV file that contains list of stocks.
     */
    public static CsvCompanyListLoader listFromNASDAQ(String CsvListFilePath){
        return new CsvCompanyListLoader(CsvListFilePath, 1,0,2,3);
    }

    //========

    /**
     * Converts the List of companies info to a list that contains only the symbols of companies.
     * @return  List<String> - Stock Symbols list.
     */
    public List<String> loadSymbols(){
        List<String>    symbolsList = new ArrayList<>();

        for (String[] line : this.csvList) {
            symbolsList.add(line[SYMBOLS_COL].replaceAll("\\s","").replaceAll("\\.","-"));    // Also Avoid space & dots in list
        }

        return symbolsList;
    }

    /**
     * Filter stocks with higher price than the one given.
     * @param price     int - Minimum price threshold.
     */
    public void filterHigherPrices(int price){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : this.csvList) {
            if (Double.parseDouble(line[PRICE_COL]) < price){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            this.csvList.remove(line);
        }
    }

    /**
     * Filter stocks with lower price than the one given.
     * @param price     int - Maximum price threshold.
     */
    public void filterLowerPrices(int price){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : this.csvList) {
            if (Double.parseDouble(line[PRICE_COL]) > price){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            this.csvList.remove(line);
        }
    }

    /**
     * Filter stocks with bigger market cap than the one given.
     * @param capSize     int - Minimum market cap threshold.
     */
    public void filterBiggerCap(int capSize){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : this.csvList) {
            if (Double.parseDouble(line[CAP_SIZE_COL]) < capSize){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            this.csvList.remove(line);
        }
    }

    /**
     * Filter stocks with smaller market cap than the one given.
     * @param capSize     int - Maximum market cap threshold.
     */
    public void filterSmallerCap(int capSize){
        List<String[]>  deletedLines = new ArrayList<>();

        // Find only Stocks with higher price.
        for (String[] line : this.csvList) {
            if (Double.parseDouble(line[CAP_SIZE_COL]) > capSize){
                deletedLines.add(line);
            }
        }
        for (String[] line : deletedLines) {
            this.csvList.remove(line);
        }
    }

    public void setCsvList(List<String[]> list) {
        this.csvList = list;
    }

    public List<String[]> getCsvList() {
        return csvList;
    }

    //==========================================================================

    public static void main(String args[]) {

        CsvCompanyListLoader nyseList = listFromNASDAQ("nasdaqList2017.csv");
        nyseList.filterHigherPrices(700);

        // Print columns
        for (String[] line : nyseList.getCsvList()) {
            System.out.println(line[0]);
        }
    }
}